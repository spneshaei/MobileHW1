package edu.sharif.ce.mobile.crypto.models;

public class Candle {
    private String cryptoId;
    private double high;
    private double low;
    private double close;
    private double open;

    public Candle(String cryptoId, double high, double low, double close, double open) {
        this.cryptoId = cryptoId;
        this.high = high;
        this.low = low;
        this.close = close;
        this.open = open;
    }

    public String getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(String cryptoId) {
        this.cryptoId = cryptoId;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }
}
