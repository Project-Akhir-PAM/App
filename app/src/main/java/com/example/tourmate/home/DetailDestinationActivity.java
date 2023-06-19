package com.example.tourmate.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tourmate.R;
import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.databinding.ActivityDetailDestinationBinding;
import com.example.tourmate.model.Destination;
import com.example.tourmate.navbar.NavbarActivity;
import com.example.tourmate.response.CUDDestinationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDestinationActivity extends AppCompatActivity {

    ActivityDetailDestinationBinding binding;
    Destination destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Back");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        destination = getIntent().getParcelableExtra("get_destination");
        if (destination != null) {
            binding.tvDesTitle.setText(destination.getName());
            binding.tvDesLoc.setText(destination.getLocation());
            binding.tvDesDesc.setText(destination.getDescription());
            Glide.with(this)
                    .load(destination.getImage())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            binding.layoutImg.setBackground(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            Log.d("Error load image", errorDrawable.toString());
                        }
                    });
        }


        binding.btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(DetailDestinationActivity.this, UpdateDestinationActivity.class);

            i.putExtra("get_destination", destination);
            startActivity(i);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_delete) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Apakah anda yakin ingin menghapus destinasi ?");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Yes",
                    (dialog, id) -> {
                        Call<CUDDestinationResponse> client = ApiConfig.getApiService().deleteDestination(destination.getId());
                        client.enqueue(new Callback<CUDDestinationResponse>() {
                            @Override
                            public void onResponse(Call<CUDDestinationResponse> call, Response<CUDDestinationResponse> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(DetailDestinationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(DetailDestinationActivity.this, NavbarActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<CUDDestinationResponse> call, Throwable t) {
                                Log.e("Error Retrofit", "onFailure: " + t.getMessage());
                            }
                        });
            });

            builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());

            AlertDialog alert11 = builder1.create();
            alert11.show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}