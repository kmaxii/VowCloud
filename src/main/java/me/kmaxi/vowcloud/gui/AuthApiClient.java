package me.kmaxi.vowcloud.gui;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthApiClient {

    private static final String baseUrl = "http://129.151.214.102:8080/auth";
    private static final String apiKey = "test";


    public static ServerRespons checkAuthentication(String key) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/check?key=" + key))
                    .header("Authorization", apiKey)
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

            return ServerRespons.mapResponse(response.statusCode());
        } catch (ConnectException e) {
            return ServerRespons.SERVER_DOWN;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ServerRespons.SERVER_ERROR;
        }
    }



}
