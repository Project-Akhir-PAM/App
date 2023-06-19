package com.example.tourmate.home;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourmate.R;
import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.databinding.FragmentHomeBinding;
import com.example.tourmate.home.DestinationAdapter;
import com.example.tourmate.model.Destination;
import com.example.tourmate.response.DestinationResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements TextWatcher {

    private FragmentHomeBinding binding;
    private DestinationAdapter destinationAdapter;
    private List<Destination> destinationList;
    private View view;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        this.destinationList = new ArrayList<>();

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.action_bar_custom);
            TextView tvTitle = getActivity().findViewById(R.id.tvTitle);
            tvTitle.setText("Home");
        }

        getAllData();

        this.destinationAdapter = new DestinationAdapter(view.getContext(), destinationList);
        binding.rvDestination.setAdapter(this.destinationAdapter);
        binding.rvDestination.setLayoutManager(new LinearLayoutManager(view.getContext()));

        binding.etSearch.addTextChangedListener(this);

        return this.view;
    }

    private void getAllData() {
        Call<DestinationResponse> client = ApiConfig.getApiService().getAllDestination();
        client.enqueue(new Callback<DestinationResponse>() {
            @Override
            public void onResponse(Call<DestinationResponse> call, Response<DestinationResponse> response) {
                destinationList.clear();
                if (response.isSuccessful()) {
                    destinationList = response.body().getData();

                    destinationAdapter = new DestinationAdapter(view.getContext(), destinationList);
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
                    destinationAdapter = new DestinationAdapter(view.getContext(), destinationList);
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