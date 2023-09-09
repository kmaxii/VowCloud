package me.kmaxi.vowcloud;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioPacket {

    private final byte[] audioData;

    private final int totalAudioLength;

    public AudioPacket(DatagramPacket packet) {
        byte[] data = packet.getData();

        // Extract the total audio length from the header (first 4 bytes)
        ByteBuffer headerBuffer = ByteBuffer.wrap(data, 0, 4);
        totalAudioLength = headerBuffer.getInt();

        // Decode the received audio data (skip the first 4 bytes for the header)
        audioData = Arrays.copyOfRange(data, 4, packet.getLength());
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public int getTotalAudioLength() {
        return totalAudioLength;
    }
}
