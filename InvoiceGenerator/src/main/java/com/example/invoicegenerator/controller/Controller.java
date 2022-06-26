package com.example.invoicegenerator.controller;

import com.example.invoicegenerator.communication.Producer;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

@RestController
public class Controller {

    @PostMapping("/invoices/{customerId}")
    public void startDataGathering(@PathVariable int customerId) {
        try {
            System.out.println("startDataGathering(id " + customerId + ")");

            // send message to DataCollectionDispatcher, starts data gathering process
            String postMessage = new JSONObject().put("customerId", customerId).toString();
            Producer.send(postMessage, "DCD_START", BROKER_URL);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
