package com.example.connection;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestSender {
    static Logger logger = LoggerFactory.getLogger(RequestSender.class);

    String baseURL;
    HttpURLConnection connection = null;

    public RequestSender (String baseURL)
    {
        this.baseURL = baseURL;
    }

    public String[] sendRequest(String urlExtension, String requestMethod, JSONObject requestBody)// throws Exception
    {
        logger.info(String.format("Sending request %s to %s", requestBody.toString(), urlExtension));

        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL(baseURL + urlExtension);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestMethod);
        } catch (Exception e) {
            logger.error("Error connecting: " + e.toString());
            return null;
        }


        con.setDoInput(true);
        con.setDoOutput(true);

        con.setRequestProperty("Content-Type", "application/json");

        OutputStreamWriter wr = null;
        try {
            wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(requestBody.toString());
            wr.flush();
        } catch (IOException e) {
            logger.error("Error sending: " + e.toString());
        }

        int responseCode = HttpStatus.SC_REQUEST_TIMEOUT;
        String responseMessage = "";
        StringBuilder sb = new StringBuilder();

        try {
            responseCode = con.getResponseCode();
            responseMessage = con.getResponseMessage();

            BufferedReader br = new BufferedReader(
                            new InputStreamReader(((responseCode == HttpStatus.SC_OK) ?
                                    con.getInputStream() : con.getErrorStream()))
            );

            String line = null;

            while ((line = br.readLine())!= null){
                sb.append(line + "\n");
            }

            br.close();
        } catch (IOException e) {
            logger.error("Error Receiving: " + e.toString());
        }

        logger.info(String.format("Response code: %d", responseCode));

        return new String[]{
                String.format("%d", responseCode),
                sb.toString(),
                responseMessage
        };
    }
}
