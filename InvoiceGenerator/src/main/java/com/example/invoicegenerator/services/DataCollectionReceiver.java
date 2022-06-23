package com.example.invoicegenerator.services;

import com.example.invoicegenerator.communication.Producer;
import com.example.invoicegenerator.store.CustomerDto;
import com.example.invoicegenerator.store.Job;

import java.util.ArrayList;
import java.util.List;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

public class DataCollectionReceiver extends BaseService {

    private List<Job> jobs = new ArrayList<Job>();

    public DataCollectionReceiver(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected CustomerDto executeInternal(CustomerDto dto) {

        // get message from DataCollectionDispatcher that new job started
        if (dto.isNewJob()) {
            jobs.add(new Job(dto.getCount(), dto.getCustomerId())); // add new Job with number of expected Dtos and corresponding customerId
            return null;
        }

        //System.out.println("DataCollectionReceiver: jobs: " + jobs.size());

        // get message from StationDataCollector with station information
        for (Job job:
             jobs) {
            // add station information to corresponding customer
            if (job.getCustomerId() == dto.getCustomerId() && job.getCount() > 0) {
                job.addJob(dto);
                System.out.println("DataCollectionReceiver: executeInternal(customerId " + dto.getCustomerId() + " stationId " + dto.getStationId() + " amount " + dto.getAmount() + ") -- JOB ADDED");
            }

            //System.out.println("DataCollectionReceiver: job.count: " + job.getCount());

            // if all information gathered, send message to PdfGenerator with full invoice amount, and delete job
            if (job.getCount() == 0) {
                double sum = 0;
                for (CustomerDto item :
                        job.getJobs()) {
                    sum+=item.getAmount();
                }
                Producer.send(new CustomerDto(job.getCustomerId(), sum), "PG_START", BROKER_URL);
                jobs.remove(job);
                return null;
            }
        }

        return null;

    }
}
