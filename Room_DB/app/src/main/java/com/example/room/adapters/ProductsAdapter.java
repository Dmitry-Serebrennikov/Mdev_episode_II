package com.example.room.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.room.R;
import com.example.room.models.Product;

import java.util.List;

public class ProductsAdapter extends BaseAdapter {
    Context context;
    List<Product> products;

    public ProductsAdapter(Context context, List<Product> products){
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Product product = products.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);

        TextView name = convertView.findViewById(R.id.name);
        TextView author = convertView.findViewById(R.id.author);
        TextView price = convertView.findViewById(R.id.price);

        name.setText(product.getName());
        author.setText(product.getAuthor());
        price.setText(String.valueOf(product.getPrice()));

        return convertView;
    }
}
