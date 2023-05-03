package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class PercentageChange {
    @SerializedName("percent_change_LBP_to_USD")
    public Float percent_change_LBP_to_USD;

    @SerializedName("percent_change_USD_to_LBP")
    public Float percent_change_USD_to_LBP;
}
