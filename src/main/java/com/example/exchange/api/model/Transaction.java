package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;
public class Transaction {
    @SerializedName("usd_amount")
    Float usdAmount;
    @SerializedName("lbp_amount")
    Float lbpAmount;
    @SerializedName("usd_to_lbp")
    Boolean usdToLbp;
    @SerializedName("added_date")
    String addedDate;
    @SerializedName("second_party")
    String secondParty;

    @SerializedName("user_id")
    Integer id;

    String transactionType;


    public Transaction(Float usdAmount, Float lbpAmount, Boolean usdToLbp)
    {
        this.usdAmount = usdAmount;
        this.lbpAmount = lbpAmount;
        this.usdToLbp = usdToLbp;
    }

    public Float getUsdAmount() {
        return usdAmount;
    }

    public Float getLbpAmount() {
        return lbpAmount;
    }

    public Boolean getUsdToLbp() {
        return usdToLbp;
    }

    public Integer getId() {
        return id;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getSecondParty() {
        return secondParty;
    }

    public String getTransactionType() {
        if (usdToLbp){
            return "USD to LBP";
        }
        else{
            return "LBP to USD";
        }
    }
}
