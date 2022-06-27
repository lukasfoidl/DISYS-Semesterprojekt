package com.example.invoicegenerator.data;

public class StationData {

    private int stationId = -1;
    private double amount = 0;
    private String date = "";
    private int kwh = -1;

    // Constructors

    public StationData(int stationId, double amount, String date, int kwh) {
        this.stationId = stationId;
        this.amount = amount;
        this.date = date;
        this.kwh = kwh;
    }

    // Getter and Setter

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getKwh() {
        return kwh;
    }

    public void setKwh(int kwh) {
        this.kwh = kwh;
    }
}
