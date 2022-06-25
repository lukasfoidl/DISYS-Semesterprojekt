package com.example.invoicegenerator.data;

import java.util.ArrayList;
import java.util.List;

public class Job {

    private int size;
    private int customerId;
    private List<StationData> stations;

    // Constructors

    public Job(int size, int customerId) {
        this.size = size;
        this.customerId = customerId;
        this.stations = new ArrayList<StationData>();
    }

    // Functionality

    public void addJob(StationData job) {
        stations.add(job);
        size--;
    }

    // Getter and Setter

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<StationData> getStations() {
        return stations;
    }

    public void setStations(List<StationData> stations) {
        this.stations = stations;
    }
}
