package edu.sharif.ce.mobile.crypto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.sharif.ce.mobile.crypto.models.Crypto;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationCenter;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationID;
import edu.sharif.ce.mobile.crypto.notifhandling.Subscriber;
import edu.sharif.ce.mobile.crypto.utils.NetworkInterface;
import edu.sharif.ce.mobile.crypto.utils.Rester;

public class MainActivity extends AppCompatActivity {

    RecyclerView cryptoList;
    CryptoAdapter adapter;

    private final WeakHandler handler = new WeakHandler(this);

    private static class WeakHandler extends Handler implements Subscriber {
        private final WeakReference<MainActivity> activity;

        public WeakHandler(MainActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = this.activity.get();
            if (activity != null && msg.what == NotificationID.Crypto.NEW_DATA_LOADED_FOR_UI
                    && activity.cryptoList != null && activity.adapter != null) {
                activity.adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationCenter.registerForNotification(this.handler, NotificationID.Crypto.NEW_DATA_LOADED_FOR_UI);
        Rester.getInstance().getCryptoData(this);

        //just for test
//        Crypto first = new Crypto("1", "Bitcoin");
//        first.setPrice(1000);
//        first.setImageUrl("https://miro.medium.com/max/410/1*U7phpu7aKKrU05JvMvs-wA.png");
//        first.setPercentChange1H(2);
//        first.setPercentChange24H(-3);
//        first.setPercentChange7D(0);
//        first.setSymbol("BTC");
//
//        Crypto second = new Crypto("2", "Etherium");
//        second.setPrice(2000);
//        second.setImageUrl("https://s2.coinmarketcap.com/static/img/coins/64x64/2.png");
//        second.setPercentChange1H(-2);
//        second.setPercentChange24H(3);
//        second.setPercentChange7D(-4);
//        second.setSymbol("ETH");
//
//        ArrayList<Crypto> sampleList = new ArrayList<>();
//        sampleList.add(first);
//        sampleList.add(second);

        cryptoList = findViewById(R.id.crypto_list);

        adapter = new CryptoAdapter(Crypto.getCryptos());
        cryptoList.setAdapter(adapter);
        cryptoList.setLayoutManager(new LinearLayoutManager(this));

        finish_load();

    }

    public void finish_load() {
        SpinKitView spinKit = findViewById(R.id.spin_kit);
        spinKit.setVisibility(View.INVISIBLE);
        TextView watchlist = findViewById(R.id.watchlist);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        watchlist.setLayoutParams(params);
    }
}