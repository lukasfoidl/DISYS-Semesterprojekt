package com.example.invoicegenerator.controller;

import com.example.invoicegenerator.communication.Producer;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.invoicegenerator.InvoiceGeneratorApplication.BROKER_URL;

@RestController
public class Controller {

    // generate Invoice Mapping
    @PostMapping("/invoices/{customerId}")
    public void startDataGathering(@PathVariable int customerId) {

        System.out.println("startDataGathering(id " + customerId + ")");

        // send message to DataCollectionDispatcher, starts data gathering process
        String postMessage = new JSONObject().put("customerId", customerId).toString();
        Producer.send(postMessage, "DCD_START", BROKER_URL);
    }

    // download invoice Mapping
    @GetMapping("/invoices/{customerId}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable int customerId) {

        // read file or respond with 404
        byte[] content;
        try {
            Path pdfPath = Paths.get("./Invoices/Invoice_" + customerId + ".pdf");
            content = Files.readAllBytes(pdfPath);
        } catch (Exception e) {
            ResponseEntity<byte[]> response = new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
            return response;
        }

        // create response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "Invoice_" + customerId + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(content, headers, HttpStatus.OK);

        return response;
    }

}
