package edu.sharif.ce.mobile.crypto;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.sharif.ce.mobile.crypto.models.Crypto;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationCenter;
import edu.sharif.ce.mobile.crypto.notifhandling.NotificationID;
import edu.sharif.ce.mobile.crypto.notifhandling.Subscriber;
import edu.sharif.ce.mobile.crypto.utils.Rester;


public class ChartFragment extends Fragment {


    private static class WeakHandler extends Handler implements Subscriber {
        private final WeakReference<ChartFragment> fragment;
        private WeakReference<Context> context;

        public WeakHandler(ChartFragment fragment, Context context) {
            this.fragment = new WeakReference<>(fragment);
            this.context = new WeakReference<>(context);
        }


        @Override
        public void handleMessage(Message msg) {
            ChartFragment unwrappedFragment = fragment.get();
            Context unwrappedContext = context.get();
            if (unwrappedFragment == null || unwrappedContext == null) return;
            if (msg.what == NotificationID.Candle.NEW_DATA_LOADED_FOR_UI || msg.what == NotificationID.Candle.DATA_LOADED_FROM_CACHE) {
                if (msg.what == NotificationID.Candle.NEW_DATA_LOADED_FOR_UI) {
                    Rester.getInstance().saveCandle(unwrappedContext, unwrappedFragment.crypto,
                            unwrappedFragment.getRangeFromType(unwrappedFragment.type));
                    unwrappedFragment.progressBar.setVisibility(View.GONE);
                }
                try {
                    unwrappedFragment.provideData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == NotificationID.Candle.NO_INTERNET_CONNECTION) {
                FragmentActivity activity = unwrappedFragment.getActivity();
                if (activity == null) return;
                unwrappedFragment.progressBar.setVisibility(View.GONE);
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


    private int type;
    private Crypto crypto;

    private CandleStickChart chart;
    private ProgressBar progressBar;
    private WeakHandler mHandler;

    public static final int TYPE_ONE_MONTH = 1;
    public static final int TYPE_ONE_WEEK = 0;

    public ChartFragment() {
    }

    public static ChartFragment newInstance(Crypto crypto, int type) {
        Bundle args = new Bundle();
        args.putSerializable("crypto", crypto);
        args.putInt("type", type);
        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public int getRangeFromType(int type) {
        return type == TYPE_ONE_MONTH ? 30 : 7;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.type = getArguments().getInt("type");
        this.crypto = (Crypto) getArguments().getSerializable("crypto");
        mHandler = new WeakHandler(this, getContext());
        NotificationCenter.registerForNotification(mHandler, NotificationID.Candle.NEW_DATA_LOADED_FOR_UI);
        NotificationCenter.registerForNotification(mHandler, NotificationID.Candle.DATA_LOADED_FROM_CACHE);
        NotificationCenter.registerForNotification(mHandler, NotificationID.Candle.NO_INTERNET_CONNECTION);
        Rester.getInstance().getCandleData(getContext(), crypto, getRangeFromType(type));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        chart = view.findViewById(R.id.chart);
        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    private void provideData() {
        chart.setHighlightPerDragEnabled(true);
        chart.setDrawBorders(true);
        chart.setBorderColor(getResources().getColor(R.color.colorGray));
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setDrawGridLines(true);
        rightAxis.setDrawGridLines(false);
        chart.requestDisallowInterceptTouchEvent(true);
        Description description = new Description();
        description.setText(crypto.getName() + " | " + crypto.getSymbol());
        chart.setDescription(description);
        description.setTextSize(16);
        description.setYOffset(10);
        description.setXOffset(10);
        description.setTextColor(getResources().getColor(R.color.topicColor));
        chart.setBackgroundColor(getResources().getColor(R.color.crypto_backgrounds));

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);// disable x axis grid lines
        xAxis.setDrawLabels(true);
        rightAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);

        Legend l = chart.getLegend();
        l.setEnabled(true);

        ArrayList<CandleEntry> yValsCandleStick = type == TYPE_ONE_WEEK ?
                crypto.getLastWeekCandles() : crypto.getLastMonthCandles();
        if (yValsCandleStick == null || yValsCandleStick.isEmpty()) return;
        CandleDataSet set1 = new CandleDataSet(yValsCandleStick, "Candles");
        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(getResources().getColor(R.color.colorGray));
        set1.setShadowWidth(0.8f);
        set1.setDecreasingColor(getResources().getColor(R.color.colorRed));
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(getResources().getColor(R.color.colorAccent));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.LTGRAY);
        set1.setDrawValues(false);
//      create a data object with the datasets
        CandleData data = new CandleData(set1);
//      set data
        chart.setData(data);
        chart.invalidate();
//        chart.animateXY(200,200);
    }
}