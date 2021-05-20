package com.example.recyclerviewcolors;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ColorViewHolder extends RecyclerView.ViewHolder {
    TextView tv;
    Context context;
    public ColorViewHolder(@NonNull View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.color);
        context = itemView.getContext();
        // создать объекты интерфейса и
        // повешать обработчики событий

    }

    void bindTo(String color) {
        // заполнить объекты интерфейса значениями (данными)
        // TODO: задать цвет у текстового поля
        // TODO: написать название цвета в текстовом поле
        Resources res = context.getResources();
        String packageName = context.getPackageName();
        int colorId = res.getIdentifier(color, "color", packageName);
        tv.setBackgroundColor(res.getColor(colorId));
        tv.setText("color: " + color.replace(MainActivity.CUSTOM_PREFIX, ""));
    }

}
