package edu.sharif.ce.mobile.crypto.models;

import com.github.mikephil.charting.data.CandleEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class Crypto implements Serializable {
    private static ArrayList<Crypto> cryptos = new ArrayList<>();

    private String id;
    private double price;
    private String name;
    private String symbol;
    private String imageUrl;
    private double percentChange1H;
    private double percentChange24H;
    private double percentChange7D;
    private String candleData;
    private ArrayList<CandleEntry> lastWeekCandles;
    private ArrayList<CandleEntry> lastMonthCandles;

    public synchronized static Crypto getCryptoWithID(String id) {
        for (Crypto crypto : cryptos) if (crypto.id.equals(id)) return crypto;
        return null;
    }

    public synchronized String getCandleData() {
        return candleData;
    }

    public synchronized void setCandleData(String candleData) {
        this.candleData = candleData;
    }

    public Crypto(String id, String name) {
        this.id = id;
        this.name = name;
        this.lastMonthCandles = new ArrayList<>();
        this.lastWeekCandles = new ArrayList<>();
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized void setLastWeekCandles(ArrayList<CandleEntry> lastWeekCandles) {
        this.lastWeekCandles = lastWeekCandles;
    }

    public synchronized void setLastMonthCandles(ArrayList<CandleEntry> lastMonthCandles) {
        this.lastMonthCandles = lastMonthCandles;
    }

    public synchronized void setId(String id) {
        this.id = id;
    }

    public synchronized double getPrice() {
        return price;
    }

    public synchronized void setPrice(double price) {
        this.price = price;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized String getSymbol() {
        return symbol;
    }

    public synchronized void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public synchronized String getImageUrl() {
        return imageUrl;
    }

    public synchronized void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public synchronized double getPercentChange1H() {
        return percentChange1H;
    }

    public synchronized void setPercentChange1H(double percentChange1H) {
        this.percentChange1H = percentChange1H;
    }

    public synchronized double getPercentChange24H() {
        return percentChange24H;
    }

    public synchronized void setPercentChange24H(double percentChange24H) {
        this.percentChange24H = percentChange24H;
    }

    public synchronized double getPercentChange7D() {
        return percentChange7D;
    }

    public synchronized void setPercentChange7D(double percentChange7D) {
        this.percentChange7D = percentChange7D;
    }

    public synchronized ArrayList<CandleEntry> getLastWeekCandles() {
        return lastWeekCandles;
    }

    public synchronized ArrayList<CandleEntry> getLastMonthCandles() {
        return lastMonthCandles;
    }

    public synchronized static ArrayList<Crypto> getCryptos() {
        return cryptos;
    }

    private synchronized static void clearCryptos() {
        cryptos.clear();
    }

    public synchronized static void addAllCryptosIfNotRepeated(ArrayList<Crypto> newCryptos) {
        for (Crypto newCrypto : newCryptos) {
            Crypto oldCrypto = getCryptoWithID(newCrypto.id);
            if (oldCrypto != null) {
                copyCryptoDetails(newCrypto, oldCrypto);
                continue;
            }
            cryptos.add(newCrypto);
        }
    }

    private synchronized static void copyCryptoDetails(Crypto newCrypto, Crypto oldCrypto) {
        oldCrypto.id = newCrypto.id;
        oldCrypto.imageUrl = newCrypto.imageUrl;
        oldCrypto.lastMonthCandles = newCrypto.lastMonthCandles;
        oldCrypto.lastWeekCandles = newCrypto.lastWeekCandles;
        oldCrypto.name = newCrypto.name;
        oldCrypto.percentChange1H = newCrypto.percentChange1H;
        oldCrypto.percentChange7D = newCrypto.percentChange7D;
        oldCrypto.percentChange24H = newCrypto.percentChange24H;
        oldCrypto.price = newCrypto.price;
        oldCrypto.symbol = newCrypto.symbol;
        oldCrypto.candleData = newCrypto.candleData;
    }


    public synchronized static void setCryptos(ArrayList<Crypto> newCryptos) {
        clearCryptos();
        cryptos.addAll(newCryptos);
    }
}
