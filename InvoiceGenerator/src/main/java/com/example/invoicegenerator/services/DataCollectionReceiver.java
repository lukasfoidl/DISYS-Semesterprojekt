package com.example.invoicegenerator.services;

import com.example.invoicegenerator.dto.CustomerDto;

public class DataCollectionReceiver extends BaseService {

    public DataCollectionReceiver(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected CustomerDto executeInternal(CustomerDto dto) {

        System.out.println("DataCollectionReceiver: executeInternal(customerId " + dto.getCustomerId() + " stationId " + dto.getStationId() + " amount " + dto.getAmount() + ")");

        return null;

    }
}
