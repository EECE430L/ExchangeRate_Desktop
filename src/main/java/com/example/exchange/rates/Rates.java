package com.example.exchange.rates;

import com.example.exchange.Authentication;
import com.example.exchange.api.ExchangeService;
import com.example.exchange.api.model.ExchangeRates;
import com.example.exchange.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rates {
    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;
    public TextField lbpTextField;
    public TextField usdTextField;
    public ToggleGroup transactionType;
    public ToggleGroup conversionType;
    public Label AmountLabel;
    public TextField calculatorTextField;

    public void initialize() {
        fetchRates();
    }
    private void fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(new Callback<ExchangeRates>() {
             @Override
             public void onResponse(Call<ExchangeRates> call,
                                    Response<ExchangeRates> response) {
                 ExchangeRates exchangeRates = response.body();
                 Platform.runLater(() -> {
                     if (exchangeRates.lbpToUsd ==null || exchangeRates.lbpToUsd.isNaN()) {
                         buyUsdRateLabel.setText("N/A");
                     }else{
                         buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());
                     }
                     if (exchangeRates.usdToLbp ==null || exchangeRates.usdToLbp.isNaN())
                         sellUsdRateLabel.setText("N/A");
                     else
                         sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());

                 });
             }
             @Override
             public void onFailure(Call<ExchangeRates> call, Throwable
                     throwable) {
             }
         });
    }

    public void addTransaction(ActionEvent actionEvent) {
        if (usdTextField==null || lbpTextField==null || usdTextField.getText().isEmpty() || lbpTextField.getText().isEmpty() || transactionType.getSelectedToggle() == null) {
            return;
        }
        Transaction transaction = new Transaction(
                Float.parseFloat(usdTextField.getText()),
                Float.parseFloat(lbpTextField.getText()),
                ((RadioButton)
                        transactionType.getSelectedToggle()).getText().equals("Sell USD")
        );

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().addTransaction(transaction,
                authHeader).enqueue(new Callback<Object>() {
          @Override
          public void onResponse(Call<Object> call, Response<Object>
                  response) {
              try {
                  Thread.sleep(1000); //waiting to make sure backend has finished processing the transaction
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              fetchRates();
              Platform.runLater(() -> {
                  usdTextField.setText("");
                  lbpTextField.setText("");
                  transactionType.selectToggle(null);
              });
          }
          @Override
          public void onFailure(Call<Object> call, Throwable throwable)
          {
          }
      });
    }

    public void calculateRate(){
        if (calculatorTextField.getText()==null || calculatorTextField.getText().isEmpty() || conversionType.getSelectedToggle() == null ) {
            return;
        }
        if (((RadioButton) conversionType.getSelectedToggle()).getText().equals("TO LBP")) {
            if (sellUsdRateLabel.getText().equals("N/A"))
                AmountLabel.setText("N/A");
            else
                AmountLabel.setText(String.valueOf(Float.parseFloat(calculatorTextField.getText()) * Float.parseFloat(sellUsdRateLabel.getText())));
        } else {
            if (buyUsdRateLabel.getText().equals("N/A"))
                AmountLabel.setText("N/A");
            else {
                AmountLabel.setText(String.valueOf(Float.parseFloat(calculatorTextField.getText()) / Float.parseFloat(buyUsdRateLabel.getText())));
            }
        }

    }

}