package com.example.recyclerviewcolors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ColorAdapter extends ListAdapter<String, ColorViewHolder> {
    LayoutInflater inflater;

    protected ColorAdapter(LayoutInflater inflater) {
        super(DIFF_CALLBACK); // задаём специальный объект-сравниватель
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // создать View с разметкой
        View item = inflater.inflate(R.layout.item, parent, false);
        return new ColorViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        // привязать данные
        holder.bindTo(getItem(position));
    }

    // объект сравнивает два элемента списка: буквально и по содержанию
    static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldColor, @NonNull String newColor) {
            return oldColor.equals(newColor);
        }
        @Override
        public boolean areContentsTheSame(@NonNull String oldColor, @NonNull String newColor) {
            return areItemsTheSame(oldColor, newColor);
        }
    };

}
