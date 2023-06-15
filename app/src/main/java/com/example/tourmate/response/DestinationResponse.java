package com.example.tourmate.response;

import com.example.tourmate.model.Destination;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DestinationResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Destination> data;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Destination> getData() {
        return data;
    }
}
