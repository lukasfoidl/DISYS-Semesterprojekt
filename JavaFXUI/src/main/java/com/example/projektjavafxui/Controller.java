package com.example.projektjavafxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Controller {

    private static final String API = "";

    @FXML
    private TextField tf_customerId;

    @FXML
    private Label l_progress;

    @FXML
    private Button b_download;

    @FXML
    private void generateInvoice() throws IOException, InterruptedException, URISyntaxException {
        l_progress.setText("Progressing...");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API + "/invoice/" + tf_customerId.getText()))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        tf_customerId.setText("");
        l_progress.setText("");
        b_download.setDisable(false);
    }

    @FXML
    private void downloadInvoice() {
        System.out.println("Download invoice");
    }

    private void generatedInvoice() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        // Da wir ein PDF erzeugen müssen und dieses an den Kunden retounieren müssen, habe ich diese Funktion noch ausgelassen,
        // nicht sicher wie das funktionieren soll. Bis jetzt im Scene Builder ein List Element erzeugt, aber wie gesagt, glaube
        // ich nicht, dass wir eine Liste zurück geben sollen sondern eben ein PDF in der dann die ganzen Daten stehen.

    }
}