package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.adapters.ReviewAdapter;
import com.example.internetinisknygynas.models.Cart;
import com.example.internetinisknygynas.models.Favorite;
import com.example.internetinisknygynas.models.Review;
import com.example.internetinisknygynas.models.UserRatedBook;
import com.example.internetinisknygynas.utils.Callbacks;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BookActivity extends AppCompatActivity implements Callbacks {
    private LocalStorage localStorage;
    private List<UserRatedBook> userRatedBookList = new ArrayList<>();
    private static final String URL_GET_REVIEWS = "http://10.0.2.2/internetinis-knygynas-server/getReviewList.php";
    private static final String URLGETRATINGS = "http://10.0.2.2/internetinis-knygynas-server/getRatings.php";
    private static final String URL_POST_FAVORITE = "http://10.0.2.2/internetinis-knygynas-server/favorite.php";
    private static final String URL_DELETE_FAVORITE = "http://10.0.2.2/internetinis-knygynas-server/deleteFavorite.php";
    private List<Review> reviewList = new ArrayList<>();
    private RatingBar ratingBar;
    private TextView number_of_ratings;
    private List<Cart> cartList = new ArrayList<>();
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    // footer
    private TextView subtotal;
    private ConstraintLayout constraintLayout;
    private DrawerLayout drawerLayout;
//    Favorite
    private List<Favorite> favoriteList;


    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        localStorage = new LocalStorage(this);

        TextView title = findViewById(R.id.textView10);
        TextView author = findViewById(R.id.textView12);
        ImageView image = findViewById(R.id.imageView3);
        number_of_ratings = findViewById(R.id.textView13);
        TextView price = findViewById(R.id.textView17);
        TextView available_quantity = findViewById(R.id.textView18);
        TextView author2 = findViewById(R.id.textAutorius);
        TextView publisher = findViewById(R.id.textLeidejas);
        TextView year = findViewById(R.id.textMetai);
        TextView pages = findViewById(R.id.textPuslapiai);
        TextView isbn = findViewById(R.id.textISBN);
        TextView format = findViewById(R.id.textFormatas);
        TextView language = findViewById(R.id.textKalba);
        TextView description = findViewById(R.id.textView29);

        if(localStorage.getBookTitle() != null) {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
            circularProgressDrawable.start();
            Glide.with(this)
                    .load(localStorage.getBookImage())
                    .centerCrop()
                    .placeholder(circularProgressDrawable)
                    .into(image);

            title.setText(localStorage.getBookTitle());
            author.setText(localStorage.getBookAuthor());
            price.setText(localStorage.getBookPrice() + "€");
            available_quantity.setText("Sandėlyje liko " + localStorage.getBookAvailableQuantity() + " vnt.");
            author2.setText(localStorage.getBookAuthor());
            publisher.setText(localStorage.getBookPublisher());
            year.setText(localStorage.getBookYear());
            pages.setText(localStorage.getBookPages());
            isbn.setText(localStorage.getBookISBN());
            format.setText(localStorage.getBookFormat());
            language.setText(localStorage.getBookLanguage());
            description.setText(localStorage.getBookDescription());
        }

//        Add to cart
        cartList = localStorage.getCartList();
        constraintLayout = findViewById(R.id.cart_constraint);

        subtotal = findViewById(R.id.textView73);

        if (cartList.size() > 0) {
            constraintLayout.setVisibility(View.VISIBLE);
            subtotal.setText(String.format(Locale.US,"%.2f", localStorage.getTotalPrice()));
        } else {
            constraintLayout.setVisibility(View.GONE);
        }

        Button buttonAddToCart = findViewById(R.id.button10);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        if(!cartList.isEmpty()) {
            for (int i = 0; i < cartList.size(); i++) {
                if(Objects.equals(localStorage.getBookId(), cartList.get(i).getId())) {
                    buttonAddToCart.setVisibility(View.GONE);
                    buttonCancel.setVisibility(View.VISIBLE);
                }
            }
        }

        buttonAddToCart.setOnClickListener(view -> {
            buttonAddToCart.setVisibility(View.GONE);
            buttonCancel.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.VISIBLE);

            Cart cart = new Cart();
            cart.setId(localStorage.getBookId());
            cart.setTitle(localStorage.getBookTitle());
            cart.setAuthor(localStorage.getBookAuthor());
            cart.setPrice(localStorage.getBookPrice());
            cart.setNumber_of_ratings(localStorage.getBookNumberOfRatings());
            cart.setImage(localStorage.getBookImage());
            cart.setAvailable_quantity(localStorage.getBookAvailableQuantity());
            cart.setPublisher(localStorage.getBookPublisher());
            cart.setYear(localStorage.getBookYear());
            cart.setPages(localStorage.getBookPages());
            cart.setISBN(localStorage.getBookISBN());
            cart.setFormat(localStorage.getBookFormat());
            cart.setLanguage(localStorage.getBookLanguage());
            cart.setDescription(localStorage.getBookDescription());
            cart.setRating(localStorage.getBookRating());
            cart.setQuantity("1");
            cart.setSubTotal(Double.parseDouble(localStorage.getBookPrice()));
            cartList = localStorage.getCartList();
            cartList.add(cart);

            String cartString = gson.toJson(cartList);
            localStorage.setCart(cartString);
            updateOrderPrice();

            Toast.makeText(this, "Prekė pridėta į krepšelį", Toast.LENGTH_SHORT).show();

        });

        buttonCancel.setOnClickListener(view -> {
            buttonCancel.setVisibility(View.GONE);
            buttonAddToCart.setVisibility(View.VISIBLE);

            for (int i = 0; i < cartList.size(); i++) {
                if (Objects.equals(localStorage.getBookId(), cartList.get(i).getId())) {
                    cartList.remove(i);
                    Gson gson1 = new Gson();
                    String cartStr = gson1.toJson(cartList);
                    localStorage.setCart(cartStr);
                    Toast.makeText(this, "Prekė pašalinta iš krepšelio!", Toast.LENGTH_SHORT).show();
                }
            }

            if (cartList.isEmpty()) constraintLayout.setVisibility(View.GONE);

            updateOrderPrice();

        });

        Button buttonApmoketi = findViewById(R.id.button17);
        buttonApmoketi.setOnClickListener(view -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        ImageView imageCart = findViewById(R.id.imageView5);
        imageCart.setOnClickListener(view -> {
            startActivity(new Intent(this, CartActivity.class));
        });


//        Rating
        ratingBar = findViewById(R.id.ratingBar2);
        ratingBar.setRating(0);
        number_of_ratings.setText(0 + " atsiliepimai");
        getAverageRatingFromDb();


//        Review
        Button buttonAddReview = findViewById(R.id.button11);
        ConstraintLayout constraintReview = findViewById(R.id.constraintLayout5);
//        localStorage.getBookId + localStorage.getUserRatedBooks
        if(localStorage.getUserRatedBooks() != null) {
            Gson gson = new Gson();
            String jsonUserRatedBooks = localStorage.getUserRatedBooks();
            Type type = new TypeToken<List<UserRatedBook>>() {}.getType();
            userRatedBookList = gson.fromJson(jsonUserRatedBooks, type);

            for(UserRatedBook userRatedBook : userRatedBookList) {
                if(userRatedBook.getBook_id().equals(localStorage.getBookId())) {
                    System.out.println("FOUND IT!!!!!!!!!!!");
                    constraintReview.setVisibility(View.GONE);
                }
                else {
                    System.out.println("BOOK NOT RATED");
                }
            }
        }
        buttonAddReview.setOnClickListener(view -> {
            startActivity(new Intent(this, RatingActivity.class));
        });
        
//        Reviews
        getReviewsFromDb();

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

//        Favorite
        ImageView imageFavorite = findViewById(R.id.imageView9);
        ImageView imageFavoriteRed = findViewById(R.id.imageView10);

        favoriteList = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Favorite>>() {}.getType();
        if(localStorage.getFavorites() != null) {
            favoriteList = gson.fromJson(localStorage.getFavorites(), type);
        }

        if(!favoriteList.isEmpty()) {
            for(int i = 0; i < favoriteList.size(); i++) {
                if(Objects.equals(favoriteList.get(i).getBook_id(), localStorage.getBookId())) {
                    imageFavorite.setVisibility(View.INVISIBLE);
                    imageFavoriteRed.setVisibility(View.VISIBLE);

                }
            }
        } else {
            System.out.println("Favorites empty");
        }

        imageFavorite.setOnClickListener(view -> {
            imageFavorite.setVisibility(View.INVISIBLE);
            imageFavoriteRed.setVisibility(View.VISIBLE);

            Favorite favorite = new Favorite();
            favorite.setUser_id(localStorage.getCurrentUser());
            favorite.setBook_id(localStorage.getBookId());
            favorite.setBookTitle(localStorage.getBookTitle());
            favorite.setBookImage(localStorage.getBookImage());
            favorite.setBookAuthor(localStorage.getBookAuthor());
            favorite.setBookPrice(localStorage.getBookPrice());
//            Local storage
            favoriteList.add(favorite);
            Gson gson1 = new Gson();
            String favoritesString = gson1.toJson(favoriteList);
            localStorage.setFavorites(favoritesString);

            postFavoriteToDB(favorite);
        });

        imageFavoriteRed.setOnClickListener(view -> {
            imageFavorite.setVisibility(View.VISIBLE);
            imageFavoriteRed.setVisibility(View.INVISIBLE);
            
            removeFavoriteFromDB();
            for (int i = 0; i < favoriteList.size(); i++) {
                if(favoriteList.get(i).getId().equals(localStorage.getBookId())) {
                    favoriteList.remove(i);
                    Gson gson1 = new Gson();
                    String favoritesString = gson1.toJson(favoriteList);
                    localStorage.setFavorites(favoritesString);
                }
            }
        });


    }

    private void removeFavoriteFromDB() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_FAVORITE, response -> {
            if (response.equals("success")) {
                System.out.println("removeFromDatabase success");
            } else if (response.equals("failure")) {
                Toast.makeText(this, "Įvyko klaida, bandykite dar kartą", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("book_id", localStorage.getBookId());
                data.put("user_id", localStorage.getCurrentUser());
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void postFavoriteToDB(Favorite favorite) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST_FAVORITE, response -> {
            if (response.equals("success")) {
                System.out.println("postFavirteToDb success");
            } else if (response.equals("failure")) {
                Toast.makeText(this, "Įvyko klaida, bandykite dar kartą", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("user_id", localStorage.getCurrentUser());
                data.put("book_id", favorite.getBook_id());
                data.put("bookTitle", favorite.getBookTitle());
                data.put("bookImage", favorite.getBookImage());
                data.put("bookAuthor", favorite.getBookAuthor());
                data.put("bookPrice", favorite.getBookPrice());

                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getAverageRatingFromDb() {
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETRATINGS, response -> {
            if(!response.equals("empty")) {
                System.out.println(response);
                String[] splited = response.split(", ");
                if(Objects.equals(splited[2], localStorage.getBookId())) {
                    localStorage.setBookRating(splited[0]);
                    ratingBar.setRating(Float.parseFloat(localStorage.getBookRating()));
                    localStorage.setBookNumberOfRatings(splited[1]);
                    number_of_ratings.setText(localStorage.getBookNumberOfRatings() + " atsiliepimai");
                }

            }

        }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("book_id", localStorage.getBookId());
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getReviewsFromDb() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_REVIEWS, response -> {
            try {
                JSONObject jsonobject = new JSONObject(response);
                JSONArray jsonArray = jsonobject.getJSONArray("review");
                final int responseItems = jsonArray.length();
                for (int i = 0; i < responseItems; i++) {
                    JSONObject data = jsonArray.getJSONObject(i);

                    Review review = new Review();
                    review.setId(data.getString("id"));
                    String user = data.getString("name") + " " + data.getString("surname");
                    review.setUser(user);
                    review.setUser_id(data.getString("user_id"));
                    review.setBook_id(data.getString("book_id"));
                    review.setCreatedAt(data.getString("createdAt"));
                    review.setUpdatedAt(data.getString("updatedAt"));
                    review.setRating(data.getString("rating"));
                    review.setComment(data.getString("comment"));
                    if(Objects.equals(localStorage.getBookId(), review.getBook_id())) {
                        reviewList.add(review);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RecyclerView recyclerView = findViewById(R.id.recViewReview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviewList);
            recyclerView.setAdapter(reviewAdapter);

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
    public void updateOrderPrice() {
        subtotal.setText(String.format(Locale.US,"%.2f", localStorage.getTotalPrice()));
        constraintLayout.setVisibility(View.VISIBLE);

        if (localStorage.getTotalPrice() == 0.00) {
            invalidateOptionsMenu();

            constraintLayout.setVisibility(View.GONE);
        }

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