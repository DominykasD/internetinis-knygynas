package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.activities.CartActivity;
import com.example.internetinisknygynas.utils.LocalStorage;

public class AddressActivity extends AppCompatActivity {
    AutoCompleteTextView autoAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        LocalStorage localStorage = new LocalStorage(this);

        ImageView backArrow = findViewById(R.id.imageView42);
        Button confirm = findViewById(R.id.button13);
        autoAddress = findViewById(R.id.autoAddress);
        AutoCompleteTextView autoNumber = findViewById(R.id.autoNumber);
        AutoCompleteTextView autoCity = findViewById(R.id.autoCity);


        backArrow.setOnClickListener(view -> {
            finish();
        });
        if (localStorage.getLocationAddress() != null && localStorage.getLocationNumber() != null && localStorage.getLocationCity() != null) {
            autoAddress.setText(localStorage.getLocationAddress());
            autoNumber.setText(localStorage.getLocationNumber());
            autoCity.setText(localStorage.getLocationCity());
        }
        confirm.setOnClickListener(view -> {
            if (!autoAddress.getText().toString().trim().isEmpty() && !autoNumber.getText().toString().trim().isEmpty()
                    && !autoCity.getText().toString().trim().isEmpty()) {
                String address = autoAddress.getText().toString();
                String number = autoNumber.getText().toString();
                String city = autoCity.getText().toString();

                localStorage.setLocationAddress(address);
                localStorage.setLocationNumber(number);
                localStorage.setLocationCity(city);
                Toast.makeText(this, "Pristatymo vieta sėkmingai užpildyta!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, CartActivity.class));
            } else {
                Toast.makeText(this, "Ne visi laukai užpildyti", Toast.LENGTH_SHORT).show();
            }
        });
    }
}