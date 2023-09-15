package me.kmaxi.vowcloud.events.mixins;

import me.kmaxi.vowcloud.AudioPlayer;
import me.kmaxi.vowcloud.VoiceClient;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.events.ReceiveChatEvent;
import me.kmaxi.vowcloud.gui.AuthApiClient;
import me.kmaxi.vowcloud.utils.Utils;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineStartedMixin {

    @Inject(
            at = @At(value = "RETURN"),
            method = "loadLibrary")

    private void injectOnSneak(CallbackInfo ci) {

        if (VowCloud.voiceClient != null) {
            VowCloud.voiceClient.closeConnection();
        }

        switch (AuthApiClient.checkAuthentication(VowCloud.getInstance().config.getAccessCode())) {
            case CORRECT -> {
                Utils.sendMessage("Connecting to server");
                VowCloud.getInstance().audioPlayer = new AudioPlayer();
                // VowCloud.voiceClient = new VoiceClient("129.151.214.102", 25565);
                VowCloud.voiceClient = new VoiceClient("localhost", 25565);
            }
            case WRONG -> {
                Utils.sendMessage("Invalid access code");
            }
            case SERVER_DOWN -> {
                Utils.sendMessage("Could not connect to server. Server down.");
            }
            case SERVER_ERROR -> {
                Utils.sendMessage("Unknown error. Please contact staff");
            }
        }


    }
}
