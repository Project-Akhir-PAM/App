package com.example.tourmate.api;

import android.graphics.Bitmap;

import com.example.tourmate.response.CUDDestinationResponse;
import com.example.tourmate.response.DestinationResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("destination")
    Call<DestinationResponse> getAllDestination();

    @GET("destination")
    Call<DestinationResponse> searchDestination(@Query("search") String search);


    @Multipart
    @POST("destination/create")
    Call<CUDDestinationResponse> createDestination(
            @Part("name") RequestBody name,
//            @Part MultipartBody.Part image,
            @Part("location") RequestBody location,
            @Part("description") RequestBody description,
            @Part("category_id") RequestBody cat_id
    );

    @DELETE("destination/delete/{id}")
    Call<CUDDestinationResponse> deleteDestination(@Path("id") int id);

    @FormUrlEncoded
    @PATCH("destination/update/{id}")
    Call<CUDDestinationResponse> updateDestination(
            @Path("id") int id,
            @Field("name") String name,
            @Field("location") String location,
            @Field("description") String description,
            @Field("category_id") int categoryId
    );

}
