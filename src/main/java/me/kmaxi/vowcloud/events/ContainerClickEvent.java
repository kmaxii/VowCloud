package me.kmaxi.vowcloud.events;

import me.kmaxi.vowcloud.VowCloud;
import me.kmaxi.vowcloud.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class ContainerClickEvent {
    public static boolean listenForClick = false;


    public static void onClick(int slot) {
        if (!listenForClick) {
            return;
        }

        //Classes are 1-5, 10-14, 19-22
        if (!(slot >= 1 && slot <= 5 || slot >= 10 && slot <= 14 || slot >= 19 && slot <= 22)) {
            return;
        }

        listenForClick = false;


        String characterName = getItemName(slot);
        characterName = characterName.substring(characterName.indexOf("Select ") + 7);
        characterName = characterName.replace("]", "").trim();
        characterName = characterName.equals("This Character") ? Minecraft.getInstance().player.getDisplayName().getString() : characterName;

        Utils.sendMessage("Character name " + characterName);

        VowCloud.getInstance().config.setLastPlayedCharacterName(characterName);
    }

    @NotNull
    private static String getItemName(int slot) {
        assert Minecraft.getInstance().player != null;
        var containerMenu = Minecraft.getInstance().player.containerMenu;

        //If the player uses Wynntils Character selection then they just send this packet, without actually picking up the item
        //which means we need to get the item in the slot. If the player uses Wynncrafts normal selection screen this code is called
        //right when they have the item in the hand, so we need to get the carried item
        ItemStack itemClicked = containerMenu.getCarried().getItem() != Items.AIR ? containerMenu.getCarried() : containerMenu.getSlot(slot).getItem();


        return itemClicked.getDisplayName().getString();
    }
}
