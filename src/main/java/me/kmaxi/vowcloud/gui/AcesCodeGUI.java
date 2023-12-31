package me.kmaxi.vowcloud.gui;


import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import me.kmaxi.vowcloud.Audio.VoiceClient;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AcesCodeGUI extends LightweightGuiDescription {

    private final ResourceLocation image = new ResourceLocation(VowCloud.MODID, "icon.png");

    private WTextField wTextField;

    private static String errorText = "";

    public static boolean stopShowing = false;

    public AcesCodeGUI() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 200);

        //    AddLogo(root);
        AddStartText(root);

        AddTextField(root);

        AddButtons(root);
    }

    private void AddLogo(WGridPanel root) {
        WSprite sprite = new WSprite(image);
        root.add(sprite, 6, -4, 5, 5);
    }

    private void AddStartText(WGridPanel root) {
        root.add(new WLabel(Component.literal("§5VOICES OF WYNN")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 1);
        root.add(new WLabel(Component.literal("§fPlease enter your unique access code:")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 2);
        root.add(new WLabel(Component.literal("§fTo get it do /token in the Vow discord (discord.gg/Gd68zftm)")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 3);
        root.add(new WLabel(Component.literal("§c" + errorText)).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 5);

    /*    root.add(new WLabel(Component.literal("encountered while playing Wynncraft to improve")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 3);
        root.add(new WLabel(Component.literal("Voices of Wynn?")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 4);
        root.add(new WLabel(Component.literal("With full report, your nickname will be sent with the")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 6);
        root.add(new WLabel(Component.literal("report, so that we could contact you in case we")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 7);
        root.add(new WLabel(Component.literal("have questions about it.")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 8);*/
    }

    private void AddTextField(WGridPanel root) {
        wTextField = new WTextField();
        root.add(wTextField, 1, 6, 15, 20);
    }


    private void AddButtons(WGridPanel root) {
        WButton confirmButton = new WButton(Component.literal("Confirm"));
        confirmButton.setOnClick(this::onConfirmClick);
        confirmButton.setAlignment(HorizontalAlignment.CENTER);
        // confirmButton.setIcon(new ItemIcon(new ItemStack(Items.DIAMOND)));
        root.add(confirmButton, 3, 9, 4, 1);


        WButton closeButton = new WButton(Component.literal("§cClose"));
        closeButton.setOnClick(this::onCloseClick);
        closeButton.setAlignment(HorizontalAlignment.CENTER);
        closeButton.setIcon(new ItemIcon(new ItemStack(Items.BARRIER)));
        root.add(closeButton, 10, 9, 4, 1);

    }

    public static void OpenGui() {
        Minecraft.getInstance().setScreen(new AcesCodeScreen(new AcesCodeGUI()));
    }


    private void onCloseClick() {
        stopShowing = true;
        SetTitleScreen();
    }


    private void onConfirmClick() {
        String accessCode = wTextField.getText();
        AuthInfo authInfo = AuthApiClient.getAuthInformation(accessCode);
        if (authInfo == null){
            Utils.sendMessage("VOWCLOUD ERROR! AUTH INFO IS NULL");
            return;
        }
        if (authInfo.isValid()) {
            VowCloud.getInstance().config.setAccessCode(wTextField.getText());
            VoiceClient.serverAddress = authInfo.ip();
            SetTitleScreen();
            return;
        }

        switch (authInfo.deniedReason()) {
            case "invalid" -> {
                errorText = "Invalid Access Code";
            }
            case "expired" -> {
                errorText = "Expired Access Code.";
            }
            case "Server down" ->{
                errorText = "Server down. Please contact staff";
            }
        }

        SetTitleScreen();


    }

    private void SetTitleScreen() {
        Minecraft.getInstance().setScreen(new TitleScreen());
    }
}
