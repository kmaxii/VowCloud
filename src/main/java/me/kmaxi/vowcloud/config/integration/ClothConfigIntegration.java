package me.kmaxi.vowcloud.config.integration;

import de.maxhenkel.configbuilder.entry.*;
import me.kmaxi.vowcloud.Loggers;
import me.kmaxi.vowcloud.VowCloud;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

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



        builder.setSavingRunnable(() -> {
            Loggers.log("Saving configs");
            VowCloud.CONFIG.enabled.save();

        });

        return builder.build();
    }

    private static <T> AbstractConfigListEntry<T> fromConfigEntry(ConfigEntryBuilder entryBuilder, Component name, Component description, ConfigEntry<T> entry) {
        if (entry instanceof FloatConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startFloatField(name, e.get())
                    .setTooltip(description)
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> e.set(d))
                    .build();
        } else if (entry instanceof BooleanConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startBooleanToggle(name, e.get())
                    .setTooltip(description)
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(b -> e.set(b))
                    .build();
        }

        return null;
    }

}
