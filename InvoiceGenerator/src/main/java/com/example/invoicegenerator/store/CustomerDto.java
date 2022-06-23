package com.example.invoicegenerator.store;

import java.io.Serializable;

public class CustomerDto implements Serializable {

    private int customerId = -1;
    private double amount = 0;
    private int stationId = -1;
    private int count = -1;
    private boolean isNewJob = false;

    // Constructors

    public CustomerDto(int customerId) {
        this.customerId = customerId;
    }

    public CustomerDto(int customerId, int count, boolean isNewJob) {
        this.customerId = customerId;
        this.count = count;
        this.isNewJob = isNewJob;
    }

    public CustomerDto(int customerId, int stationId) {
        this.customerId = customerId;
        this.stationId = stationId;
    }

    public CustomerDto(int customerId, double amount) {
        this.customerId = customerId;
        this.amount = amount;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isNewJob() {
        return isNewJob;
    }

    public void setNewJob(boolean newJob) {
        isNewJob = newJob;
    }
}
