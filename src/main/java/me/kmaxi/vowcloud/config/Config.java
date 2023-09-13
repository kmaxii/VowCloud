package me.kmaxi.vowcloud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

abstract class Config {
    private final String configFileName;
    private final Map<String, Object> configData;
    private final Gson gson;

    protected Config(String configFileName) {
        this.configFileName = configFileName;
        this.configData = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadConfig();
    }

    protected boolean getBoolean(String key, boolean defaultValue) {
        return configData.containsKey(key) ? (boolean) configData.get(key) : defaultValue;
    }

    protected void setBoolean(String key, boolean value) {
        configData.put(key, value);
        saveConfig();
    }

    protected String getString(String key, String defaultValue) {
        return configData.containsKey(key) ? (String) configData.get(key) : defaultValue;
    }

    protected void setString(String key, String value) {
        configData.put(key, value);
        saveConfig();
    }

    protected int getInt(String key, int defaultValue) {
        return configData.containsKey(key) ? (int) configData.get(key) : defaultValue;
    }

    protected void setInt(String key, int value) {
        configData.put(key, value);
        saveConfig();
    }

    protected float getFloat(String key, float defaultValue) {
        return configData.containsKey(key) ? (float) configData.get(key) : defaultValue;
    }

    protected void setFloat(String key, float value) {
        configData.put(key, value);
        saveConfig();
    }

    private void loadConfig() {

        File file = new File(configFileName);
        if (!file.exists()) {
           return;
        }

        try (FileReader reader = new FileReader(configFileName)) {
            configData.clear();
            configData.putAll(gson.fromJson(reader, Map.class));
        } catch (IOException e) {
            // File doesn't exist or there was an error reading it; ignore.
        }
    }

    private void saveConfig() {
        try (FileWriter writer = new FileWriter(configFileName)) {
            gson.toJson(configData, writer);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error appropriately.
        }
    }

/*    public static void main(String[] args) {
        Config config = new Config("config.json");

        // Example usage:
        boolean boolValue = config.getBoolean("enable_feature", true);
        String stringValue = config.getString("username", "guest");
        int intValue = config.getInt("app_version", 1);
        float floatValue = config.getFloat("user_rating", 4.5f);

        System.out.println("Boolean Value: " + boolValue);
        System.out.println("String Value: " + stringValue);
        System.out.println("Int Value: " + intValue);
        System.out.println("Float Value: " + floatValue);

        // Updating values
        config.setBoolean("enable_feature", false);
        config.setString("username", "newuser");
        config.setInt("app_version", 2);
        config.setFloat("user_rating", 4.8f);
    }*/
}
