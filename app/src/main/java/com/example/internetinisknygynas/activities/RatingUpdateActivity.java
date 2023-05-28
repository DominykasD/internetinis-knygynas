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
import com.example.internetinisknygynas.activities.BookActivity;
import com.example.internetinisknygynas.utils.LocalStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RatingUpdateActivity extends AppCompatActivity {
    private LocalStorage localStorage;
    private final String URLUPDATEREVIEW = "http://10.0.2.2/internetinis-knygynas-server/updateReview.php";
    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_update);
        localStorage = new LocalStorage(this);

        ImageView imageBack = findViewById(R.id.imageView2);
        imageBack.setOnClickListener(view -> finish());

        RatingBar ratingBar = findViewById(R.id.ratingBar4);
        ratingBar.setRating(Float.parseFloat(localStorage.getReviewRating()));

        EditText editTextComment = findViewById(R.id.editTextComment);
        editTextComment.setText(localStorage.getReviewComment());
        
        Button buttonUpdateReview = findViewById(R.id.button12);
        buttonUpdateReview.setOnClickListener(view -> {
            updateReviewInDb(ratingBar.getRating(), editTextComment.getText().toString().trim());
        });

        //        Date
        date = Calendar.getInstance().getTime();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        formattedDate = simpleDateFormat.format(date);
    }

    private void updateReviewInDb(float rating, String editTextComment) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUPDATEREVIEW, response -> {
            if (response.equals("success")) {
                Toast.makeText(this, "Atsiliepimas sėkmingai atnaujintas", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(this, BookActivity.class));


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
                data.put("id", localStorage.getReviewBookId());
                data.put("updatedAt", formattedDate);
                data.put("rating", String.valueOf(rating));
                data.put("comment", editTextComment);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}