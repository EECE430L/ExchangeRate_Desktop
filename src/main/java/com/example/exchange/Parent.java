package com.example.exchange;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Parent implements Initializable, OnPageCompleteListener{
    public BorderPane borderPane;
    public Button transactionButton;
    public Button loginButton;
    public Button registerButton;
    public Button logoutButton;
    public Button showHideButton;
    public HBox hBox;
    private static final Logger logger = Logger.getLogger(Parent.class.getName());

    public VBox menuContainer;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateNavigation();
        URL url2 = getClass().getResource("/images/showmore.png");
        Image image = new Image(url2.toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        showHideButton.setGraphic(imageView);

    }
    public void ratesSelected() {
        swapContent(Section.RATES);
    }
    public void statisticsSelected() {
        swapContent(Section.STATISTICS);
    }
    public void graphsSelected() {
        swapContent(Section.GRAPHS);
    }
    public void transactionsSelected() {
        swapContent(Section.TRANSACTIONS);
    }
    public void loginSelected() {
        swapContent(Section.LOGIN);
    }
    public void registerSelected() {
        swapContent(Section.REGISTER);
    }
    public void logoutSelected() {
        Authentication.getInstance().deleteToken();
        swapContent(Section.RATES);
    }
    public void myExchangesSelected(){
        swapContent(Section.MYEXCHANGES);
    }


    @FXML
    public void toggleMenuVisibility() {
        if (menuContainer.isVisible()) {
            menuContainer.setVisible(false);
            hBox.getChildren().remove(menuContainer);
        }else{
            menuContainer.setVisible(true);
            hBox.getChildren().add(0, menuContainer);
        }
    }
    private void swapContent(Section section) {
        try {
            URL url = getClass().getResource(section.getResource());
            FXMLLoader loader = new FXMLLoader(url);
            borderPane.setCenter(loader.load());
            if (section.doesComplete()) {
                ((PageCompleter)
                        loader.getController()).setOnPageCompleteListener(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateNavigation();
    }

    private enum Section {
        RATES,
        GRAPHS,
        TRANSACTIONS,
        STATISTICS,
        MYEXCHANGES,
        LOGIN,
        REGISTER;
        public String getResource() {
            return switch (this) {
                case RATES ->
                        "/rates/rates.fxml";
                case GRAPHS ->
                        "/graph_currency/graph_currency.fxml";
                case TRANSACTIONS ->
                        "/transactions/transactions.fxml";
                case LOGIN ->
                        "/login/login.fxml";
                case REGISTER ->
                        "/register/register.fxml";
                case STATISTICS ->
                        "/statistics/statistics.fxml";
                case MYEXCHANGES ->
                    "/my_exchanges/my_exchanges.fxml";
                default -> null;
            };
        }
        public boolean doesComplete() {
            return switch (this) {
                case LOGIN, REGISTER -> true;
                default -> false;
            };
        }


    }
    @Override
    public void onPageCompleted() {
        swapContent(Section.RATES);
    }

    private void updateNavigation() {
        boolean authenticated = Authentication.getInstance().getToken() !=
                null;
        transactionButton.setManaged(authenticated);
        transactionButton.setVisible(authenticated);
        loginButton.setManaged(!authenticated);
        loginButton.setVisible(!authenticated);
        registerButton.setManaged(!authenticated);
        registerButton.setVisible(!authenticated);
        logoutButton.setManaged(authenticated);
        logoutButton.setVisible(authenticated);
    }
}