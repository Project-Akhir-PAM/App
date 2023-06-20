package com.example.tourmate.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailCategoryResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private DataCategory dataCategory;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public int getCode(){
        return code;
    }

    public DataCategory getData(){
        return dataCategory;
    }

    public String getMessage(){
        return message;
    }

    public String getStatus(){
        return status;
    }
}
