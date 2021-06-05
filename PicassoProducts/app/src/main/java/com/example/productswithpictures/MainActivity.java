package com.example.productswithpictures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.productswithpictures.adapter.ItemListAdapter;
import com.example.productswithpictures.data.Item;
import com.example.productswithpictures.data.ItemDb;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemListAdapter adapter;

    ItemDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room
                .databaseBuilder(getApplicationContext(), ItemDb.class, "db")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase dbSupport) {
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            db.itemDao().insert(
                                    new Item("Конкретная математика", "Книги", 11111111,"Available", 5000, "https://raw.githubusercontent.com/Dmitry-Serebrennikov/Mdev_episode_II/master/PicassoProducts/pictures/math.png"),
                                    new Item("ПК для учебы", "Техника", 22222222,"Available", 100000, "https://github.com/Dmitry-Serebrennikov/Mdev_episode_II/blob/master/PicassoProducts/pictures/comp.jpg?raw=true"),
                                    new Item("Прикладной фотоаппарат", "Техника", 33333333,"Available", 25000, "https://upload.wikimedia.org/wikipedia/commons/c/c8/Practica_L2_camera_front.jpg"),
                                    new Item("Обувь для программирования", "Одежда и обувь", 44444444,"Available", 300, "https://i.pinimg.com/originals/11/50/ea/1150eabd900cf92ff43d9ca0c50c00c1.jpg"),
                                    new Item("Лехина колесница", "Авто", 55555555,"Available", 20000, "https://wroom.ru/i/cars2/lada_2101_1.jpg"),
                                    new Item("Проездной", "Прикладник style", 77777777,"Available", 600, "https://irkobl.ru/itcio/images/%D0%B8%D1%80%D0%BA%D1%83%D1%82%D1%81%D0%BA.png"),
                                    new Item("Прикладной рюкзак", "Прикладник style", 88888888,"Available", 3500, "https://image.freepik.com/free-photo/vintage-backpack_172251-329.jpg"),
                                    new Item("Сделать наконец домашку", "Услуга", 99999999,"unavailable", 0, "https://picsum.photos/id/1/1000"));
                        });
                    }
                })
                .build();

        adapter = new ItemListAdapter();

        recyclerView = findViewById(R.id.products_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        db.itemDao().getAll().observe(this, items -> adapter.submitList(items));
    }
}
