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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.activities.BookActivity;
import com.example.internetinisknygynas.models.Book;
import com.example.internetinisknygynas.utils.LocalStorage;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private final Context context;
    private final List<Book> searchList;
    private LocalStorage localStorage;
    // filter
    private final List<Book> filterList;

    public SearchAdapter(Context context, List<Book> searchList) {
        this.context = context;
        this.searchList = searchList;
        filterList = new ArrayList<>(searchList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_search_result, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        localStorage = new LocalStorage(context);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.start();

        Glide.with(context)
                .load(searchList.get(position).getImage())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.bookImage);
        holder.Title.setText(searchList.get(position).getTitle());
        holder.Author.setText(searchList.get(position).getAuthor());
        holder.price.setText(searchList.get(position).getPrice() + " €");

        holder.bookConstraint.setOnClickListener(view -> {
            localStorage.setBookTitle(searchList.get(position).getTitle());
            localStorage.setBookAuthor(searchList.get(position).getAuthor());
            localStorage.setBookPrice(searchList.get(position).getPrice());
            localStorage.setBookNumberOfRatings(searchList.get(position).getNumber_of_ratings());
            localStorage.setBookImage(searchList.get(position).getImage());
            localStorage.setBookAvailableQuantity(searchList.get(position).getAvailable_quantity());
            localStorage.setBookPublisher(searchList.get(position).getPublisher());
            localStorage.setBookYear(searchList.get(position).getYear());
            localStorage.setBookPages(searchList.get(position).getPages());
            localStorage.setBookISBN(searchList.get(position).getISBN());
            localStorage.setBookFormat(searchList.get(position).getFormat());
            localStorage.setBookLanguage(searchList.get(position).getLanguage());
            localStorage.setBookDescription(searchList.get(position).getDescription());
            localStorage.setBookRating(searchList.get(position).getRating());
            localStorage.setBookId(searchList.get(position).getId());
            context.startActivity(new Intent(context, BookActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
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
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Book book : filterList) {
                    if(book.getTitle().toLowerCase().contains(filterPattern)) {
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
            searchList.clear();
            searchList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView Title, Author, price;
        ConstraintLayout bookConstraint;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.BookImage);
            Title = itemView.findViewById(R.id.Title);
            Author = itemView.findViewById(R.id.Author);
            price = itemView.findViewById(R.id.price);
            bookConstraint = itemView.findViewById(R.id.bookConstraint);
        }
    }
}
