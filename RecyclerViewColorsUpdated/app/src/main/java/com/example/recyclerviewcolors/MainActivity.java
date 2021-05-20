package com.example.recyclerviewcolors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String CUSTOM_PREFIX = "custom_";
    RecyclerView rview;
    // TODO: создать массив с цветами (которые заданы в ресурсах)
    List<String> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colors  = getListOfColorNames();

        rview = findViewById(R.id.rview);
        ColorAdapter adapter = new ColorAdapter(getLayoutInflater());
        adapter.submitList(colors);
        rview.setLayoutManager(new LinearLayoutManager(this));
        // задаём оформление
        rview.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rview.setAdapter(adapter);

    }

    public List<String> getListOfColorNames() {
        List<String> lst = new ArrayList();
        try {
            Field[] fields = Class.forName(getPackageName()+".R$color").getDeclaredFields();
            for(Field field : fields) {
                if(field.getName().startsWith(CUSTOM_PREFIX))
                    lst.add(field.getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lst;
    }
}