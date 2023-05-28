package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.activities.CartActivity;
import com.example.internetinisknygynas.utils.LocalStorage;

import java.util.Objects;

public class PaymentMethodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        LocalStorage localStorage = new LocalStorage(this);

        ImageView imageViewBackArrow = findViewById(R.id.imageView42);
        RadioButton radioButtonPaypal = findViewById(R.id.radioButtonPaypal);
        RadioButton radioButtonCard = findViewById(R.id.radioButtonCard);
        Button buttonConfirm = findViewById(R.id.button13);

        imageViewBackArrow.setOnClickListener(view -> finish());

        if (Objects.equals(localStorage.getPaymentMethod(), "Paypal")) {
            radioButtonPaypal.setChecked(true);
        }

        buttonConfirm.setOnClickListener(view -> {
            if (radioButtonPaypal.isChecked()) {
                String paymentMethod = "Paypal";
                localStorage.setPaymentMethod(paymentMethod);
                startActivity(new Intent(this, CartActivity.class));
                Toast.makeText(this, "Sėkmingai pasirinktas mokėjimo būdas!", Toast.LENGTH_SHORT).show();
                finish();
            } else if (radioButtonCard.isChecked()) {
                String paymentMethod = "Kredito / Debeto kortele";
                localStorage.setPaymentMethod(paymentMethod);
                Toast.makeText(this, "Sėkmingai pasirinktas mokėjimo būdas!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, CartActivity.class));
                finish();
            }else {
                Toast.makeText(this, "Pasirinkite mokėjimo būdą", Toast.LENGTH_SHORT).show();
            }
        });
    }
}