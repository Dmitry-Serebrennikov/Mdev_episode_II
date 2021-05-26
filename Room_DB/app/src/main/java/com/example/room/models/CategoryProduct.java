package com.example.room.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_products")
public class CategoryProduct {
    @PrimaryKey
    int id;
    @NonNull
    int productId, categoryId;

    public CategoryProduct(int id, int productId, int categoryId){
        this.id = id;
        this.categoryId = categoryId;
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
