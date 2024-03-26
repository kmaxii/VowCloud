package me.kmaxi.vowcloud.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;

public class VowConfig {

    public final ConfigEntry<Boolean> enabled;

    public final ConfigEntry<Boolean> autoProgress;

    public final ConfigEntry<Float> autoProgressDelay;

    public final ConfigEntry<Boolean> sendFunFacts;

    public final ConfigEntry<Boolean> useLocalHostServer;

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
    }

}
