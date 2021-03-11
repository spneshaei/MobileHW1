package edu.sharif.ce.mobile.crypto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

    TextView pullToRefresh;
    SpinKitView spinKit;
    TextView watchlist;
    RecyclerView cryptoList;
    CryptoAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager layoutManager;

    private final WeakHandler handler = new WeakHandler(this);

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

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
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinKit = findViewById(R.id.spin_kit);
        watchlist = findViewById(R.id.watchlist);
        pullToRefresh = findViewById(R.id.pullToRefresh);

        cryptoList = findViewById(R.id.crypto_list);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new CryptoAdapter(Crypto.getCryptos());
        cryptoList.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        cryptoList.setLayoutManager(layoutManager);

        NotificationCenter.registerForNotification(this.handler, NotificationID.Crypto.NEW_DATA_LOADED_FOR_UI);
        NotificationCenter.registerForNotification(this.handler, NotificationID.Crypto.DATA_LOADED_FROM_CACHE);
        NotificationCenter.registerForNotification(this.handler, NotificationID.Crypto.NO_INTERNET_CONNECTION);
        Log.e("Rester_Main", "getCryData");
        System.out.println("BEFORE LOADING...");
        Rester.getInstance().getCryptoData(this, 1, 0);
        System.out.println("LOADED...");
        cryptoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if (loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        spinKit.setVisibility(View.VISIBLE);
                        pullToRefresh.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        watchlist.setLayoutParams(params);
                        Rester.getInstance().getCryptoData(MainActivity.this, Crypto.getCryptos().size() + 1, 10);
                        loading = true;
                    }
                }
            }
        });
    }

    public void finishLoad() {
        Rester.getInstance().saveCryptos(this);
        finishLoadingViews();
    }

    private void finishLoadingViews() {
        spinKit.setVisibility(View.INVISIBLE);
        pullToRefresh.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        watchlist.setLayoutParams(params);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        Rester.getInstance().getCryptoData(this, 1, 0);
    }
}