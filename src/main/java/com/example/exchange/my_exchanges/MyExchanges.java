package com.example.exchange.my_exchanges;

import com.example.exchange.Authentication;
import com.example.exchange.Parent;
import com.example.exchange.api.ExchangeService;
import com.example.exchange.api.model.Offer;
import com.example.exchange.api.model.OfferResponse;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyExchanges {

    public TextField username;

    public TextField offering;
    public TextField receiving;
    public ChoiceBox<String> currency;

    public void initialize(){}

    public void showAlert(String msg, final String title)
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
        Offer offer = new Offer(username.getText(), currency.getValue() == "USD", Float.parseFloat(offering.getText()),
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
}
