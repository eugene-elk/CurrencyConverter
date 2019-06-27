package com.example.currencycalculator;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Currency {

    private String code;
    private String charCode;
    private Integer count;
    private String name;
    private double exchangeRate;

    public Currency(String code, String charCode, Integer count, String name, double exchangeRate) {
        this.code = code;
        this.charCode = charCode;
        this.count = count;
        this.name = name;
        this.exchangeRate = exchangeRate;
    }

    public String getCharCode() {
        return charCode;
    }

    public int getCount() {
        return count;
    }

    public double getRate() {
        return exchangeRate;
    }

    public double getExchangeRate() {
        return exchangeRate / count;
    }

    public String getName() {
        return name;
    }

    public static NumberFormat formatter = new DecimalFormat("#0.000");

    public static double calculateClear(double from, double to, double count) {
        return count * (from / to);
    }

    public static String Calculate(double from, double to, double count) {
        return formatter.format(count * from / to);
    }
}
