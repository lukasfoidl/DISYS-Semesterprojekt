package com.example.invoicegenerator.services;

import com.example.invoicegenerator.communication.Producer;
import com.itextpdf.text.ExceptionConverter;
import org.json.JSONObject;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

public class StationDataCollector extends BaseService {

    public StationDataCollector(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected String executeInternal(String receiveMessage) {

        // extract values from JSON
        int customerId = new JSONObject(receiveMessage).getInt("customerId");
        int stationId = new JSONObject(receiveMessage).getInt("stationId");

        System.out.println("StationDataCollector: executeInternal(customerId " + customerId + " stationId " + stationId + ")");

        // TODO: load amount for specific customer for specific station from DB
        double amount = 256.12;

//        try {
//            Thread.sleep(15000);
//        } catch (Exception e) {
//            System.out.println("ERROR! " + e.getMessage());
//        }

        // send message to DataCollectionReceiver with loaded station information
        String postMessage = new JSONObject()
                .put("customerId", customerId)
                .put("stationId", stationId)
                .put("amount", amount)
                .put("isNewJob", false)
                .toString();
        Producer.send(postMessage, "DCR_START", BROKER_URL);

        return null;

    }
}
