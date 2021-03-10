package edu.sharif.ce.mobile.crypto.utils;

import android.util.Log;

import com.github.mikephil.charting.data.CandleEntry;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import edu.sharif.ce.mobile.crypto.models.Candle;
import edu.sharif.ce.mobile.crypto.models.Crypto;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationCenter;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationID;


public class NetworkInterface {
    private final static String API_KEY_FOR_COIN_MARKET_CAP = "ae590806-c68e-46c5-8577-e5640c7d4b41";
    private final static String API_KEY_FOR_COIN_API = "B7F93831-CA70-46CC-A721-E73AD6ED0282";

    public static void getCryptoData(final int start, int limit) {
        OkHttpClient okHttpClient = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?limit="
                .concat(String.valueOf(limit)).concat("&start=".concat(String.valueOf(start))))
                .newBuilder();

        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder().url(url)
                .addHeader("X-CMC_PRO_API_KEY", API_KEY_FOR_COIN_MARKET_CAP)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("network", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("network", response.body().string());
                } else {
                    String body = response.body().string();
                    Log.d("response", body);
                    try {
                        ArrayList<Crypto> cryptoArrayList = new ArrayList<>();
                        JSONObject outerObj = new JSONObject(body);
                        Log.d("json", outerObj.get("data").toString());
                        JSONArray data_array = new JSONArray(outerObj.getString("data"));
                        for (int i = 0; i < data_array.length(); i++) {
                            JSONObject object = (JSONObject) data_array.get(i);
                            Crypto crypto = new Crypto(object.getString("id"), object.getString("name"));
                            crypto.setSymbol(object.getString("symbol"));
                            JSONObject inner_obj = object.getJSONObject("quote").getJSONObject("USD");
                            crypto.setPrice(inner_obj.getDouble("price"));
                            crypto.setPercentChange1H(inner_obj.getDouble("percent_change_1h"));
                            crypto.setPercentChange24H(inner_obj.getDouble("percent_change_24h"));
                            crypto.setPercentChange7D(inner_obj.getDouble("percent_change_7d"));
                            cryptoArrayList.add(crypto);
                            Crypto.addCrypto(crypto);
                        }
                        NetworkInterface.getCryptoImageUrls(start, cryptoArrayList);
                    } catch (JSONException e) {
                        Log.e("json_parser", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }
        });

    }


    public static void getCryptoImageUrls(final int start, final ArrayList<Crypto> cryptoArrayList) {
        StringBuilder sb = new StringBuilder();
        for (Crypto crypto : cryptoArrayList) {
            sb.append(crypto.getId()).append(",");
        }
        if (sb.toString().equals("")) return;
        String query_params = sb.deleteCharAt(sb.length() - 1).toString();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://pro-api.coinmarketcap.com/v2/cryptocurrency/info?id="
                .concat(query_params))
                .newBuilder();

        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder().url(url)
                .addHeader("X-CMC_PRO_API_KEY", API_KEY_FOR_COIN_MARKET_CAP)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("network", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("network", response.body().string());
                } else {
                    String body = response.body().string();
                    Log.d("response", body);
                    try {
                        JSONObject data = new JSONObject(body).getJSONObject("data");
                        for (Crypto crypto : cryptoArrayList) {
                            JSONObject object = data.getJSONObject(crypto.getId());
                            crypto.setImageUrl(object.getString("logo"));
                        }
                        // load in static here...
                        if (start >= Crypto.getCryptos().size()) {
                            Crypto.addAllCryptos(cryptoArrayList);
                        } else {
//                            NetworkInterface.cryptoArrayList.replaceAll();
                        }
                        NotificationCenter.notify(NotificationID.Crypto.NEW_DATA_LOADED_FOR_RESTER);
                    } catch (JSONException e) {
                        Log.e("json_parser", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }
        });
    }

    public static void getCandles(final Crypto crypto, final int range) {

        OkHttpClient okHttpClient = new OkHttpClient();

        String miniUrl = "period_id=1DAY".concat("&limit=").concat(String.valueOf(range));

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://rest.coinapi.io/v1/ohlcv/".concat(crypto.getSymbol()).concat("/USD/latest?".concat(miniUrl)))
                .newBuilder();

        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder().url(url)
                .addHeader("X-CoinAPI-Key", API_KEY_FOR_COIN_API)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("network", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("network", response.body().string());
                } else {
                    String body = response.body().string();
                    Log.d("response", body);
                    try {
                        ArrayList<CandleEntry> candleArrayList = new ArrayList<>();
                        JSONArray data_array = new JSONArray(body);
                        for (int i = 0; i < range; i++) {
                            JSONObject object = (JSONObject) data_array.get(i);
                            double high = object.getDouble("price_high");
                            double low = object.getDouble("price_low");
                            double close = object.getDouble("price_close");
                            double open = object.getDouble("price_open");
                            candleArrayList.add(new Candle(crypto.getId(), high, low, close, open, i));
                        }
                        if (range == 30) {
                            crypto.setLastMonthCandles(candleArrayList);
                        } else if (range == 7) {
                            crypto.setLastWeekCandles(candleArrayList);
                        }
                        NotificationCenter.notify(NotificationID.Candle.CANDLES_LOADED);
                    } catch (JSONException e) {
                        Log.e("json_parser", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }
        });

    }
}

