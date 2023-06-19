package com.example.tourmate.navbar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tourmate.R;
import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.databinding.FragmentCreateUpdateBinding;
import com.example.tourmate.response.CUDDestinationResponse;
import com.example.tourmate.response.DestinationResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUpdateFragment extends Fragment {

    private FragmentCreateUpdateBinding binding;
    private String[] items = {"", "Nature", "Museum", "Amusement Park", "Park"};
    private View view;
//    private File imageFile;

    public CreateUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateUpdateBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.action_bar_custom);
            TextView tvTitle = getActivity().findViewById(R.id.tvTitle);
            tvTitle.setText("Post");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
        binding.spCategory.setAdapter(adapter);

//        binding.ibAddImage.setOnClickListener(v -> {
//            if (ActivityCompat.checkSelfPermission(view.getContext(),
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 10);
//            } else {
//                ActivityCompat.requestPermissions((Activity) view.getContext(),
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//            }
//        });

        binding.imgLayout.setBackgroundResource(R.drawable.img_bromo);


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

            if (name.isEmpty() || loc.isEmpty() || desc.isEmpty()) {
                Toast.makeText(view.getContext(), "Harap lengkapi semua form", Toast.LENGTH_SHORT).show();
            } else {
                createData(name, loc, desc, category_id);
            }
        });

        return this.view;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = ((AppCompatActivity)getActivity()).getContentResolver().openInputStream(imageUri);
//
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                int targetWidth = 1024;
//                int targetHeight = 768;
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImage, targetWidth, targetHeight, true);
//
//                imageFile = createTempImageFile(imageStream);
//                System.out.println(imageFile);
//
//                Glide.with(this)
//                        .load(scaledBitmap)
//                        .into(new SimpleTarget<Drawable>() {
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                binding.imgLayout.setBackground(resource);
//                            }
//
//                            @Override
//                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                                Log.d("Error load image", errorDrawable.toString());
//                            }
//                        });
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
//            }
//
//        } else {
//            Toast.makeText(view.getContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
//        }
//    }

    private void createData(String nama, String loc, String desc, int category) {
//        File file = new File(imageFile.toURI());
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        RequestBody nameRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), nama);
        RequestBody locRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), loc);
        RequestBody descRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), desc);
        RequestBody catIdRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(category));

        Call<CUDDestinationResponse> client = ApiConfig.getApiService().createDestination(nameRequestBody, locRequestBody, descRequestBody, catIdRequestBody);
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

    private File createTempImageFile(InputStream imageStream) {
        try {
            File tempDir = view.getContext().getCacheDir();
            File tempFile = File.createTempFile("temp_image", ".jpg", tempDir);

            OutputStream os = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = imageStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
            os.close();
            imageStream.close();

            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}