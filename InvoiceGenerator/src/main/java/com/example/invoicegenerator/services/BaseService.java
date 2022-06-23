package com.example.invoicegenerator.services;

import com.example.invoicegenerator.communication.Consumer;
import com.example.invoicegenerator.communication.Producer;
import com.example.invoicegenerator.store.CustomerDto;

public abstract class BaseService implements Runnable {

    protected final String inDestination;
    protected final String outDestination;
    protected final String brokerUrl;

    public BaseService(String inDestination, String outDestination, String brokerUrl) {
        this.inDestination = inDestination;
        this.outDestination = outDestination;
        this.brokerUrl = brokerUrl;
    }

    @Override
    public void run() {
        while (true) {
            execute(inDestination, outDestination, brokerUrl);
        }
    }

    private void execute(String inDestination, String outDestination, String brokerUrl) {
        CustomerDto input = Consumer.receive(inDestination, 10000, brokerUrl);

        if (input.getCustomerId() == -1) {
            return;
        }

        CustomerDto output = executeInternal(input);
        Producer.send(output, outDestination, brokerUrl);
    }

    protected abstract CustomerDto executeInternal(CustomerDto dto);
}

