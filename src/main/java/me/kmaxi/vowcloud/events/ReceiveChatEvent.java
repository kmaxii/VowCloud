package me.kmaxi.vowcloud.events;

import me.kmaxi.vowcloud.Audio.AudioPlayer;
import me.kmaxi.vowcloud.utils.LineData;
import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.utils.LineFormatter;
import me.kmaxi.vowcloud.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;

public class ReceiveChatEvent {

    private static final Vec3 mixedFeelingsNPC1 = new Vec3(-5881, 17, -2464);
    private static final Vec3 mixedFeelingsNPC2 = new Vec3(-5835, 16, -2463);
    private static final Vec3 mixedFeelingsNPC3 = new Vec3(-5807, 16, -2421);

    public static boolean stopMod = false;

    private static final HashSet<String> onCooldown = new HashSet<>();

    public static void resetCooldowns() {
        onCooldown.clear();
    }

    public static void receivedChat(String msg) {
        if (stopMod) return;

        msg = replaceNameWithSoldier(msg);

        LineData lineData = LineFormatter.formatToLineData(msg);


        if (onCooldown.contains(lineData.getSoundLine())) {
            return;
        }
        onCooldown.add(lineData.getSoundLine());

        if (isInMixedFeelingsQuest()) {
            String result = getMixedFeelingsLine(lineData.getSoundLine());
            if (result != null) {
                lineData.setSoundLine(result);
            }
        }

        AudioPlayer audioPlayer = VowCloud.getInstance().audioPlayer;
        if (audioPlayer == null)
            return;

        audioPlayer.onNpcDialogue(lineData);
        if (VowCloud.voiceClient != null)
            VowCloud.voiceClient.sendRequest(lineData.getSoundLine());
    }

    private static String replaceNameWithSoldier(String msg) {
        //Replace player Name with "soldier"
        String name = GetPlayerName();
        if (msg.contains(name)) {
            msg = msg.replace(name, "soldier");
        }
        return msg;
    }


    private static boolean isInMixedFeelingsQuest() {
        Player player = Minecraft.getInstance().player;
        return player.position().distanceTo(mixedFeelingsNPC1) < 250;
    }

    private static String getMixedFeelingsLine(String msg) {
        Player player = Minecraft.getInstance().player;

        if (player.position().distanceTo(mixedFeelingsNPC1) < 15) {
            msg = GetRightMixedFeelingsLine("mixedfeelingscorkuscitycitizen1", msg);
        } else if (player.position().distanceTo(mixedFeelingsNPC2) < 15) {
            msg = GetRightMixedFeelingsLine("mixedfeelingscorkuscitycitizen2", msg);
        } else if (player.position().distanceTo(mixedFeelingsNPC3) < 15) {
            msg = GetRightMixedFeelingsLine("mixedfeelingscorkuscitycitizen3", msg);
        }

        return msg;
    }

    private static String GetRightMixedFeelingsLine(String fileName, String msg) {
        boolean foundMsg = false;

        if (msg.equalsIgnoreCase("2/5corkuscitycitizencorkushasbeenurgingtouristsandenvoysfromtheotherprovincestogainrecognition")) {
            fileName = fileName + "1";
            foundMsg = true;
        } else if (msg.equalsIgnoreCase("2/5corkuscitycitizenyouknowaboutthepatriotsofcorkus?")) {
            fileName = fileName + "2";
            foundMsg = true;
        } else if (msg.equalsIgnoreCase("2/6corkuscitycitizenhmmasithappensihaveseensomestrangethingsaroundhere")) {
            fileName = fileName + "3";
            foundMsg = true;
        }
        if (!foundMsg) return null;


        return fileName;
    }


    private static String GetPlayerName() {
        return VowCloud.getInstance().config.getLastPlayedCharacterName();

    }
}


