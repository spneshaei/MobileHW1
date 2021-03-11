package edu.sharif.ce.mobile.crypto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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

        final String[] tabTitles = new String[]{"Last Week", "Last Month"};
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, mPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabTitles[position]);
            }
        }).attach();
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
            return ChartFragment.newInstance(crypto, type);
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}