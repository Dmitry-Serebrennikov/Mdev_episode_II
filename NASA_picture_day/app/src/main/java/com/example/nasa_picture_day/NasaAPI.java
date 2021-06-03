package com.example.nasa_picture_day;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaAPI {
    @GET("/planetary/apod")
    Call<Result> getDataForDate(@Query("api_key") String key, @Query("date") String date);

}
