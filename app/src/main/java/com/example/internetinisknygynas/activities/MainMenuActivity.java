package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.adapters.BestRatedAdapter;
import com.example.internetinisknygynas.adapters.NewAdapter;
import com.example.internetinisknygynas.adapters.SearchAdapter;
import com.example.internetinisknygynas.models.Book;
import com.example.internetinisknygynas.models.UserRatedBook;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.PaymentButtonIntent;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity {
    private final String URLGETRATEDBOOKS = "http://10.0.2.2/internetinis-knygynas-server/getReviewedBooks.php";
    private final String URL = "http://10.0.2.2/internetinis-knygynas-server/getBookList.php";
    private LocalStorage localStorage;
    private DrawerLayout drawerLayout;
    private List<UserRatedBook> userRatedBookList;
    private List<Book> searchList;
    private List<Book> newList;
    private List<Book> bestRatedList;
    private SearchAdapter searchAdapter;

    private static final String YOUR_CLIENT_ID = "AX48PJtv9fLbO-E243l70FaaBHTpR_o1pvtQPmsVeQeOgUwwjaXXEO0j1Uoc8ocKkoyhzv87PZ6YVW37";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        localStorage = new LocalStorage(this);

        // Check app version
        int sdkVersion = Build.VERSION.SDK_INT;
        CheckoutConfig config  = new CheckoutConfig(
                getApplication(),
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                CurrencyCode.EUR,
                UserAction.PAY_NOW,
                PaymentButtonIntent.CAPTURE,
                new SettingsConfig(
                        true,
                        false
                )
        );
        PayPalCheckout.setConfig(config);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        } else {
            Toast.makeText(this, "Paypal Checkout SDK only available for API 23+", Toast.LENGTH_SHORT).show();
        }

//        Profile
        ImageView profile = findViewById(R.id.imageView2);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation);
        TextView cartSize = findViewById(R.id.textView51);
        Button cart = findViewById(R.id.button4);
        profile.setOnClickListener(view -> {
            drawerLayout.openDrawer(Gravity.LEFT);

            cartSize.setText(localStorage.getCartSize());
            cart.setOnClickListener(view1 -> {
                drawerLayout.close();
                if (localStorage.getCurrentUser() != null) {
                    startActivity(new Intent(this, CartActivity.class));
                } else {
                    Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
                }
            });
        });
        setupDrawerContent(navigationView);

        // Header
        View headerLayout = navigationView.getHeaderView(0);
        TextView fullName = headerLayout.findViewById(R.id.textView114);

        if(localStorage.getCurrentUserFullName() != null) {
            fullName.setText("Labas,\n" + localStorage.getCurrentUserFullName());
        }

//        SearchView
        searchList = new ArrayList<>();
        newList = new ArrayList<>();
        bestRatedList = new ArrayList<>();
        getBookList();


        SearchView searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        ConstraintLayout constraintMain = findViewById(R.id.constraintMain);
        ConstraintLayout constraintSearch = findViewById(R.id.constraintSearch);
        constraintMain.setOnClickListener(view -> {
            searchView.clearFocus();
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty()) {
                    constraintSearch.setVisibility(View.VISIBLE);
                    constraintMain.setVisibility(View.GONE);
                    searchAdapter.getFilter().filter(s);
                } else {
                    constraintSearch.setVisibility(View.GONE);
                    constraintMain.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });


//        getUserRatedBooks
        if(localStorage.getCurrentUser() != null) {
            userRatedBookList = new ArrayList<>();
            getUserRatedBooksFromDb();
        }

