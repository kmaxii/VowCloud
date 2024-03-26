package me.kmaxi.vowcloud;

import de.maxhenkel.configbuilder.ConfigBuilder;
import me.kmaxi.vowcloud.Audio.AudioPlayer;
import me.kmaxi.vowcloud.Audio.VoiceClient;
import me.kmaxi.vowcloud.config.VowConfig;
import me.kmaxi.vowcloud.config.VowUserConfig;
import me.kmaxi.vowcloud.text.ChatHandler3;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;

public class VowCloud implements ModInitializer {

    public static VoiceClient voiceClient;
    private static VowCloud instance;
    public ChatHandler3 chatHandler3;

    public AudioPlayer audioPlayer;


    public VowUserConfig config;

    public static VowConfig CONFIG;

    public static String MODID = "vowcloud";
    public static final String VERSION = "1.4";


    public static VowCloud getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        Loggers.log("Initializing VowCloud");

        chatHandler3 = new ChatHandler3();
        instance = this;

        InitializeConfig();


        ClientTickEvents.END_WORLD_TICK.register(cli -> {
            // Your ticking method
            chatHandler3.onTick();
            if (audioPlayer != null)
                audioPlayer.openAlPlayer.onTick();
        });
    }

    private void InitializeConfig() {
        config = new VowUserConfig("config/vowcloudtoken.json");
        if (CONFIG == null) {
            CONFIG = ConfigBuilder.builder(VowConfig::new)
                    .path(FabricLoader.getInstance().getConfigDir()
                            .resolve(MODID).resolve("vowcloud.properties")).build();
        }
    }


}
