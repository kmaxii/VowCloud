package me.kmaxi.vowcloud.gui;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.kmaxi.vowcloud.Loggers;

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
            Gson gson = new Gson();

            AuthResponse authResponse = gson.fromJson(result.toString(), AuthResponse.class);


            boolean isValid = authResponse.isValid();
            String ip = isValid ? authResponse.getIp() : "";
            String invalidReason = !isValid ? authResponse.getReason() : "";

            return new AuthInfo(authResponse.isValid(), ip, invalidReason);

        } catch (Exception e) {
           // throw new RuntimeException(e);
        }
        return null;
    }


    public static void main(String[] args) {
        getAuthInformation("LPHVECSBTG0C44C8");
    }

    class AuthResponse {
        private boolean valid;
        private String ip;
        private String reason;

        public boolean isValid() {
            return valid;
        }

        public String getIp() {
            return ip;
        }

        public String getReason() {
            return reason;
        }
    }

}
