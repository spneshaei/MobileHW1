package edu.sharif.ce.mobile.crypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import edu.sharif.ce.mobile.crypto.models.Crypto;

public class CandleInfoActivity extends AppCompatActivity {

    ViewPager2 mPager;
    ChartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candle_info);
        mPager = findViewById(R.id.view_pager);
        adapter = new ChartAdapter(this, (Crypto) getIntent().getExtras().get("crypto"));
        mPager.setAdapter(adapter);
    }

    static class ChartAdapter extends FragmentStateAdapter {
        Crypto crypto;

        public ChartAdapter(@NonNull FragmentActivity fragmentActivity, Crypto crypto) {
            super(fragmentActivity);
            this.crypto = crypto;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            int type;
            switch (position) {
                default:
                case 0:
                    type = ChartFragment.TYPE_ONE_WEEK;
                    break;
                case 1:
                    type = ChartFragment.TYPE_ONE_MONTH;
                    break;
            }
            return new ChartFragment(crypto, type);
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}