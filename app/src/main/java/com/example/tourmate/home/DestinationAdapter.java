package com.example.tourmate.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.databinding.ItemDestinationBinding;
import com.example.tourmate.model.Destination;

import java.util.ArrayList;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private final Context context;
    private final List<Destination> destinationList;

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

            binding.itemLayout.setOnClickListener(v -> {
                Toast.makeText(context, "TES : "+destination.getName(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
