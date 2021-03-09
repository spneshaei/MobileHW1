package edu.sharif.ce.mobile.crypto.models;

import com.github.mikephil.charting.data.CandleEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
    private ArrayList<CandleEntry> lastWeekCandles;
    private ArrayList<CandleEntry> lastMonthCandles;


    public Crypto(String id, String name) {
        this.id = id;
        this.name = name;
        this.lastMonthCandles = new ArrayList<>();
        this.lastWeekCandles = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setLastWeekCandles(ArrayList<CandleEntry> lastWeekCandles) {
        this.lastWeekCandles = lastWeekCandles;
    }

    public void setLastMonthCandles(ArrayList<CandleEntry> lastMonthCandles) {
        this.lastMonthCandles = lastMonthCandles;
    }

    public void addCandleToLastWeekCandles(Candle candle) {
        this.lastWeekCandles.add(candle);
    }

    public void addCandleToLastMonthCandles(Candle candle) {
        this.lastMonthCandles.add(candle);
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPercentChange1H() {
        return percentChange1H;
    }

    public void setPercentChange1H(double percentChange1H) {
        this.percentChange1H = percentChange1H;
    }

    public double getPercentChange24H() {
        return percentChange24H;
    }

    public void setPercentChange24H(double percentChange24H) {
        this.percentChange24H = percentChange24H;
    }

    public double getPercentChange7D() {
        return percentChange7D;
    }

    public void setPercentChange7D(double percentChange7D) {
        this.percentChange7D = percentChange7D;
    }

    public ArrayList<CandleEntry> getLastWeekCandles() {
        return lastWeekCandles;
    }

    public ArrayList<CandleEntry> getLastMonthCandles() {
        return lastMonthCandles;
    }

    public static ArrayList<Crypto> getCryptos() {
        return new ArrayList<>(Collections.unmodifiableList(cryptos));
    }

    private static void clearCryptos() {
        cryptos.clear();
    }

    public static void addCrypto(Crypto crypto) {
        cryptos.add(crypto);
    }

    public static void addAllCryptos(ArrayList<Crypto> newCryptos) {
        cryptos.addAll(newCryptos);
    }

    public static void setCryptos(ArrayList<Crypto> newCryptos) {
        clearCryptos();
        cryptos.addAll(newCryptos);
    }
}
