package com.example.productswithpictures.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productswithpictures.R;
import com.example.productswithpictures.data.Item;
import com.squareup.picasso.Picasso;

public class ItemListAdapter extends androidx.recyclerview.widget.ListAdapter<Item, ItemListAdapter.ViewHolder> {

    public ItemListAdapter() {
        super(new ItemDiffUtil());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getCurrentList().get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView name;
        private final TextView category;
        private final TextView vendorCode;
        private final TextView stockAvailability;
        private final TextView price;
        private final ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            vendorCode = itemView.findViewById(R.id.vendorCode);
            stockAvailability = itemView.findViewById(R.id.stockAvailability);
            price = itemView.findViewById(R.id.price);
            picture = itemView.findViewById(R.id.picture);
        }

        public void bind(Item item){
            name.setText(item.getTitle());
            category.setText(item.getCategory());
            vendorCode.setText("Vendor code: " + String.valueOf(item.getVendorCode()));
            stockAvailability.setText("Product availability: " + item.getStockAvailability());
            price.setText("Price: " + String.valueOf(item.getPrice() + " ₽"));

            picture.post(() ->
                    Picasso.get()
                            .load(item.getImageRef())
                            .resize(picture.getMeasuredWidth(), picture.getMeasuredHeight())
                            .centerCrop()
                            .error(R.drawable.no_image)
                            .into(picture));

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
                    && oldItem.getCategory().equals(newItem.getCategory())
                    && oldItem.getVendorCode() == newItem.getVendorCode()
                    && oldItem.getStockAvailability().equals(newItem.getStockAvailability())
                    && oldItem.getPrice() == newItem.getPrice()
                    && oldItem.getImageRef().equals(newItem.getImageRef());
        }
    }
}
