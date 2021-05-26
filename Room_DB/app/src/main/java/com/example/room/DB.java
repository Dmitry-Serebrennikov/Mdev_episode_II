package com.example.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.room.models.Category;
import com.example.room.models.Product;
import com.example.room.models.CategoryProduct;

@Database(entities = {Product.class, Category.class, CategoryProduct.class}, version = 1)
public abstract class DB extends RoomDatabase {
    abstract RoomDAO manager();
    private static final String DB_NAME="products.db";
    private static DB INSTANCE=null;


    synchronized static DB get(Context context){
        if (INSTANCE==null) {
            INSTANCE=create(context, false);
        }
        return(INSTANCE);
    }

    static DB create(Context context, boolean memoryOnly){
        RoomDatabase.Builder<DB> dbBuilder;
        if (memoryOnly) {
            dbBuilder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),DB.class);
        }
        else {
            dbBuilder=Room.databaseBuilder(context.getApplicationContext(), DB.class, DB_NAME);
                    //.createFromAsset("products.db");
        }
        return(dbBuilder.build());
    }
}
