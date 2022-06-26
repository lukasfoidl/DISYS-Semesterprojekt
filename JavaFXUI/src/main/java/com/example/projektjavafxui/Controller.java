package com.example.projektjavafxui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private int customerId = -1;
    private boolean run = true;

    @FXML
    private TextField tf_customerId;

    @FXML
    private Label l_progress;

    @FXML
    private Button b_download;

    @FXML
    private void onDownload() {
        l_progress.setText("Processing...");

        // Vaildation
        if (!isTextNumber(tf_customerId.getText())) {
            l_progress.setText("Error! Invalid customer id.");
            return;
        }

        b_download.setDisable(true);

        // File Dialog
        String path = "";
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(InvoiceApplication.stage);
        if (file != null) {
            path = file.getAbsolutePath();
        } else {
            l_progress.setText("Process cancelled.");
            b_download.setDisable(false);
            return;
        }

        // Request - Response
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(InvoiceApplication.API + tf_customerId.getText()))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                l_progress.setText("Error! The server responded with status code " + response.statusCode() + ".");
                b_download.setDisable(false);
                return;
            }
        } catch (Exception e) {
            System.out.println("Error! " + e.getMessage());
            l_progress.setText("Error! Something went wrong talking to the server.");
            b_download.setDisable(false);
            return;
        }

        customerId = Integer.parseInt(tf_customerId.getText());

        // Background task
        run = true;
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        String finalPath = path;
        exec.scheduleAtFixedRate(new Runnable() {
            private int count = 1;

            @Override
            public void run() {
                System.out.println(count);
                System.out.println(run);

                // check if task has been cancelled
                if (!run) {
                    exec.shutdown();
                    return;
                }

                // ask server for Pdf
                boolean success = getPdf(customerId, finalPath);
                count++;

                // file successfully downloaded
                if (success) {
                    Platform.runLater(() -> {
                        l_progress.setText("Download succeeded.");
                        b_download.setDisable(false);
                    });
                    exec.shutdown();
                    return;
                }

                // after 6 times cancel task
                if (count > 6) {
                    Platform.runLater(() -> {
                        l_progress.setText("Error! Timeout of 30 seconds reached, file could not be downloaded.");
                        b_download.setDisable(false);
                    });
                    exec.shutdown();
                }
            }
        }, 0, 5, TimeUnit.SECONDS); // execute every 5 seconds
    }

    private boolean isTextNumber(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    @FXML
    private void onCancel() {
        l_progress.setText("Process cancelled.");
        b_download.setDisable(false);
        run = false;
    }

    public boolean getPdf(int customerId, String path) {
        // Request - Response
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(InvoiceApplication.API + customerId))
                    .GET()
                    .build();
            HttpResponse<Path> response = HttpClient.newHttpClient().
                    send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(path)));

            if (response.statusCode() != 200) {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error! " + e.getMessage());
            return false;
        }
        return true;
    }
}