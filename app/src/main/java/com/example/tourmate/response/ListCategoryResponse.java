package com.example.tourmate.response;

import java.util.List;

import com.example.tourmate.model.Destination;
import com.google.gson.annotations.SerializedName;

public class ListCategoryResponse{

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private List<Destination> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public int getCode(){
        return code;
    }

    public List<Destination> getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public String getStatus(){
        return status;
    }
}