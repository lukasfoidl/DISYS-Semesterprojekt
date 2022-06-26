package com.example.invoicegenerator.services;

import com.example.invoicegenerator.communication.Producer;
import com.itextpdf.text.ExceptionConverter;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

public class StationDataCollector extends BaseService {

    int test = 1;

    public StationDataCollector(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected String executeInternal(String receiveMessage) {

        // extract values from JSON
        int customerId = new JSONObject(receiveMessage).getInt("customerId");
        int stationId = new JSONObject(receiveMessage).getInt("stationId");

        System.out.println("StationDataCollector: executeInternal(customerId " + customerId + " stationId " + stationId + ")");

        // TODO: load data for specific customer for specific station from DB

        double amount = -1;
        String date = "";
        int kwh = -1;
        if (test == 1) {
            amount = 256.12;
            date = "2022/06/18 12:01";
            kwh = 1558;
        }
        if (test==2) {
            amount = 347.89;
            date = "2022/06/22 14:28";
            kwh = 2256;
        }
        if (test==3) {
            amount = 224.30;
            date = "2022/05/14 22:40";
            kwh = 1478;
            test = 0;
        }
        test++;

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
                .put("date", date)
                .put("kwh", kwh)
                .toString();
        Producer.send(postMessage, "DCR_START", BROKER_URL);

        return null;

    }
}
