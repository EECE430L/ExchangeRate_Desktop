package com.example.exchange.graph_currency;

import com.example.exchange.Authentication;
import com.example.exchange.api.ExchangeService;
import com.example.exchange.api.model.Fluctuation;
import com.example.exchange.api.model.FluctuationRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class GraphCurrency {
    @FXML
    private LineChart<String, Number> lineChart;

    private static final Logger logger = Logger.getLogger(GraphCurrency.class.getName());

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    private final Tooltip closestDataPointsTooltip = new Tooltip();
    List<XYChart.Data<String, Number>> closestDataPoints = new ArrayList<>();

    public void initialize() {
        // Create a data series with some sample data

        XYChart.Series<String, Number> LBPToUSD = new XYChart.Series<>();
        XYChart.Series<String, Number> USDToLBP = new XYChart.Series<>();
        closestDataPointsTooltip.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
        //get today's date
        Date endDate = new Date();
        //get the start day 1 week before today
        Date startDate = new Date(endDate.getTime() - 7 * 24 * 3600 * 1000);
        //get the fluctuations between 1 week ago and today
        fetchFluctuations(LBPToUSD, startDate.getDate() + "", endDate.getDate() + "", startDate.getMonth() + 1 + "", endDate.getMonth() + 1 + "", startDate.getYear() + 1900 + "", endDate.getYear() + 1900 + "", USDToLBP);
        for (XYChart.Data<String, Number> dataPoint : LBPToUSD.getData()) {
            Logger.getGlobal().info(dataPoint.getYValue().toString());
        }
        for (XYChart.Data<String, Number> dataPoint : LBPToUSD.getData()) {
            Logger.getGlobal().warning(dataPoint.getXValue().toString());
        }

        lineChart.setOnMouseMoved(event -> {
            if (!closestDataPoints.isEmpty()){
                for (XYChart.Data<String, Number> dataPoint : closestDataPoints) {
                    dataPoint.getNode().setStyle("");
                }
            }
            Point2D mouseSceneCoords = new Point2D(event.getSceneX(), event.getSceneY());
            Axis<Number> yAxis = lineChart.getYAxis();
            Axis<String> xAxis =  lineChart.getXAxis();
            double xAxisLoc = xAxis.sceneToLocal(mouseSceneCoords).getX();
            double yAxisLoc = yAxis.sceneToLocal(mouseSceneCoords).getY();


            double closestDistance = Double.MAX_VALUE;

            for (XYChart.Series<String, Number> s : lineChart.getData()) {
                for (XYChart.Data<String, Number> d : s.getData()) {
                    double distance = Math.hypot(xAxis.getDisplayPosition(d.getXValue()) - xAxisLoc,
                            yAxis.getDisplayPosition(d.getYValue()) - yAxisLoc);
                    if (distance < closestDistance) {
                        closestDataPoints.clear();
                        closestDistance = distance;
                        closestDataPoints.add(d);
                    } else if (!closestDataPoints.isEmpty() &&d.getXValue().equals(closestDataPoints.get(0).getXValue())) {
                        closestDataPoints.add(d);
                    }
                }
            }
            for (XYChart.Series<String, Number> s : lineChart.getData()) {
                for (XYChart.Data<String, Number> d : s.getData()) {
                    if (d.getXValue().equals(closestDataPoints.get(0).getXValue()) && !closestDataPoints.contains(d)) {
                        closestDataPoints.add(d);
                    }
                    }
                }

            if (!closestDataPoints.isEmpty()) {
                StringBuilder tooltipTextBuilder = new StringBuilder();
                tooltipTextBuilder.append(String.format("Date: %s\n",  closestDataPoints.get(0).getXValue()));
                for (XYChart.Data<String, Number> dataPoint : closestDataPoints) {
                    dataPoint.getNode().setStyle("-fx-background-color: #ff0000, yellow;");
                    if (dataPointInSeries(dataPoint, LBPToUSD) && dataPointInSeries(dataPoint, USDToLBP)){
                        tooltipTextBuilder.append(String.format("BUY USD Rate: %.2f\n",
                                dataPoint.getYValue().doubleValue()));
                        tooltipTextBuilder.append(String.format("SELL USD Rate: %.2f\n",
                                dataPoint.getYValue().doubleValue()));
                        break;
                    }
                    else if (dataPointInSeries(dataPoint, LBPToUSD)) {
                        tooltipTextBuilder.append(String.format("BUY USD Rate: %.2f\n",
                                dataPoint.getYValue().doubleValue()));
                    }else{
                        tooltipTextBuilder.append(String.format("SELL USD Rate: %.2f\n",
                                dataPoint.getYValue().doubleValue()));
                    }

                }
                closestDataPointsTooltip.setText(tooltipTextBuilder.toString());
                closestDataPointsTooltip.show(lineChart, event.getScreenX(), event.getScreenY());
            } else {
                closestDataPointsTooltip.hide();
            }
        });

    }

    public void getDatesAndSubmit(){
        String StartDay = startDate.getValue().getDayOfMonth() + "";
        String EndDay = endDate.getValue().getDayOfMonth() + "";
        String StartMonth = startDate.getValue().getMonthValue() + "";
        String EndMonth = endDate.getValue().getMonthValue() + "";
        String StartYear = startDate.getValue().getYear() + "";
        String EndYear = endDate.getValue().getYear() + "";
        XYChart.Series<String, Number> LBPToUSD = new XYChart.Series<>();
        XYChart.Series<String, Number> USDToLBP = new XYChart.Series<>();
        fetchFluctuations(LBPToUSD, StartDay, EndDay, StartMonth, EndMonth, StartYear, EndYear, USDToLBP);
    }



    public void fetchFluctuations(XYChart.Series<String, Number> LBPToUSD,
            String StartDay, String EndDay, String StartMonth, String EndMonth, String StartYear, String EndYear, XYChart.Series<String, Number> USDToLBP) {
        lineChart.getData().forEach(series -> series.getData().clear());
        lineChart.getData().clear();
        LBPToUSD.getData().clear();
        USDToLBP.getData().clear();
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        FluctuationRequest fluctuationRequest = new FluctuationRequest();
        fluctuationRequest.StartDay = StartDay;
        fluctuationRequest.EndDay = EndDay;
        fluctuationRequest.StartMonth = StartMonth;
        fluctuationRequest.EndMonth = EndMonth;
        fluctuationRequest.StartYear = StartYear;
        fluctuationRequest.EndYear = EndYear;
        ArrayList<Fluctuation> listLBPExchange = new ArrayList<>();
        ExchangeService.exchangeApi().getLbpFluctuations(fluctuationRequest.StartDay, fluctuationRequest.EndDay,
                fluctuationRequest.StartMonth, fluctuationRequest.EndMonth, fluctuationRequest.StartYear, fluctuationRequest.EndYear,
                authHeader).enqueue(
                new Callback<List<Fluctuation>>() {
                       @Override
                          public void onResponse(Call<List<Fluctuation>> call,
                                                 Response<List<Fluctuation>> response) {
                                 List<Fluctuation> ListLBPExchange = response.body();
                                 //logger.log(java.util.logging.Level.INFO, "LBP to USD: " + ListLBPExchange.size());
                                    for (Fluctuation lbpExchangeRateDate : ListLBPExchange) {
                                        listLBPExchange.add(lbpExchangeRateDate);
                                    }

                                    for (Fluctuation lbpExchangeRateDate : listLBPExchange) {
                                        if (lbpExchangeRateDate.getLbpToUsdRate() != null) {
                                            Platform.runLater(()->LBPToUSD.getData().add(new XYChart.Data<>(lbpExchangeRateDate.getDate(), lbpExchangeRateDate.getLbpToUsdRate())));
                                        }
                                    }
                                    logger.info("LBP to USD: " + listLBPExchange.size());
                                    LBPToUSD.setName("LBP to USD");
                                    lineChart.setAnimated(false);
                                    Platform.runLater(() -> {
                                        lineChart.getData().add(LBPToUSD);
                                    }
                                    );

                                    //XYChart.Series<String, Number> USDToLBP = new XYChart.Series<>();
                                    for (Fluctuation lbpExchangeRateDate : listLBPExchange) {
                                        if (lbpExchangeRateDate.getUsdToLbpRate() != null) {
                                            Platform.runLater(()->USDToLBP.getData().add(new XYChart.Data<>(lbpExchangeRateDate.getDate(), lbpExchangeRateDate.getUsdToLbpRate())));
                                        }
                                    }
                                    USDToLBP.setName("USD to LBP");

                                    Platform.runLater(() -> {
                                                lineChart.getData().add(USDToLBP);
                                                Node node = USDToLBP.getNode();
                                                if (node != null) {
                                                    node.setStyle("-fx-stroke: #ff0000;");
                                                    //node.getStyleClass().add("usd-to-lbp-legend");
                                                    node.getStyleClass().add("usd-to-lbp-symbol");

                                                }
                                                for (XYChart.Data<String, Number> data : USDToLBP.getData()) {
                                                    Node dataNode = data.getNode();
                                                    if (dataNode != null) {
                                                        dataNode.getStyleClass().add("usd-to-lbp-data");
                                                    }
                                                }
                                            }
                                    );
                                }
                            @Override
                            public void onFailure(Call<List<Fluctuation>> call,
                                                  Throwable throwable) {
                            }
                }
        );
    }

    public boolean dataPointInSeries(XYChart.Data<String, Number> dataPoint, XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> d : series.getData()) {
            if (d.getYValue().equals(dataPoint.getYValue()) && d.getXValue().equals(dataPoint.getXValue())) {
                return true;
            }
        }
        return false;
    }


}
