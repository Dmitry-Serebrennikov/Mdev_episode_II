package com.example.nasa_picture_day;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;

public class WebHelper {
    public static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
