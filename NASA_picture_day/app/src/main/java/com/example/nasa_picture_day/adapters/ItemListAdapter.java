package com.example.nasa_picture_day.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nasa_picture_day.R;
import com.example.nasa_picture_day.data.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends androidx.recyclerview.widget.ListAdapter<Item, ItemListAdapter.ViewHolder>{


    public ItemListAdapter() {
        super(new ItemDiffUtil());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //?
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getCurrentList().get(position));
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final ImageView image;
        private final TextView date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(Item item) {
            title.setText(item.getTitle());
            date.setText(item.getDate());
            image.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
        }
    }

    public static final class ItemDiffUtil extends DiffUtil.ItemCallback<Item>{

        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId() == newItem.getId()
                    && oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getDate().equals(newItem.getDate())
                    ;
        }
    }
}
