package com.example.tourmate.api;

import android.graphics.Bitmap;

import com.example.tourmate.response.DestinationResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @GET("destination")
    Call<DestinationResponse> getAllDestination();

    @GET("destination")
    Call<DestinationResponse> searchDestination(@Query("search") String search);


    @Multipart
    @POST("destination/create")
    Call<DestinationResponse> createDestination(
            @Part("name") RequestBody name,
//            @Part MultipartBody.Part image,
            @Part("location") RequestBody location,
            @Part("description") RequestBody description,
            @Part("category_id") RequestBody cat_id
    );


}
