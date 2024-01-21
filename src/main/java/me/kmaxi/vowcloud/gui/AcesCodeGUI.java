package me.kmaxi.vowcloud.gui;


import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import me.kmaxi.vowcloud.Audio.VoiceClient;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Debug;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

        AddNormalVersionInfo(root);
    }

    private void AddLogo(WGridPanel root) {
        WSprite sprite = new WSprite(image);
        root.add(sprite, 6, -4, 5, 5);
    }

    private void AddStartText(WGridPanel root) {
        root.add(new WLabel(Component.literal("§5VOICES OF WYNN CLOUD")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 0);
        root.add(new WLabel(Component.literal("§fThe Cloud version of Voices of Wynn is currently in closed beta.")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 1);
        root.add(new WLabel(Component.literal("§fIf you're a supporter or contributor, join our Discord and use /token in any channel.")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 2);
        root.add(new WLabel(Component.literal("§fYou'll get a unique token to enter in the field below.")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 3);
        root.add(new WLabel(Component.literal("§c" + errorText)).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 4);

    /*    root.add(new WLabel(Component.literal("encountered while playing Wynncraft to improve")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 3);
        root.add(new WLabel(Component.literal("Voices of Wynn?")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 4);
        root.add(new WLabel(Component.literal("With full report, your nickname will be sent with the")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 6);
        root.add(new WLabel(Component.literal("report, so that we could contact you in case we")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 7);
        root.add(new WLabel(Component.literal("have questions about it.")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 8);*/
    }


    private void AddNormalVersionInfo(WGridPanel root) {
        wTextField = new WTextField();
        root.add(new WLabel(Component.literal("§fIf you're not a supporter you can still use the public classic version:")).setHorizontalAlignment(HorizontalAlignment.CENTER), 8, 9);
        WButton openDownloadLink = new WButton(Component.literal("Download Classic Version"));
        String url = "https://www.curseforge.com/minecraft/mc-mods/voices-of-wynn/files/all?page=1&pageSize=20";
        openDownloadLink.setOnClick(() -> OpenUrl(url));
        openDownloadLink.setAlignment(HorizontalAlignment.CENTER);
        root.add(openDownloadLink, 4, 10, 9, 1);



    }

    private void OpenUrl(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

        try {
            if (os.contains("win")) {
                // Windows
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                // macOS
                rt.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux/Unix
                String[] browsers = { "firefox", "chrome", "chromium", "opera", "epiphany", "konqueror", "mozilla" };
                StringBuffer cmd = new StringBuffer();
                for (int i=0; i<browsers.length; i++)
                    if(i == 0)
                        cmd.append(String.format( "%s \"%s\"", browsers[i], url));
                    else
                        cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
                rt.exec(new String[] { "sh", "-c", cmd.toString() });
            } else {
                System.err.println("Unsupported Operating System for browser opening.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AddTextField(WGridPanel root) {
        wTextField = new WTextField();
        root.add(wTextField, 1, 5, 15, 20);
    }


    private void AddButtons(WGridPanel root) {
        WButton confirmButton = new WButton(Component.literal("§aConfirm"));
        confirmButton.setOnClick(this::onConfirmClick);
        confirmButton.setAlignment(HorizontalAlignment.CENTER);
        // confirmButton.setIcon(new ItemIcon(new ItemStack(Items.DIAMOND)));
        root.add(confirmButton, 6, 7, 5, 1);

        WButton discordButton = new WButton(Component.literal("§9Discord"));
        discordButton.setOnClick(() -> OpenUrl("https://discord.com/servers/voices-of-wynn-814401551292563477"));
        discordButton.setAlignment(HorizontalAlignment.CENTER);

        root.add(discordButton, 1, 7, 4, 1);



        WButton closeButton = new WButton(Component.literal("§cTurn Off"));
        closeButton.setOnClick(this::onCloseClick);
        closeButton.setAlignment(HorizontalAlignment.CENTER);
 //       closeButton.setIcon(new ItemIcon(new ItemStack(Items.BARRIER)));
        root.add(closeButton, 12, 7, 4, 1);

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
            authInfo = new AuthInfo(false, "", "Server down");
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
                errorText = "Server is down. Please, join our Discord server and contact staff";
            }
        }

        SetTitleScreen();


    }

    private void SetTitleScreen() {
        Minecraft.getInstance().setScreen(new TitleScreen());
    }
}
