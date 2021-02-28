package edu.sharif.ce.mobile.crypto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import edu.sharif.ce.mobile.crypto.notifhandling.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static class WeakHandler extends Handler implements Subscriber {
        private final WeakReference<MainActivity> activity;

        public WeakHandler(MainActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = this.activity.get();
            if (activity != null) {
                switch (msg.what) {
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        NetworkInterface.getCryptoData(1,5);
    }
}