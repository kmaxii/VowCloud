package me.kmaxi.vowcloud.utils;

import me.kmaxi.vowcloud.Audio.VoiceClient;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.gui.AcesCodeGUI;
import me.kmaxi.vowcloud.gui.AuthApiClient;
import me.kmaxi.vowcloud.gui.AuthInfo;

public class SetupServerConnection {

    public static void setupConnection() {
        if (VowCloud.voiceClient != null) {
            VowCloud.voiceClient.closeConnection();
        }

        AuthInfo authInfo = AuthApiClient.getAuthInformation(VowCloud.getInstance().config.getAccessCode());

        if (authInfo == null) {
            Utils.sendMessage("VOWCLOUD ERROR! AUTH INFO IS NULL");
            return;
        }

        if (authInfo.isValid()) {
            VoiceClient.serverAddress = VowCloud.CONFIG.useLocalHostServer.get() ? "localhost" : authInfo.ip();
            VowCloud.voiceClient = new VoiceClient(25565);
            return;
        }

        switch (authInfo.deniedReason()) {
            case "invalid" -> {
                Utils.sendMessage("Invalid access code for Vow. For more info do /token in our discord: https://discord.gg/uDuqhMyrUK");
                AcesCodeGUI.stopShowing = false;
                VowCloud.getInstance().config.setAccessCode("");
            }
            case "expired" -> Utils.sendMessage("Expired access code for Vow. Please update your subscription");
            case "Server down" -> Utils.sendMessage("ERROR! Server down. Please contact staff");
        }
    }
}
