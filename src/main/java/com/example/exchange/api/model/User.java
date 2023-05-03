package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    Integer id;
    @SerializedName("user_name")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("email")
    String email;
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email=email;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
