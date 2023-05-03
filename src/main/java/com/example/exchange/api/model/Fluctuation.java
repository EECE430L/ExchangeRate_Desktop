package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Fluctuation {
    @SerializedName("lbpToUsdRate")
    public Float lbpToUsdRate;

    @SerializedName("usdToLbpRate")
    public Float usdToLbpRate;
    @SerializedName("Date")
    public String Date;



    public String getDate() {
        return Date;
    }

    public Float getLbpToUsdRate() {
        return lbpToUsdRate;
    }

    public Float getUsdToLbpRate() {
        return usdToLbpRate;
    }
}
