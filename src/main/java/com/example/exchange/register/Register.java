package com.example.exchange.register;

import com.example.exchange.Authentication;
import com.example.exchange.OnPageCompleteListener;
import com.example.exchange.PageCompleter;
import com.example.exchange.api.ExchangeService;
import com.example.exchange.api.model.Token;
import com.example.exchange.api.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.logging.Logger;

public class Register implements PageCompleter {
    private OnPageCompleteListener onPageCompleteListener;
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public TextField emailTextField;
    //make logger
    public Logger logger = Logger.getLogger(Register.class.getName());
    public void register(ActionEvent actionEvent) {
        if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || emailTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Could not create user");
            alert.setHeaderText("Please fill in all fields");
            alert.setContentText("Username, password and email are required");
            // Set the alert dialog to be non-blocking and show it
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            alert.showAndWait();
            return;
        }
        if (!emailTextField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){ //reference: https://www.w3resource.com/javascript/form/email-validation.php
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Could not create user");
            alert.setHeaderText("Please enter a valid email");
            alert.setContentText("Email must be in the form of word@mail.com");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            alert.showAndWait();
            return;
        }
        User user = new User(usernameTextField.getText(),
                passwordTextField.getText(), emailTextField.getText());
        ExchangeService.exchangeApi().addUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User>
                        response) {
                    logger.info(response.message());
                    if (response.code() == 409) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Could not create user");
                            alert.setHeaderText("Do you already have an account?");
                            alert.setContentText("Username or email already exists");
                            // Set the alert dialog to be non-blocking and show it
                            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage.setAlwaysOnTop(true);
                            alert.showAndWait();
                            return;
                        });

                    }else if(response.code()==400){
                        Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Could not create user");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Oops! Something went wrong. Please try again!");
                                    // Set the alert dialog to be non-blocking and show it
                                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                    stage.setAlwaysOnTop(true);
                                    alert.showAndWait();
                                    return;
                        });
                    }else {
                        ExchangeService.exchangeApi().authenticate(user).enqueue(new Callback<Token>() {
                            @Override
                            public void onResponse(Call<Token> call,
                                                   Response<Token> response) {


                                Authentication.getInstance().saveToken(response.body().getToken());
                                Platform.runLater(() -> {
                                    onPageCompleteListener.onPageCompleted();
                                });
                            }

                            @Override
                            public void onFailure(Call<Token> call, Throwable
                                    throwable) {
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                }
            });

    }
    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }
}