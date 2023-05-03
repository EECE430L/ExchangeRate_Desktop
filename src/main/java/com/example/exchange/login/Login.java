package com.example.exchange.login;

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

public class Login  implements PageCompleter {
    private OnPageCompleteListener onPageCompleteListener;
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public Logger logger = Logger.getLogger(Login.class.getName());
    public void login(ActionEvent actionEvent) {
        User user = new User(usernameTextField.getText(),
                passwordTextField.getText());
        ExchangeService.exchangeApi().authenticate(user).enqueue(new Callback<Token>() {
             @Override
             public void onResponse(Call<Token> call, Response<Token>
                     response) {
                 logger.info(response.message());
                try {
                    Authentication.getInstance().saveToken(response.body().getToken());
                    Platform.runLater(() -> {
                        onPageCompleteListener.onPageCompleted();
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Failed!");
                        alert.setHeaderText(null);
                        alert.setContentText("Username or password is incorrect!");
                        // Set the alert dialog to be non-blocking and show it
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.setAlwaysOnTop(true);
                        alert.showAndWait();
                        usernameTextField.setText("");
                        passwordTextField.setText("");
                        return;
                    });
                }

             }
             @Override
             public void onFailure(Call<Token> call, Throwable throwable) {
             }
         });
    }
    public void setOnPageCompleteListener(OnPageCompleteListener
                                                  onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }
}
