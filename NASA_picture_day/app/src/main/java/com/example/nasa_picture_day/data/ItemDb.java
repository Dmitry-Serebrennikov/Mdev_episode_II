package com.example.nasa_picture_day.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class}, version = 1)
public abstract class ItemDb extends RoomDatabase {
    public abstract ItemDao itemDao();
}