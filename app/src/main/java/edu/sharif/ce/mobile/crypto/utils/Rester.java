package edu.sharif.ce.mobile.crypto.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import edu.sharif.ce.mobile.crypto.notifhandling.NotificationCenter;

/**
 * Created by Seyyed Parsa Neshaei on 2/28/21
 * All Rights Reserved
 */
public class Rester {
    private static final Rester ourInstance = new Rester();
    private ThreadPoolExecutor executor;
    static Rester getInstance() {
        return ourInstance;
    }

    private Rester() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    }

    public void getCryptoData(Context context) {
        try {
            String data = readFromFile(context, "crypto.txt");
            // data is in the cache

        } catch (Exception ignored) {
        }

    }

    private String readFromFile(Context context, String file) throws Exception {
        InputStream inputStream = context.openFileInput("config.txt");
        if (inputStream == null) return "";
        String ret = "";
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();
        while ((receiveString = bufferedReader.readLine()) != null) {
            stringBuilder.append("\n").append(receiveString);
        }
        inputStream.close();
        ret = stringBuilder.toString();
        return ret;
    }
}
