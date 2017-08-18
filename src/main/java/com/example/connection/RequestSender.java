package com.example.connection;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestSender {
    String baseURL;
    HttpURLConnection connection = null;

    RequestSender (String baseURL)
    {
        this.baseURL = baseURL;
    }

    public String sendRequest(String urlExtension, String requestMethod, JSONObject requestBody) throws Exception {

        URL url = new URL(baseURL + urlExtension);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(requestMethod);

        con.setDoInput(true);
        con.setDoOutput(true);

        con.setRequestProperty("Content-Type", "application/json");

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(requestBody.toString());
        wr.flush();

        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String line = null;

        while ((line = br.readLine())!= null){
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
    }
}
