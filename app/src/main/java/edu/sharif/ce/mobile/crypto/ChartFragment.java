package edu.sharif.ce.mobile.crypto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;

import java.util.ArrayList;

import edu.sharif.ce.mobile.crypto.models.Candle;
import edu.sharif.ce.mobile.crypto.models.Crypto;


public class ChartFragment extends Fragment {

    private ArrayList<Candle> candleData;
    private int type;
    private Crypto crypto;

    private CandleStickChart chart;
    private ProgressBar progressBar;

    public static final int TYPE_ONE_MONTH = 1;
    public static final int TYPE_ONE_WEEK = 0;

    public ChartFragment(Crypto crypto, int type) {
        this.type = type;
        this.crypto = crypto;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        chart = view.findViewById(R.id.chart);
        progressBar = view.findViewById(R.id.progress_bar);
        provideData();
        return view;
    }

    private void provideData () {
        CandleDataSet dataSet;
        switch (type) {
            default:
            case TYPE_ONE_WEEK:
                dataSet = new CandleDataSet(crypto.getLastWeekCandles(), "DataSet");
                break;
            case TYPE_ONE_MONTH:
                dataSet = new CandleDataSet(crypto.getLastMonthCandles(), "Dataset");
                break;
        }
        CandleData data = new CandleData(dataSet);
        chart.setData(data);
        chart.invalidate();
    }
}