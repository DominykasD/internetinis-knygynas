package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internetinisknygynas.R;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {
    private static final String URL_POST_BOOK = "http://10.0.2.2/internetinis-knygynas-server/postBook.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        ImageView imageBack = findViewById(R.id.imageView46);
        imageBack.setOnClickListener(view -> finish());

        EditText bookTitle = findViewById(R.id.editTextTitle);
        EditText bookCategory = findViewById(R.id.editTextSurname2);
        EditText bookPages = findViewById(R.id.editTextSurname3);
        EditText bookDescription = findViewById(R.id.editTextSurname4);
        EditText bookPrice = findViewById(R.id.editTextSurname5);
        EditText bookImage = findViewById(R.id.editTextSurname6);
        EditText bookAvailableQuantity = findViewById(R.id.editTextSurname7);
        EditText bookPublisher = findViewById(R.id.editTextSurname8);
        EditText bookAuthor = findViewById(R.id.editTextSurname9);
        EditText bookYear = findViewById(R.id.editTextSurname10);
        EditText bookISBN = findViewById(R.id.editTextSurname11);
        EditText bookFormat = findViewById(R.id.editTextSurname12);
        EditText bookLanguage = findViewById(R.id.editTextSurname13);

        if (!bookTitle.getText().toString().trim().isEmpty() && !bookCategory.getText().toString().trim().isEmpty() && !bookPages.getText().toString().trim().isEmpty() &&
                !bookDescription.getText().toString().trim().isEmpty() && !bookPrice.getText().toString().trim().isEmpty() && !bookImage.getText().toString().trim().isEmpty() &&
                !bookAvailableQuantity.getText().toString().trim().isEmpty() && !bookPublisher.getText().toString().trim().isEmpty() && !bookAuthor.getText().toString().trim().isEmpty() &&
                bookYear.getText().toString().trim().isEmpty() && !bookISBN.getText().toString().trim().isEmpty() &&  !bookFormat.getText().toString().trim().isEmpty() &&  !bookLanguage.getText().toString().trim().isEmpty()) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST_BOOK, response -> {
                if (response.equals("success")) {
                    System.out.println("success");
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
                    data.put("title", bookTitle.getText().toString().trim());
                    data.put("category", bookCategory.getText().toString().trim());
                    data.put("pages", bookPages.getText().toString().trim());
                    data.put("description", bookDescription.getText().toString().trim());
                    data.put("price", bookPrice.getText().toString().trim());
                    data.put("image", bookImage.getText().toString().trim());
                    data.put("available_quantity", bookAvailableQuantity.getText().toString().trim());
                    data.put("publisher", bookPublisher.getText().toString().trim());
                    data.put("rating", "0");
                    data.put("number_of_ratings", "0");
                    data.put("author", bookAuthor.getText().toString().trim());
                    data.put("year", bookYear.getText().toString().trim());
                    data.put("ISBN", bookISBN.getText().toString().trim());
                    data.put("format", bookFormat.getText().toString().trim());
                    data.put("language", bookLanguage.getText().toString().trim());
                    data.put("status", "Turime sandėlyje!");
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

}