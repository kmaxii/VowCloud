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

        ConfigCategory soundPhysics = builder.getOrCreateCategory(Component.translatable("cloth_config.vow_cloud.category.sound_physics"));

        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.sound_physics_enabled"),
                Component.translatable("cloth_config.vow_cloud.sound_physics_enabled.description"),
                        VowCloud.CONFIG.physicsEnabled));

        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.attenuation_factor"),
                Component.translatable("cloth_config.vow_cloud.attenuation_factor.description"),
                VowCloud.CONFIG.attenuationFactor
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud_remastered.reverb_gain"),
                Component.translatable("cloth_config.vow_cloud.reverb_gain.description"),
                VowCloud.CONFIG.reverbGain
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.reverb_brightness"),
                Component.translatable("cloth_config.vow_cloud.reverb_brightness.description"),
                VowCloud.CONFIG.reverbBrightness
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.block_absorption"),
                Component.translatable("cloth_config.vow_cloud.block_absorption.description"),
                VowCloud.CONFIG.blockAbsorption
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.occlusion_variation"),
                Component.translatable("cloth_config.vow_cloud.occlusion_variation.description"),
                VowCloud.CONFIG.occlusionVariation
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.default_block_reflectivity"),
                Component.translatable("cloth_config.vow_cloud.default_block_reflectivity.description"),
                VowCloud.CONFIG.defaultBlockReflectivity
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.default_block_occlusion_factor"),
                Component.translatable("cloth_config.vow_cloud.default_block_occlusion_factor.description"),
                VowCloud.CONFIG.defaultBlockOcclusionFactor
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.air_absorption"),
                Component.translatable("cloth_config.vow_cloud.air_absorption.description"),
                VowCloud.CONFIG.airAbsorption
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.underwater_filter"),
                Component.translatable("cloth_config.vow_cloud.underwater_filter.description"),
                VowCloud.CONFIG.underwaterFilter
        ));

        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.environment_evaluation_ray_count"),
                Component.translatable("cloth_config.vow_cloud.environment_evaluation_ray_count.description"),
                VowCloud.CONFIG.environmentEvaluationRayCount
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.environment_evaluation_ray_bounces"),
                Component.translatable("cloth_config.vow_cloud.environment_evaluation_ray_bounces.description"),
                VowCloud.CONFIG.environmentEvaluationRayBounces
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.non_full_block_occlusion_factor"),
                Component.translatable("cloth_config.vow_cloud.non_full_block_occlusion_factor.description"),
                VowCloud.CONFIG.nonFullBlockOcclusionFactor
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.max_occlusion_rays"),
                Component.translatable("cloth_config.vow_cloud.max_occlusion_rays.description"),
                VowCloud.CONFIG.maxOcclusionRays
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.max_occlusion"),
                Component.translatable("cloth_config.vow_cloud.max_occlusion.description"),
                VowCloud.CONFIG.maxOcclusion
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.strict_occlusion"),
                Component.translatable("cloth_config.vow_cloud.strict_occlusion.description"),
                VowCloud.CONFIG.strictOcclusion
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.sound_direction_evaluation"),
                Component.translatable("cloth_config.vow_cloud.sound_direction_evaluation.description"),
                VowCloud.CONFIG.soundDirectionEvaluation
        ));
        soundPhysics.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.redirect_non_occluded_sounds"),
                Component.translatable("cloth_config.vow_cloud.redirect_non_occluded_sounds.description"),
                VowCloud.CONFIG.redirectNonOccludedSounds
        ));

        ConfigCategory reflectivity = builder.getOrCreateCategory(Component.translatable("cloth_config.vow_cloud.category.reflectivity"));

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

        ConfigCategory occlusion = builder.getOrCreateCategory(Component.translatable("cloth_config.vow_cloud.category.occlusion"));

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

        ConfigCategory logging = builder.getOrCreateCategory(Component.translatable("cloth_config.vow_cloud.category.debug"));

        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.render_sound_bounces"),
                Component.translatable("cloth_config.vow_cloud.render_sound_bounces.description"),
                VowCloud.CONFIG.renderSoundBounces
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.vow_cloud.render_occlusion"),
                Component.translatable("cloth_config.vow_cloud.render_occlusion.description"),
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
