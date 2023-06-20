package com.example.tourmate.response;

import java.util.List;


import com.example.tourmate.model.Destination;
import com.google.gson.annotations.SerializedName;

public class DataCategory {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("destinations")
    private List<Destination> destinations;

    public List<Destination> getDestinations(){
        return destinations;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }
}