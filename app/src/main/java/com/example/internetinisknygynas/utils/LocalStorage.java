package com.example.internetinisknygynas.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.internetinisknygynas.models.Cart;
import com.example.internetinisknygynas.models.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalStorage extends AppCompatActivity {
    private static final String BOOKDESCRIPTION = "BOOKDESCRIPTION";
    private static final String USER = "USER";
    private static final String FULLNAME = "FULLNAME";
    private static final String USEREMAIL = "USEREMAIL";
    private static final String BOOKTITLE = "BOOKTITLE";
    private static final String BOOKAUTHOR = "BOOKAUTHOR";
    private static final String BOOKPRICE = "BOOKPRICE";
    private static final String BOOKNUMBEROFRATINGS = "BOOKNUMBEROFRATINGS";
    private static final String BOOKIMAGE = "BOOKIMAGE";
    private static final String BOOKAVAILABLEQUANTITY = "BOOKAVAILABLEQUANTITY";
    private static final String BOOKPUBLISHER = "BOOKPUBLISHER";
    private static final String BOOKYEAR = "BOOKYEAR";
    private static final String BOOKPAGES = "BOOKPAGES";
    private static final String BOOKISBN = "BOOKISBN";
    private static final String BOOKFORMAT = "BOOKFORMAT";
    private static final String BOOKLANGUAGE = "BOOKLANGUAGE";
    private static final String BOOKRATING = "BOOKRATING";
    private static final String BOOKUSERRATING = "BOOKUSERRATING";
    private static final String BOOKID = "BOOKID";
    private static final String USERRATEDBOOKS = "USERRATEDBOOKS";
    private static final String REVIEWRATING = "REVIEWRATING";
    private static final String REVIEWCOMMENT = "REVIEWCOMMENT";
    private static final String REVIEWID = "REVIEWID";
    private static final String CART = "CART";
    private static final String LOCATIONADRESS = "LOCATIONADRESS";
    private static final String LOCATIONNUMBER = "LOCATIONNUMBER";
    private static final String LOCATIONCITY = "LOCATIONCITY";
    private static final String PAYMENTMETHOD = "PAYMENTMETHOD";
    private static final String ORDERNUMBER = "ORDERNUMBER";
    private static final String ORDERDATE = "ORDERDATE";
    private static final String PLACIAU = "PLACIAU";
    private static final String NORAI = "NORAI";
    private static final String POSITION = "POSITION";
    private static final String FAVORITESS = "FAVORITESS";
    private static final String ORDEREDCART = "setOrderedCart";


    private final SharedPreferences sharedPreferences;

    private List<Cart> cartList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();
    private Gson gson;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("Preferences", 0);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public int cartCount() {

        gson = new Gson();
        if (getCart() != null) {
            String jsonCart = getCart();
            Type type = new TypeToken<List<Cart>>() {
            }.getType();
            cartList = gson.fromJson(jsonCart, type);
            return cartList.size();
        }
        return 0;
    }

    public List<Cart> getCartList() {
        gson = new Gson();
        if(getCart() != null) {
            String jsonCart = getCart();
            Type type = new TypeToken<List<Cart>>() {}.getType();
            cartList = gson.fromJson(jsonCart, type);
            return cartList;
        }
        return cartList;
    }



    public List<Order> getOrderedList() {
        gson = new Gson();
        if(getOrdered() != null) {
            String jsonCart = getOrdered();
            Type type = new TypeToken<List<Order>>() {}.getType();
            orderList = gson.fromJson(jsonCart, type);
            return orderList;
        }
        return orderList;
    }

    public Double getTotalPrice() {
        cartList = getCartList();
        double total = 0.0;
        if (cartCount() > 0) {
            for (int i = 0; i < cartList.size(); i++) {
                total = total + (double) cartList.get(i).getSubTotal();
            }
            return total;
        }
        return total;
    }

    public String getCartSize() {
        cartList = getCartList();
        int total = 0;
        if (cartCount() > 0) {
            for (int i = 0; i < cartList.size(); i++) {
                total = total +  Integer.parseInt(cartList.get(i).getQuantity());
            }
            return String.valueOf(total);
        }
        return String.valueOf(total);
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

// dfffffffffffffff
    public void setBookTitle(String title) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKTITLE, title);
        editor.apply();
    }

    public String getBookTitle() {
        if(sharedPreferences.contains(BOOKTITLE))
            return sharedPreferences.getString(BOOKTITLE, null);
        else return null;
    }

    public void setBookAuthor(String author) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKAUTHOR, author);
        editor.apply();
    }

    public String getBookAuthor() {
        if(sharedPreferences.contains(BOOKAUTHOR))
            return sharedPreferences.getString(BOOKAUTHOR, null);
        else return null;
    }

    public void setBookPrice(String price) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKPRICE, price);
        editor.apply();
    }

    public String getBookPrice() {
        if(sharedPreferences.contains(BOOKPRICE))
            return sharedPreferences.getString(BOOKPRICE, null);
        else return null;
    }

    public void setBookNumberOfRatings(String number_of_ratings) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKNUMBEROFRATINGS, number_of_ratings);
        editor.apply();
    }

    public String getBookNumberOfRatings() {
        if(sharedPreferences.contains(BOOKNUMBEROFRATINGS))
            return sharedPreferences.getString(BOOKNUMBEROFRATINGS, null);
        else return null;
    }

    public void setBookImage(String image) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKIMAGE, image);
        editor.apply();
    }

    public String getBookImage() {
        if(sharedPreferences.contains(BOOKIMAGE))
            return sharedPreferences.getString(BOOKIMAGE, null);
        else return null;
    }

    public void setBookAvailableQuantity(String available_quantity) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKAVAILABLEQUANTITY, available_quantity);
        editor.apply();
    }

    public String getBookAvailableQuantity() {
        if(sharedPreferences.contains(BOOKAVAILABLEQUANTITY))
            return sharedPreferences.getString(BOOKAVAILABLEQUANTITY, null);
        else return null;
    }

    public void setBookPublisher(String publisher) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKPUBLISHER, publisher);
        editor.apply();
    }

    public String getBookPublisher() {
        if(sharedPreferences.contains(BOOKPUBLISHER))
            return sharedPreferences.getString(BOOKPUBLISHER, null);
        else return null;
    }

    public void setBookYear(String year) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKYEAR, year);
        editor.apply();
    }

    public String getBookYear() {
        if(sharedPreferences.contains(BOOKYEAR))
            return sharedPreferences.getString(BOOKYEAR, null);
        else return null;
    }

    public void setBookPages(String pages) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKPAGES, pages);
        editor.apply();
    }

    public String getBookPages() {
        if(sharedPreferences.contains(BOOKPAGES))
            return sharedPreferences.getString(BOOKPAGES, null);
        else return null;
    }

    public void setBookISBN(String isbn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKISBN, isbn);
        editor.apply();
    }

    public String getBookISBN() {
        if(sharedPreferences.contains(BOOKISBN))
            return sharedPreferences.getString(BOOKISBN, null);
        else return null;
    }

    public void setBookFormat(String format) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKFORMAT, format);
        editor.apply();
    }

    public String getBookFormat() {
        if(sharedPreferences.contains(BOOKFORMAT))
            return sharedPreferences.getString(BOOKFORMAT, null);
        else return null;
    }

    public void setBookLanguage(String language) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKLANGUAGE, language);
        editor.apply();
    }

    public String getBookLanguage() {
        if(sharedPreferences.contains(BOOKLANGUAGE))
            return sharedPreferences.getString(BOOKLANGUAGE, null);
        else return null;
    }

    public void setBookDescription(String description) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKDESCRIPTION, description);
        editor.apply();
    }

    public String getBookDescription() {
        if(sharedPreferences.contains(BOOKDESCRIPTION))
            return sharedPreferences.getString(BOOKDESCRIPTION, null);
        else return null;
    }

    public void setBookRating(String rating) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKRATING, rating);
        editor.apply();
    }

    public String getBookRating() {
        if(sharedPreferences.contains(BOOKRATING))
            return sharedPreferences.getString(BOOKRATING, null);
        else return null;
    }

    public void setBookUserRating(String rating) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKUSERRATING, rating);
        editor.apply();
    }

    public String getBookUserRating() {
        if(sharedPreferences.contains(BOOKUSERRATING))
            return sharedPreferences.getString(BOOKUSERRATING, null);
        else return null;
    }

    public void setBookId(String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKID, id);
        editor.apply();
    }

    public String getBookId() {
        if(sharedPreferences.contains(BOOKID))
            return sharedPreferences.getString(BOOKID, null);
        else return null;
    }

    public void setUserRatedBooks(String ratedBooksString) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERRATEDBOOKS, ratedBooksString);
        editor.apply();
    }

    public String getUserRatedBooks() {
        if(sharedPreferences.contains(USERRATEDBOOKS))
            return sharedPreferences.getString(USERRATEDBOOKS, null);
        else return null;
    }

    public void deleteUserRatedBooks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USERRATEDBOOKS);
        editor.apply();
    }

    public void setReviewRating(String rating) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REVIEWRATING, rating);
        editor.apply();
    }

    public String getReviewRating() {
        if(sharedPreferences.contains(REVIEWRATING))
            return sharedPreferences.getString(REVIEWRATING, null);
        else return null;
    }

    public void setReviewComment(String comment) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REVIEWCOMMENT, comment);
        editor.apply();
    }

    public String getReviewComment() {
        if(sharedPreferences.contains(REVIEWCOMMENT))
            return sharedPreferences.getString(REVIEWCOMMENT, null);
        else return null;
    }

    public void setReviewBookId(String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REVIEWID, id);
        editor.apply();
    }

    public String getReviewBookId() {
        if(sharedPreferences.contains(REVIEWID))
            return sharedPreferences.getString(REVIEWID, null);
        else return null;
    }

    public void setCart(String cartString) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CART, cartString);
        editor.apply();
    }

    public String getCart() {
        if(sharedPreferences.contains(CART))
            return sharedPreferences.getString(CART, null);
        else return null;
    }

    public void deleteCart() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART);
        editor.apply();
    }

    public void setLocationAddress(String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOCATIONADRESS, address);
        editor.apply();
    }

    public String getLocationAddress() {
        if(sharedPreferences.contains(LOCATIONADRESS))
            return sharedPreferences.getString(LOCATIONADRESS, null);
        else return null;
    }

    public void setLocationNumber(String number) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOCATIONNUMBER, number);
        editor.apply();
    }

    public String getLocationNumber() {
        if(sharedPreferences.contains(LOCATIONNUMBER))
            return sharedPreferences.getString(LOCATIONNUMBER, null);
        else return null;
    }

    public void setLocationCity(String city) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOCATIONCITY, city);
        editor.apply();
    }

    public String getLocationCity() {
        if(sharedPreferences.contains(LOCATIONCITY))
            return sharedPreferences.getString(LOCATIONCITY, null);
        else return null;
    }

    public void setPaymentMethod(String payment) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PAYMENTMETHOD, payment);
        editor.apply();
    }

    public String getPaymentMethod() {
        if(sharedPreferences.contains(PAYMENTMETHOD))
            return sharedPreferences.getString(PAYMENTMETHOD, null);
        else return null;
    }

    public void setOrderNumber(String orderNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ORDERNUMBER, orderNumber);
        editor.apply();
    }

    public String getOrderNumber() {
        if(sharedPreferences.contains(ORDERNUMBER))
            return sharedPreferences.getString(ORDERNUMBER, null);
        else return null;
    }

    public void setOrderDate(String formattedDate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ORDERDATE, formattedDate);
        editor.apply();
    }

    public String getOrderDate() {
        if(sharedPreferences.contains(ORDERDATE))
            return sharedPreferences.getString(ORDERDATE, null);
        else return null;
    }

    public void setClickedPlačiau(String b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PLACIAU, b);
        editor.apply();
    }

    public String getClickedPlačiau() {
        if(sharedPreferences.contains(PLACIAU))
            return sharedPreferences.getString(PLACIAU, null);
        else return null;
    }

    public void setClickedNorai(String norai) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NORAI, norai);
        editor.apply();
    }

    public String getClickedNorai() {
        if(sharedPreferences.contains(NORAI))
            return sharedPreferences.getString(NORAI, null);
        else return null;
    }

    public void setLastCliclkedAddressPostion(String position) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(POSITION, position);
        editor.apply();
    }

    public String getLastCliclkedAddressPostion() {
        if(sharedPreferences.contains(POSITION))
            return sharedPreferences.getString(POSITION, null);
        else return null;
    }

    public void setFavorites(String favoritesString) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FAVORITESS, favoritesString);
        editor.apply();
    }

    public String getFavorites() {
        if(sharedPreferences.contains(FAVORITESS))
            return sharedPreferences.getString(FAVORITESS, null);
        else return null;
    }

    public void deleteFavorites() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(FAVORITESS);
        editor.apply();
    }


    public void setOrdered(String orderedCart) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ORDEREDCART, orderedCart);
        editor.apply();
    }


    public String getOrdered() {
        if(sharedPreferences.contains(ORDEREDCART))
            return sharedPreferences.getString(ORDEREDCART, null);
        else return null;
    }
}
