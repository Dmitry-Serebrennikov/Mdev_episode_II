package com.example.image_search_feat_retrofit2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayAPI {
    @GET("/api")
    Call<Result> getAllData(@Query("key") String key);

    @GET("/api")
    Call<Result> getDataByQuery(@Query("key") String key, @Query("q") String query);

    @GET("/api")
    Call<Result> getDataByTypeQuery(@Query("key") String key, @Query("q") String query, @Query("image_type") String imageType);


}
