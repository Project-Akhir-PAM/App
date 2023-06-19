package com.example.tourmate.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tourmate.databinding.ItemDestinationBinding;
import com.example.tourmate.model.Destination;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private final Context context;
    private List<Destination> destinationList;

    public DestinationAdapter(Context context, List<Destination> destinationList) {
        this.context = context;
        this.destinationList = destinationList;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDestinationBinding binding = ItemDestinationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DestinationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        holder.bind(destinationList.get(position));
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    public class DestinationViewHolder extends RecyclerView.ViewHolder {
        final ItemDestinationBinding binding;
        public DestinationViewHolder(ItemDestinationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Destination destination) {
            binding.tvItemTitle.setText(destination.getName());
            binding.tvItemDesc.setText(destination.getDescription());
            Glide.with(context)
                    .load(destination.getImage())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            binding.itemLayout.setBackground(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            if (errorDrawable != null) {
                                binding.itemLayout.setBackground(errorDrawable);
                            } else {
                                Log.d("Error load image", "Drawable is null");
                            }
                        }

                    });

            binding.itemLayout.setOnClickListener(v -> {
                Intent i = new Intent(context, DetailDestinationActivity.class);

                i.putExtra("get_destination", destination);
                context.startActivity(i);
            });
        }
    }
}
