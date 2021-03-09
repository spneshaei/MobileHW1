package edu.sharif.ce.mobile.crypto.models;

import com.github.mikephil.charting.data.CandleEntry;

public class Candle extends CandleEntry {
    private String cryptoId;

    public Candle(String cryptoId, double high, double low, double close, double open, int x) {
        super(x, (float) high, (float) low, (float) open, (float) close);
        this.cryptoId = cryptoId;
    }

    public String getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(String cryptoId) {
        this.cryptoId = cryptoId;
    }
}
