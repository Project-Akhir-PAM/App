package com.example.tourmate.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.databinding.ActivityHomeBinding;
import com.example.tourmate.model.Destination;
import com.example.tourmate.response.DestinationResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private DestinationAdapter destinationAdapter;
    private List<Destination> destinationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.destinationList = new ArrayList<>();

        getAllData();

        this.destinationAdapter = new DestinationAdapter(this, destinationList);
        binding.rvDestination.setAdapter(this.destinationAdapter);
        binding.rvDestination.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getAllData() {
        Call<DestinationResponse> client = ApiConfig.getApiService().getAllDestination();
        client.enqueue(new Callback<DestinationResponse>() {
            @Override
            public void onResponse(Call<DestinationResponse> call, Response<DestinationResponse> response) {
                destinationList.clear();
                if (response.isSuccessful()) {
                    destinationList = response.body().getData();

                    destinationAdapter = new DestinationAdapter(Home.this, destinationList);
                    binding.rvDestination.setAdapter(destinationAdapter);
                } else {
                    if (response.body() != null) {
                        Log.e("", "onFailure: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<DestinationResponse> call, Throwable t) {
                Log.e("Error Retrofit", "onFailure: "+ t.getMessage());
            }
        });
    }
}