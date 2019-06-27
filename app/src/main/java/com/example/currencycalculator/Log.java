package com.example.currencycalculator;

public class Log {
    private String count;
    private String currencyFrom;
    private String currencyTo;
    private String value;
    private String date;

    public String getCount() {
        return count;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public String getDate() {
        return date;
    }

    public String getValue() {
        return value;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Log(double count, String currencyFrom, String currencyTo, String value, String date) {
        this.count = Double.toString(count);
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.value = value;
        this.date = date;
    }

    public Log() {

    }

    @Override
    public String toString() {

        if (currencyTo == null) return "Просмотр курсов валют" + "\n" + "Дата курса: " + date;
        else return count + " " + currencyFrom + " = " + currencyTo + " " + value + "\n" + "Дата курса: " + date;
    }
}
