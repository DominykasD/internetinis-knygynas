package com.example.internetinisknygynas.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class LocalStorage extends AppCompatActivity {
    private final SharedPreferences sharedPreferences;

    private static final String USER = "USER";
    private static final String FULLNAME = "FULLNAME";
    private static final String USEREMAIL = "USEREMAIL";

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("Preferences", 0);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void setCurrentUser(String user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER, user);
        editor.apply();
    }

    public String getCurrentUser() {
        if(sharedPreferences.contains(USER))
            return sharedPreferences.getString(USER, null);
        else return null;
    }

    public void deleteCurrentUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER);
        editor.remove(FULLNAME);
        editor.remove(USEREMAIL);
        editor.apply();
    }

    public void setCurrentUserFullName(String fullName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FULLNAME, fullName);
        editor.apply();
    }

    public String getCurrentUserFullName() {
        if(sharedPreferences.contains(FULLNAME))
            return sharedPreferences.getString(FULLNAME, null);
        else return null;
    }

    public void setCurrentUserEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USEREMAIL, email);
        editor.apply();
    }

    public String getCurrentUserEmail() {
        if(sharedPreferences.contains(USEREMAIL))
            return sharedPreferences.getString(USEREMAIL, null);
        else return null;
    }
}
