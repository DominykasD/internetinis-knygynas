package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.adapters.CartAdapter;
import com.example.internetinisknygynas.models.Cart;
import com.example.internetinisknygynas.utils.Callbacks;
import com.example.internetinisknygynas.utils.LocalStorage;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CartActivity extends AppCompatActivity implements Callbacks {
    private TextView orderPrice, total, delivery;
    private LocalStorage localStorage;
    private ConstraintLayout constraintLayout, emptyCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        localStorage = new LocalStorage(this);

        orderPrice = findViewById(R.id.textView37);
        delivery = findViewById(R.id.textView38);
        total = findViewById(R.id.textView42);
        TextView textViewAddress = findViewById(R.id.textView51);
        TextView paymentMethod = findViewById(R.id.textView55);
        TextView deleteCart = findViewById(R.id.textView112);
        ImageView arrowBack = findViewById(R.id.imageView24);
        Button address = findViewById(R.id.button4);
        Button time = findViewById(R.id.button6);
        Button payment = findViewById(R.id.button7);
        Button confirm = findViewById(R.id.button5);
        constraintLayout = findViewById(R.id.cartContent);
        emptyCart = findViewById(R.id.emptyCart);

        orderPrice.setText(String.format(Locale.US,"%.2f", localStorage.getTotalPrice()));
        total.setText(String.format(Locale.US,"%.2f", localStorage.getTotalPrice() + 1.99));

        // Set up recycler view
        List<Cart> cartList;
        cartList = localStorage.getCartList();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCheckout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter cartAdapter = new CartAdapter(this, cartList);
        recyclerView.setAdapter(cartAdapter);


/*        if (cartList.size() == 0) {
            constraintLayout.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
        }*/

        deleteCart.setOnClickListener(view -> {
            localStorage.deleteCart();
            finish();
            startActivity(new Intent(this, MainMenuActivity.class));
        });

        arrowBack.setOnClickListener(view -> {
            finish();
        });

        address.setOnClickListener(view -> {
            startActivity(new Intent(this, AddressActivity.class));
        });

        payment.setOnClickListener(view -> {
            startActivity(new Intent(this, PaymentMethodActivity.class));
        });

        if (localStorage.getPaymentMethod() != null) {
            paymentMethod.setText(localStorage.getPaymentMethod());
        }

        if (localStorage.getLocationAddress() != null && localStorage.getLocationNumber() != null) {
            textViewAddress.setText(localStorage.getLocationAddress() + " " + localStorage.getLocationNumber());
        }


        // Patvirtinti / Confirm
        confirm.setOnClickListener(view -> {
            if (localStorage.getLocationAddress() != null && localStorage.getLocationNumber() != null) {
                if (localStorage.getPaymentMethod() != null) {
                    if (localStorage.getCurrentUser() != null) {
                        AlertDialog alertDialog = showConfirmDialog();
                        alertDialog.show();

                    } else {
                        Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Pasirinkite atsiskaitymo būdą", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Pasirinkite mokėjimo adresą", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private AlertDialog showConfirmDialog() {
        AlertDialog confirmationDialog = new AlertDialog.Builder(this)
                .setTitle("Užsakymo patvirtinimas")
                .setMessage("Ar tikrai norite atlikti užsakymą?")
                .setPositiveButton("Taip", (dialog, which) -> {
                    if(Objects.equals(localStorage.getPaymentMethod(), "Paypal")) {
//                        startActivity(new Intent(this, PaymentActivity.class));
                        startActivity(new Intent(this, PaymentActivity.class));
                        dialog.dismiss();
                    } else {
                        startActivity(new Intent(this, PaymentCardActivity.class));
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Ne", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
        return  confirmationDialog;
    }

    @Override
    public void updateOrderPrice() {
        orderPrice.setText(String.format(Locale.US,"%.2f", localStorage.getTotalPrice()));
        total.setText(String.format(Locale.US,"%.2f", localStorage.getTotalPrice() + 1.99));

        if (localStorage.getTotalPrice() == 0.00) {
            invalidateOptionsMenu();
//
//            constraintLayout.setVisibility(View.GONE);
//            emptyCart.setVisibility(View.VISIBLE);
        }

    }
}