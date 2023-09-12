package me.kmaxi.vowcloud;

import me.kmaxi.vowcloud.text.ChatHandler3;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class VowCloud implements ModInitializer {

    public static VoiceClient voiceClient;
    private static VowCloud instance;

    public ChatHandler3 chatHandler3;

    public static VowCloud getInstance(){
        return instance;
    }
    @Override
    public void onInitialize() {
        chatHandler3 = new ChatHandler3();
        instance = this;

        VowCloud.voiceClient = new VoiceClient("localhost", 12345);


        ClientTickEvents.END_WORLD_TICK.register(cli -> {
            // Your ticking method
            chatHandler3.onTick();
        });
    }


}
