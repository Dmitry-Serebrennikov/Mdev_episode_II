package com.example.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button signOut;
    TextView email, name;
    ImageView photo;
    Uri photo_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signOut = findViewById(R.id.sign_out_button);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        photo = findViewById(R.id.photoUrl);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
            photo_url = signInAccount.getPhotoUrl();
            //imageView.load("https://www.example.com/image.jpg")
            Picasso.get().load(photo_url).into(photo);
            //photo.load(photo_url);
        }

        View.OnClickListener butViewClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                setResult(RESULT_OK, intent);
                startActivity(intent);
            };
        };
        findViewById(R.id.sign_out_button).setOnClickListener(butViewClickListener);
    }
}