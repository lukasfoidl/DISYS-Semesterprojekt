package com.example.invoicegenerator.controller;

import com.example.invoicegenerator.communication.Producer;
import com.example.invoicegenerator.store.CustomerDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

@RestController
public class Controller {

    @PostMapping("/invoices/{customerId}")
    public void startDataGathering(@PathVariable int customerId) {
        System.out.println("startDataGathering(id " + customerId + ")");

        Producer.send(new CustomerDto(customerId), "DCD_START", BROKER_URL);
    }

}
