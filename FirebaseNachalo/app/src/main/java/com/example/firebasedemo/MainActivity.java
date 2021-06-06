package com.example.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity  {

    Place place = new Place("Beijing", 80, 70);
    Button send;
    EditText name, lon, lat;
    TextView res;
    double lonD, latD;
    String lonS, latS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        lon  = findViewById(R.id.longitude);
        lat  = findViewById(R.id.latitude);
        send = findViewById(R.id.send);
        res  = findViewById(R.id.result);

        View.OnClickListener sendBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place.name = name.getText().toString();
                place.lon = lonD;
                place.lat = latD;
                lonS = lon.getText().toString();
                lonD = Double.parseDouble(lonS);
                latS = lat.getText().toString();
                latD = Double.parseDouble(latS);

                res.setText("Поздравляем! Данные успешно отправлены!");

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref_name = db.getReference("name");
                ref_name.setValue(place.name);
                DatabaseReference ref_lat = db.getReference("coordinates").child("latitude");
                ref_lat.setValue(place.lat);
                DatabaseReference ref_lon = db.getReference("coordinates").child("longitude");
                ref_lon.setValue(place.lon);
            }
        };
        send.setOnClickListener(sendBtn);
    }
}