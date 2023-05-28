package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.models.UserRatedBook;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RatingActivity extends AppCompatActivity {
    private final String URLREVIEW = "http://10.0.2.2/internetinis-knygynas-server/review.php";
    private LocalStorage localStorage;
    private RatingBar ratingBar;
    private EditText editTextComment;
    private List<UserRatedBook> userRatedBookList = new ArrayList<>();
    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        localStorage = new LocalStorage(this);

        ImageView imageBack = findViewById(R.id.imageView2);
        imageBack.setOnClickListener(view -> finish());

//        Button add
        Button buttonAddReview = findViewById(R.id.button12);
        ratingBar = findViewById(R.id.ratingBar4);
        editTextComment = findViewById(R.id.editTextComment);
        buttonAddReview.setOnClickListener(view -> {
            if(ratingBar.getRating() != 0 && !editTextComment.getText().toString().trim().isEmpty()) {
                putReviewToDb(ratingBar.getRating(), editTextComment.getText().toString().trim());
                Toast.makeText(this, "Komentaras sėkmingai pridėtas!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(this, BookActivity.class));

            }
        });

//        Date
        date = Calendar.getInstance().getTime();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        formattedDate = simpleDateFormat.format(date);
        System.out.println(formattedDate);
    }

    private void putReviewToDb(float rating, String comment) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLREVIEW, response -> {
            if (response.equals("success")) {

                UserRatedBook userRatedBook = new UserRatedBook();
                userRatedBook.setBook_id(localStorage.getBookId());
                userRatedBook.setUser_id(localStorage.getCurrentUser());
                userRatedBook.setRating(String.valueOf(rating));
                userRatedBook.setComment(comment);

                if (Objects.equals(localStorage.getCurrentUser(), userRatedBook.getUser_id())) {
                    userRatedBookList.add(userRatedBook);
                    Gson gson = new Gson();
                    String ratedBooksString = gson.toJson(userRatedBookList);
                    localStorage.setUserRatedBooks(ratedBooksString);
                }
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
                data.put("book_id", localStorage.getBookId());
                data.put("createdAt", formattedDate);
                data.put("updatedAt", formattedDate);
                data.put("rating", String.valueOf(rating));
                data.put("comment", comment);

                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}