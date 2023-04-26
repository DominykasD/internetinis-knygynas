package com.example.internetinisknygynas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private final Context context;
    private final List<Book> books;

    public BookListAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }


    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_book, parent, false);
        return new BookListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.ViewHolder holder, int position) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.start();

        Glide.with(context)
                .load(books.get(position).getImage())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.image);
        holder.title.setText(books.get(position).getTitle());
        holder.author.setText(books.get(position).getAuthor());
        holder.price.setText(books.get(position).getPrice());
        holder.status.setText(books.get(position).getStatus());
        holder.rating.setRating(Float.parseFloat(books.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, author, price, status;
        RatingBar rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView19);
            title = itemView.findViewById(R.id.textView16);
            author = itemView.findViewById(R.id.textView6);
            price = itemView.findViewById(R.id.textView7);
            status = itemView.findViewById(R.id.textView8);
            rating = itemView.findViewById(R.id.ratingBar);
        }
    }
}
