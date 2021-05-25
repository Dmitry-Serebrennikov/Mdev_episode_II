package com.example.image_search_feat_retrofit2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.image_search_feat_retrofit2.adapters.ImageAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    String[] imageTypes = {"all", "photo", "vector", "illustrator"};
    EditText request;
    TextView count;
    TextView selection;
    String vibor;
    Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        request = findViewById(R.id.request);
        count = findViewById(R.id.count);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, imageTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                String type_item = (String)parent.getItemAtPosition(position);
                //selection.setText(type_item);//(type_item);
                vibor = type_item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String type_item = (String)parent.getItemAtPosition(1);
                selection.setText(type_item);
                vibor = imageTypes[0];
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
        search = findViewById(R.id.search);
        search.setOnClickListener(v -> {
            CustomApp.getPixabayAPI().getDataByTypeQuery(CustomApp.KEY, request.getText().toString(),selection.toString()) //vibor)//vibor.toString()/*selection.toString(), значение из спиннера*/)
                    .enqueue(new Callback<Result>() {

                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    count.setText("total of " + response.body().getTotalHits() + " images were found on your request: " + "«" + request.getText() + "»");
                    new downloadImageTask().execute( response.body().hits);
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                }
            });

        });
    }

    private class downloadImageTask extends AsyncTask<Hit, Void, ArrayList<Drawable>> {
        ArrayList<Drawable> drawables;
        List<Hit> hits;

        public downloadImageTask(){
            drawables = new ArrayList<>();
        }

        protected ArrayList<Drawable> doInBackground(Hit... hits) {
            this.hits = Arrays.asList(hits);
            for (Hit hit: hits){
                Drawable drawable;
                try {
                    drawable=WebHelper.loadImageFromWebOperations(hit.getLargeImageURL());
                    drawables.add(drawable);
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                    e.printStackTrace();
                }
            }
            return drawables;
        }

        protected void onPostExecute(ArrayList<Drawable> drawables) {
            ListView imageList = findViewById(R.id.imageList);
            ImageAdapter adapter = new ImageAdapter(drawables, hits, MainActivity.this);
            imageList.setAdapter(adapter);
        }
    }
}