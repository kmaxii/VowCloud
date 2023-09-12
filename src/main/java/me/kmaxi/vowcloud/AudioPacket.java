package me.kmaxi.vowcloud;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioPacket {
    private final byte[] audioData;
    private final int totalAudioLength;
    private final boolean isMovingSound;

    public AudioPacket(DatagramPacket packet) {
        byte[] data = packet.getData();

        // Extract the total audio length from the header (4 bytes)
        ByteBuffer headerBuffer = ByteBuffer.wrap(data, 0, 5);
        totalAudioLength = headerBuffer.getInt();

        // Extract the boolean flag (1 byte) right after the total audio length
        isMovingSound = data[4] == 1;

        // Decode the received audio data (skip the first 5 bytes for the header and boolean)
        audioData = Arrays.copyOfRange(data, 5, packet.getLength());
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public int getTotalAudioLength() {
        return totalAudioLength;
    }

    public boolean isMovingSound() {
        return isMovingSound;
    }
}
