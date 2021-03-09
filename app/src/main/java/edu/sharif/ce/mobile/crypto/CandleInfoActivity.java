package edu.sharif.ce.mobile.crypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

public class CandleInfoActivity extends AppCompatActivity {

    ViewPager2 mPager;
    ChartAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candle_info);
        mPager = findViewById(R.id.view_pager);
        adapter = new ChartAdapter(this);
        mPager.setAdapter(adapter);
    }

    static class ChartAdapter extends FragmentStateAdapter {

        public ChartAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return null;
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}