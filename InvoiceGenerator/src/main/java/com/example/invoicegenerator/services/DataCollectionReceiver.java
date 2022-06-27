package com.example.invoicegenerator.services;

import com.example.invoicegenerator.communication.Producer;
import com.example.invoicegenerator.data.Job;
import com.example.invoicegenerator.data.StationData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

public class DataCollectionReceiver extends BaseService {

    private List<Job> jobs = new ArrayList<Job>();

    public DataCollectionReceiver(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected String executeInternal(String receiveMessage) {

        // extract values from JSON
        int customerId = new JSONObject(receiveMessage).getInt("customerId");
        Boolean isNewJob = new JSONObject(receiveMessage).getBoolean("isNewJob");

        System.out.println("DataCollectionReceiver: jobs: " + jobs.size());

        // get message from DataCollectionDispatcher that new job started
        if (isNewJob) {
            int jobSize = new JSONObject(receiveMessage).getInt("jobSize");
            jobs.add(new Job(jobSize, customerId)); // add new Job with number of expected Dtos and corresponding customerId
            System.out.println("DataCollectionReceiver: CREATED NEW JOB -- size: " + jobSize);
            return null;
        }

        // get message from StationDataCollector with station information
        for (Job job: jobs) {

            // add station information to corresponding customer
            if (job.getCustomerId() == customerId && job.getSize() > 0) {

                int stationId = new JSONObject(receiveMessage).getInt("stationId");
                double amount = new JSONObject(receiveMessage).getDouble("amount");

                job.addJob(new StationData(stationId, amount));

                System.out.println("DataCollectionReceiver: executeInternal(customerId " + customerId + " stationId " + stationId + " amount " + amount + ") -- STATIONDATA ADDED");
            }

            //System.out.println("DataCollectionReceiver: job.size: " + job.getSize());

            // if all information gathered, send message to PdfGenerator with data of actual job, delete job afterwards
            if (job.getSize() == 0) {

                JSONArray array = new JSONArray();
                for (StationData item : job.getStations()) {
                    JSONObject obj = new JSONObject()
                        .put("stationId", item.getStationId())
                        .put("amount", item.getAmount());
                    array.put(obj);
                }

                String postMessage = new JSONObject()
                        .put("customerId", job.getCustomerId())
                        .put("stationData", array)
                        .toString();
                Producer.send(postMessage, "PG_START", BROKER_URL);

                jobs.remove(job);
                return null;
            }
        }

        return null;

    }
}
