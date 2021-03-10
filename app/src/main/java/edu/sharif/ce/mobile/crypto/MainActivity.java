package edu.sharif.ce.mobile.crypto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;

import edu.sharif.ce.mobile.crypto.models.Crypto;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationCenter;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationID;
import edu.sharif.ce.mobile.crypto.notifhandling.Subscriber;
import edu.sharif.ce.mobile.crypto.utils.Rester;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView cryptoList;
    CryptoAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    private final WeakHandler handler = new WeakHandler(this);

    private static class WeakHandler extends Handler implements Subscriber {
        private final WeakReference<MainActivity> activity;

        public WeakHandler(MainActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = this.activity.get();
            if (activity != null) {
                if ((msg.what == NotificationID.Crypto.NEW_DATA_LOADED_FOR_UI || msg.what == NotificationID.Crypto.DATA_LOADED_FROM_CACHE) && activity.cryptoList != null && activity.adapter != null) {
                    activity.adapter.notifyDataSetChanged();
                    if (msg.what == NotificationID.Crypto.NEW_DATA_LOADED_FOR_UI) {
                        activity.finishLoad();
                    }
                } else if (msg.what == NotificationID.Crypto.NO_INTERNET_CONNECTION) {
                    activity.finishLoadingViews();
                    Snackbar.make(activity.findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(activity.getResources().getColor(android.R.color.holo_red_light))
                            .show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cryptoList = findViewById(R.id.crypto_list);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new CryptoAdapter(Crypto.getCryptos());
        cryptoList.setAdapter(adapter);
        cryptoList.setLayoutManager(new LinearLayoutManager(this));


        NotificationCenter.registerForNotification(this.handler, NotificationID.Crypto.NEW_DATA_LOADED_FOR_UI);
        NotificationCenter.registerForNotification(this.handler, NotificationID.Crypto.DATA_LOADED_FROM_CACHE);
        NotificationCenter.registerForNotification(this.handler, NotificationID.Crypto.NO_INTERNET_CONNECTION);
        Rester.getInstance().getCryptoData(this, 1, 10);
    }

    public void finishLoad() {
        Rester.getInstance().saveCryptos(this);
        finishLoadingViews();
    }

    private void finishLoadingViews() {
        SpinKitView spinKit = findViewById(R.id.spin_kit);
        spinKit.setVisibility(View.INVISIBLE);
        TextView watchlist = findViewById(R.id.watchlist);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        watchlist.setLayoutParams(params);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        Rester.getInstance().getCryptoData(this, 1, 10);
    }
}