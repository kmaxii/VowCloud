package me.kmaxi.vowcloud.Audio;

import me.kmaxi.vowcloud.Loggers;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;

import static org.lwjgl.openal.AL11.alSource3i;
import static org.lwjgl.openal.EXTEfx.*;

public class SoundEffects {

    private int maxAuxSends;
    private int sourceID;

    private int[] auxFXSlots = new int[4];
    private int[] reverbs = new int[4];

    private int directFilter;

    private int[] sendFilters = new int[4];


    public SoundEffects(int sourceId) {
        long currentContext = ALC10.alcGetCurrentContext();
        long currentDevice = ALC10.alcGetContextsDevice(currentContext);

        if (ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
            Loggers.log("EFX Extension recognized");
        } else {
            Loggers.log("EFX Extension not found on current device. Aborting.");
            return;
        }

        this.maxAuxSends = ALC10.alcGetInteger(currentDevice, EXTEfx.ALC_MAX_AUXILIARY_SENDS);
        Loggers.log("Max auxiliary sends: " + maxAuxSends);

        this.sourceID = sourceId;

        setupEFX();


        // Apply EFX effect
        int echoSlot = alGenAuxiliaryEffectSlots();
        int reverbSlot = alGenAuxiliaryEffectSlots();
        // Add a big echo effect
        int echoEffect = alGenEffects();
        int reverbEffect = alGenEffects();

        // Set up the effect parameters, for example, reverb
        alEffecti(reverbEffect, AL_EFFECT_TYPE, AL_EFFECT_REVERB);
        alEffectf(reverbEffect, AL_REVERB_DENSITY, 1.0f); // Maximize the density for extreme effect

        alEffecti(echoEffect, AL_EFFECT_TYPE, AL_EFFECT_ECHO);
        alEffectf(echoEffect, AL_ECHO_DELAY, 0.1f); // Adjust delay for the desired echo effect
        alEffectf(echoEffect, AL_ECHO_LRDELAY, 0.1f); // Adjust left/right delay for stereo effect
        alEffectf(echoEffect, AL_ECHO_DAMPING, 0.5f); // Adjust damping for the echo effect

        alAuxiliaryEffectSloti(reverbSlot, AL_EFFECTSLOT_EFFECT, reverbEffect);
        alAuxiliaryEffectSloti(echoSlot, AL_EFFECTSLOT_EFFECT, echoEffect);

        alSource3i(sourceID, AL_AUXILIARY_SEND_FILTER, echoSlot, 0, AL_FILTER_NULL);
        alSource3i(sourceID, AL_AUXILIARY_SEND_FILTER, reverbSlot, 1, AL_FILTER_NULL);



/*        alSource3i(sourceId, AL_AUXILIARY_SEND_FILTER, auxFXSlots[0], 0, AL_FILTER_NULL);
        alSource3i(sourceId, AL_AUXILIARY_SEND_FILTER, auxFXSlots[1], 1, AL_FILTER_NULL);
        alSource3i(sourceId, AL_AUXILIARY_SEND_FILTER, auxFXSlots[2], 2, AL_FILTER_NULL);
        alSource3i(sourceId, AL_AUXILIARY_SEND_FILTER, auxFXSlots[3], 3, AL_FILTER_NULL);*/
    }

    private void setupEFX() {
        for (int i = 0; i < 4; i++) {
            auxFXSlots[i] = EXTEfx.alGenAuxiliaryEffectSlots();
            EXTEfx.alAuxiliaryEffectSloti(auxFXSlots[i], EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL11.AL_TRUE);
        }

        for (int i = 0; i < 4; i++) {
            reverbs[i] = EXTEfx.alGenEffects();
            EXTEfx.alEffecti(reverbs[i], EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_EAXREVERB);
        }

        directFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(directFilter, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);

        for (int i = 0; i < 4; i++) {
            sendFilters[i] = EXTEfx.alGenFilters();
            EXTEfx.alFilteri(sendFilters[i], EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
        }
        syncReverbParams();
    }

    private void syncReverbParams() {
        //Set the global reverb parameters and apply them to the effect and effectslot
        setReverbParams(ReverbParams.getReverb0(), auxFXSlots[0], reverbs[0]);
        setReverbParams(ReverbParams.getReverb1(), auxFXSlots[1], reverbs[1]);
        setReverbParams(ReverbParams.getReverb2(), auxFXSlots[2], reverbs[2]);
        setReverbParams(ReverbParams.getReverb3(), auxFXSlots[3], reverbs[3]);

    }

    protected static void setReverbParams(ReverbParams r, int auxFXSlot, int reverbSlot) {
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_DENSITY, r.density);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_DIFFUSION, r.diffusion);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_GAIN, r.gain);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_GAINHF, r.gainHF);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_DECAY_TIME, r.decayTime);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_DECAY_HFRATIO, r.decayHFRatio);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_REFLECTIONS_GAIN, r.reflectionsGain);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_LATE_REVERB_GAIN, r.lateReverbGain);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_LATE_REVERB_DELAY, r.lateReverbDelay);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_AIR_ABSORPTION_GAINHF, r.airAbsorptionGainHF);
        EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, r.roomRolloffFactor);

        // Attach updated effect object
        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverbSlot);
    }
}
