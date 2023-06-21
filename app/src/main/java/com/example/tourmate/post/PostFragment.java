package com.example.tourmate.post;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourmate.R;
import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.databinding.FragmentPostBinding;
import com.example.tourmate.helper.FileUtils;
import com.example.tourmate.response.CUDDestinationResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private String[] items = {"", "Nature", "Museum", "Amusement Park", "Park"};
    private View view;
    private String selectedImage;
    private int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 9001;
    private MultipartBody.Part filePart;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.action_bar_custom);
            TextView tvTitle = getActivity().findViewById(R.id.tvTitle);
            tvTitle.setText("Post");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
        binding.spCategory.setAdapter(adapter);

        binding.ibAddImage.setOnClickListener(v -> {
            // Memeriksa izin
            if (ContextCompat.checkSelfPermission(view.getContext(),
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                // Meminta izin jika belum diberikan
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
            } else {
                // Izin sudah diberikan, jalankan galeri
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        binding.btnSubmit.setOnClickListener(v -> {
            int category_id = 1;
            String name = binding.etPostName.getText().toString();
            String loc = binding.etPostLoc.getText().toString();
            String desc = binding.etPostCategory.getText().toString();
            String category = binding.spCategory.getSelectedItem().toString();
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
                Toast.makeText(view.getContext(), "Harap lengkapi semua form", Toast.LENGTH_SHORT).show();
            } else {
                createData(name, loc, desc, category_id);
            }
        });

        return this.view;
    }

    private void createData(String nama, String loc, String desc, int category) {
        // Cek apakah selectedImage tidak kosong atau null
        if (!TextUtils.isEmpty(selectedImage)) {
            File file = new File(Uri.parse(selectedImage).getPath());
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            this.filePart = MultipartBody.Part.createFormData("img", file.getName(), imageRequestBody);
        } else {
            Toast.makeText(view.getContext(), "Harap pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody nameRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), nama);
        RequestBody locRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), loc);
        RequestBody descRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), desc);
        RequestBody catIdRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(category));

        Call<CUDDestinationResponse> client = ApiConfig.getApiService().createDestination(nameRequestBody, this.filePart, locRequestBody, descRequestBody, catIdRequestBody);
        client.enqueue(new Callback<CUDDestinationResponse>() {
            @Override
            public void onResponse(Call<CUDDestinationResponse> call, Response<CUDDestinationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
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
                selectedImage = FileUtils.getPath(PostFragment.this.getContext(), image);
                Glide.with(PostFragment.this).load(image).into(binding.ivImage);
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
                Toast.makeText(getActivity(), "Izin akses penyimpanan ditolak.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}