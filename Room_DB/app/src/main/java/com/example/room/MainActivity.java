package com.example.room;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.room.adapters.CategoryAdapter;
import com.example.room.adapters.ProductsAdapter;
import com.example.room.models.Category;
import com.example.room.models.CategoryProduct;
import com.example.room.models.Product;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    DB db;
    List<Product> productList;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DB.create(this, false);
        new Thread(() -> initDb()).start();
        new Thread(){
            @Override
            public void run(){
                productList = db.manager().selectAllProd();
                categoryList = db.manager().selectAllCat();
                setCursorInUIThread(productList, categoryList);
            }
        }.start();
    }

    private void initDb() {
        for (Product product : db.manager().selectAllProd()) {
            db.manager().delete(product);
        }
        for (Category category : db.manager().selectAllCat()) {
            db.manager().delete(category);
        }
        for (CategoryProduct catProd : db.manager().selectAllCatProd()) {
            db.manager().delete(catProd);
        }
        for(int i = 0; i<3; i++){
            Category category = new Category(i, "Cat"+i);
            db.manager().insert(category);
        }
        for(int i = 0; i<10;i++){
            Product product = new Product(i,"Prod"+i,"Auth"+i, i*10);
            db.manager().insert(product);
        }
        for(int i = 0; i<10;i++) {
            CategoryProduct cp = new CategoryProduct(i,i, i%3);
            db.manager().insert(cp);
        }
        
    }

    public void setCursorInUIThread(List<Product> productList, List<Category> categoryList) {
        runOnUiThread(() -> {

            CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryList);
            ListView categories = findViewById(R.id.categories);
            categories.setAdapter(categoryAdapter);

            ProductsAdapter productsAdapter = new ProductsAdapter(this, productList);
            ListView products = findViewById(R.id.products);
            products.setAdapter(productsAdapter);

            AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
                int index = categoryList.get(position).getId();
                new Thread() {
                    @Override
                    public void run(){
                        setCursorInUIThread(db.manager().selectByCategory(index), categoryList);
                    }

                }.start();
            };
            categories.setOnItemClickListener(listener);

        });
    }

    public void onClick(View v){
        setCursorInUIThread(productList, categoryList);
    }

}

