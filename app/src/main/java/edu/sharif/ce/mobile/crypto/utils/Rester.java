package edu.sharif.ce.mobile.crypto.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import edu.sharif.ce.mobile.crypto.models.Crypto;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationCenter;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationID;
import edu.sharif.ce.mobile.crypto.notifhandling.Subscriber;

/**
 * Created by Seyyed Parsa Neshaei on 2/28/21
 * All Rights Reserved
 */
public class Rester implements Subscriber {
    private static final Rester ourInstance = new Rester();
    private ThreadPoolExecutor executor;
    public static Rester getInstance() {
        return ourInstance;
    }

    private Rester() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    }

    public void getCryptoData(Context context) {
        try {
            String data = readFromFile(context, "crypto.txt");
            if (!data.equals("")) {
                // data is in the cache
                setCryptosFromJSON(data);
                NotificationCenter.notify(NotificationID.Crypto.DATA_LOADED_FROM_CACHE);
            }
        } catch (Exception ignored) {
        }
//        if (!isConnected()) {
//            NotificationCenter.notify(NotificationID.Crypto.NO_INTERNET_CONNECTION);
//            return;
//        }
        NotificationCenter.registerForNotification(this, NotificationID.Crypto.NEW_DATA_LOADED_FOR_RESTER);
        NetworkInterface.getCryptoData(1, 10);
    }

    private boolean isConnected() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
            sock.connect(sockaddr, timeoutMs);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void setCryptosFromJSON(String json) {
        Crypto[] cryptos = new Gson().fromJson(json, Crypto[].class);
        Crypto.setCryptos(new ArrayList<>(Arrays.asList(cryptos)));
    }

    private String readFromFile(Context context, String file) throws Exception {
        InputStream inputStream = context.openFileInput(file);
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

    private void writeToFile(String file, Context context, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("WriteToFile-Rester", "File write failed: " + e.toString());
        }
    }

    @Override
    public boolean sendEmptyMessage(int what) {

//        if (what == NotificationID.Crypto.NEW_DATA_LOADED_FOR_RESTER) {
//            writeToFile("crypto.txt", MyApplication.getAppContext(), new Gson().toJson(Crypto.getCryptos()));
//        }
        NotificationCenter.notify(NotificationID.Crypto.NEW_DATA_LOADED_FOR_UI);

        return false;
    }
}
