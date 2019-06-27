package com.example.currencycalculator;

import java.util.List;

public class Currencies {
    private List<Currency> currencyList;
    private String date;

    public Currencies(List<Currency> currencies, String date) {
        currencyList = currencies;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String[] getCurrenciesShortnames() {
        String[] result = new String[currencyList.size()];
        for (int i = 0; i < currencyList.size(); i++) {
            result[i] = currencyList.get(i).getCharCode();
        }
        return result;
    }

    public Currency getByShortname(String shortName) {
        for (int i = 0; i < currencyList.size(); i++) {
            if (currencyList.get(i).getCharCode().equals(shortName)) return currencyList.get(i);
        }
        return null;
    }

    public boolean isEmpty() {
        return currencyList.size() == 1;
    }

    public String[] infoToString() {
        String[] result = new String[currencyList.size() + 1];
        result[0] = "Дата курса: " + date;
        for (int i = 0; i < currencyList.size(); i++) {
            result[i+1] = currencyList.get(i).getCharCode() + ", " + currencyList.get(i).getName() + " = " + Currency.formatter.format(currencyList.get(i).getExchangeRate());
        }
        return result;
    }
}
