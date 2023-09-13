package me.kmaxi.vowcloud.events;

import me.kmaxi.vowcloud.AudioPlayer;
import me.kmaxi.vowcloud.VoiceClient;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.utils.Utils;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


public class JoinServerEvent {


    private static final DecimalFormat df = new DecimalFormat("0.00");


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
            if (VowCloud.voiceClient != null){
                VowCloud.voiceClient.closeConnection();
            }

            VowCloud.voiceClient = new VoiceClient("129.151.214.102", 25565);
            VowCloud.getInstance().audioPlayer = new AudioPlayer();
            System.out.println("Connecting to server");
            Utils.sendMessage("Connecting to server");
        }
    }
}
