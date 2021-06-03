package com.example.nasa_picture_day.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ItemDao {

    @Query("SELECT * FROM Item")
    LiveData<List<Item>> getAll();

    @Insert
    long insert(Item item);
}