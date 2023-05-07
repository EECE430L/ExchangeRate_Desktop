package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;
import javafx.scene.control.Button;

public class OfferResponse {
    @SerializedName("receiver")
    public String receiver;
    @SerializedName("usd_to_lbp")
    public boolean usd_to_lbp;
    @SerializedName("offered_amount")
    public String offered_amount;
    @SerializedName("amount_requested")
    public String amount_requested;

    @SerializedName("requested_amount")
    public String requested_amount;

    @SerializedName("offerer")
    public String offerer;

    @SerializedName("id")
    public Integer id;

    public String type;

    public String getType(){
        if (usd_to_lbp){
            return "USD to LBP";
        }else{
            return "LBP to USD";
        }
    }

    public String getRequested_amount() {
        return requested_amount;
    }
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isUsd_to_lbp() {
        return usd_to_lbp;
    }

    public void setUsd_to_lbp(boolean usd_to_lbp) {
        this.usd_to_lbp = usd_to_lbp;
    }

    public String getOffered_amount() {
        return offered_amount;
    }

    public void setOffered_amount(String offered_amount) {
        this.offered_amount = offered_amount;
    }

    public String getAmount_requested() {
        return amount_requested;
    }

    public void setAmount_requested(String amount_requested) {
        this.amount_requested = amount_requested;
    }

    public String getOfferer() {
        return offerer;
    }

    public void setOfferer(String offerer) {
        this.offerer = offerer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Button getAccept() {
        Button button = new Button("Accept");

        button.setOnAction(event -> {
            // Call your function here
        });

        return button;
    }
}
