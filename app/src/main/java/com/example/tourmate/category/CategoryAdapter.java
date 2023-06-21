package com.example.tourmate.category;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tourmate.databinding.ItemDestinationBinding;
import com.example.tourmate.home.DetailDestinationActivity;
import com.example.tourmate.model.Destination;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.DestinationViewHolder> implements Filterable {
    private List<Destination> dataItemList = new ArrayList<>();
    CustomFilter filter;

    public CategoryAdapter() {
        super();
    }

    public void setDestinationList(List<Destination> dataList) {
        if (dataList.size() != 0) {
            dataItemList.clear();
        }
        dataItemList.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDestinationBinding binding = ItemDestinationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DestinationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        holder.bind(dataItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String charString = constraint.toString().toLowerCase(Locale.ROOT).trim();
            FilterResults filterResults = new FilterResults();

            if (charString.length() == 0) {
                filterResults.values = dataItemList;
            } else {
                ArrayList<Destination> filters = new ArrayList<>();
                for (int i = 0; i < dataItemList.size(); i++) {
                    if (dataItemList.get(i).getName().toLowerCase().contains(charString)) {
                        filters.add(dataItemList.get(i));
                    }
                }
                filterResults.values = filters;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            dataItemList = (ArrayList<Destination>) results.values;
            notifyDataSetChanged();
        }
    }

    public class DestinationViewHolder extends RecyclerView.ViewHolder {
        final ItemDestinationBinding binding;

        public DestinationViewHolder(ItemDestinationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Destination dataItem) {
            binding.tvItemTitle.setText(dataItem.getName());
            binding.tvItemDesc.setText(dataItem.getDescription());
            Glide.with(itemView.getContext())
                    .load(dataItem.getImage())
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

            binding.itemLayout.setOnClickListener(v -> {
//                Toast.makeText(itemView.getContext(), "TES : " + dataItem.getName(), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(itemView.getContext(), DetailDestinationActivity.class);
                i.putExtra("get_destination", dataItem);
                itemView.getContext().startActivity(i);

            });
        }
    }
}
