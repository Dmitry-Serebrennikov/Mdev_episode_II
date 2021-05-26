package com.example.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.room.models.Category;
import com.example.room.models.CategoryProduct;
import com.example.room.models.Product;

import java.util.List;

@Dao
interface RoomDAO {
    @Query("SELECT id FROM products ORDER BY id DESC")
    int selectLastIndex();

    @Query("SELECT * FROM products ORDER BY id")
    List<Product> selectAllProd();

    @Query("SELECT * FROM categories ORDER BY id")
    List<Category> selectAllCat();
    @Query("SELECT * FROM category_products ORDER BY id")
    List<CategoryProduct> selectAllCatProd();

    @Query("SELECT * FROM products as p, category_products as cp WHERE  p.id = cp.productId AND cp.categoryId = :index")
    List<Product> selectByCategory(int index);

    @Query("SELECT id, name, author, price FROM products WHERE id = :id")
    Product selectById(int id);

    @Insert
    void insert(Product... products);
    @Insert
    void insert(Category... categories);
    @Insert
    void insert(CategoryProduct... categoryProducts);

    @Delete
    void delete(Product... products);
    @Delete
    void delete(Category... categories);
    @Delete
    void delete(CategoryProduct... categoryProducts);

    @Update
    void update(Product... products);
}