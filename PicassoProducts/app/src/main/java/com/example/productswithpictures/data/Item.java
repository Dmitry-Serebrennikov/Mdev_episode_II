package com.example.productswithpictures.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "vendor_code")
    private long vendorCode;

    @ColumnInfo(name = "stock_availability")
    private String stockAvailability;

    @ColumnInfo(name = "price")
    private long price;

    @ColumnInfo(name = "image_ref")
    private String imageRef;

    public Item(String title, String category, long vendorCode, String stockAvailability, long price, String imageRef) {
        this.title = title;
        this.category = category;
        this.vendorCode = vendorCode;
        this.stockAvailability = stockAvailability;
        this.price = price;
        this.imageRef = imageRef;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(long vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getStockAvailability() {
        return stockAvailability;
    }

    public void setStockAvailability(String stockAvailability) {
        this.stockAvailability = stockAvailability;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }
}
