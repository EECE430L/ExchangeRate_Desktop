module com.example.exchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires retrofit2.converter.gson;
    opens com.example.exchange to javafx.fxml;
    opens com.example.exchange.api.model to javafx.base, gson;
    exports com.example.exchange;
    exports com.example.exchange.rates;
    opens com.example.exchange.rates to javafx.fxml;
    requires java.prefs;
    opens com.example.exchange.login to javafx.fxml;
    opens com.example.exchange.register to javafx.fxml;
    opens com.example.exchange.transactions to javafx.fxml;
    exports com.example.exchange.graph_currency;
    opens com.example.exchange.graph_currency to javafx.fxml;
    exports com.example.exchange.statistics;
    opens com.example.exchange.statistics to javafx.fxml;
    exports com.example.exchange.my_exchanges;
    opens com.example.exchange.my_exchanges to javafx.fxml;
}