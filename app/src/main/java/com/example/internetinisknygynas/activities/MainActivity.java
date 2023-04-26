package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.utils.LocalStorage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalStorage localStorage = new LocalStorage(this);

//        Buttons
        Button buttonLogin, buttonRegister;

        buttonLogin = findViewById(R.id.button);
        buttonRegister = findViewById(R.id.button2);

        buttonLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        buttonRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        if(localStorage.getCurrentUser() != null ){
            finish();
            startActivity(new Intent(this, MainMenuActivity.class));
        } else {
            System.out.println("Neprisijunges");
        }
    }
}