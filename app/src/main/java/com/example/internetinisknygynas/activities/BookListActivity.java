package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internetinisknygynas.adapters.FilterSpinnerAdapter;
import com.example.internetinisknygynas.utils.SortOption;
import com.example.internetinisknygynas.adapters.SortSpinnerAdapter;
import com.example.internetinisknygynas.adapters.BookListAdapter;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.models.Book;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListActivity extends AppCompatActivity implements FilterSpinnerAdapter.CheckboxChangeListener, SortSpinnerAdapter.SortSpinnerTextChangeListener {
    private final String URL = "http://10.0.2.2/internetinis-knygynas-server/getBookList.php";
    private List<Book> bookList;
    private LocalStorage localStorage;
    private FilterSpinnerAdapter spinnerAdapter;
    private SortSpinnerAdapter sortSpinnerAdapter;
    private BookListAdapter bookListAdapter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        localStorage = new LocalStorage(this);

        bookList = new ArrayList<>();
        getBookList();

//        Filter
        Spinner spinnerFilter = findViewById(R.id.spinner);
        // List of items for the Spinner
        List<String> items = new ArrayList<>();
        items.add("Pratybos");
        items.add("Vadovėlis");
        items.add("Turime sandėlyje");
        items.add("Neturime sandėlyje");
// Nuo 1 klasės iki 4
        items.add("Pradinis ugdymas");
// Nuo 5 klasės 10 klasės
        items.add("Pagrindinis ugdymas");
// 11, 12 klasė
        items.add("Vidurinis ugdymas");
        items.add("Šviesa");
        items.add("Didakta");

        // Initialize the custom adapter
        spinnerAdapter = new FilterSpinnerAdapter(this,  items, bookListAdapter);

        // Set the custom adapter to the Spinner
        spinnerAdapter.setCheckboxChangeListener(this);
        spinnerFilter.setAdapter(spinnerAdapter);

        TextView spinnerText = findViewById(R.id.textView60);
        spinnerText.setOnClickListener(view -> {
            spinnerFilter.performClick();
        });

//        Sort by spinner
        Spinner spinnerSort = findViewById(R.id.spinner2);
        List<String> itemsSort = new ArrayList<>();
        itemsSort.add("Geriausiai įvertinti");
        itemsSort.add("Brangiausios");
        itemsSort.add("Pigiausios");

        // Initialize the custom adapter
        sortSpinnerAdapter = new SortSpinnerAdapter(this,  itemsSort);

        // Set the custom adapter to the Spinner
        sortSpinnerAdapter.setSortSpinnerTextChangeListener(this);
        spinnerSort.setAdapter(sortSpinnerAdapter);

        TextView sortText = findViewById(R.id.textView61);
        sortText.setOnClickListener(view -> {
            spinnerSort.performClick();
        });

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

        ImageView imageCart = findViewById(R.id.imageView5);
        imageCart.setOnClickListener(view -> {
            startActivity(new Intent(this, CartActivity.class));
        });
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
                    bookList.add(book);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RecyclerView recyclerView = findViewById(R.id.recViewResList);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            bookListAdapter = new BookListAdapter(this, bookList);
            recyclerView.setAdapter(bookListAdapter);

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

    @Override
    public void onCheckboxChanged(int position, String item, boolean b, List<Boolean> checkedItems) {
        System.out.println(checkedItems);
        if(!b) {
            bookListAdapter.getFilter().filter(null);
        } else {
            bookListAdapter.getFilter().filter(item);

        }
    }

    @Override
    public void onTextChange(int position, String item) {
        SortOption selectedSortOption = SortOption.values()[position];
        bookListAdapter.setSortOption(selectedSortOption);
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