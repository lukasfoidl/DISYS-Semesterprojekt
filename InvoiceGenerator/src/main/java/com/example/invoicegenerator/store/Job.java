package com.example.invoicegenerator.store;

import java.util.ArrayList;
import java.util.List;

public class Job {

    private int count;
    private int customerId;
    private List<CustomerDto> jobs;

    // Constructors

    public Job(int count, int customerId) {
        this.count = count;
        this.customerId = customerId;
        this.jobs = new ArrayList<CustomerDto>();
    }

    // Functionality

    public void addJob(CustomerDto job) {
        jobs.add(job);
        count--;
    }

    // Getter and Setter

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<CustomerDto> getJobs() {
        return jobs;
    }

    public void setJobs(List<CustomerDto> jobs) {
        this.jobs = jobs;
    }
}
