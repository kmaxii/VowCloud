package me.kmaxi.vowcloud;

import de.maxhenkel.opus4j.OpusDecoder;
import de.maxhenkel.opus4j.UnknownPlatformException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioPlayer {

    private final OpusDecoder decoder;

    private int lastPlayedSoundLength;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public final OpenAlPlayer openAlPlayer;

    private LineData lastSentLineData;

    public void onNpcDialogue(LineData lineData) {
        lastSentLineData = lineData;
    }


    public AudioPlayer() {
        openAlPlayer = new OpenAlPlayer();

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
    }


    public void play(AudioPacket audioPacket) {

        int toPlayLength = audioPacket.getTotalAudioLength();


        if (toPlayLength != lastPlayedSoundLength) {
            handleNewAudioStarted(audioPacket);
        }

        lastPlayedSoundLength = toPlayLength;

        //We run the playing on a different thread as the playing of sound slows down the thread it is running on
        executorService.execute(() -> {

            short[] decoded = decoder.decode(audioPacket.getAudioData());

            write(decoded);
        });
    }

    private void handleNewAudioStarted(AudioPacket audioPacket) {

        stopPlayingCurrentSound();
        openAlPlayer.updateSpeaker(audioPacket.isMovingSound() ? "" : lastSentLineData.getNPCName());
    }

    public void stopPlayingCurrentSound() {
        openAlPlayer.stopAudio();
    }





}
