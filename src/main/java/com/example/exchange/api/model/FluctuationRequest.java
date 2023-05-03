package com.example.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class FluctuationRequest {
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



    public String serializeQuery(){
        return "start_month=" + StartMonth + "&end_month=" + EndMonth + "&start_year=" + StartYear + "&end_year=" + EndYear + "&start_day=" + StartDay + "&end_day" + EndDay;
    }
}
