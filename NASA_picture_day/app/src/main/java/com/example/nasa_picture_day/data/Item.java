package com.example.nasa_picture_day.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.nasa_picture_day.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity
public class Item {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name ="date")
    String date;
    @ColumnInfo(name ="title")
    String title;
    @ColumnInfo(name = "url")
    String url;
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    byte[] image;


/*


    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "image_ref")
    private String imageRef;
*/

    public Item(String date, String title, String url, byte[] image) {
        this.date = date;
        this.title = title;
        this.url = url;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}