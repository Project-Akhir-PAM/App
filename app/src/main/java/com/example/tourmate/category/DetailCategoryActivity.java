package com.example.tourmate.category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.api.ApiService;
import com.example.tourmate.databinding.ActivityDetailCategoryBinding;


import com.example.tourmate.model.Destination;
import com.example.tourmate.response.DetailCategoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCategoryActivity extends AppCompatActivity implements TextWatcher {

    private CategoryAdapter categoryAdapter;
    private List<Destination> dataItemList;
    private ActivityDetailCategoryBinding binding;

    private int typeCategory = 0;
    private String categoryName = "Category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.dataItemList = new ArrayList<>();
        binding.etSearch.addTextChangedListener(this);

        this.categoryAdapter = new CategoryAdapter();
        binding.rvDestination.setAdapter(this.categoryAdapter);
        binding.rvDestination.setLayoutManager(new LinearLayoutManager(this));

        typeCategory = getIntent().getIntExtra("category_type", 0);
        categoryName = getIntent().getStringExtra("category_name");

        binding.tvCategory.setText(categoryName);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Back");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getDetailCategory(); // Retrieve data from the API
    }

    private void getDetailCategory() {
        if (typeCategory != 0) {
            Call<DetailCategoryResponse> client = ApiConfig.getApiService().getDetailCategory(typeCategory);
            client.enqueue(new Callback<DetailCategoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<DetailCategoryResponse> call, @NonNull Response<DetailCategoryResponse> response) {
                    if (response.isSuccessful()) {
                        DetailCategoryResponse destinationResponse = response.body();
                        if (destinationResponse != null) {
                            dataItemList.addAll(destinationResponse.getData().getDestinations());

                            // Update the data in the adapter and notify the changes
                            categoryAdapter.setDestinationList(dataItemList);
                        } else {
                            Log.e("dataItemList", "data is null: " + response.message());
                        }
                    } else {
                        Log.e("Error", "onFailure: " + response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DetailCategoryResponse> call, @NonNull Throwable t) {
                    Log.e("Error", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void searchDetailCategory() {
        ApiService apiService = ApiConfig.getApiService();
        Call<DetailCategoryResponse> client = apiService.searchDetailCategory(binding.etSearch.getText().toString());
        client.enqueue(new Callback<DetailCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailCategoryResponse> call, @NonNull Response<DetailCategoryResponse> response) {
                dataItemList.clear();
                if (response.isSuccessful()) {
                    DetailCategoryResponse destinationResponse = response.body();
                    if (destinationResponse != null) {
                        dataItemList.addAll(destinationResponse.getData().getDestinations());

                        // Update the data in the adapter and notify the changes
                        categoryAdapter.setDestinationList(dataItemList);
                    } else {
                        Log.e("dataItemList", "data is null: " + response.message());
                    }
                } else {
                    Log.e("Error Retrofit", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailCategoryResponse> call, @NonNull Throwable t) {
                Log.e("Error Retrofit", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        searchDetailCategory();
    }
}
