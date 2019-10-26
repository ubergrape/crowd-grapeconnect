package com.grape.crowd.plugin.grapeconnect;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class GrapeLogout {

    private static void postRequest(String url, String token, String param ) {
        String charset = "UTF-8";
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
            connection.setRequestProperty("Authorization", "Token " + token);

            OutputStream output = connection.getOutputStream();
            output.write(param.getBytes(charset));
            InputStream response = connection.getInputStream();
            System.out.println(new Date().toString() + "Grape Connect: " + connection.getOutputStream().toString() + " " + param.getBytes(charset));
            System.out.println(new Date().toString() + "Grape Connect: " + response.toString());
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout(String url, String token, String crowdUsername)
    {
        System.out.println(new Date().toString() + " Grape Connect: Logging out user " + crowdUsername);
        String jsonPayload = "{\"crowd.CrowdUser\":{\"username\":"+ crowdUsername +"}}";
        postRequest(url, token, jsonPayload);
    }
}
