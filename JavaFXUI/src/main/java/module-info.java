module com.example.projektjavafxui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens com.example.projektjavafxui to javafx.fxml;
    exports com.example.projektjavafxui;
}