package com.example.image_search_feat_retrofit2.adapters;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.image_search_feat_retrofit2.Hit;
import com.example.image_search_feat_retrofit2.R;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private final LayoutInflater lInflater;
    List<Hit> hits;
    List<Drawable> drawables;

    public ImageAdapter(ArrayList<Drawable> drawables, List<Hit> hits, Context context) {
        this.hits = hits;
        this.drawables = drawables;
        Context ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return hits.size();
    }

    @Override
    public Object getItem(int position) {
        return hits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Hit hit = (Hit) getItem(position);

        ((TextView) view.findViewById(R.id.item_id)).setText(hit.getTags());
        View finalView = view;
        ((ImageView) finalView.findViewById(R.id.item_image)).setImageDrawable(drawables.get(position));
        return view;
    }


}
