package me.kmaxi.vowcloud;

import net.minecraft.world.phys.Vec3;
import org.lwjgl.openal.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;

public class OpenAlPlayer {
    private long device;
    private long context;
    private int sourceID;
    private final ExecutorService executorService;

    protected volatile int bufferIndex;

    protected final int[] buffers;

    private static final int bufferSize = 960;
    private static final int sampleRate = 48000;
    private static final int bufferSampleSize = 960;

    private static final float maxDistance = 20000;


    public OpenAlPlayer() {
        this.buffers = new int[3000];

        executorService = Executors.newSingleThreadExecutor();
        createOpelAL();
    }

    private void createOpelAL() {

        executorService.execute(() -> {
            // Initialize OpenAL
            device = ALC11.alcOpenDevice((ByteBuffer) null);
            System.out.println("Device is: " + device);
            ALCCapabilities deviceCaps = ALC.createCapabilities(device);
            System.out.println("Device caps are: " + deviceCaps.alcOpenDevice);
            context = alcCreateContext(device, (IntBuffer) null);
            System.out.println("Context is: " + context);

            alcMakeContextCurrent(context);

            AL.createCapabilities(deviceCaps);

            // Create an OpenAL source
            sourceID = AL10.alGenSources();
            System.out.println("Source id: " + sourceID);


            // Create an OpenAL buffer
            AL10.alGenBuffers(buffers);
            checkAlError();
            AL11.alSourcei(sourceID, AL11.AL_LOOPING, AL11.AL_FALSE);
            checkAlError();
            AL11.alDistanceModel(AL11.AL_LINEAR_DISTANCE);
            checkAlError();
            AL11.alSourcef(sourceID, AL11.AL_MAX_DISTANCE, maxDistance);
            checkAlError();
            AL11.alSourcef(sourceID, AL11.AL_REFERENCE_DISTANCE, 0F);
            checkAlError();

        });


    }


    public void playAudio(short[] pcmData) {
        executorService.execute(() -> {

            removeProcessedBuffersSync();

            writeSync(pcmData, 1F);

            startPlayingIfStoppedSync();

        });

    }

    private void startPlayingIfStoppedSync(){
        int state = AL11.alGetSourcei(sourceID, AL11.AL_SOURCE_STATE);
        boolean stopped = state == AL11.AL_INITIAL || state == AL11.AL_STOPPED || AL11.alGetSourcei(sourceID, AL11.AL_BUFFERS_QUEUED) <= 0;

        if (stopped) {
            AL11.alSourcePlay(sourceID);
        }
    }

    public void stopAudio(){
        executorService.execute(this::stopPlayingSync);
    }

    private void stopPlayingSync(){
        AL11.alSourceStop(sourceID);
    }

    private void writeSync(short[] data, float volume) {
        //setPositionSync(Optional.of(new Vec3(-1570, 51, -1632)));
        setPositionSync(Optional.empty());

        AL11.alSourcef(sourceID, AL11.AL_MAX_GAIN, 6F);
        AL11.alSourcef(sourceID, AL11.AL_GAIN, volume);
        AL11.alListenerf(AL11.AL_GAIN, 1F);

        int queuedBuffers = AL11.alGetSourcei(sourceID, AL11.AL_BUFFERS_QUEUED);
        if (queuedBuffers >= buffers.length) {
            System.out.println("WARNING! AUDIO BUFFERS RAN OUT");
            int sampleOffset = AL11.alGetSourcei(sourceID, AL11.AL_SAMPLE_OFFSET);
            int buffersToSkip = queuedBuffers - 100;
            AL11.alSourcei(sourceID, AL11.AL_SAMPLE_OFFSET, sampleOffset + buffersToSkip * bufferSampleSize);
            removeProcessedBuffersSync();
        }

        AL11.alBufferData(buffers[bufferIndex], AL11.AL_FORMAT_MONO16, data, sampleRate);
        AL11.alSourceQueueBuffers(sourceID, buffers[bufferIndex]);
        bufferIndex = (bufferIndex + 1) % buffers.length;
    }

    public void setPosition(Optional<Vec3> soundPos) {
        executorService.execute(() -> {
            setPositionSync(soundPos);
        });
    }


    private void setPositionSync(Optional<Vec3> soundPos) {


        soundPos.ifPresentOrElse((pos) -> {
            AL11.alSourcei(sourceID, AL11.AL_SOURCE_RELATIVE, AL11.AL_FALSE);

            AL11.alSource3f(sourceID, AL11.AL_POSITION, (float) pos.x, (float) pos.y, (float) pos.z);
        },  () -> {

            AL11.alSourcei(sourceID, AL11.AL_SOURCE_RELATIVE, AL11.AL_TRUE);

            AL11.alSource3f(sourceID, AL11.AL_POSITION, (float) 0, (float) 0, (float) 0);
        });




    }


    private void removeProcessedBuffersSync() {
        int processed = AL11.alGetSourcei(sourceID, AL11.AL_BUFFERS_PROCESSED);
        for (int i = 0; i < processed; i++) {
            AL11.alSourceUnqueueBuffers(sourceID);
        }
    }


    public void cleanup() {
        AL10.alDeleteSources(sourceID);
        //  AL10.alDeleteBuffers(bufferID);
        ALC11.alcDestroyContext(context);
        ALC11.alcCloseDevice(device);
        executorService.shutdown(); // Shutdown the executor
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        OpenAlPlayer audioPlayer = new OpenAlPlayer();

        int sampleRate = 48000;
        int channels = 1;
        String soundLoc = "acquiringcredentials-barman-2.wav";
        String soundLoc2 = "acquiringcredentials-barman-1.wav";
        short[] pcmData = AudioConverter.convert(Path.of(soundLoc));

        int packetSize = 480;

        //audioPlayer.startPlayback();


        audioPlayer.playAudio(pcmData);


        pcmData = AudioConverter.convert(Path.of(soundLoc2));


        audioPlayer.playAudio(pcmData);


        // Test playing audio split into packets like it is from the server

        for (int i = 0; i < pcmData.length; i += packetSize) {
            int endIndex = Math.min(i + packetSize, pcmData.length);
            short[] packet = Arrays.copyOfRange(pcmData, i, endIndex);

            audioPlayer.playAudio(packet);
        }


        // Sleep for some time to allow audio playback to finish

        //     audioPlayer.cleanup();
    }

    public static boolean checkAlError() {
        int error = AL11.alGetError();
        if (error == AL11.AL_NO_ERROR) {
            return false;
        }
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        System.out.println("Voicechat sound manager Al error: " + stack.getClassName() + "." + stack.getMethodName() + "[" + stack.getLineNumber() + "] " + getAlError(error));
        return true;
    }

    private static String getAlError(int i) {
        switch (i) {
            case AL11.AL_INVALID_NAME:
                return "Invalid name";
            case AL11.AL_INVALID_ENUM:
                return "Invalid enum ";
            case AL11.AL_INVALID_VALUE:
                return "Invalid value";
            case AL11.AL_INVALID_OPERATION:
                return "Invalid operation";
            case AL11.AL_OUT_OF_MEMORY:
                return "Out of memory";
            default:
                return "Unknown error";
        }
    }
}