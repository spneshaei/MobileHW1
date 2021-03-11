package edu.sharif.ce.mobile.crypto.utils;

import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.data.CandleEntry;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import edu.sharif.ce.mobile.crypto.models.Candle;
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
    private Date timeOfLastRequest = Calendar.getInstance().getTime();
    private boolean isFirstRequest = true;
    public static Rester getInstance() {
        return ourInstance;
    }

    private boolean isFirstRequest() {
        return isFirstRequest;
    }

    private void didFirstRequest() {
        isFirstRequest = false;
    }

    private synchronized long getTimeOfLastRequest() {
        if (isFirstRequest()) return 0;
        return timeOfLastRequest.getTime();
    }

    private synchronized void setTimeOfLastRequest(Date timeOfLastRequest) {
        didFirstRequest();
        this.timeOfLastRequest = timeOfLastRequest;
    }

    private Rester() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    }

    public void getCryptoData(final Context context, final int start, final int limit) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String data = readFromFile(context, "crypto.txt");
                    if (!data.equals("")) {
                        // data is in the cache
                        setCryptosFromJSON(data);
                        NotificationCenter.notify(NotificationID.Crypto.DATA_LOADED_FROM_CACHE);
                    }
                } catch (Exception ignored) {
                }
                if (!isConnected()) {
                    NotificationCenter.notify(NotificationID.Crypto.NO_INTERNET_CONNECTION);
                    return;
                }
                if (System.currentTimeMillis() - getTimeOfLastRequest() < 500) {
                    NotificationCenter.notify(NotificationID.Crypto.NEW_DATA_LOADED_FOR_RESTER);
                    return;
                }
                setTimeOfLastRequest(Calendar.getInstance().getTime());
                NotificationCenter.registerForNotification(Rester.this, NotificationID.Crypto.NEW_DATA_LOADED_FOR_RESTER);
                NetworkInterface.getCryptoData(start, limit > 0 ? limit : Math.max(10, Crypto.getCryptos().size()));
            }
        });
    }

    public void getCandleData(final Context context, final Crypto crypto, final int range) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String data = readFromFile(context, "candle-" + crypto.getId() +"-" + range +".txt");
                    if (!data.equals("")) {
                        // data is in the cache
                        setCandlesFromJSON(data, crypto, range);
                    }
                } catch (Exception ignored) {
                }
                if (!isConnected()) {
                    NotificationCenter.notify(NotificationID.Candle.NO_INTERNET_CONNECTION);
                    return;
                }
                if (System.currentTimeMillis() - getTimeOfLastRequest() < 500) {
                    NotificationCenter.notify(NotificationID.Candle.NEW_DATA_LOADED_FOR_RESTER);
                    return;
                }
                setTimeOfLastRequest(Calendar.getInstance().getTime());
                NotificationCenter.registerForNotification(Rester.this, NotificationID.Candle.NEW_DATA_LOADED_FOR_RESTER);
                NetworkInterface.getCandles(crypto, range);
            }
        });
    }

    private void setCandlesFromJSON(String data, Crypto crypto, int range) {
        ArrayList<CandleEntry> candleArrayList = new ArrayList<>();
        try {
            JSONArray data_array = new JSONArray(data);
            for (int i = 0; i < range; i++) {
                JSONObject object = (JSONObject) data_array.get(i);
                double high = object.getDouble("price_high");
                double low = object.getDouble("price_low");
                double close = object.getDouble("price_close");
                double open = object.getDouble("price_open");
                candleArrayList.add(new Candle(crypto.getId(), high, low, close, open, range - i));
            }
            crypto.setCandleData(data);
            if (range == 30) crypto.setLastMonthCandles(candleArrayList);
            else if (range == 7) crypto.setLastWeekCandles(candleArrayList);
            Collections.sort(candleArrayList, new Comparator<CandleEntry>() {
                @Override
                public int compare(CandleEntry candleEntry, CandleEntry t1) {
                    return ((int) (candleEntry.getX() - t1.getX()));
                }
            });
            NotificationCenter.notify(NotificationID.Candle.DATA_LOADED_FROM_CACHE);
        } catch (Exception ignored) {
        }
    }

    private synchronized boolean isConnected() {
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

    private synchronized void setCryptosFromJSON(String json) {
        Crypto[] cryptos = new Gson().fromJson(json, Crypto[].class);
        Crypto.setCryptos(new ArrayList<>(Arrays.asList(cryptos)));
    }

    private synchronized String readFromFile(Context context, String file) throws Exception {
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

    public synchronized void saveCryptos(final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                writeToFile("crypto.txt", context, new Gson().toJson(Crypto.getCryptos()));
            }
        });
    }

    public synchronized void saveCandle(final Context context, final Crypto crypto, final int range) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                writeToFile("candle-" + crypto.getId() +"-" + range +".txt", context, crypto.getCandleData());
            }
        });
    }

    private synchronized void writeToFile(String file, Context context, String data) {
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
