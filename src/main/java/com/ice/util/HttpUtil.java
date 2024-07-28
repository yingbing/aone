package com.ice.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static String sendPost(String url, String jsonPayload, String authToken) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("X-Rundeck-Auth-Token", authToken);

        if (jsonPayload != null) {
            StringEntity entity = new StringEntity(jsonPayload);
            httpPost.setEntity(entity);
        }

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return handleResponse(response);
        }
    }

    public static String sendGet(String url, String authToken) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("X-Rundeck-Auth-Token", authToken);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return handleResponse(response);
        }
    }

    private static String handleResponse(CloseableHttpResponse response) throws Exception {
        HttpEntity responseEntity = response.getEntity();
        String responseStr = EntityUtils.toString(responseEntity);
        EntityUtils.consume(responseEntity);

        if (response.getStatusLine().getStatusCode() == 200) {
            return responseStr;
        } else {
            throw new RuntimeException("Failed: HTTP error code : " + response.getStatusLine().getStatusCode() + " Response: " + responseStr);
        }
    }
}
