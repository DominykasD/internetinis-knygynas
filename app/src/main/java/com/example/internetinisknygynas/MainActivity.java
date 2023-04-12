package com.example.internetinisknygynas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}