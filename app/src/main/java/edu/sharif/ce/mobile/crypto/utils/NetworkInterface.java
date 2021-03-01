package edu.sharif.ce.mobile.crypto.utils;

import android.util.Log;

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

import edu.sharif.ce.mobile.crypto.models.Crypto;


public class NetworkInterface {
    private final static String API_KEY_FOR_COIN_MARKET_CAP = "ae590806-c68e-46c5-8577-e5640c7d4b41";
    private static ArrayList<Crypto> cryptoArrayList;

    public static void getCryptoData(int start, int limit) {
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
                        }
                        NetworkInterface.cryptoArrayList = cryptoArrayList;
                        NetworkInterface.getCryptoImageUrls(cryptoArrayList);
                    } catch (JSONException e) {
                        Log.e("json_parser", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }
        });

    }


    public static void getCryptoImageUrls(ArrayList<Crypto> cryptoArrayList) {
        StringBuilder sb = new StringBuilder();
        for (Crypto crypto : cryptoArrayList) {
            sb.append(crypto.getId()).append(",");
        }
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
                        ArrayList<Crypto> cryptoArrayList = NetworkInterface.cryptoArrayList;
                        JSONObject data = new JSONObject(body).getJSONObject("data");
                        for (Crypto crypto : cryptoArrayList) {
                            JSONObject object = data.getJSONObject(crypto.getId());
                            crypto.setImageUrl(object.getString("logo"));
                        }
                    } catch (JSONException e) {
                        Log.e("json_parser", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }
        });
    }

    public static void getCandles(){

    }
}

