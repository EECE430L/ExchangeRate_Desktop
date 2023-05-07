package com.example.exchange.statistics;

import com.example.exchange.Authentication;
import com.example.exchange.api.ExchangeService;
import com.example.exchange.api.model.NumTransactions;
import com.example.exchange.api.model.PercentageChange;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

public class Statistics {
    @FXML
    BorderPane borderPane;
    @FXML
    Region hspacer;

    @FXML
    Region vspacer0;
    @FXML
    Region vspacer1;

    @FXML
    Region vspacer2;

    @FXML
    Region vspacer3;

    @FXML
    Button lbp1;

    @FXML
    Button usd1;

    @FXML
    Button lbp2;

    @FXML
    Button usd2;
    @FXML
    Label usdToLbpNum;
    @FXML
    Label lbpToUsdNum;
    Logger logger = Logger.getLogger(Authentication.class.getName());

    @FXML
    Label buyUsdChange;

    @FXML
    Label sellUsdChange;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    public Tooltip tooltip;


    public void attachImage(Button button, String path){
        URL url2 = getClass().getResource(path);
        Image image = new Image(url2.toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(25);
        button.setGraphic(imageView);
    }
    public void initialize(){
        if (tooltip == null) {
            tooltip = new Tooltip();
        }
        tooltip.setOpacity(0.0);
        tooltip.setShowDelay(Duration.ZERO);
        Tooltip.install(borderPane,tooltip);
        hspacer.setPrefWidth(borderPane.getPrefWidth()/10);
        vspacer1.setPrefHeight(borderPane.getPrefHeight()/10);
        vspacer2.setPrefHeight(borderPane.getPrefHeight()/10);
        vspacer3.setPrefHeight(borderPane.getPrefHeight()/10);
        vspacer0.setPrefHeight(borderPane.getPrefHeight()/10);
        attachImage(lbp1,"/images/leb.jpg");
        attachImage(usd1,"/images/usa.png");
        attachImage(lbp2,"/images/leb.jpg");
        attachImage(usd2,"/images/usa.png");
        Date _endDate = new Date();
        Date _startDate = new Date(_endDate.getTime() - 7 * 24 * 3600 * 1000);
        endDate.setValue(_endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        startDate.setValue(_startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        fetchStatistics( _startDate.getDate() + "", _endDate.getDate() + "", _startDate.getMonth() + 1 + "", _endDate.getMonth() + 1 + "", _startDate.getYear() + 1900 + "", _endDate.getYear() + 1900 + "");
        borderPane.requestFocus();
    }

    public void getDatesAndSubmit(){
        if(startDate.getValue() == null || endDate.getValue() == null){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please select a start and end date");
                alert.showAndWait();
                return;
            });
            return;

        }
        String StartDay = startDate.getValue().getDayOfMonth() + "";
        String EndDay = endDate.getValue().getDayOfMonth() + "";
        String StartMonth = startDate.getValue().getMonthValue() + "";
        String EndMonth = endDate.getValue().getMonthValue() + "";
        String StartYear = startDate.getValue().getYear() + "";
        String EndYear = endDate.getValue().getYear() + "";
        XYChart.Series<String, Number> LBPToUSD = new XYChart.Series<>();
        XYChart.Series<String, Number> USDToLBP = new XYChart.Series<>();
        fetchStatistics( StartDay, EndDay, StartMonth, EndMonth, StartYear, EndYear);
    }

    public void fetchStatistics(String StartDay, String EndDay, String StartMonth, String EndMonth, String StartYear, String EndYear){
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().getRatesPercentChange(StartDay, EndDay, StartMonth, EndMonth, StartYear, EndYear, authHeader)
                .enqueue(
                        new Callback<PercentageChange>() {
                            @Override
                            public void onResponse(Call<PercentageChange> call,
                                                   Response<PercentageChange> response) {
                                if (response.isSuccessful()) {
                                    PercentageChange percentageChange = response.body();
                                    Float percentageUsdLbp = percentageChange.percent_change_USD_to_LBP;
                                    Float percentageLbpUsd = percentageChange.percent_change_LBP_to_USD;
                                    Platform.runLater(() -> {
                                        if (percentageUsdLbp!= null && !percentageUsdLbp.isNaN() && percentageUsdLbp > 0) {
                                            sellUsdChange.setStyle("-fx-text-fill: green");
                                            sellUsdChange.setText("+" + percentageUsdLbp.toString());
                                        } else if (percentageUsdLbp!= null && !percentageUsdLbp.isNaN() && percentageUsdLbp < 0) {
                                            sellUsdChange.setStyle("-fx-text-fill: red");
                                            sellUsdChange.setText( percentageUsdLbp.toString());
                                        }

                                        if (percentageLbpUsd!=null &&!percentageLbpUsd.isNaN() && percentageLbpUsd > 0) {
                                            buyUsdChange.setStyle("-fx-text-fill: green");
                                            buyUsdChange.setText("+" + percentageLbpUsd.toString());
                                        } else if (percentageLbpUsd!=null &&!percentageLbpUsd.isNaN() && percentageLbpUsd < 0) {
                                            buyUsdChange.setStyle("-fx-text-fill: red");
                                            buyUsdChange.setText(percentageLbpUsd.toString());
                                        }
                                    });
                                }

                                ExchangeService.exchangeApi().getNumTransactions(StartDay, EndDay, StartMonth, EndMonth, StartYear, EndYear, authHeader)
                                        .enqueue(
                                                new Callback<NumTransactions>() {
                                                    @Override
                                                    public void onResponse(Call<NumTransactions> call,
                                                                           Response<NumTransactions> response) {
                                                        if (response.isSuccessful()) {
                                                            NumTransactions numTransactions = response.body();
                                                            Integer numUsdLbp = numTransactions.num_usd_to_lbp_transactions;
                                                            Integer numLbpUsd = numTransactions.num_lbp_to_usd_transactions;
                                                            Platform.runLater(() -> {
                                                                if (numUsdLbp != null) {
                                                                    usdToLbpNum.setText(numUsdLbp.toString());
                                                                }else{
                                                                    usdToLbpNum.setText("0");
                                                                }
                                                                if (numLbpUsd != null) {
                                                                    lbpToUsdNum.setText(numLbpUsd.toString());
                                                                }else{
                                                                    lbpToUsdNum.setText("0");
                                                                }
                                                                tooltip.setText("Between "+StartDay+" "+StartMonth+" "+StartYear+" and " +EndDay+" "+EndMonth+" "+EndYear+ "there are " + numUsdLbp + " USD to LBP transactions and " + numLbpUsd + " LBP to USD transactions"+ " and the percentage change is " + sellUsdChange.getText() + " for USD to LBP and " + buyUsdChange.getText() + " for LBP to USD");
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<NumTransactions> call, Throwable t) {

                                                    }
                                                });
                            }

                            @Override
                            public void onFailure(Call<PercentageChange> call, Throwable t) {

                            }
                        }
                );


    }

    public void readStatistics() {
        borderPane.requestFocus();
    }
}
