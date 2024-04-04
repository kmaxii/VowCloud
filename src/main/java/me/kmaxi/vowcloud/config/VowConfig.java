package me.kmaxi.vowcloud.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;

public class VowConfig {

    public final ConfigEntry<Boolean> enabled;

    public final ConfigEntry<Boolean> autoProgress;

    public final ConfigEntry<Float> autoProgressDelay;

    public final ConfigEntry<Boolean> sendFunFacts;

    public final ConfigEntry<Boolean> useLocalHostServer;

    public final ConfigEntry<Boolean> physicsEnabled;

    public final ConfigEntry<Float> attenuationFactor;
    public final ConfigEntry<Float> reverbGain;
    public final ConfigEntry<Float> reverbBrightness;
    public final ConfigEntry<Float> blockAbsorption;
    public final ConfigEntry<Float> occlusionVariation;
    public final ConfigEntry<Float> defaultBlockReflectivity;
    public final ConfigEntry<Float> defaultBlockOcclusionFactor;
    public final ConfigEntry<Float> airAbsorption;
    public final ConfigEntry<Float> underwaterFilter;

    public final ConfigEntry<Integer> environmentEvaluationRayCount;
    public final ConfigEntry<Integer> environmentEvaluationRayBounces;
    public final ConfigEntry<Float> nonFullBlockOcclusionFactor;
    public final ConfigEntry<Integer> maxOcclusionRays;
    public final ConfigEntry<Float> maxOcclusion;
    public final ConfigEntry<Boolean> strictOcclusion;
    public final ConfigEntry<Boolean> soundDirectionEvaluation;
    public final ConfigEntry<Boolean> redirectNonOccludedSounds;

    public final ConfigEntry<Boolean> renderSoundBounces;
    public final ConfigEntry<Boolean> renderOcclusion;


    public VowConfig(ConfigBuilder builder) {
        enabled = builder.booleanEntry("enabled", true)
                .comment("Enable or disable the mod");
        physicsEnabled = builder.booleanEntry("reverbEnabled", true)
                .comment("Enable or disable the reverb and sound physics");

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
        blockAbsorption = builder.floatEntry("block_absorption", 1F, 0.1F, 4F)
                .comment("The amount of sound that will be absorbed when traveling through blocks");
        occlusionVariation = builder.floatEntry("occlusion_variation", 0.35F, 0F, 16F)
                .comment("Higher values mean smaller objects won't be considered as occluding");
        defaultBlockReflectivity = builder.floatEntry("default_block_reflectivity", 0.5F, 0.1F, 4F)
                .comment(
                        "The default amount of sound reflectance energy for all blocks",
                        "Lower values result in more conservative reverb simulation with shorter reverb tails",
                        "Higher values result in more generous reverb simulation with higher reverb tails"
                );
        defaultBlockOcclusionFactor = builder.floatEntry("default_block_occlusion_factor", 1F, 0F, 10F)
                .comment(
                        "The default amount of occlusion for all blocks",
                        "Lower values will result in sounds being less muffled through walls",
                        "Higher values mean sounds will be not audible though thicker walls"
                );
        airAbsorption = builder.floatEntry("air_absorption", 1F, 0F, 5F)
                .comment(
                        "A value controlling the amount that air absorbs high frequencies with distance",
                        "A value of 1.0 is physically correct for air with normal humidity and temperature",
                        "Higher values mean air will absorb more high frequencies with distance",
                        "0 disables this effect"
                );
        underwaterFilter = builder.floatEntry("underwater_filter", 0.25F, 0F, 1F)
                .comment(
                        "How much sound is filtered when the player is underwater",
                        "0.0 means no filter",
                        "1.0 means fully filtered"
                );


        environmentEvaluationRayCount = builder.integerEntry("environment_evaluation_ray_count", 32, 8, 64)
                .comment(
                        "The number of rays to trace to determine reverberation for each sound source",
                        "More rays provides more consistent tracing results but takes more time to calculate",
                        "Decrease this value if you experience lag spikes when sounds play"
                );
        environmentEvaluationRayBounces = builder.integerEntry("environment_evaluation_ray_bounces", 4, 2, 64)
                .comment(
                        "The number of rays bounces to trace to determine reverberation for each sound source",
                        "More bounces provides more echo and sound ducting but takes more time to calculate",
                        "Decrease this value if you experience lag spikes when sounds play"
                );
        nonFullBlockOcclusionFactor = builder.floatEntry("non_full_block_occlusion_factor", 0.25F, 0F, 1F)
                .comment("If sound hits a non-full-square side, block occlusion is multiplied by this");
        maxOcclusionRays = builder.integerEntry("max_occlusion_rays", 16, 1, 128)
                .comment(
                        "The maximum amount of rays to determine occlusion",
                        "Directly correlates to the amount of blocks between walls that are considered"
                );
        maxOcclusion = builder.floatEntry("max_occlusion", 64F, 0F, 1024F)
                .comment("The amount at which occlusion is capped");
        strictOcclusion = builder.booleanEntry("strict_occlusion", false)
                .comment("If enabled, the occlusion calculation only uses one path between the sound source and the listener instead of 9");
        soundDirectionEvaluation = builder.booleanEntry("sound_direction_evaluation", true)
                .comment("Whether to try calculating where the sound should come from based on reflections");
        redirectNonOccludedSounds = builder.booleanEntry("redirect_non_occluded_sounds", true)
                .comment("Skip redirecting non-occluded sounds (the ones you can see directly)");

        renderSoundBounces = builder.booleanEntry("render_sound_bounces", false)
                .comment("If enabled, the path of the sound will be rendered in game");
        renderOcclusion = builder.booleanEntry("render_occlusion", false)
                .comment("If enabled, occlusion will be visualized in game");

    }

}
