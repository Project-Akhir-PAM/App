package com.example.tourmate.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tourmate.R;
import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.api.ApiService;
import com.example.tourmate.databinding.ActivityDetailDestinationBinding;
import com.example.tourmate.model.Destination;
import com.example.tourmate.navbar.NavbarActivity;
import com.example.tourmate.response.CUDDestinationResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

            binding.ibDownload.setOnClickListener(v -> {
                downloadImage();
            });

        }

        binding.btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(DetailDestinationActivity.this, UpdateDestinationActivity.class);

            i.putExtra("get_destination", destination);
            startActivity(i);
        });

    }

    private void downloadImage() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourmate.ruangbaca-fisipedu.my.id/")
                .client(client)
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = apiService.downloadImage(destination.getImage());
        Log.d("TES", "downloadImage: "+destination.getImage());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                    Toast.makeText(DetailDestinationActivity.this, "download image sukses : "+writtenToDisk, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error Retrofit", "onFailure: " + t.getMessage());
            }
        });

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            String filename = getFilenameFromUrl(destination.getImage());
            if (filename == null) {
                filename = "image.png";
            }

            File imgDownload = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(imgDownload);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("Image Download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private String getFilenameFromUrl(String url) {
        String filename = null;
        try {
            URL parsedUrl = new URL(url);
            String path = parsedUrl.getPath();
            filename = path.substring(path.lastIndexOf('/') + 1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return filename;
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