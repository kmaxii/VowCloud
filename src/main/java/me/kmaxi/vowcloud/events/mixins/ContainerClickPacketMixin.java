package me.kmaxi.vowcloud.events.mixins;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.kmaxi.vowcloud.events.ContainerClickEvent;
import me.kmaxi.vowcloud.utils.Utils;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerboundContainerClickPacket.class)

public class ContainerClickPacketMixin {

    @Inject(at = @At("RETURN"), method = "<init>(IIIILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/item/ItemStack;Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;)V")
    private void onConstructor(int containerID, int transactionId, int clickedSlot, int mouseButton, ClickType clickType, ItemStack itemStack, Int2ObjectMap<ItemStack> int2ObjectMap, CallbackInfo ci) {
        ContainerClickEvent.onClick(clickedSlot);
    }

}
