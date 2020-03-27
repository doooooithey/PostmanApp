package com.example.androidapp.Setting;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class httpSetting {
    public static String base="http://120.77.218.241:8080/background/";
    public static String postmanTel="";
    public static String postmanPassword="";
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
