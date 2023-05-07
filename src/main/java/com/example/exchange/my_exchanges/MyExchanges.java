package com.example.exchange.my_exchanges;

import com.example.exchange.Authentication;
import com.example.exchange.Parent;
import com.example.exchange.api.ExchangeService;
import com.example.exchange.api.model.Offer;
import com.example.exchange.api.model.OfferResponse;
import com.example.exchange.api.model.ProcessOffer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.logging.Logger;

public class MyExchanges {

    public TextField username;

    public TextField offering;
    public TextField receiving;
    public ChoiceBox<String> currency;

    public TableColumn sentRequested;
    public TableColumn sentOffered;
    public TableColumn sentType;
    public TableColumn sentParty;
    public TableView sentTableView;
    public AnchorPane sentAnchorPane;
    public Tab tab;

    public TableColumn receivedRequested;
    public TableColumn receivedOffered;
    public TableColumn receivedType;
    public TableColumn receivedParty;
    public TableView receivedTableView;
    public AnchorPane receivedAnchorPane;

    public TableColumn acceptOffer;
    public TableColumn rejectOffer;
    public Logger logger = Logger.getLogger(MyExchanges.class.getName());

    public void initialize(){
        sentRequested.setCellValueFactory(new
                PropertyValueFactory<OfferResponse, Float>("requested_amount"));
        sentOffered.setCellValueFactory(new
                PropertyValueFactory<OfferResponse, Float>("offered_amount"));
        sentType.setCellValueFactory(new
                PropertyValueFactory<OfferResponse, String>("type"));
        sentParty.setCellValueFactory(new
                PropertyValueFactory<OfferResponse, String>("receiver")
        );
        receivedRequested.setCellValueFactory(new
                PropertyValueFactory<OfferResponse, Float>("requested_amount"));
        receivedOffered.setCellValueFactory(new
                PropertyValueFactory<OfferResponse, Float>("offered_amount"));
        receivedType.setCellValueFactory(new
                PropertyValueFactory<OfferResponse, String>("type"));
        receivedParty.setCellValueFactory(new
                PropertyValueFactory<OfferResponse, String>("offerer")
        );

        fetchSentOffers();
        fetchReceivedOffers();
        addAcceptButtonToTable();
        addRejectButtonToTable();
    }

    public void showAlert(String msg, final String title) //https://www.geeksforgeeks.org/javafx-alert-with-examples/
    {
        Alert.AlertType type;
        if (title=="Error"){
            type = Alert.AlertType.ERROR;
        }else{
            type = Alert.AlertType.CONFIRMATION;
        }
        Platform.runLater(() -> {

            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setContentText(msg);
            // Set the alert dialog to be non-blocking and show it
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            alert.showAndWait();
            return;
        });
    }

    public void clearFields(){
        Platform.runLater(()-> {
                    username.setText("");
                    offering.setText("");
                    receiving.setText("");
                    currency.setValue("");
                }
        );

    }
    public void acceptOffer(OfferResponse offer){
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ProcessOffer processOffer = new ProcessOffer(offer.getId(), true);
        ExchangeService.exchangeApi().processOffer(processOffer, authHeader).enqueue(new Callback<ProcessOffer>() {
            @Override
            public void onResponse(Call<ProcessOffer> call, Response<ProcessOffer> response) {
                Platform.runLater(() -> {
                    fetchReceivedOffers();
                });
            }

            @Override
            public void onFailure(Call<ProcessOffer> call, Throwable throwable) {
                Platform.runLater(() -> {
                    fetchReceivedOffers();
                });
            }
        });

    }

