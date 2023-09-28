package me.kmaxi.vowcloud.events.mixins;


import me.kmaxi.vowcloud.events.ContainerClickEvent;
import me.kmaxi.vowcloud.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MenuScreens.class)

public class GuiOpenListener {


    @Inject(at = @At("HEAD"), method = "create")
        private static <T extends AbstractContainerMenu>void create(MenuType<T> menuType, Minecraft minecraft, int i, Component title, CallbackInfo ci) {

        if (!title.getString().contains("Select a Character"))
            return;

        ContainerClickEvent.listenForClick = true;
    }


}
