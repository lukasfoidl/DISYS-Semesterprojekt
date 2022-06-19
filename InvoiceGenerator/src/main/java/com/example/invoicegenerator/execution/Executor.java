package com.example.invoicegenerator.execution;

import com.example.invoicegenerator.services.BaseService;

import java.util.List;

public class Executor {

    private List<BaseService> services;

    public Executor(List<BaseService> services) {
        this.services = services;
    }

    public void start() {
        for (BaseService service: services) {
            Thread thread = new Thread(service);
            thread.start();
        }
    }
}

