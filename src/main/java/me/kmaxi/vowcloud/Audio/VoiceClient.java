package me.kmaxi.vowcloud.Audio;

import me.kmaxi.vowcloud.Loggers;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.config.IntegratedConfig;
import me.kmaxi.vowcloud.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class VoiceClient {
    private DatagramSocket socket;

    private InetAddress serverInetAddress;
    private int serverPort;


    public static String serverAddress = "";

    public VoiceClient(int serverPort) {

        if (IntegratedConfig.useLocalHostServer)
            serverAddress = "localhost";

        if (serverAddress.isEmpty()) {
            Loggers.error("NO SERVER ADRESS FOUND");
            return;
        }

        try {
            socket = new DatagramSocket();
            this.serverPort = serverPort;
            serverInetAddress = InetAddress.getByName(serverAddress);
            Loggers.error("Connected to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start a separate thread for listening to incoming data
        Thread receiveThread = new Thread(this::receiveData);
        receiveThread.start();
        sendRequest("AUTH:" + VowCloud.getInstance().config.getAccessCode());
    }

    public void sendRequest(String request) {
        if (socket == null)
            return;

        try {
            byte[] requestData = request.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                    requestData,
                    requestData.length,
                    serverInetAddress,
                    serverPort
            );

            socket.send(sendPacket);
        } catch (IOException e) {
            Loggers.error("Error sending packet to server");
            e.printStackTrace();
        }
    }

    public void receiveData() {
        try {
            // This needs to be bigger then the largest packet that can be received
            byte[] buffer = new byte[128];

            boolean connectionLost = false;

            while (!connectionLost) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(receivePacket);
                } catch (SocketTimeoutException e) {
                    //TODO Handle a timeout exception

                    // Set connectionLost to true to exit the loop
                    connectionLost = true;
                    Utils.sendMessage("Lost connection to server");
                    continue;
                }

                AudioPacket audioPacket = new AudioPacket(receivePacket);
                VowCloud.getInstance().audioPlayer.play(audioPacket);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (socket != null) {
            socket.close();
            Loggers.log("Connection closed");
        }
    }

    public static void main(String[] args) {
     //   VoiceClient client = new VoiceClient("129.151.214.102", 25565);
        VoiceClient client = new VoiceClient(25565);

        // Send the first request
        String request1 = "1/1aledarohyoufell!trydoingitagain.";
        client.sendRequest(request1);

        // Add a delay if needed
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Send the second request
        String request2 = "1/2guard...wait.whatisthat?!";
        client.sendRequest(request2);

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // Close the connection when done
    }
}
