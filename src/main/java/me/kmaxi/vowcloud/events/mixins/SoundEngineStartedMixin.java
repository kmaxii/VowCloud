package me.kmaxi.vowcloud.events.mixins;

import me.kmaxi.vowcloud.Audio.AudioPlayer;
import me.kmaxi.vowcloud.gui.AuthInfo;
import me.kmaxi.vowcloud.Audio.VoiceClient;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.gui.AcesCodeGUI;
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

    private void onSoundEngineStarted(CallbackInfo ci) {

        if (VowCloud.voiceClient != null) {
            VowCloud.voiceClient.closeConnection();
        }

        AuthInfo authInfo = AuthApiClient.getAuthInformation(VowCloud.getInstance().config.getAccessCode());

        if (authInfo.isValid()) {
            VoiceClient.serverAddress = authInfo.ip();
            VowCloud.getInstance().audioPlayer = new AudioPlayer();
            VowCloud.voiceClient = new VoiceClient(25565);
            return;
        }

        switch (authInfo.deniedReason()) {
            case "invalid" -> {
                Utils.sendMessage("Invalid access code for Vow");
                AcesCodeGUI.stopShowing = false;
                VowCloud.getInstance().config.setAccessCode("");
            }
            case "expired" -> Utils.sendMessage("Expired access code for Vow. Please update your subscription");
            case "Server dow" -> Utils.sendMessage("ERROR! Server down. Please contact staff");
        }

    }
}
