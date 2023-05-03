package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class NumTransactions {
    @SerializedName("num_lbp_to_usd_transactions")
    public Integer num_lbp_to_usd_transactions;

    @SerializedName("num_usd_to_lbp_transactions")
    public Integer num_usd_to_lbp_transactions;
}
