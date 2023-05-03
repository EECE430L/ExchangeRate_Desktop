package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class StatisticRequest {
    @SerializedName("start_month")
    public String StartMonth;
    @SerializedName("end_month")
    public String EndMonth;
    @SerializedName("start_year")
    public String StartYear;
    @SerializedName("end_year")
    public String EndYear;
    @SerializedName("start_day")
    public String StartDay;
    @SerializedName("end_day")
    public String EndDay;
}
