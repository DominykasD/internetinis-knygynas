package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internetinisknygynas.adapters.FavoriteAdapter;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.models.Favorite;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FavoriteActivity extends AppCompatActivity {
    private static final String URL_GET_FAVORITE_DATA = "http://10.0.2.2/internetinis-knygynas-server/getFavoriteData.php";
    private List<Favorite> favoriteList;
    private LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        localStorage = new LocalStorage(this);

        ImageView backButton = findViewById(R.id.imageView47);

        backButton.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, MainMenuActivity.class));
        });

        favoriteList = new ArrayList<>();
        getFavoriteListFromDB();
    }

    private void getFavoriteListFromDB() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_FAVORITE_DATA, response -> {
            try {
                JSONObject jsonobject = new JSONObject(response);
                JSONArray jsonArray = jsonobject.getJSONArray("favorite");
                final int responseItems = jsonArray.length();
                for (int i = 0; i < responseItems; i++) {
                    JSONObject data = jsonArray.getJSONObject(i);

                    Favorite favorite = new Favorite();
                    favorite.setId(data.getString("id"));
                    favorite.setUser_id(data.getString("user_id"));
                    favorite.setBook_id(data.getString("book_id"));
                    favorite.setBookTitle(data.getString("bookTitle"));
                    favorite.setBookImage(data.getString("bookImage"));
                    favorite.setBookAuthor(data.getString("bookAuthor"));
                    favorite.setBookPrice(data.getString("bookPrice"));
                    favorite.setNumberOfRatings(data.getString("number_of_ratings"));
                    favorite.setAvailableQuantity(data.getString("available_quantity"));
                    favorite.setBookPublisher(data.getString("publisher"));
                    favorite.setBookYear(data.getString("year"));
                    favorite.setBookPages(data.getString("pages"));
                    favorite.setBookISBN(data.getString("ISBN"));
                    favorite.setBookFormat(data.getString("format"));
                    favorite.setBookLanguage(data.getString("language"));
                    favorite.setBookDescription(data.getString("description"));
                    favorite.setBookRating(data.getString("rating"));

                    if (Objects.equals(localStorage.getCurrentUser(), favorite.getUser_id())) {
                        favoriteList.add(favorite);
                        Gson gson = new Gson();
                        String favoritesString = gson.toJson(favoriteList);
                        localStorage.setFavorites(favoritesString);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RecyclerView recyclerView = findViewById(R.id.recViewFavorite);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            FavoriteAdapter favoriteAdapter = new FavoriteAdapter(this, favoriteList);
            recyclerView.setAdapter(favoriteAdapter);


        }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}