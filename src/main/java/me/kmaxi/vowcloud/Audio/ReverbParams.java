package me.kmaxi.vowcloud.Audio;


import me.kmaxi.vowcloud.VowCloud;

public class ReverbParams {
    public float decayTime;                 // min: 0.1F 	max: 10.0F
    public float density;                   // min: 0.0F 	max: 1.0F
    public float diffusion;                 // min: 0.0F 	max: 1.0F
    public float gain;                      // min: 0.0F 	max: 1.0F
    public float gainHF;                    // min: 0.0F 	max: 1.0F
    public float decayHFRatio;              // min: 0.1F 	max: 2.0F
    public float reflectionsGain;           // min: 0.1F 	max: 3.16F
    public float reflectionsDelay;          // min: 0.0F 	max: 0.3F
    public float lateReverbGain;            // min: 0.0F 	max: 10.0F
    public float lateReverbDelay;           // min: 0.0F 	max: 0.1F
    public float airAbsorptionGainHF;       // min: 0.892F 	max: 1.0F
    public float roomRolloffFactor;         // min: 0.0F	max: 10.0F
    public static ReverbParams getReverbParams(int id){
        ReverbParams r = new ReverbParams();
        switch (id){
            case 0:
                r.decayTime = 0.15F;
                r.density = 0F;
                r.diffusion = 1F;
                r.gain = 0.2F * globalReverbMultiplier() * 0.85F;
                r.gainHF = 0.99F;
                r.decayHFRatio = 0.6F * VowCloud.CONFIG.reverbBrightness.get();
                r.reflectionsGain = 2.5F;
                r.reflectionsDelay = 0.001F;
                r.lateReverbDelay = 0.011F;
                r.roomRolloffFactor = 0.16F * VowCloud.CONFIG.attenuationFactor.get();
                break;
            case 1:
                r.decayTime = 0.55F;
                r.density = 0F;
                r.diffusion = 1F;
                r.gain = 0.3f * globalReverbMultiplier() * 0.85F;
                r.gainHF = 0.99F;
                r.decayHFRatio = 0.7F * VowCloud.CONFIG.reverbBrightness.get();
                r.reflectionsGain = 0.2F;
                r.reflectionsDelay = 0.015F;
                r.lateReverbDelay = 0.011F;
                r.roomRolloffFactor = 0.15F * VowCloud.CONFIG.attenuationFactor.get();
                break;
            case 2:
                r.decayTime = 1.68F;
                r.density = 0.1F;
                r.diffusion = 1F;
                r.gain = 0.5F * globalReverbMultiplier() * 0.85F;
                r.gainHF = 0.99F;
                r.decayHFRatio = 0.7F * VowCloud.CONFIG.reverbBrightness.get();
                r.reflectionsGain = 0.F;
                r.reflectionsDelay = 0.021F;
                r.lateReverbDelay = 0.021F;
                r.roomRolloffFactor = 0.13F * VowCloud.CONFIG.attenuationFactor.get();
                break;
            case 3:
                r.decayTime = 4.142F;
                r.density = 0.5F;
                r.diffusion = 1F;
                r.gain = 0.4f * globalReverbMultiplier() * 0.85F;
                r.gainHF = 0.89F;
                r.decayHFRatio = 0.7F * VowCloud.CONFIG.reverbBrightness.get();
                r.reflectionsGain = 0F;
                r.reflectionsDelay = 0.025F;
                r.lateReverbDelay = 0.021F;

                r.roomRolloffFactor = 0.11F * VowCloud.CONFIG.attenuationFactor.get();
                break;
        }

        r.lateReverbGain = 1.26F;
        r.airAbsorptionGainHF = 0.994F;

        return r;
    }
        private static float globalReverbMultiplier() {
        return 0.7F * VowCloud.CONFIG.reverbGain.get();
    }
}
