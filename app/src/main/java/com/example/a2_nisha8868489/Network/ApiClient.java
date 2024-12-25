package com.example.a2_nisha8868489.Network;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiClient {

    // OkHttpClient instance
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Makes a GET request to the given URL.
     * @param url The API endpoint to hit.
     * @param callback Callback to handle response or failure.
     */
    public static void get(String url, Callback callback) {
        // Build the HTTP GET request
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Enqueue the request (asynchronous call)
        client.newCall(request).enqueue(callback);
    }

    /**
     * Makes a POST request to the given URL.
     * @param url The API endpoint to hit.
     * @param jsonBody The JSON body to send.
     * @param callback Callback to handle response or failure.
     */
    public static void post(String url, String jsonBody, Callback callback) {
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}