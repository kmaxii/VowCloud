package me.kmaxi.vowcloud.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;

public class VowConfig {

    public final ConfigEntry<Boolean> enabled;

    public final ConfigEntry<Boolean> autoProgress;

    public final ConfigEntry<Float> autoProgressDelay;

    public final ConfigEntry<Boolean> sendFunFacts;

    public final ConfigEntry<Boolean> useLocalHostServer;

    public final ConfigEntry<Float> attenuationFactor;
    public final ConfigEntry<Float> reverbGain;
    public final ConfigEntry<Float> reverbBrightness;

    public VowConfig(ConfigBuilder builder) {
        enabled = builder.booleanEntry("enabled", true)
                .comment("Enable or disable the mod");

        autoProgress = builder.booleanEntry("autoProgress", true)
                .comment("Automatically progress the text");

        autoProgressDelay = builder.floatEntry("autoProgressDelay", 0.1F)
                .comment("The delay to add until progressed when audio finished playing");

        sendFunFacts = builder.booleanEntry("sendFunFacts", true)
                .comment("Sends a fun fact in chat when joining Wynncraft");

        useLocalHostServer = builder.booleanEntry("useLocalHostServer", false)
                .comment("Use a local host server for the mod. ONLY SET THIS TO TRUE IF YOU KNOW WHAT YOU ARE DOING!");


        attenuationFactor = builder
                .floatEntry("attenuation_factor", 1F, 0.1F, 1F)
                .comment(
                        "Affects how quiet a sound gets based on distance",
                        "Lower values mean distant sounds are louder",
                        "This setting requires you to be in singleplayer or having the mod installed on the server",
                        "1.0 is the physically correct value"
                );
        reverbGain = builder
                .floatEntry("reverb_gain", 1F, 0.1F, 2F)
                .comment("The volume of simulated reverberations");
        reverbBrightness = builder.floatEntry("reverb_brightness", 1F, 0.1F, 2F)
                .comment(
                        "The brightness of reverberation",
                        "Higher values result in more high frequencies in reverberation",
                        "Lower values give a more muffled sound to the reverb"
                );
    }

}
