package me.kmaxi.vowcloud.config;

public class VowConfig extends Config{

    private static final String ACCESS_CODE = "ACCESS_CODE";
    public VowConfig(String configFileName) {
        super(configFileName);
    }

    public String getAccessCode(){
        return getString(ACCESS_CODE, "");
    }

    public void setAccessCode(String accessCode){
        setString(ACCESS_CODE, accessCode);
    }
}