//        Cart image
        ImageView imageCart = findViewById(R.id.imageView);
        imageCart.setOnClickListener(view -> startActivity(new Intent(this, CartActivity.class)));
    }

    private void getBookList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            try {
                JSONObject jsonobject = new JSONObject(response);
                JSONArray jsonArray = jsonobject.getJSONArray("book");
                final int responseItems = jsonArray.length();
                for (int i = 0; i < responseItems; i++) {
                    JSONObject data = jsonArray.getJSONObject(i);

                    Book book = new Book();
                    book.setId(data.getString("id"));
                    book.setTitle(data.getString("title"));
                    book.setCategory(data.getString("category"));
                    book.setPages(data.getString("pages"));
                    book.setDescription(data.getString("description"));
                    book.setPrice(data.getString("price"));
                    book.setImage(data.getString("image"));
                    book.setAvailable_quantity(data.getString("available_quantity"));
//                    This
                    book.setSubTotal("0");
                    book.setPublisher(data.getString("publisher"));
                    book.setRating(data.getString("rating"));
                    book.setNumber_of_ratings(data.getString("number_of_ratings"));
                    book.setAuthor(data.getString("author"));
                    book.setYear(data.getString("year"));
                    book.setISBN(data.getString("ISBN"));
                    book.setFormat(data.getString("format"));
                    book.setLanguage(data.getString("language"));
                    book.setStatus(data.getString("status"));
                    book.setNew_book(data.getString("new_book"));
                    searchList.add(book);
                    if(book.getNew_book().equals("1")) {
                        newList.add(book);
                    }
                    bestRatedList.add(book);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RecyclerView recyclerView = findViewById(R.id.recViewSearch);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            searchAdapter = new SearchAdapter(this, searchList);
            recyclerView.setAdapter(searchAdapter);

//            News
            RecyclerView recyclerViewNew = findViewById(R.id.recyclerView);
            recyclerViewNew.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            NewAdapter newsAdapter = new NewAdapter(this, newList);
            recyclerViewNew.setAdapter(newsAdapter);

//            Best Rated
            RecyclerView recyclerViewRating = findViewById(R.id.recyclerView2);
            recyclerViewRating.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            Collections.sort(bestRatedList, (item1, item2) ->
                    Float.compare(Float.parseFloat(item2.getRating()), Float.parseFloat(item1.getRating())));
            BestRatedAdapter bestRatedAdapter = new BestRatedAdapter(this, bestRatedList);
            recyclerViewRating.setAdapter(bestRatedAdapter);

        }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getUserRatedBooksFromDb() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETRATEDBOOKS, response -> {
            try {
                JSONObject jsonobject = new JSONObject(response);
                JSONArray jsonArray = jsonobject.getJSONArray("books");

                final int responseItems = jsonArray.length();
                for (int i = 0; i < responseItems; i++) {
                    JSONObject data = jsonArray.getJSONObject(i);

                    UserRatedBook userRatedBook = new UserRatedBook();
                    userRatedBook.setBook_id(data.getString("book_id"));
                    userRatedBook.setUser_id(data.getString("user_id"));
                    userRatedBook.setRating(data.getString("rating"));
                    userRatedBook.setComment(data.getString("comment"));
                    if (Objects.equals(localStorage.getCurrentUser(), userRatedBook.getUser_id())) {
                        userRatedBookList.add(userRatedBook);
                        Gson gson = new Gson();
                        String ratedBooksString = gson.toJson(userRatedBookList);
                        localStorage.setUserRatedBooks(ratedBooksString);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("user_id", localStorage.getCurrentUser());
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
    }

    private void selectDrawerItem(MenuItem item) {
        if (item.getItemId() == R.id.item_profile) {
            if(localStorage.getCurrentUser() != null) {
                drawerLayout.close();
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_orders) {
            drawerLayout.close();
            startActivity(new Intent(this, OrdersActivity.class));
        }
        if (item.getItemId() == R.id.item_likes) {
            if(localStorage.getCurrentUser() != null) {
                drawerLayout.close();
                startActivity(new Intent(this, FavoriteActivity.class));
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_books) {
            if(localStorage.getCurrentUser() != null) {
                drawerLayout.close();
                startActivity(new Intent(this, BookListActivity.class));
            } else {
                Toast.makeText(this, "Prisijunkite prie sistemos", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_logout) {
            localStorage.deleteCurrentUser();
            localStorage.deleteCart();
            localStorage.deleteFavorites();
            localStorage.deleteUserRatedBooks();
            drawerLayout.close();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}