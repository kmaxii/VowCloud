package me.kmaxi.vowcloud;

import net.fabricmc.api.ModInitializer;

public class VowCloud implements ModInitializer {

    public static VoiceClient voiceClient;
    @Override
    public void onInitialize() {
        VoiceClient client = new VoiceClient("localhost", 12345);
        //client.connectToServer("localhost", 12345);
    }


}
