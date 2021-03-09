package edu.sharif.ce.mobile.crypto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.sharif.ce.mobile.crypto.models.Candle;
import edu.sharif.ce.mobile.crypto.models.Crypto;


public class ChartFragment extends Fragment {

    private ArrayList<Candle> candleData;
    private int type;
    private Crypto crypto;

    public static int TYPE_ONE_DAY = 1;
    public static int TYPE_ONE_HOUR = 0;
    public static int TYPE_ONE_WEEK = 2;

    public ChartFragment(Crypto crypto, int type) {
        this.type = type;
        this.crypto = crypto;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }
}