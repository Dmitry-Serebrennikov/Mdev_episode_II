package com.example.room.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.room.R;
import com.example.room.models.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    Context cntx;
    List<Category> categories;

    public CategoryAdapter(Context cntx, List<Category> categories) {
        this.cntx = cntx;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Category category = categories.get(position);
        convertView = LayoutInflater.from(cntx).inflate(R.layout.category_item, parent, false);

        TextView id = convertView.findViewById(R.id._id);
        TextView name = convertView.findViewById(R.id.name);
        id.setText(String.valueOf(category.getId()));
        name.setText(category.getName());

        return convertView;
    }
}
