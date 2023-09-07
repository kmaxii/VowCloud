package me.kmaxi.vowcloud;


import de.maxhenkel.opus4j.OpusDecoder;
import de.maxhenkel.opus4j.OpusEncoder;
import de.maxhenkel.opus4j.UnknownPlatformException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class VoiceClient {
    private Socket socket;
    private DataOutputStream out;
    private ObjectInputStream in;
    private final AudioFormat audioFormat;

    public VoiceClient(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new DataOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
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

        if (out == null)
            return;

        try {
            out.writeUTF(request);
            out.flush();
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

            while (true) {
                int bytesRead = in.read(buffer);

                if (bytesRead == -1) {
                    break; // Connection closed
                }

                // Decode the received audio data
                short[] decoded = decoder.decode(buffer);
                // Decode a missing packet with FEC (Forward Error Correction)
             //   decoded = decoder.decodeFec();

                // Write the decoded audio data to the audio line for playback
                line.write(encodeShortsToBytes(decoded), 0, decoded.length * 2); // 16-bit samples, so multiply by 2

            }

            // Close the audio line and the decoder when done
            line.drain();
            line.close();
            decoder.close();
        } catch (IOException | LineUnavailableException | UnknownPlatformException e) {
            e.printStackTrace();
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
        try {
            if (socket != null) {
                socket.close();
                System.out.println("Connection closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
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

