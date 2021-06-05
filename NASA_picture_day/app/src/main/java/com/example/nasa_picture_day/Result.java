package com.example.nasa_picture_day;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Result {
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("date")
    @Expose
    Date date;
    @SerializedName("url")
    @Expose
    String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
