package com.rhorn.Ghetto;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.Reader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.json.JSONException;
public class GhettoKillA {
    private static final String LOG_TAG = "[GhettoKillA]";

    public String ghettoServerKill() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        
        Callable<String> stringCallable = () -> {

            String serverShutdown = "http://localhost:1242/server_shutdown";
            String isAlive = "false";
            try {
                UrlResponseStringJSONGet(serverShutdown);
            } catch (IOException e) {
                Log.i(LOG_TAG, e.toString());
                isAlive = "true";
            }
            return isAlive;
        };

        Future<String> callableFuture = executorService.submit(stringCallable);  // store return value
        executorService.shutdown();
        return callableFuture.get();
    }

    public void UrlResponseStringJSONGet(String url) throws IOException {
        /* Log JSON of Python HTTP server POST response "/server_shutdown"
        {'server_shutdown': ' recorder_shutdown_init'} */
        URL requestURL = new URL(url);

        HttpURLConnection response = (HttpURLConnection)requestURL.openConnection();
        response.setRequestMethod("POST");
        response.setRequestProperty("Content-Type", "application/json");
        response.setRequestProperty("Accept", "application/json");
        response.setDoOutput(true);

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (response.getInputStream(), StandardCharsets.UTF_8))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        JSONObject jsonJava;
        try {
            jsonJava = new JSONObject(String.valueOf(textBuilder));
            Log.d(LOG_TAG, jsonJava.toString());
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.toString());
        }
    }
}