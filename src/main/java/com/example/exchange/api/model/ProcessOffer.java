package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class ProcessOffer {
    @SerializedName("offer_id")
    public Integer offer_id;
    @SerializedName("accepted")
    public boolean accepted;

    public ProcessOffer(Integer offer_id, boolean accepted) {
        this.offer_id = offer_id;
        this.accepted = accepted;
    }
}
