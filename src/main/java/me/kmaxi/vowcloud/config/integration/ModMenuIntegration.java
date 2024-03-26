package me.kmaxi.vowcloud.config.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.kmaxi.vowcloud.Loggers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (isClothConfigLoaded()) {
            return ClothConfigIntegration::createConfigScreen;
        }
        return parent -> null;
    }

    private static boolean isClothConfigLoaded() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
            try {
                Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
                Loggers.log("Using Cloth Config GUI");
                return true;
            } catch (Exception e) {
                Loggers.error("Failed to load Cloth Config:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

}