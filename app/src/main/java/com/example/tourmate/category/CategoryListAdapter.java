package com.example.tourmate.category;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tourmate.R;
import com.example.tourmate.databinding.ItemCategoryBinding;
import com.example.tourmate.model.Destination;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    private List<Destination> dataItemList = new ArrayList<>();

    public CategoryListAdapter() {
        super();
    }

    public void setCategoryList(List<Destination> dataList) {
        dataItemList.clear();
        dataItemList.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(dataItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    public void filter(String query) {
        List<Destination> filteredList = new ArrayList<>();
        for (Destination item : dataItemList) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        setCategoryList(filteredList);
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        final ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Destination dataItem) {
            binding.tvItemTitle.setText(dataItem.getName());
            if (dataItem.getId() == 1) {
//                binding.tvItemDesc.setText(itemView.getContext().getText(R.string.nature_places));

                Glide.with(itemView.getContext())
                        .load(ContextCompat.getDrawable(itemView.getContext(), R.drawable.img_nature))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                binding.itemLayout.setBackground(resource);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                Log.d("Error load image", errorDrawable.toString());
                            }
                        });
            } else if (dataItem.getId() == 2) {
                int sizeInDP = 48;

                int marginInDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, sizeInDP, itemView.getResources().getDisplayMetrics());

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, marginInDp, 0, 0);
                binding.container.setLayoutParams(params);

//                binding.tvItemDesc.setText(itemView.getContext().getText(R.string.museum_places));
                Glide.with(itemView.getContext())
                        .load(ContextCompat.getDrawable(itemView.getContext(), R.drawable.img_museum))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                binding.itemLayout.setBackground(resource);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                Log.d("Error load image", errorDrawable.toString());
                            }
                        });
            } else if (dataItem.getId() == 3) {
//                binding.tvItemDesc.setText(itemView.getContext().getText(R.string.amusement_park_places));
                Glide.with(itemView.getContext())
                        .load(ContextCompat.getDrawable(itemView.getContext(), R.drawable.img_amusement_park))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                binding.itemLayout.setBackground(resource);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                Log.d("Error load image", errorDrawable.toString());
                            }
                        });
            } else if (dataItem.getId() == 4) {
//                binding.tvItemDesc.setText(itemView.getContext().getText(R.string.park_places));
                Glide.with(itemView.getContext())
                        .load(ContextCompat.getDrawable(itemView.getContext(), R.drawable.img_park))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                binding.itemLayout.setBackground(resource);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                Log.d("Error load image", errorDrawable.toString());
                            }
                        });
            }

            binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetailCategoryActivity.class);
                    intent.putExtra("category_type", dataItem.getId());
                    intent.putExtra("category_name", dataItem.getName());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
