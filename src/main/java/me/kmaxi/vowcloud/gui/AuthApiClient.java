package me.kmaxi.vowcloud.gui;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthApiClient {

    private static final String baseUrl = "https://voicesofwynn.com/api/premium/check";


    public static AuthInfo getAuthInformation(String key) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(baseUrl + "?code=" + key);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            int responseCode= conn.getResponseCode();

            if (responseCode != 200) {
                return new AuthInfo(false, "", "Server down");
            }

            JSONObject jsonObject = new JSONObject(result.toString());


            boolean isValid = jsonObject.getBoolean("valid");


            String ip = isValid ? jsonObject.getString("ip") : "";

            String invalidReason = !isValid ? jsonObject.getString("reason") : "";

            return new AuthInfo(isValid, ip, invalidReason);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        getAuthInformation("LPHVECSBTG0C44C8");
    }


}
