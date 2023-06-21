package com.example.tourmate.category;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.tourmate.R;
import com.example.tourmate.api.ApiConfig;
import com.example.tourmate.databinding.FragmentSearchBinding;
import com.example.tourmate.model.Destination;
import com.example.tourmate.response.ListCategoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchFragment extends Fragment {

    private CategoryListAdapter categoryListAdapter;
    private List<Destination> dataItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSearchBinding binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentSearchBinding binding = FragmentSearchBinding.bind(view);
        this.dataItemList = new ArrayList<>();

        categoryListAdapter = new CategoryListAdapter();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.rvListCategory.setLayoutManager(staggeredGridLayoutManager);
        binding.rvListCategory.setAdapter(this.categoryListAdapter);

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions( ActionBar.DISPLAY_SHOW_CUSTOM);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView( R.layout.action_bar_custom);
            TextView tvTitle = getActivity().findViewById(R.id.tvTitle);
            tvTitle.setText("Search");
        }
        binding.etSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    // Show all categories
                    categoryListAdapter.setCategoryList(dataItemList);
                } else {
                    // Filter the dataItemList based on the search query
                    categoryListAdapter.filter(query);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );

        getListCategory();

    }

    private void getListCategory() {
        Call<ListCategoryResponse> client = ApiConfig.getApiService().getAllCategory();
        client.enqueue(new Callback<ListCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<ListCategoryResponse> call, @NonNull Response<ListCategoryResponse> response) {
                if (response.isSuccessful()) {
                    ListCategoryResponse dataResponse = response.body();
                    if (dataResponse != null) {
                        dataItemList.addAll(dataResponse.getData());

                        // Update the data in the adapter and notify the changes
                        categoryListAdapter.setCategoryList(dataItemList);
                    } else {
                        Log.e("dataItemList", "data is null: " + response.message());
                    }
                } else {
                    Log.e("Error", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListCategoryResponse> call, @NonNull Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }

}
