package me.kmaxi.vowcloud.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class IntegratedConfig extends MidnightConfig {
    @Comment
    public static Comment text1;                       // Comments are rendered like an option without a button and are excluded from the config file
    @Entry(name = "Auto progress")
    public static boolean autoProgress = false;               // Example for a boolean option
    @Entry(name = "Auto progress delay")
    public static float autoProgressDelay = 0.1f; // And so can floats! Precision defines the amount of decimal places

}
