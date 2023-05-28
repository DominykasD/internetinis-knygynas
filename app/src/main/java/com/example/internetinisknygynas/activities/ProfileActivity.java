package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.utils.LocalStorage;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static final String URL_UPDATE_USER = "http://10.0.2.2/internetinis-knygynas-server/updateUser.php";
    private LocalStorage localStorage;
    private EditText name, surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        localStorage = new LocalStorage(this);

        ImageView backButton = findViewById(R.id.imageView46);

        name = findViewById(R.id.editTextName2);
        surname = findViewById(R.id.editTextSurname2);
        TextView email = findViewById(R.id.textView120);
        Button update = findViewById(R.id.button19);

        backButton.setOnClickListener(view -> {
            finish();
        });

        if(localStorage.getCurrentUserFullName() != null) {
            String[] strings = localStorage.getCurrentUserFullName().split("\\s+");
            name.setText(strings[0]);
            surname.setText(strings[1]);
            if(localStorage.getCurrentUserEmail() != null) {
                email.setText(localStorage.getCurrentUserEmail());
            }
        }

        update.setOnClickListener(view -> {
            if(!name.getText().toString().trim().isEmpty() && !surname.getText().toString().trim().isEmpty()) {
                localStorage.setCurrentUserFullName(name.getText().toString().trim() + " " + surname.getText().toString().trim());
                updateUserDb();
            } else {
                Toast.makeText(this, "Nevisi laukai užpildyti!", Toast.LENGTH_SHORT).show();
            }
        });

//        Admin
        Button buttonAdmin = findViewById(R.id.button9);
        if(localStorage.getCurrentUserEmail().equals("admin@admin.com")) {
            buttonAdmin.setVisibility(View.VISIBLE);
        } else {
            buttonAdmin.setVisibility(View.INVISIBLE);
        }
        buttonAdmin.setOnClickListener(view -> {
            startActivity(new Intent(this, AdminActivity.class));
        });
    }

    private void updateUserDb() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_USER, response -> {
            if (response.equals("success")) {
                Toast.makeText(this, "Paskyra sėkmingai atnaujinta", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(this, MainMenuActivity.class));

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
                data.put("name", name.getText().toString().trim());
                data.put("surname", surname.getText().toString().trim());
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}