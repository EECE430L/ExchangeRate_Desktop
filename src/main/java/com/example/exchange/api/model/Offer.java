package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Offer {
    @SerializedName("receiver")
    public String receiver;
    @SerializedName("usd_to_lbp")
    public boolean usd_to_lbp;
    @SerializedName("amount_offered")
    public Float amount_offered;
    @SerializedName("amount_requested")
    public Float amount_requested;

    public Offer(String receiver, boolean usd_to_lbp, Float amount_offered, Float amount_requested) {
        this.receiver = receiver;
        this.usd_to_lbp = usd_to_lbp;
        this.amount_offered = amount_offered;
        this.amount_requested = amount_requested;
    }
}
