package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class OfferResponse {
    @SerializedName("receiver")
    public String receiver;
    @SerializedName("usd_to_lbp")
    public boolean usd_to_lbp;
    @SerializedName("offered_amount")
    public String offered_amount;
    @SerializedName("amount_requested")
    public String amount_requested;

    @SerializedName("offerer")
    public String offerer;

    @SerializedName("id")
    public Integer id;
}
