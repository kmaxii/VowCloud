package me.kmaxi.vowcloud.events;

import me.kmaxi.vowcloud.AudioPlayer;
import me.kmaxi.vowcloud.VoiceClient;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.gui.AuthApiClient;
import me.kmaxi.vowcloud.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;


public class JoinServerEvent {


    public static void run(String ip) {

        String serverIP = ip.toLowerCase();
        if (serverIP.startsWith("play.wynncraft")
                || serverIP.startsWith("media.wynncraft")
                || serverIP.startsWith("beta.wynncraft")
                || serverIP.startsWith("lobby.wynncraft")) {
            System.out.println("Joined Live Wynncraft server");
        }

        Timer timer = new Timer();
        //In 8 seconds
        timer.schedule(new SchedulerTask(), 8000L);
    }

    public static class SchedulerTask extends TimerTask {
        @Override
        public void run() {
            if (VowCloud.voiceClient != null) {
                VowCloud.voiceClient.closeConnection();
            }

            switch (AuthApiClient.checkAuthentication(VowCloud.getInstance().config.getAccessCode())) {
                case CORRECT -> {
                    Utils.sendMessage("Connecting to server");
                    VowCloud.getInstance().audioPlayer = new AudioPlayer();
                    // VowCloud.voiceClient = new VoiceClient("129.151.214.102", 25565);
                    VowCloud.voiceClient = new VoiceClient("localhost", 25565);
                }
                case WRONG -> {
                    Utils.sendMessage("Invalid access code");
                }
                case SERVER_DOWN -> {
                    Utils.sendMessage("Could not connect to server. Server down.");
                }
                case SERVER_ERROR -> {
                    Utils.sendMessage("Unknown error. Please contact staff");
                }
            }


        }
    }
}
