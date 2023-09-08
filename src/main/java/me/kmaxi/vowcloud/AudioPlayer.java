package me.kmaxi.vowcloud;

import javax.sound.sampled.*;

public class AudioPlayer {


    private static final AudioFormat audioFormat = new AudioFormat(48000, 16, 1, true, false);

    private final SourceDataLine line;
    private final FloatControl gainControl;

    public AudioPlayer() {
        // Create an audio line for streaming playback
        try {
            line = AudioSystem.getSourceDataLine(audioFormat);
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        line.start();
        gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);

    }

    public void write(short[] data) {
        write(encodeShortsToBytes(data));
    }
    public void write(byte[] data) {
        line.write(data, 0, data.length); // 16-bit samples, so multiply by 2
    }

    /**
     * Set the volume.
     * @param volume A percentage value between 0 and 100.
     */
    public void setVolume(int volume) {
        gainControl.setValue(mapPercentageToGain(volume));
    }

    public void close() {
        line.close();
    }
    private static final float maxGain = 6.0205994f;
    private static final float minGain = -80.0f;
    private static float mapPercentageToGain(int percentage) {
        // Ensure percentage is within the valid range [0, 100]
        percentage = Math.min(100, Math.max(0, percentage));

        // Map the percentage to a value within the gain range
        return minGain + (percentage / 100.0f) * (maxGain - minGain);
    }

    // Helper function to convert short[] to byte[]
    private static byte[] encodeShortsToBytes(short[] shorts) {
        byte[] bytes = new byte[shorts.length * 2]; // 16-bit samples, so multiply by 2
        for (int i = 0; i < shorts.length; i++) {
            bytes[i * 2] = (byte) (shorts[i] & 0xFF);
            bytes[i * 2 + 1] = (byte) (shorts[i] >> 8);
        }
        return bytes;
    }





}
