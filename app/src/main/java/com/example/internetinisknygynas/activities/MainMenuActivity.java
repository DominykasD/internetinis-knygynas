package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.android.material.navigation.NavigationView;

public class MainMenuActivity extends AppCompatActivity {
    private LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ImageView profile = findViewById(R.id.imageView2);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        localStorage = new LocalStorage(this);
        NavigationView navigationView = findViewById(R.id.navigation);

        profile.setOnClickListener(view -> {
            drawerLayout.openDrawer(Gravity.LEFT);
//            cartSize.setText(localStorage.getCartSize());
//            cart.setOnClickListener(view1 -> {
//                drawerLayout.close();
//                if (localStorage.getCurrentUser() != null) {
//                    startActivity(new Intent(this, CheckoutActivity.class));
//                } else {
//                    Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
//                }
//            });
        });
        setupDrawerContent(navigationView);

        // Header
        View headerLayout = navigationView.getHeaderView(0);
        TextView fullName = headerLayout.findViewById(R.id.textView114);

        if(localStorage.getCurrentUserFullName() != null) {
            fullName.setText("Labas,\n" + localStorage.getCurrentUserFullName());
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
    }

    private void selectDrawerItem(MenuItem item) {
        /*if (item.getItemId() == R.id.item_profile) {
            if(localStorage.getCurrentUser() != null) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_orders) {
            startActivity(new Intent(this, OrdersActivity.class));
        }
        if (item.getItemId() == R.id.item_adress) {
            if(localStorage.getCurrentUser() != null) {
                startActivity(new Intent(this, AddressActivity.class));
                localStorage.setAddressOperation("Pasirinkite adresą");
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_payment) {
            if(localStorage.getCurrentUser() != null) {
                startActivity(new Intent(this, PaymentMethodActivity.class));
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_offers) {
            if(localStorage.getCurrentUser() != null) {
                startActivity(new Intent(this, DiscountActivity.class));
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_credits) {
            if(localStorage.getCurrentUser() != null) {
                startActivity(new Intent(this, CreditsActivity.class));
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_likes) {
            if(localStorage.getCurrentUser() != null) {
                startActivity(new Intent(this, FavoriteActivity.class));
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_qr) {
            if(localStorage.getCurrentUser() != null) {
//                startActivity(new Intent(this, CreditsActivity.class));
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }*/
        if (item.getItemId() == R.id.item_logout) {
            localStorage.deleteCurrentUser();
//            localStorage.deleteCart();
//            localStorage.deleteFavorites();
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}