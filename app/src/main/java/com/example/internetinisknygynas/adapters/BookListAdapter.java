package com.example.internetinisknygynas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.example.internetinisknygynas.utils.SortOption;
import com.example.internetinisknygynas.activities.BookActivity;
import com.example.internetinisknygynas.models.Book;
import com.example.internetinisknygynas.models.Review;
import com.example.internetinisknygynas.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> implements Filterable {
    private final Context context;
    private final List<Book> books;
    private LocalStorage localStorage;
    private final String URLGETREVIEWS = "http://10.0.2.2/internetinis-knygynas-server/getReviewList.php";
    private static final List<Review> reviewList = new ArrayList<>();
    private final List<Book> filterList;
    private SortOption currentSortOption;

    public BookListAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
        filterList = new ArrayList<>(books);
    }


    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_book, parent, false);
        return new BookListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.ViewHolder holder, int position) {
        localStorage = new LocalStorage(context);


        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.start();

        Glide.with(context)
                .load(books.get(position).getImage())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.image);
        holder.title.setText(books.get(position).getTitle());
        holder.author.setText(books.get(position).getAuthor());
        holder.price.setText(books.get(position).getPrice() + "€");
        holder.status.setText(books.get(position).getStatus());

        getBookRatings(holder, position);

        holder.image.setOnClickListener(view -> {
            localStorage.setBookTitle(books.get(position).getTitle());
            localStorage.setBookAuthor(books.get(position).getAuthor());
            localStorage.setBookPrice(books.get(position).getPrice());
            localStorage.setBookNumberOfRatings(books.get(position).getNumber_of_ratings());
            localStorage.setBookImage(books.get(position).getImage());
            localStorage.setBookAvailableQuantity(books.get(position).getAvailable_quantity());
            localStorage.setBookPublisher(books.get(position).getPublisher());
            localStorage.setBookYear(books.get(position).getYear());
            localStorage.setBookPages(books.get(position).getPages());
            localStorage.setBookISBN(books.get(position).getISBN());
            localStorage.setBookFormat(books.get(position).getFormat());
            localStorage.setBookLanguage(books.get(position).getLanguage());
            localStorage.setBookDescription(books.get(position).getDescription());
            localStorage.setBookRating(books.get(position).getRating());
            localStorage.setBookId(books.get(position).getId());
            context.startActivity(new Intent(context, BookActivity.class));
        });

    }

    private void getBookRatings(ViewHolder holder, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETREVIEWS, response -> {
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
                    reviewList.add(review);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!reviewList.isEmpty()) {
                for (int i = 0; i < reviewList.size(); i++) {
                    if (Objects.equals(reviewList.get(i).getBook_id(), books.get(position).getId())) {
                        holder.rating.setRating(Float.parseFloat(reviewList.get(i).getRating()));
                        books.get(position).setRating(reviewList.get(i).getRating());
                    } else {
                        holder.rating.setRating(0);
                    }
                }
            } else {
                holder.rating.setRating(0);
            }

        }, error -> Toast.makeText(context, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public Filter getFilter() {
        return resFilter;
    }

    private Filter resFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Book> filteredList = new ArrayList<>();
            if( charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(filterList);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Book book : filterList) {
                    if(book.getCategory().toLowerCase().contains(filterPattern) || book.getStatus().toLowerCase().contains(filterPattern) || book.getPublisher().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            books.clear();
            books.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public void setSortOption(SortOption sortOption) {
        currentSortOption = sortOption;
        sortItemList(); // Call the method to sort the dataset based on the new sort option
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }

    private void sortItemList() {
        switch (currentSortOption) {
            case BEST_RATING:
                Collections.sort(books, (item1, item2) ->
                        Float.compare(Float.parseFloat(item2.getRating()), Float.parseFloat(item1.getRating())));
                break;
            case MOST_EXPENSIVE:
                Collections.sort(books, (item1, item2) ->
                        Float.compare(Float.parseFloat(item2.getPrice()), Float.parseFloat(item1.getPrice())));
                break;
            case LEAST_EXPENSIVE:
                Collections.sort(books, (item1, item2) ->
                        Float.compare(Float.parseFloat(item1.getPrice()), Float.parseFloat(item2.getPrice())));
                break;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, author, price, status;
        RatingBar rating;
        ConstraintLayout constraintBook;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView19);
            title = itemView.findViewById(R.id.textView16);
            author = itemView.findViewById(R.id.textView6);
            price = itemView.findViewById(R.id.textView7);
            status = itemView.findViewById(R.id.textView8);
            rating = itemView.findViewById(R.id.ratingBar);
            constraintBook = itemView.findViewById(R.id.constraintBook);
        }
    }
}
