package com.example.firebasedemo;

import android.content.Intent;
import android.widget.EditText;

public class Place {
    public String name;
    public double lat, lon;

    public Place(EditText nameText, EditText latText, EditText lonText) {}
    public Place(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }
    public String toString () {
        return "name: " + name + ", latitude: " + lat + ", longtitude: " + lon;
    }
}
