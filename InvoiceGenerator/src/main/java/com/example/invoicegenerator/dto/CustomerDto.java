package com.example.invoicegenerator.dto;

import java.io.Serializable;

public class CustomerDto implements Serializable {

    private int customerId;
    private double amount;
    private int stationId;

    // Constructors

    public CustomerDto(
            int customerId,
            double amount,
            int stationId
    ) {
        this.customerId = customerId;
        this.amount = amount;
        this.stationId = stationId;
    }

    public CustomerDto(int customerId) {
        this.customerId = customerId;
    }

    public CustomerDto(int customerId, int stationId) {
        this.customerId = customerId;
        this.stationId = stationId;
    }

    // Getter and Setter

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }
}
