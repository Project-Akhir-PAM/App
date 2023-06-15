package com.example.tourmate.api;

import com.example.tourmate.response.DestinationResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("destination")
    Call<DestinationResponse> getAllDestination();
}
