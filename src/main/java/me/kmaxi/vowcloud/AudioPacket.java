package me.kmaxi.vowcloud;

import me.kmaxi.vowcloud.utils.Utils;
import net.minecraft.world.phys.Vec3;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioPacket {
    private final byte[] audioData;
    private final int totalAudioLength;
    private final boolean isMovingSound;

    private Vec3 position;
    private int fallOff;

    public AudioPacket(DatagramPacket packet) {
        byte[] data = packet.getData();

        // Extract the total audio length from the header (4 bytes)
        ByteBuffer headerBuffer = ByteBuffer.wrap(data, 0, 21);
        totalAudioLength = headerBuffer.getInt();

        // Extract the boolean flag (1 byte) right after the total audio length
        isMovingSound = data[4] == 1;

        headerBuffer.position(5);
        fallOff = headerBuffer.getInt();

        float x = headerBuffer.getFloat();
        float y = headerBuffer.getFloat();
        float z = headerBuffer.getFloat();

        position = new Vec3(x, y, z);

  //      Utils.sendMessage("Is moving sound: " + isMovingSound + " fallOff: " + fallOff + " Position: " + position);

        // Decode the received audio data (skip the first 21 bytes for the header)
        audioData = Arrays.copyOfRange(data, 21, packet.getLength());
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

    public Vec3 getPosition() {
        return position;
    }

    public int getFallOff() {
        return fallOff;
    }
}
