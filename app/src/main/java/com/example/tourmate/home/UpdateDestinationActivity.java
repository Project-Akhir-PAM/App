package com.example.tourmate.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.tourmate.R;
import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.databinding.ActivityDetailDestinationBinding;
import com.example.tourmate.databinding.ActivityUpdateDestinationBinding;
import com.example.tourmate.model.Destination;
import com.example.tourmate.navbar.NavbarActivity;
import com.example.tourmate.response.CUDDestinationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDestinationActivity extends AppCompatActivity {

    ActivityUpdateDestinationBinding binding;
    Destination destination;
    private String[] items = {"", "Nature", "Museum", "Amusement Park", "Park"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Back");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
        binding.spEdtCategory.setAdapter(adapter);

        destination = getIntent().getParcelableExtra("get_destination");
        if (destination != null){
            binding.etEdtName.setText(destination.getName());
            binding.etEdtLoc.setText(destination.getLocation());
            binding.etEdtDesc.setText(destination.getDescription());

            binding.imgLayout.setBackgroundResource(R.drawable.img_bromo);
        }

        binding.btnUpdate.setOnClickListener(v -> {
            int category_id = 1;

            String name = binding.etEdtName.getText().toString();
            String loc = binding.etEdtLoc.getText().toString();
            String desc = binding.etEdtDesc.getText().toString();

            String category = binding.spEdtCategory.getSelectedItem().toString();
            switch (category){
                case "Nature":
                    category_id = 1;
                    break;
                case "Museum":
                    category_id = 2;
                    break;
                case "Amusement Park":
                    category_id = 3;
                    break;
                case "Park":
                    category_id = 4;
                    break;
            }

            if (name.isEmpty() || loc.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua form", Toast.LENGTH_SHORT).show();
            } else {
                Call<CUDDestinationResponse> client = ApiConfig.getApiService().updateDestination(destination.getId(), name, loc, desc, category_id);
                client.enqueue(new Callback<CUDDestinationResponse>() {
                    @Override
                    public void onResponse(Call<CUDDestinationResponse> call, Response<CUDDestinationResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UpdateDestinationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(UpdateDestinationActivity.this, NavbarActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<CUDDestinationResponse> call, Throwable t) {
                        Log.e("Error Retrofit", "onFailure: " + t.getMessage());
                    }
                });
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}