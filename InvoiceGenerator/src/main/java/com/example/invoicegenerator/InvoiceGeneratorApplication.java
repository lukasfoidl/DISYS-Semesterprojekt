package com.example.invoicegenerator;

import com.example.invoicegenerator.execution.Executor;
import com.example.invoicegenerator.services.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class InvoiceGeneratorApplication {

    public final static String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {

        SpringApplication.run(InvoiceGeneratorApplication.class, args);

        List<BaseService> services = new ArrayList<>();
        services.add(new DataCollectionDispatcher("DCD_START", "DCD_END", BROKER_URL));
        services.add(new StationDataCollector("SDC_START", "SDC_END", BROKER_URL));
        services.add(new DataCollectionReceiver("DCR_START", "DCR_END", BROKER_URL));
        services.add(new PdfGenerator("PG_START", "PG_END", BROKER_URL));

        Executor executor = new Executor(services);
        executor.start();
    }

}
