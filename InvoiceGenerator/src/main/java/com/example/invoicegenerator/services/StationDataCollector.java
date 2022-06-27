package com.example.invoicegenerator.services;

import com.example.invoicegenerator.communication.Producer;
import org.json.JSONObject;

import java.sql.ResultSet;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

public class StationDataCollector extends BaseService {

    public StationDataCollector(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected String executeInternal(String receiveMessage) {
        try {
            // extract values from JSON
            int customerId = new JSONObject(receiveMessage).getInt("customerId");
            int stationId = new JSONObject(receiveMessage).getInt("stationId");

            System.out.println("StationDataCollector: executeInternal(customerId " + customerId + " stationId " + stationId + ")");

            DataAccessService dataAccessService = new DataAccessService();

            ResultSet rs = dataAccessService.getStationData(customerId, stationId);
            double kWh = 0;
            double price = 0;
            while (rs.next()){
                kWh= rs.getDouble("kWh");
                price = rs.getDouble("price");
            }

            // TODO: load amount for specific customer for specific station from DB
            double amount = kWh * price;

            // send message to DataCollectionReceiver with loaded station information
            String postMessage = new JSONObject()
                    .put("customerId", customerId)
                    .put("stationId", stationId)
                    .put("amount", amount)
                    .put("isNewJob", false)
                    .toString();
            Producer.send(postMessage, "DCR_START", BROKER_URL);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;

    }
}
