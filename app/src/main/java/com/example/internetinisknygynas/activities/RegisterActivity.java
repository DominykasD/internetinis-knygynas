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
import com.example.internetinisknygynas.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private boolean passwordVisible;
    private final String URL = "http://10.0.2.2/internetinis-knygynas-server/register.php";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView backButton = findViewById(R.id.imageView4);
        backButton.setOnClickListener(view -> {
            finish();
        });

        TextView loginText = findViewById(R.id.textView14);
        loginText.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

//        Password field
        EditText password = findViewById(R.id.editTextPsw);
        password.setOnTouchListener(new View.OnTouchListener() {
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

//        Register
        EditText surname = findViewById(R.id.editTextSurname);
        EditText email = findViewById(R.id.editTextEmail);
        EditText name = findViewById(R.id.editTextName);
        Button register = findViewById(R.id.button3);
        register.setOnClickListener(view -> {
            if (!email.getText().toString().trim().isEmpty() && !name.getText().toString().trim().isEmpty() && !surname.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()) {
                if (isEmailValid(email.getText().toString().trim())) {
                    if (isNameValid(name.getText().toString().trim())) {
                        if (isNameValid(surname.getText().toString().trim())) {
                            if (isPasswordValid(password.getText().toString().trim())) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                                    if (response.equals("success")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.register_response_success), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, LoginActivity.class));
                                    } else if (response.equals("failure")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.register_response_failure), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), getString(R.string.register_response_error), Toast.LENGTH_SHORT).show();
                                    }
                                }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> data = new HashMap<>();
                                        data.put("email", email.getText().toString().trim());
                                        data.put("name", name.getText().toString().trim());
                                        data.put("surname", surname.getText().toString().trim());
                                        data.put("password", password.getText().toString().trim());
                                        return data;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest);
                            } else {
                                Toast.makeText(this, getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.surname_validation), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.name_validation), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.email_validation), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, getString(R.string.field_validaton), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean isPasswordValid(String psw) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,}$";
        Pattern pattern = Pattern.compile(regex);

        char[] chars = psw.toCharArray();
        Matcher matcher = pattern.matcher(psw);
        return matcher.matches();
    }

    private boolean isNameValid(String name) {
        String regex = "^([^0-9]*)$";
        Pattern pattern = Pattern.compile(regex);

        char[] chars = name.toCharArray();
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private boolean isEmailValid(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);

        char[] chars = email.toCharArray();
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}