package edu.sharif.ce.mobile.crypto.utils;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Objects;

public class NetworkInterface {
    private final static String API_KEY_FOR_COIN_MARKET_CAP = "ae590806-c68e-46c5-8577-e5640c7d4b41";

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
                    Log.e("network2", response.body().string());
                } else {
                    Log.d("response", response.body().string());
                }
            }
        });

    }
}

