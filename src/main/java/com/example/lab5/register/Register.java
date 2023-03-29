package com.example.lab5.register;

import com.example.lab5.Authentication;
import com.example.lab5.OnPageCompleteListener;
import com.example.lab5.PageCompleter;
import com.example.lab5.api.ExchangeService;
import com.example.lab5.api.model.Token;
import com.example.lab5.api.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register implements PageCompleter {
    private OnPageCompleteListener onPageCompleteListener;
    public TextField usernameTextField;
    public TextField passwordTextField;
    public void register(ActionEvent actionEvent) {
        User user = new User(usernameTextField.getText(),
                passwordTextField.getText());
        ExchangeService.exchangeApi().addUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User>
                        response) {

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
                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                }
            });
    }
    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }
}