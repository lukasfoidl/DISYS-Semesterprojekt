package com.example.invoicegenerator.services;

import com.example.invoicegenerator.data.StationData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PdfGenerator extends BaseService {

    public PdfGenerator(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected String executeInternal(String receiveMessage) {
        try {
            // extract values from JSON
            int customerId = new JSONObject(receiveMessage).getInt("customerId");
            JSONArray array = new JSONObject(receiveMessage).getJSONArray("stationData");
            ArrayList<StationData> stationData = new ArrayList<>();

            for (int i=0; i < array.length(); ++i){
                JSONObject obj = array.getJSONObject(i);
                stationData.add(new StationData(obj.getInt("stationId"), obj.getDouble("amount")));
            }

            /*for (Object item : array) {
                JSONObject obj = (JSONObject) item;
                stationData.add(new StationData(obj.getInt("stationId"), obj.getDouble("amount")));
            }*/

            double sum = 0;
            for (StationData item : stationData) {
                sum += item.getAmount();
            }
            System.out.println("PdfGenerator: executeInternal(customerId " + customerId + " amount: " + sum + ")");

            // TODO: generate PDF and make available for client
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
