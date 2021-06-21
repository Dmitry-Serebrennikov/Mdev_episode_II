package com.example.mapnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.mapnote.MainActivity.FIREBASE_PREFERENCES;
import static com.example.mapnote.MainActivity.FIREBASE_PREFERENCES_LOGIN;
import static com.example.mapnote.MainActivity.FIREBASE_PREFERENCES_PASSWORD;
import static com.example.mapnote.MainActivity.defmail;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Launcher);
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        SharedPreferences firebasePrefs = getSharedPreferences(FIREBASE_PREFERENCES, MODE_PRIVATE);
        String login = firebasePrefs.getString(FIREBASE_PREFERENCES_LOGIN, "");
        String password = firebasePrefs.getString(FIREBASE_PREFERENCES_PASSWORD, "");
        if (login.equals("")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            auth.signInWithEmailAndPassword(login + defmail, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(this, NoteActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }
    }
}