package com.geek.chris.study.week2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class GeekTestHttpSend {
    private static final String ENCODING_UTF_8 = "UTF-8";

    public static String doHttpSend(String url) throws IOException {
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            response = HttpClients.createDefault().execute(httpGet);

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return EntityUtils.toString(response.getEntity(), ENCODING_UTF_8);
            } else {
                System.out.println("respFailed,code:" + response.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (null != response) {
                response.close();
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8801";
        String respMsg = GeekTestHttpSend.doHttpSend(url);
        System.out.println("url: " + url);
        System.out.println("respMsg: " + respMsg);

    }
}
