package com.example.tourmate.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tourmate.R;
import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.databinding.ActivityHomeBinding;
import com.example.tourmate.model.Destination;
import com.example.tourmate.response.DestinationResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements TextWatcher {

    private ActivityHomeBinding binding;
    private DestinationAdapter destinationAdapter;
    private List<Destination> destinationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.destinationList = new ArrayList<>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.action_bar_custom);
            TextView tvTitle = findViewById(R.id.tvTitle);
            tvTitle.setText("Home");
        }

        getAllData();

        this.destinationAdapter = new DestinationAdapter(this, destinationList);
        binding.rvDestination.setAdapter(this.destinationAdapter);
        binding.rvDestination.setLayoutManager(new LinearLayoutManager(this));

        binding.etSearch.addTextChangedListener(this);
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

    private void searchData() {
        Call<DestinationResponse> client = ApiConfig.getApiService().searchDestination(binding.etSearch.getText().toString());
        client.enqueue(new Callback<DestinationResponse>() {
            @Override
            public void onResponse(Call<DestinationResponse> call, Response<DestinationResponse> response) {
                destinationList.clear();
                if (response.isSuccessful()) {
                    destinationList = response.body().getData();
                    destinationAdapter = new DestinationAdapter(Home.this, destinationList);
                    binding.rvDestination.setAdapter(destinationAdapter);
                }
            }

            @Override
            public void onFailure(Call<DestinationResponse> call, Throwable t) {
                Log.e("Error Retrofit", "onFailure: "+ t.getMessage());
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        searchData();
    }
}