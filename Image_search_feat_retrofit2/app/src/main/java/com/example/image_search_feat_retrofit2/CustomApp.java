package com.example.image_search_feat_retrofit2;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomApp extends Application {

    public static final String KEY = "18604857-7e12ef7062c3a484cefe75bf6";
    private static PixabayAPI pixabayAPI;
    private Retrofit retrofit;
    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pixabay.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pixabayAPI = retrofit.create(PixabayAPI.class);
    }

    public static PixabayAPI getPixabayAPI() {
        return pixabayAPI;
    }
}
