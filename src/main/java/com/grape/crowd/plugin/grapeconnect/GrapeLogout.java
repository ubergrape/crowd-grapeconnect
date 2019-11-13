package com.grape.crowd.plugin.grapeconnect;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.util.Date;

public class GrapeLogout {

    public static class LogoutResponse {
        public int responseCode;
        public String responseContent;
        public static LogoutResponse build(int code, String content) {
            LogoutResponse logoutResponse = new LogoutResponse();
            logoutResponse.responseCode = code;
            logoutResponse.responseContent = content;
            return logoutResponse;
        }
    }

    private static LogoutResponse postRequest(String url, String token, String jsonPayload ) {
        int responseCode = -1;
        String responseContent = "";
        try {

            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_FORM_URLENCODED);

            TrustStrategy acceptingTrustStrategy = new TrustSelfSignedStrategy();
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpPost request = new HttpPost(url);

            String charset = "UTF-8";

            request.setHeader("Accept-Charset", charset);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "Token " + token);
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            System.out.println(new Date().toString() + "Grape Connect response code: " + response.getStatusLine().getStatusCode());
            String content = EntityUtils.toString(response.getEntity());
            responseContent = content;
            //System.out.println(new Date().toString() + "Grape Connect response: " + content);
            responseCode = response.getStatusLine().getStatusCode();
            return LogoutResponse.build(responseCode, responseContent);
        } catch (Exception e) {
            e.printStackTrace();
            return LogoutResponse.build(responseCode, e.toString());
        }
    }

    public static LogoutResponse logout(String url, String token, String crowdUsername)
    {
        System.out.println(new Date().toString() + " Grape Connect: Logging out user " + crowdUsername);
        String jsonPayload = "{\"crowd.CrowdUser\":{\"username\":"+ "\"" + crowdUsername + "\"" +"}}";
        return postRequest(url, token, jsonPayload);
    }
}
