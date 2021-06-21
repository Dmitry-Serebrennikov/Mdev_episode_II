package com.example.mapnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static final String FIREBASE_PREFERENCES = "firebaseprefs";
    public static final String FIREBASE_PREFERENCES_LOGIN = "login";
    public static final String FIREBASE_PREFERENCES_PASSWORD = "password";
    public static final String defmail = "@default.com";

    EditText loginET, passwordET;

    Button loginBTN, registerBTN;



    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginET = findViewById(R.id.loginET);
        passwordET = findViewById(R.id.passwordET);
        registerBTN = findViewById(R.id.registerBTN);
        loginBTN = findViewById(R.id.loginBTN);

        FirebaseApp.initializeApp(MainActivity.this);

        auth = FirebaseAuth.getInstance();

        SharedPreferences firebasePrefs = getSharedPreferences(FIREBASE_PREFERENCES, MODE_PRIVATE);

        loginBTN.setOnClickListener(view -> {
            if (!passwordET.getText().toString().isEmpty() && !loginET.getText().toString().isEmpty()) {
                loginBTN.setEnabled(false);
                registerBTN.setEnabled(false);
                auth.signInWithEmailAndPassword(loginET.getText().toString() + defmail, passwordET.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                SharedPreferences.Editor editor = firebasePrefs.edit();
                                editor.putString(FIREBASE_PREFERENCES_LOGIN, loginET.getText().toString());
                                editor.putString(FIREBASE_PREFERENCES_PASSWORD, passwordET.getText().toString());
                                editor.apply();

                                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                loginBTN.setEnabled(true);
                                registerBTN.setEnabled(true);
                            }
                        });
            }
        });

        registerBTN.setOnClickListener(view -> {
            if (!passwordET.getText().toString().isEmpty() && !loginET.getText().toString().isEmpty()) {
                loginBTN.setEnabled(false);
                registerBTN.setEnabled(false);
                auth.createUserWithEmailAndPassword(loginET.getText().toString() + defmail, passwordET.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                SharedPreferences.Editor editor = firebasePrefs.edit();
                                editor.putString(FIREBASE_PREFERENCES_LOGIN, loginET.getText().toString());
                                editor.putString(FIREBASE_PREFERENCES_PASSWORD, passwordET.getText().toString());
                                editor.apply();
                                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                loginBTN.setEnabled(true);
                                registerBTN.setEnabled(true);
                            }
                        });
            }
        });
    }



}