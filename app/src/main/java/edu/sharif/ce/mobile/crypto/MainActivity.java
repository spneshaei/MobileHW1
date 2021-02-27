package edu.sharif.ce.mobile.crypto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.sharif.ce.mobile.crypto.utils.NetworkInterface;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkInterface.getCryptoData(1,10);
    }
}