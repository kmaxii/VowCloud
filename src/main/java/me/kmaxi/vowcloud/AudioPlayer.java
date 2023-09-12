package me.kmaxi.vowcloud;

import de.maxhenkel.opus4j.OpusDecoder;
import de.maxhenkel.opus4j.UnknownPlatformException;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioPlayer {


    private static final AudioFormat audioFormat = new AudioFormat(48000, 16, 1, true, false);

    private final SourceDataLine line;
    private final FloatControl gainControl;
    private final OpusDecoder decoder;

    private int lastPlayedSoundLength;
    private int stopPlayingSoundWithLength;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final OpenAlPlayer openAlPlayer;

    public AudioPlayer() {
        openAlPlayer = new OpenAlPlayer();

        // Create an audio line for streaming playback
        try {
            line = AudioSystem.getSourceDataLine(audioFormat);
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        line.start();
        gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);

        // Create a new Opus decoder instance with the same parameters as the encoder
        try {
            decoder = new OpusDecoder(48000, 1);
        } catch (IOException | UnknownPlatformException e) {
            throw new RuntimeException(e);
        }
        decoder.setFrameSize(960);

    }

    private void write(short[] data) {
        openAlPlayer.playAudio(data);

    //    write(encodeShortsToBytes(data));
    }

    private void write(byte[] data) {
        line.write(data, 0, data.length); // 16-bit samples, so multiply by 2
    }

    public void play(AudioPacket audioPacket) {

        int toPlayLength = audioPacket.getTotalAudioLength();

        if (toPlayLength == stopPlayingSoundWithLength) {
            return;
        }

        if (toPlayLength != lastPlayedSoundLength) {
            stopPlayingCurrentSound();

        }

        lastPlayedSoundLength = toPlayLength;

        //We run the playing on a different thread as the playing of sound slows down the thread it is running on
        executorService.execute(() ->{

            //As it gets slowed down, we need to check this again because once this code is queued to run
            //This audio might have already been cancelled
            if (toPlayLength == stopPlayingSoundWithLength) {
                return;
            }
            short[] decoded = decoder.decode(audioPacket.getAudioData());

            write(decoded);
        });



    }

    public void stopPlayingCurrentSound() {
        openAlPlayer.stopAudio();
    /*    line.stop();
        line.flush();
        line.start();
        stopPlayingSoundWithLength = lastPlayedSoundLength;
        System.out.println("Stop");*/
    }

    /**
     * Set the volume.
     *
     * @param volume A percentage value between 0 and 100.
     */
    public void setVolume(int volume) {
        gainControl.setValue(mapPercentageToGain(volume));
    }

    public void close() {
        line.close();
        decoder.close();
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
