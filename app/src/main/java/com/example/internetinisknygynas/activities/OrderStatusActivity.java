package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.adapters.OrderedAdapter;
import com.example.internetinisknygynas.adapters.OrdersAdapter;
import com.example.internetinisknygynas.models.Cart;
import com.example.internetinisknygynas.models.Order;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        LocalStorage localStorage = new LocalStorage(this);



        TextView orderNumber = findViewById(R.id.textView95);
        TextView date = findViewById(R.id.textView977);
        TextView trukme = findViewById(R.id.textView999);
        TextView address = findViewById(R.id.textView1011);
        TextView city = findViewById(R.id.textView1033);
        TextView closeButton = findViewById(R.id.textView1055);

        orderNumber.setText(localStorage.getOrderNumber());
        date.setText(localStorage.getOrderDate());
        trukme.setText("2-3 darbo dienos");
        address.setText(localStorage.getLocationAddress());
        city.setText(localStorage.getLocationCity());

        List<Cart> orderList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recViewOrdered);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(localStorage.getOrdered() != null) {
            Gson gson = new Gson();
            String jsonOrderedBooks = localStorage.getOrdered();
            Type type = new TypeToken<List<Cart>>() {}.getType();
            orderList = gson.fromJson(jsonOrderedBooks, type);

        }

        OrderedAdapter orderedAdapter = new OrderedAdapter(this, orderList);
        recyclerView.setAdapter(orderedAdapter);



        closeButton.setOnClickListener(view -> {
            if(localStorage.getClickedPlačiau() != null && Objects.equals(localStorage.getClickedPlačiau(), "TRUE")) {
                finish();
                startActivity(new Intent(this, OrdersActivity.class));
            } else {
                startActivity(new Intent(this, MainMenuActivity.class));
            }

        });
    }
}