package com.example.projektjavafxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InvoiceApplication extends Application {

    public static final String API = "http://localhost:8081/invoices/";
    public static Stage stage = null;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(InvoiceApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 250);
        stage.setTitle("Invoice Generator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}