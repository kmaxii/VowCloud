package me.kmaxi.vowcloud.config;

import net.minecraft.client.Minecraft;

public class VowUserConfig extends Config{

    private static final String ACCESS_CODE = "ACCESS_CODE";
    private static final String CHARACTER_NAME = "CHARACTER_NAME";
    public VowUserConfig(String configFileName) {
        super(configFileName);
    }

    public String getAccessCode(){
        return getString(ACCESS_CODE, "");
    }

    public void setAccessCode(String accessCode){
        setString(ACCESS_CODE, accessCode);
    }

    public String getLastPlayedCharacterName(){
        return getString(CHARACTER_NAME, Minecraft.getInstance().player.getName().getString());
    }

    public void setLastPlayedCharacterName(String lastPlayedCharacterName){
        setString(CHARACTER_NAME, lastPlayedCharacterName);
    }
}
