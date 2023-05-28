package com.example.internetinisknygynas.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.activities.BookActivity;
import com.example.internetinisknygynas.models.Favorite;
import com.example.internetinisknygynas.utils.LocalStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private final Context context;
    private final List<Favorite> favoriteList;
    private LocalStorage localStorage;
    private static final String URL_DELETE_FAVORITE = "http://10.0.2.2/internetinis-knygynas-server/deleteFavorite.php";

    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_favorite, parent, false);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        localStorage = new LocalStorage(context);
//        Image
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.start();
        Glide.with(context)
                .load(favoriteList.get(position).getBookImage())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.imageBook);
//        Set text
        holder.bookTitle.setText(favoriteList.get(position).getBookTitle());
        holder.bookAuthor.setText(favoriteList.get(position).getBookAuthor());
        holder.bookPrice.setText(favoriteList.get(position).getBookPrice());

//        Remove favorite
        holder.imageHeart.setOnClickListener(view -> {
            String book = favoriteList.get(position).getBook_id();
            removeFavoriteFromDB(book);
        });

//        Open book info
        holder.constraintLayout.setOnClickListener(view -> {
            localStorage.setBookTitle(favoriteList.get(position).getBookTitle());
            localStorage.setBookAuthor(favoriteList.get(position).getBookAuthor());
            localStorage.setBookPrice(favoriteList.get(position).getBookPrice());
            localStorage.setBookNumberOfRatings(favoriteList.get(position).getNumberOfRatings());
            localStorage.setBookImage(favoriteList.get(position).getBookImage());
            localStorage.setBookAvailableQuantity(favoriteList.get(position).getAvailableQuantity());
            localStorage.setBookPublisher(favoriteList.get(position).getBookPublisher());
            localStorage.setBookYear(favoriteList.get(position).getBookYear());
            localStorage.setBookPages(favoriteList.get(position).getBookPages());
            localStorage.setBookISBN(favoriteList.get(position).getBookISBN());
            localStorage.setBookFormat(favoriteList.get(position).getBookFormat());
            localStorage.setBookLanguage(favoriteList.get(position).getBookLanguage());
            localStorage.setBookDescription(favoriteList.get(position).getBookDescription());
            localStorage.setBookRating(favoriteList.get(position).getBookRating());
            localStorage.setBookId(favoriteList.get(position).getId());
            context.startActivity(new Intent(context, BookActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    private void removeFavoriteFromDB(String book) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_FAVORITE, response -> {
            if (response.equals("success")) {
                System.out.println("removeFromDatabase success");
            } else if (response.equals("failure")) {
                Toast.makeText(context, "Įvyko klaida, bandykite dar kartą", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(context, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("book_id", book);
                data.put("user_id", localStorage.getCurrentUser());
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBook, imageHeart;
        TextView bookAuthor, bookTitle, bookPrice;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageBook = itemView.findViewById(R.id.BookImage);
            bookAuthor = itemView.findViewById(R.id.Author);
            bookTitle = itemView.findViewById(R.id.Title);
            bookPrice = itemView.findViewById(R.id.price);
            imageHeart = itemView.findViewById(R.id.imageView7);
            constraintLayout = itemView.findViewById(R.id.bookConstraint);
        }
    }
}
