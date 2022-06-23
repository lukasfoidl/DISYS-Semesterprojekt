package com.example.invoicegenerator.services;

import com.example.invoicegenerator.communication.Producer;
import com.example.invoicegenerator.store.CustomerDto;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

public class StationDataCollector extends BaseService {

    public StationDataCollector(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected CustomerDto executeInternal(CustomerDto dto) {

        System.out.println("StationDataCollector: executeInternal(customerId " + dto.getCustomerId() + " stationId " + dto.getStationId() + ")");

        // TODO: load amount for specific customer for specific station from DB
        dto.setAmount(120.78);

        // send message to DataCollectionReceiver with loaded station information
        Producer.send(dto, "DCR_START", BROKER_URL);

        return null;

    }
}
