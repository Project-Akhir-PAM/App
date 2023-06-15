package com.example.tourmate.api;

import com.example.tourmate.response.DestinationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("destination")
    Call<DestinationResponse> getAllDestination();

    @GET("destination")
    Call<DestinationResponse> searchDestination(@Query("search") String search);
}
