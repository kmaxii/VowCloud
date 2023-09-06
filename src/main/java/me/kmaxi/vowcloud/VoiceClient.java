package me.kmaxi.vowcloud;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
        audioFormat = new AudioFormat(44100.0f, 16, 1, true, false);

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
            byte[] buffer = new byte[1024]; // You can adjust the buffer size

            while (true) {
                var obj = in.readObject();

                if (!(obj instanceof byte[] audioData)) {
                    System.out.println("is not byte");
                    continue;
                }
                // Write the received audio data to the audio line for playback
                line.write(audioData, 0, audioData.length);
            }
        } catch (IOException | ClassNotFoundException | LineUnavailableException e) {
            e.printStackTrace();
        }
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
        VoiceClient client = new VoiceClient("localhost", 12345);


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

