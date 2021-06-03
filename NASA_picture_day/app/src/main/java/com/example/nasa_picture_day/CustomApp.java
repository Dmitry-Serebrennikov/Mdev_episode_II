package com.example.nasa_picture_day;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.nasa_picture_day.data.ItemDb;

import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomApp extends Application {
    public static final String KEY = "rpqv0M3F46rbR5eRdawOG70cWOXxR62aQH6YbPGa";
    private static NasaAPI nasaAPI;
    private Retrofit retrofit;
    private static ItemDb db;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit =new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nasaAPI = retrofit.create(NasaAPI.class);

        db = Room
                .databaseBuilder(getApplicationContext(), ItemDb.class, "db")
                .build();
    }

    public static NasaAPI getNasaAPI() {
        return nasaAPI;
    }
    public static ItemDb getDB() {
        return db;
    }


}
