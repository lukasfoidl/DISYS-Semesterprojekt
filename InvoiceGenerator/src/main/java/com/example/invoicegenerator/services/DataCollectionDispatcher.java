package com.example.invoicegenerator.services;

import com.example.invoicegenerator.communication.Producer;
import com.example.invoicegenerator.store.CustomerDto;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

public class DataCollectionDispatcher extends BaseService {

    public DataCollectionDispatcher(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected CustomerDto executeInternal(CustomerDto dto) {

        System.out.println("DataCollectionDispatcher: executeInternal(customerId " + dto.getCustomerId() + ")");

        // TODO: find all stations where this customer has purchased, from DB
        int[] stationIds = {2, 4, 5};

        // send message to DataCollectionReceiver, that new job started
        Producer.send(new CustomerDto(dto.getCustomerId(), stationIds.length, true), "DCR_START", BROKER_URL);

        // send message to StationDataCollector, to look up required information (one for each station)
        for (int i = 0; i < stationIds.length; i++) {
            Producer.send(new CustomerDto(dto.getCustomerId(), stationIds[i]), "SDC_START", BROKER_URL);
        }

        return null;
    }
}
