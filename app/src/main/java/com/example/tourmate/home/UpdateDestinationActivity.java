package com.example.tourmate.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.databinding.ActivityDetailDestinationBinding;
import com.example.tourmate.databinding.ActivityUpdateDestinationBinding;
import com.example.tourmate.helper.FileUtils;
import com.example.tourmate.model.Destination;
import com.example.tourmate.navbar.NavbarActivity;
import com.example.tourmate.response.CUDDestinationResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDestinationActivity extends AppCompatActivity {

    private ActivityUpdateDestinationBinding binding;
    private Destination destination;
    private String[] items = {"", "Nature", "Museum", "Amusement Park", "Park"};
    private String selectedImage;
    private int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 9001;
    private MultipartBody.Part filePart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Back");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayAdapter<String> adapterSp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
        binding.spEdtCategory.setAdapter(adapterSp);

        destination = getIntent().getParcelableExtra("get_destination");
        if (destination != null){
            binding.etEdtName.setText(destination.getName());
            binding.etEdtLoc.setText(destination.getLocation());
            binding.etEdtDesc.setText(destination.getDescription());
            Glide.with(UpdateDestinationActivity.this).load(destination.getImage()).into(binding.ivImage);

            int cat_id = destination.getCategoryId();
            switch (cat_id){
                case 1:
                    binding.spEdtCategory.setSelection(1);
                    break;
                case 2:
                    binding.spEdtCategory.setSelection(2);
                    break;
                case 3:
                    binding.spEdtCategory.setSelection(3);
                    break;
                case 4:
                    binding.spEdtCategory.setSelection(4);
                    break;
            }



        }

        binding.ibAddImage.setOnClickListener(v -> {
            // Memeriksa izin
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                // Meminta izin jika belum diberikan
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
            } else {
                // Izin sudah diberikan, jalankan galeri
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

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

            if (name.isEmpty() || loc.isEmpty() || desc.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua form", Toast.LENGTH_SHORT).show();
            } else {
                updateData(name, loc, desc, category_id);
            }
        });

    }

    private void updateData(String name, String loc, String desc, int category_id) {
        // Cek apakah selectedImage tidak kosong atau null
        if (!TextUtils.isEmpty(selectedImage)) {
            File file = new File(Uri.parse(selectedImage).getPath());
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            this.filePart = MultipartBody.Part.createFormData("img", file.getName(), imageRequestBody);
        } else {
            // Jika selectedImage kosong, tetapkan this.filePart menjadi null
            this.filePart = null;
        }

        RequestBody nameRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody locRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), loc);
        RequestBody descRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), desc);
        RequestBody catIdRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(category_id));

        Call<CUDDestinationResponse> client = ApiConfig.getApiService().updateDestination(destination.getId(), "patch", nameRequestBody, this.filePart, locRequestBody, descRequestBody, catIdRequestBody);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && requestCode == 1 && data != null) {
                Uri image = data.getData();
                selectedImage = FileUtils.getPath(getApplicationContext(), image);
                Glide.with(this).load(image).into(binding.ivImage);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, jalankan galeri
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 1);
            } else {
                // Izin ditolak, muncul Toast
                Toast.makeText(this, "Izin akses penyimpanan ditolak.", Toast.LENGTH_SHORT).show();
            }
        }
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