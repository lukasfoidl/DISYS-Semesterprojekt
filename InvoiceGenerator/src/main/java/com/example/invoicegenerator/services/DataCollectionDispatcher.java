package com.example.invoicegenerator.services;

import com.example.invoicegenerator.communication.Producer;
import org.json.JSONObject;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

public class DataCollectionDispatcher extends BaseService {

    public DataCollectionDispatcher(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected String executeInternal(String receiveMessage) {

        // extract values from JSON
        int customerId = new JSONObject(receiveMessage).getInt("customerId");

        System.out.println("DataCollectionDispatcher: executeInternal(customerId " + customerId + ")");

        // TODO: find all stations where this customer has purchased, from DB
        int[] stationIds = {2, 4, 5};

        // send message to DataCollectionReceiver, that new job started
        String postMessage = new JSONObject()
                .put("customerId", customerId)
                .put("jobSize", stationIds.length)
                .put("isNewJob", true)
                .toString();
        Producer.send(postMessage, "DCR_START", BROKER_URL);

        // send message to StationDataCollector, to look up required information (one for each station)
        for (int i = 0; i < stationIds.length; i++) {
            postMessage = new JSONObject()
                    .put("customerId", customerId)
                    .put("stationId", stationIds[i])
                    .toString();
            Producer.send(postMessage, "SDC_START", BROKER_URL);
        }

        return null;
    }
}
