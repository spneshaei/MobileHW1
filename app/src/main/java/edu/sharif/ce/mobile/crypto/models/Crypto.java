package edu.sharif.ce.mobile.crypto.models;

import java.util.ArrayList;

public class Crypto {
    private String id;
    private int price;
    private String name;
    private String symbol;
    private String imageUrl;
    private double percentChange1H;
    private double percentChange24H;
    private double percentChange7D;
    private ArrayList<Candle> lastWeekCandles;
    private ArrayList<Candle> lastMonthCandles;


    public Crypto(String id, String name) {
        this.id = id;
        this.name = name;
        this.lastMonthCandles = new ArrayList<>();
        this.lastWeekCandles = new ArrayList<>();
    }

    public String getId() {
        return id;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public ArrayList<Candle> getLastWeekCandles() {
        return lastWeekCandles;
    }

    public ArrayList<Candle> getLastMonthCandles() {
        return lastMonthCandles;
    }
}
