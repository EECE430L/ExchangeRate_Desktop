package com.example.exchange.api;

import com.example.exchange.api.model.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface Exchange {
    @POST("/user/")
    Call<User> addUser(@Body User user);
    @POST("/authentication/")
    Call<Token> authenticate(@Body User user);
    @GET("/exchangeRate")
    Call<ExchangeRates> getExchangeRates();
    @POST("/transaction/")
    Call<Object> addTransaction(@Body Transaction transaction,
                                @Header("Authorization") String authorization);
    @GET("/transaction")
    Call<List<Transaction>> getTransactions(@Header("Authorization")
                                            String authorization);

    @GET("/fluctuations")
    Call<List<Fluctuation>> getLbpFluctuations(@Query("startDay") String startDay,
                                               @Query("endDay") String endDay,
                                               @Query("startMonth") String startMonth,
                                               @Query("endMonth") String endMonth,
                                               @Query("startYear") String startYear,
                                               @Query("endYear") String endYear,
                                               @Header("Authorization") String authorization);
    @GET("statistics/rates-percent-change")
    Call<PercentageChange> getRatesPercentChange(@Query("startDay") String startDay,
                                                @Query("endDay") String endDay,
                                                @Query("startMonth") String startMonth,
                                                @Query("endMonth") String endMonth,
                                                @Query("startYear") String startYear,
                                                @Query("endYear") String endYear,
                                                @Header("Authorization") String authorization);

    @GET("statistics/number-transactions")
    Call<NumTransactions> getNumTransactions(@Query("startDay") String startDay,
                                                   @Query("endDay") String endDay,
                                                   @Query("startMonth") String startMonth,
                                                   @Query("endMonth") String endMonth,
                                                   @Query("startYear") String startYear,
                                                   @Query("endYear") String endYear,
                                                   @Header("Authorization") String authorization);

    @POST("/offer")
    Call<OfferResponse> addOffer(@Body Offer offer,
                          @Header("Authorization") String authorization);

}