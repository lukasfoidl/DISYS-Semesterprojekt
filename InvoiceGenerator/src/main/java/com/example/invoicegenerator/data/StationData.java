package com.example.invoicegenerator.data;

public class StationData {

    private int stationId = -1;
    private double amount = 0;

    // Constructors

    public StationData(int stationId, double amount) {
        this.stationId = stationId;
        this.amount = amount;
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
}
