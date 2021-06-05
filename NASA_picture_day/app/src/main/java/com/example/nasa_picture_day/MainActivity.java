package com.example.nasa_picture_day;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

//import com.example.nasa_picture_day.adapters.ImageAdapter;

import com.example.nasa_picture_day.adapters.ItemListAdapter;
import com.example.nasa_picture_day.data.Item;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button search;
    DatePicker picker;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.imageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ItemListAdapter adapter = new ItemListAdapter();
        recyclerView.setAdapter(adapter);
        CustomApp.getDB().itemDao().getAll().observe(MainActivity.this, items -> adapter.submitList(items));

        picker = findViewById(R.id.date);
        search = findViewById(R.id.search);
        search.setOnClickListener(v -> {
            CustomApp.getNasaAPI().getDataForDate(CustomApp.KEY, getDate())
                    .enqueue(new Callback<Result>() {

                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            new downloadImageTask().execute(response.body());
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                        }
                    });

        });
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        return date;
        //бралась дата + время GMT + день недели, убрал в угоду минимализма// return picker.getYear() + "-" + (picker.getMonth() + 1 < 10 ? "0" : "") + (picker.getMonth() + 1)  + "-" + picker.getDayOfMonth();
    }

    private class downloadImageTask extends AsyncTask<Result, Void, Drawable> {
        List<Item> items;
        Result result;

        public downloadImageTask() {
        }

        protected Drawable doInBackground(Result... results) {
            this.result = results[0];
            Drawable drawable = null;
            try {
                drawable = WebHelper.loadImageFromWebOperations(result.getUrl());
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                Item item = new Item(getDate(), result.getTitle(),
                        result.getUrl(), stream.toByteArray());
                long insert = CustomApp.getDB().itemDao().insert(item);


            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
                e.printStackTrace();
            }
            return drawable;
        }

        protected void onPostExecute(Drawable drawable) {
            ItemListAdapter adapter = new ItemListAdapter();
            recyclerView.setAdapter(adapter);
            CustomApp.getDB().itemDao().getAll().observe(MainActivity.this, items -> adapter.submitList(items));

        }
    }
}