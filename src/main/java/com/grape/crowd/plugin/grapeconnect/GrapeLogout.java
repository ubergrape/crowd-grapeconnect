package com.grape.crowd.plugin.grapeconnect;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.Date;

public class GrapeLogout {

    private static int postRequest(String url, String token, String jsonPayload ) {
        int responseCode = -1;
        try {

            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_FORM_URLENCODED);
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);

            String charset = "UTF-8";

            request.setHeader("Accept-Charset", charset);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "Token " + token);
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            System.out.println(new Date().toString() + "Grape Connect response code: " + response.getStatusLine().getStatusCode());
            String content = EntityUtils.toString(response.getEntity());
            System.out.println(new Date().toString() + "Grape Connect response: " + content);
            responseCode = response.getStatusLine().getStatusCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseCode;
    }

    public static int logout(String url, String token, String crowdUsername)
    {
        System.out.println(new Date().toString() + " Grape Connect: Logging out user " + crowdUsername);
        String jsonPayload = "{\"crowd.CrowdUser\":{\"username\":"+ crowdUsername +"}}";
        return postRequest(url, token, jsonPayload);
    }
}
