package me.kmaxi.vowcloud.events;


import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.gui.AcesCodeGUI;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ShowGUIEvent {


    public static void onGUIOpen(Screen screen, CallbackInfo ci) {

        if (screen instanceof TitleScreen  && VowCloud.getInstance().config.getAccessCode().isEmpty()) {
            AcesCodeGUI.OpenGui();
            ci.cancel();
        }


    }
}
