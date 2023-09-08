package me.kmaxi.vowcloud;

import de.maxhenkel.opus4j.OpusDecoder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class VoiceClient {
    private DatagramSocket socket;
    private final AudioFormat audioFormat;

    private InetAddress serverInetAddress;
    private int serverPort;

    public VoiceClient(String serverAddress, int serverPort) {
        try {
            socket = new DatagramSocket();
            this.serverPort = serverPort;
            serverInetAddress = InetAddress.getByName(serverAddress);
            System.out.println("Connected to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioFormat = new AudioFormat(48000, 16, 1, true, false);

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
            // Create an audio line for streaming playback
            SourceDataLine line = AudioSystem.getSourceDataLine(audioFormat);
            line.open(audioFormat);
            line.start();

            // Define a buffer for streaming audio data
            byte[] buffer = new byte[960]; // You can adjust the buffer size


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

                // Decode the received audio data
                short[] decoded = decoder.decode(receivePacket.getData());

                // Write the decoded audio data to the audio line for playback
                line.write(encodeShortsToBytes(decoded), 0, decoded.length * 2); // 16-bit samples, so multiply by 2
            }

        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        } catch (de.maxhenkel.opus4j.UnknownPlatformException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper function to convert short[] to byte[]
    private byte[] encodeShortsToBytes(short[] shorts) {
        byte[] bytes = new byte[shorts.length * 2]; // 16-bit samples, so multiply by 2
        for (int i = 0; i < shorts.length; i++) {
            bytes[i * 2] = (byte) (shorts[i] & 0xFF);
            bytes[i * 2 + 1] = (byte) (shorts[i] >> 8);
        }
        return bytes;
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
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Send the second request
        String request2 = "1/2guard...wait.whatisthat?!";
        client.sendRequest(request2);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Continue with other tasks or user interaction
        // ...

        // Close the connection when done
        client.closeConnection();
    }
}
