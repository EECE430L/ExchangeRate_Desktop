package com.example.exchange.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ExchangeService {
    static String API_URL = "https://exchangeapp.azurewebsites.net";
    public static Exchange exchangeApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Exchange.class);
    }
}