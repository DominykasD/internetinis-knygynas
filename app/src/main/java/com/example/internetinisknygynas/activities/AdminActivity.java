package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.internetinisknygynas.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ImageView imageBack = findViewById(R.id.imageView46);
        imageBack.setOnClickListener(view -> finish());

        Button buttonAddBook = findViewById(R.id.button19);
        buttonAddBook.setOnClickListener(view -> {
            startActivity(new Intent(this, AddBookActivity.class));
        });
    }
}