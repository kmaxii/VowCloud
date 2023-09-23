package me.kmaxi.vowcloud.events.mixins;

import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.events.ReceiveChatEvent;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerboundPlayerCommandPacket.class)  // Target the class responsible for player input packets
public class SneakPacketMixin {

    @Inject(
            at = @At(
                    value = "RETURN"),
            method = "<init>(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/protocol/game/ServerboundPlayerCommandPacket$Action;I)V")

    private void injectOnSneak(Entity entity, ServerboundPlayerCommandPacket.Action action, int i, CallbackInfo ci) {
        if (action != ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY) {
            return;
        }
        ReceiveChatEvent.resetCooldowns();
        VowCloud.getInstance().audioPlayer.autoProgress.cancelShift();
    }


}
