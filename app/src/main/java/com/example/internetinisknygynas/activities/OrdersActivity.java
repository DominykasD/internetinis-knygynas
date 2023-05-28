package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.adapters.OrdersAdapter;
import com.example.internetinisknygynas.models.Order;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity {
    private final String URL = "http://10.0.2.2/internetinis-knygynas-server/getOrderList.php";
    private LocalStorage localStorage;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        localStorage = new LocalStorage(this);

        ImageView backArrow = findViewById(R.id.imageView43);
        TextView textView = findViewById(R.id.textView111);
        RecyclerView recyclerView = findViewById(R.id.recViewOrders);

        backArrow.setOnClickListener(view -> {
            finish();
        });

        if (localStorage.getCurrentUser() != null) {
            orderList = new ArrayList<>();
            getOrderList();
        } else {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

//    TODO Finish order
    private void getOrderList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            try {
                JSONObject jsonobject = new JSONObject(response);
                JSONArray jsonArray = jsonobject.getJSONArray("orders");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);

                    Order order = new Order();
                    order.setId(data.getString("id"));
                    order.setUser_id(data.getString("user_id"));
                    order.setOrderNumber(data.getString("order_number"));
                    order.setDate(data.getString("date"));
                    order.setStatus(data.getString("status"));
                    order.setCart(data.getString("cart"));
                    order.setAddress(data.getString("street"));
                    order.setNumber(data.getString("street_number"));
                    order.setCity(data.getString("city"));
                    orderList.add(order);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RecyclerView recyclerView = findViewById(R.id.recViewOrders);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            OrdersAdapter ordersAdapter = new OrdersAdapter(this, orderList);
            recyclerView.setAdapter(ordersAdapter);

        }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("user_id", localStorage.getCurrentUser());
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}