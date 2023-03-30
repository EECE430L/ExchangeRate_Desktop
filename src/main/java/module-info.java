module com.example.lab5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires retrofit2.converter.gson;
    opens com.example.lab5 to javafx.fxml;
    opens com.example.lab5.api.model to javafx.base, gson;
    exports com.example.lab5;
    exports com.example.lab5.rates;
    opens com.example.lab5.rates to javafx.fxml;
    requires java.prefs;
    opens com.example.lab5.login to javafx.fxml;
    opens com.example.lab5.register to javafx.fxml;
    opens com.example.lab5.transactions to javafx.fxml;
}