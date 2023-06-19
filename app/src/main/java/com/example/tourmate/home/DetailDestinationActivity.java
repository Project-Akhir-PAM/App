package com.example.tourmate.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.tourmate.databinding.ActivityDetailDestinationBinding;
import com.example.tourmate.model.Destination;

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
            Toast.makeText(this, "DELETE", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}