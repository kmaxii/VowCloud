package me.kmaxi.vowcloud;

import de.maxhenkel.opus4j.OpusDecoder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class VoiceClient {
    private DatagramSocket socket;

    private InetAddress serverInetAddress;
    private int serverPort;

    public AudioPlayer audioPlayer;

    public VoiceClient(String serverAddress, int serverPort) {
        try {
            socket = new DatagramSocket();
            this.serverPort = serverPort;
            serverInetAddress = InetAddress.getByName(serverAddress);
            System.out.println("Connected to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        audioPlayer = new AudioPlayer();
        // Start a separate thread for listening to incoming data
        Thread receiveThread = new Thread(this::receiveData);
        receiveThread.start();
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
            System.out.println("Request sent to server: " + request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {
        try {
            // Define a buffer for streaming audio data
            byte[] buffer = new byte[1024]; // You can adjust the buffer size

            // Create a new Opus decoder instance with the same parameters as the encoder
            OpusDecoder decoder = new OpusDecoder(48000, 1);
            decoder.setFrameSize(960); // Set the frame size

            boolean connectionLost = false;

            while (!connectionLost) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(receivePacket);
                } catch (SocketTimeoutException e) {
                    //TODO Handle a timeout exception

                    // Set connectionLost to true to exit the loop
                    connectionLost = true;
                    System.out.println("Lost connection to server");
                    continue;
                }

                // Extract the total audio length from the header (first 4 bytes)
                ByteBuffer headerBuffer = ByteBuffer.wrap(receivePacket.getData(), 0, 4);
                int totalAudioLength = headerBuffer.getInt();
                System.out.println("Total length: " + totalAudioLength);

                // Decode the received audio data (skip the first 4 bytes for the header)
                byte[] audioData = Arrays.copyOfRange(receivePacket.getData(), 4, receivePacket.getLength());
                System.out.println("Audio data length: " + audioData.length);


                short[] decoded = decoder.decode(audioData);

                audioPlayer.write(decoded);
            }

            decoder.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (de.maxhenkel.opus4j.UnknownPlatformException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        if (socket != null) {
            socket.close();
            System.out.println("Connection closed.");
        }
    }

    public static void main(String[] args) {
        VoiceClient client = new VoiceClient("localhost", 12346);

        // Send the first request
        String request1 = "1/1aledarohyoufell!trydoingitagain.";
        client.sendRequest(request1);

        // Add a delay if needed
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.audioPlayer.stopPlayingSound();

        // Send the second request
        String request2 = "1/2guard...wait.whatisthat?!";
        client.sendRequest(request2);

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // Close the connection when done
        client.closeConnection();
    }
}
