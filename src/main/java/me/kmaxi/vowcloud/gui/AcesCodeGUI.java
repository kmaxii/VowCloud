package me.kmaxi.vowcloud.gui;


import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import me.kmaxi.vowcloud.VowCloud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AcesCodeGUI extends LightweightGuiDescription {

    private final ResourceLocation image = new ResourceLocation(VowCloud.MODID, "wynnvplogo.png");

    private WTextField wTextField;

    public AcesCodeGUI() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 200);

        AddLogo(root);
        AddStartText(root);

        AddTextField(root);

        AddButtons(root);
    }

    private void AddLogo(WGridPanel root) {
        WSprite sprite = new WSprite(image);
        root.add(sprite, 6, -4, 5, 5);
    }

    private void AddStartText(WGridPanel root) {
        root.add(new WLabel(Component.literal("VOICES OF WYNN")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 1);
        root.add(new WLabel(Component.literal("Please enter your unique access code:")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 2);

    /*    root.add(new WLabel(Component.literal("encountered while playing Wynncraft to improve")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 3);
        root.add(new WLabel(Component.literal("Voices of Wynn?")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 4);
        root.add(new WLabel(Component.literal("With full report, your nickname will be sent with the")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 6);
        root.add(new WLabel(Component.literal("report, so that we could contact you in case we")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 7);
        root.add(new WLabel(Component.literal("have questions about it.")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 8);*/
    }

    private void AddTextField(WGridPanel root) {
        wTextField = new WTextField();
        root.add(wTextField, 1, 3, 15, 20);
    }


    private void AddButtons(WGridPanel root) {
        WButton noneButton = new WButton(Component.literal("Confirm"));
        noneButton.setOnClick(this::onConfirmClick);
        noneButton.setAlignment(HorizontalAlignment.CENTER);
        // noneButton.setIcon(new ItemIcon(new ItemStack(Items.DIAMOND)));
        root.add(noneButton, 6, 9, 4, 20);

    }

    public static void OpenGui() {
        Minecraft.getInstance().setScreen(new AcesCodeScreen(new AcesCodeGUI()));
    }



    private void onConfirmClick() {
        System.out.println(wTextField.getText());
        VowCloud.getInstance().config.setAccessCode(wTextField.getText());
        SetTitleScreen();
    }

    private void SetTitleScreen() {
        Minecraft.getInstance().setScreen(new TitleScreen());
    }
}