    public void rejectOffer(OfferResponse offer){
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ProcessOffer processOffer = new ProcessOffer(offer.getId(), false);
        ExchangeService.exchangeApi().processOffer(processOffer, authHeader).enqueue(new Callback<ProcessOffer>() {
            @Override
            public void onResponse(Call<ProcessOffer> call, Response<ProcessOffer> response) {
                Platform.runLater(() -> {
                    fetchReceivedOffers();
                });
            }

            @Override
            public void onFailure(Call<ProcessOffer> call, Throwable throwable) {
                Platform.runLater(() -> {
                    fetchReceivedOffers();
                });
            }
        });

    }
    private void addRejectButtonToTable() {
        //reference: https://riptutorial.com/javafx/example/27946/add-button-to-tableview

        TableColumn<OfferResponse, Void> colBtn = new TableColumn("Reject");
        colBtn.setPrefWidth(300);

        javafx.util.Callback<TableColumn<OfferResponse, Void>, TableCell<OfferResponse, Void>> cellFactory = new javafx.util.Callback<TableColumn<OfferResponse, Void>, TableCell<OfferResponse, Void>>() {
            @Override
            public TableCell<OfferResponse, Void> call(final TableColumn<OfferResponse, Void> param) {
                final TableCell<OfferResponse, Void> cell = new TableCell<OfferResponse, Void>() {

                    private final Button btn = new Button("Reject");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            OfferResponse OfferResponse = getTableView().getItems().get(getIndex());
                            rejectOffer(OfferResponse);
                            Platform.runLater(() -> {
                                fetchReceivedOffers();
                            });
                        });
                        btn.setMaxWidth(Double.MAX_VALUE); //to make it take the full cell width
                        btn.setPrefWidth(USE_COMPUTED_SIZE);
                    }


                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        receivedTableView.getColumns().add(colBtn);

    }
    private void addAcceptButtonToTable() {
        //reference: https://riptutorial.com/javafx/example/27946/add-button-to-tableview

        TableColumn<OfferResponse, Void> colBtn = new TableColumn("Accept");
        colBtn.setPrefWidth(300);

        javafx.util.Callback<TableColumn<OfferResponse, Void>, TableCell<OfferResponse, Void>> cellFactory = new javafx.util.Callback<TableColumn<OfferResponse, Void>, TableCell<OfferResponse, Void>>() {
            @Override
            public TableCell<OfferResponse, Void> call(final TableColumn<OfferResponse, Void> param) {
                final TableCell<OfferResponse, Void> cell = new TableCell<OfferResponse, Void>() {

                    private final Button btn = new Button("Accept");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            OfferResponse OfferResponse = getTableView().getItems().get(getIndex());
                            acceptOffer(OfferResponse);

                        });
                        btn.setMaxWidth(Double.MAX_VALUE);
                        btn.setPrefWidth(USE_COMPUTED_SIZE);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        receivedTableView.getColumns().add(colBtn);

    }


    public void offerTransaction() {
        if (username.getText().isEmpty() || offering.getText().isEmpty() || receiving.getText().isEmpty() || currency.getValue() == null || currency.getValue().isEmpty()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Empty Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields");
                // Set the alert dialog to be non-blocking and show it
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setAlwaysOnTop(true);
                alert.showAndWait();
                return;
            });
            return;
        }
        if (!offering.getText().matches("\\d+") || !receiving.getText().matches("\\d+")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please enter valid numbers for offering and receiving");
                // Set the alert dialog to be non-blocking and show it
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setAlwaysOnTop(true);
                alert.showAndWait();
                return;
            });
            return;
        }
        Offer offer = new Offer(username.getText(), currency.getValue().equals("USD"), Float.parseFloat(offering.getText()),
                Float.parseFloat(receiving.getText()));
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().addOffer(offer,
                authHeader).enqueue(new Callback<OfferResponse>() {


            @Override
            public void onResponse(Call<OfferResponse> call, Response<OfferResponse> response) {
                if (response.code() == 404){
                    showAlert("User not found", "Error");
                }
                else if (response.code() == 400){
                    showAlert("Invalid offer, are you trying to make an offer to yourself?", "Error");
                }
                else if (response.code() == 401){ //watching out for token that could be expired
                    showAlert("Please login first", "Error");
                    clearFields();
                    Authentication.getInstance().deleteToken();
                    Platform.runLater(()->{
                        Parent.getInstance().logoutSelected();
                    });

                }
                else if (response.code() == 201){
                    showAlert("Offer added",  "Success");
                    clearFields();
                }
            }

            @Override
            public void onFailure(Call<OfferResponse> call, Throwable t) {

            }
        });
    }

    public void refreshData(){
        fetchSentOffers();
        fetchReceivedOffers();
    }

    public void fetchSentOffers(){
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().getSentOffers(authHeader).enqueue(new Callback<List<OfferResponse>>() {
            @Override
            public void onResponse(Call<List<OfferResponse>> call, Response<List<OfferResponse>> response) {
                if (response.code() == 401){ //watching out for token that could be expired
                    showAlert("Please login first", "Error");
                    clearFields();
                    Authentication.getInstance().deleteToken();
                    Platform.runLater(()->{
                        Parent.getInstance().logoutSelected();
                    });

                }
                else if (response.code() == 200){
                    Platform.runLater(()->{
                        sentTableView.getItems().clear();
                        sentTableView.getItems().addAll(response.body());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<OfferResponse>> call, Throwable t) {

            }
        });
    }

    public void fetchReceivedOffers(){
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().getReceivedOffers(authHeader).enqueue(new Callback<List<OfferResponse>>() {
            @Override
            public void onResponse(Call<List<OfferResponse>> call, Response<List<OfferResponse>> response) {
                if (response.code() == 401){ //watching out for token that could be expired
                    showAlert("Please login first", "Error");
                    clearFields();
                    Authentication.getInstance().deleteToken();
                    Platform.runLater(()->{
                        Parent.getInstance().logoutSelected();
                    });

                }
                else if (response.code() == 200){
                    Platform.runLater(()->{
                        receivedTableView.getItems().clear();
                        receivedTableView.getItems().addAll(response.body());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<OfferResponse>> call, Throwable t) {

            }
        });
    }
}
