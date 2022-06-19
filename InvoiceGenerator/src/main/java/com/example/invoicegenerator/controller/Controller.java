package com.example.invoicegenerator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @PostMapping("/invoices/{customer_Id}")
    public void startDataGathering(@PathVariable int customer_Id) {
        System.out.println("startDataGathering(customer_Id " + customer_Id + ")");
    }

}
