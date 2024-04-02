package me.kmaxi.vowcloud.config.integration;

import de.maxhenkel.configbuilder.entry.*;
import me.kmaxi.vowcloud.Audio.blocksound.BlockDefinition;
import me.kmaxi.vowcloud.Loggers;
import me.kmaxi.vowcloud.VowCloud;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClothConfigIntegration {

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder
                .create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("cloth_config.vow_cloud.settings"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("cloth_config.vow_cloud.category.general"));

        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.enabled"),
                Component.translatable("cloth_config.vow_cloud.enabled.description"),
                VowCloud.CONFIG.enabled
        ));

        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.auto_progress"),
                Component.translatable("cloth_config.vow_cloud.auto_progress.description"),
                VowCloud.CONFIG.autoProgress
        ));

        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.auto_progress_delay"),
                Component.translatable("cloth_config.vow_cloud.auto_progress_delay.description"),
                VowCloud.CONFIG.autoProgressDelay
        ));

        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.send_fun_facts"),
                Component.translatable("cloth_config.vow_cloud.send_fun_facts.description"),
                VowCloud.CONFIG.sendFunFacts
        ));




        ConfigCategory debug = builder.getOrCreateCategory(Component.translatable("cloth_config.vow_cloud.category.debug"));

        debug.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.use_local_host"),
                Component.translatable("cloth_config.vow_cloud.use_local_host.description"),
                VowCloud.CONFIG.useLocalHostServer
        ));


        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.attenuation_factor"),
                Component.translatable("cloth_config.sound_physics_remastered.attenuation_factor.description"),
                VowCloud.CONFIG.attenuationFactor
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.reverb_gain"),
                Component.translatable("cloth_config.sound_physics_remastered.reverb_gain.description"),
                VowCloud.CONFIG.reverbGain
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.reverb_brightness"),
                Component.translatable("cloth_config.sound_physics_remastered.reverb_brightness.description"),
                VowCloud.CONFIG.reverbBrightness
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.block_absorption"),
                Component.translatable("cloth_config.sound_physics_remastered.block_absorption.description"),
                VowCloud.CONFIG.blockAbsorption
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.occlusion_variation"),
                Component.translatable("cloth_config.sound_physics_remastered.occlusion_variation.description"),
                VowCloud.CONFIG.occlusionVariation
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.default_block_reflectivity"),
                Component.translatable("cloth_config.sound_physics_remastered.default_block_reflectivity.description"),
                VowCloud.CONFIG.defaultBlockReflectivity
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.default_block_occlusion_factor"),
                Component.translatable("cloth_config.sound_physics_remastered.default_block_occlusion_factor.description"),
                VowCloud.CONFIG.defaultBlockOcclusionFactor
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.sound_distance_allowance"),
                Component.translatable("cloth_config.sound_physics_remastered.sound_distance_allowance.description"),
                VowCloud.CONFIG.soundDistanceAllowance
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.air_absorption"),
                Component.translatable("cloth_config.sound_physics_remastered.air_absorption.description"),
                VowCloud.CONFIG.airAbsorption
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.underwater_filter"),
                Component.translatable("cloth_config.sound_physics_remastered.underwater_filter.description"),
                VowCloud.CONFIG.underwaterFilter
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.evaluate_ambient_sounds"),
                Component.translatable("cloth_config.sound_physics_remastered.evaluate_ambient_sounds.description"),
                VowCloud.CONFIG.evaluateAmbientSounds
        ));

        ConfigCategory performance = builder.getOrCreateCategory(Component.translatable("cloth_config.sound_physics_remastered.category.performance"));

        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.environment_evaluation_ray_count"),
                Component.translatable("cloth_config.sound_physics_remastered.environment_evaluation_ray_count.description"),
                VowCloud.CONFIG.environmentEvaluationRayCount
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.environment_evaluation_ray_bounces"),
                Component.translatable("cloth_config.sound_physics_remastered.environment_evaluation_ray_bounces.description"),
                VowCloud.CONFIG.environmentEvaluationRayBounces
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.non_full_block_occlusion_factor"),
                Component.translatable("cloth_config.sound_physics_remastered.non_full_block_occlusion_factor.description"),
                VowCloud.CONFIG.nonFullBlockOcclusionFactor
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.max_occlusion_rays"),
                Component.translatable("cloth_config.sound_physics_remastered.max_occlusion_rays.description"),
                VowCloud.CONFIG.maxOcclusionRays
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.max_occlusion"),
                Component.translatable("cloth_config.sound_physics_remastered.max_occlusion.description"),
                VowCloud.CONFIG.maxOcclusion
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.strict_occlusion"),
                Component.translatable("cloth_config.sound_physics_remastered.strict_occlusion.description"),
                VowCloud.CONFIG.strictOcclusion
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.sound_direction_evaluation"),
                Component.translatable("cloth_config.sound_physics_remastered.sound_direction_evaluation.description"),
                VowCloud.CONFIG.soundDirectionEvaluation
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.redirect_non_occluded_sounds"),
                Component.translatable("cloth_config.sound_physics_remastered.redirect_non_occluded_sounds.description"),
                VowCloud.CONFIG.redirectNonOccludedSounds
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.update_moving_sounds"),
                Component.translatable("cloth_config.sound_physics_remastered.update_moving_sounds.description"),
                VowCloud.CONFIG.updateMovingSounds
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.sound_update_interval"),
                Component.translatable("cloth_config.sound_physics_remastered.sound_update_interval.description"),
                VowCloud.CONFIG.soundUpdateInterval
        ));

        ConfigCategory reflectivity = builder.getOrCreateCategory(Component.translatable("cloth_config.sound_physics_remastered.category.reflectivity"));

        Map<BlockDefinition, Float> defaultReflectivityMap = new LinkedHashMap<>();
        VowCloud.REFLECTIVITY_CONFIG.addDefaults(defaultReflectivityMap);

        for (Map.Entry<BlockDefinition, Float> entry : VowCloud.REFLECTIVITY_CONFIG.getBlockDefinitions().entrySet()) {
            FloatListEntry e = entryBuilder
                    .startFloatField(entry.getKey().getName(), entry.getValue())
                    .setMin(0.01F)
                    .setMax(10F)
                    .setDefaultValue(defaultReflectivityMap.getOrDefault(entry.getKey(), VowCloud.CONFIG.defaultBlockReflectivity.get()))
                    .setSaveConsumer(value -> VowCloud.REFLECTIVITY_CONFIG.setBlockDefinitionValue(entry.getKey(), value)).build();
            reflectivity.addEntry(e);
        }

        ConfigCategory occlusion = builder.getOrCreateCategory(Component.translatable("cloth_config.sound_physics_remastered.category.occlusion"));

        Map<BlockDefinition, Float> defaultOcclusionMap = new LinkedHashMap<>();
        VowCloud.OCCLUSION_CONFIG.addDefaults(defaultOcclusionMap);

        for (Map.Entry<BlockDefinition, Float> entry : VowCloud.OCCLUSION_CONFIG.getBlockDefinitions().entrySet()) {
            FloatListEntry e = entryBuilder
                    .startFloatField(entry.getKey().getName(), entry.getValue())
                    .setMin(0F)
                    .setMax(10F)
                    .setDefaultValue(defaultOcclusionMap.getOrDefault(entry.getKey(), VowCloud.CONFIG.defaultBlockOcclusionFactor.get()))
                    .setSaveConsumer(value -> VowCloud.OCCLUSION_CONFIG.setBlockDefinitionValue(entry.getKey(), value)).build();
            occlusion.addEntry(e);
        }

        ConfigCategory logging = builder.getOrCreateCategory(Component.translatable("cloth_config.sound_physics_remastered.category.debug"));

        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.debug_logging"),
                Component.translatable("cloth_config.sound_physics_remastered.debug_logging.description"),
                VowCloud.CONFIG.debugLogging
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.occlusion_logging"),
                Component.translatable("cloth_config.sound_physics_remastered.occlusion_logging.description"),
                VowCloud.CONFIG.occlusionLogging
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.environment_logging"),
                Component.translatable("cloth_config.sound_physics_remastered.environment_logging.description"),
                VowCloud.CONFIG.environmentLogging
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.performance_logging"),
                Component.translatable("cloth_config.sound_physics_remastered.performance_logging.description"),
                VowCloud.CONFIG.performanceLogging
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.render_sound_bounces"),
                Component.translatable("cloth_config.sound_physics_remastered.render_sound_bounces.description"),
                VowCloud.CONFIG.renderSoundBounces
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics_remastered.render_occlusion"),
                Component.translatable("cloth_config.sound_physics_remastered.render_occlusion.description"),
                VowCloud.CONFIG.renderOcclusion
        ));


        builder.setSavingRunnable(() -> {
            Loggers.log("Saving configs");
            VowCloud.CONFIG.enabled.save();

        });

        return builder.build();
    }

    private static <T> AbstractConfigListEntry<T> fromConfigEntry(ConfigEntryBuilder entryBuilder, Component name, Component description, ConfigEntry<T> entry) {
        if (entry instanceof DoubleConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startDoubleField(name, e.get())
                    .setTooltip(description)
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> e.set(d))
                    .build();
        } else if (entry instanceof FloatConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startFloatField(name, e.get())
                    .setTooltip(description)
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> e.set(d))
                    .build();
        } else if (entry instanceof IntegerConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startIntField(name, e.get())
                    .setTooltip(description)
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(i -> e.set(i))
                    .build();
        } else if (entry instanceof BooleanConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startBooleanToggle(name, e.get())
                    .setTooltip(description)
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(b -> e.set(b))
                    .build();
        } else if (entry instanceof StringConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startStrField(name, e.get())
                    .setTooltip(description)
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(s -> e.set(s))
                    .build();
        }

        return null;
    }

}
