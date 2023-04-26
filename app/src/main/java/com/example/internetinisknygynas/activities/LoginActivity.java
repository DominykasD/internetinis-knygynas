package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
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
import com.example.internetinisknygynas.utils.LocalStorage;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private boolean passwordVisible;
    private final String URL = "http://10.0.2.2/internetinis-knygynas-server/login.php";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LocalStorage localStorage = new LocalStorage(this);


//        Back Button
        ImageView backButton = findViewById(R.id.imageView4);
        backButton.setOnClickListener(view -> {
            finish();
        });

        TextView textRegister = findViewById(R.id.textView14);
        textRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

//        Password field
        EditText password = findViewById(R.id.editTextPsw);
        password.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= password.getRight() - password.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = password.getSelectionEnd();
                        if (passwordVisible) {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_on, 0);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });

//        Login
        EditText email = findViewById(R.id.editTextEmail);
        Button login = findViewById(R.id.button3);
        login.setOnClickListener(view -> {
            if (!email.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                    if (!response.equals("failure")) {
                        try {
                            System.out.println(response);
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonArray = jsonobject.getJSONArray("user");
                            JSONObject data = jsonArray.getJSONObject(0);

                            String id = data.getString("id");
                            String name = data.getString("name");
                            String surname = data.getString("surname");
                            User user = new User();
                            user.setId(id);
                            user.setName(name);
                            user.setSurname(surname);
                            user.setEmail(data.getString("email"));
                            localStorage.setCurrentUser(user.getId());
                            localStorage.setCurrentUserFullName(user.getName() + " " + user.getSurname());
                            localStorage.setCurrentUserEmail(user.getEmail());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(this, MainMenuActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.login_response_failure), Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> data = new HashMap<>();
                        data.put("email", email.getText().toString().trim());
                        data.put("password", password.getText().toString().trim());
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            } else {
                Toast.makeText(this, getString(R.string.login_fields), Toast.LENGTH_SHORT).show();
            }
        });

        email.setText("admin@admin.com");
        password.setText("Admin123!");
    }
}