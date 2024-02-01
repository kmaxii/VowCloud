package me.kmaxi.vowcloud;

import eu.midnightdust.lib.config.MidnightConfig;
import me.kmaxi.vowcloud.Audio.AudioPlayer;
import me.kmaxi.vowcloud.Audio.VoiceClient;
import me.kmaxi.vowcloud.config.IntegratedConfig;
import me.kmaxi.vowcloud.config.VowConfig;
import me.kmaxi.vowcloud.text.ChatHandler3;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class VowCloud implements ModInitializer {

    public static VoiceClient voiceClient;
    private static VowCloud instance;
    public ChatHandler3 chatHandler3;

    public AudioPlayer audioPlayer;


    public VowConfig config;

    public static String MODID = "vowcloud";
    public static final String VERSION = "1.3";


    public static VowCloud getInstance(){
        return instance;
    }
    @Override
    public void onInitialize() {
        chatHandler3 = new ChatHandler3();
        instance = this;

        config = new VowConfig("config/vowcloud.json");

        MidnightConfig.init(MODID, IntegratedConfig.class);

      //  VowCloud.voiceClient = new VoiceClient("129.151.214.102", 25565);


        ClientTickEvents.END_WORLD_TICK.register(cli -> {
            // Your ticking method
            chatHandler3.onTick();
            if (audioPlayer != null)
                audioPlayer.openAlPlayer.onTick();
        });
    }


}
