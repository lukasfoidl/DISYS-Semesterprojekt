package com.example.invoicegenerator.services;

import com.example.invoicegenerator.store.CustomerDto;

public class PdfGenerator extends BaseService {

    public PdfGenerator(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected CustomerDto executeInternal(CustomerDto dto) {

        System.out.println("PdfGenerator: executeInternal(customerId " + dto.getCustomerId() + " amount: " + dto.getAmount() + ")");

        // TODO: generate PDF and make available for client

        return null;
    }
}
