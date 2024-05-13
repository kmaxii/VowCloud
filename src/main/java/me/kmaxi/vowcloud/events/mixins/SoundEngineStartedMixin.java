package me.kmaxi.vowcloud.events.mixins;

import me.kmaxi.vowcloud.Audio.AudioPlayer;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.utils.SetupServerConnection;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineStartedMixin {

    @Inject(method = "loadLibrary", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/audio/Listener;reset()V"))
    private void onSoundEngineStarted(CallbackInfo ci) {
        VowCloud.getInstance().audioPlayer = new AudioPlayer();

        SetupServerConnection.setupConnection();

    }
}
