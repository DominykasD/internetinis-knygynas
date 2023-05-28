package com.example.internetinisknygynas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.models.Cart;
import com.example.internetinisknygynas.models.Order;
import com.example.internetinisknygynas.models.UserRatedBook;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class OrderedAdapter extends RecyclerView.Adapter<OrderedAdapter.ViewHolder> {
    private final Context context;
    private final List<Cart> orderedList;
    private LocalStorage localStorage;

    private List<Cart> orders;

    public OrderedAdapter(Context context, List<Cart> orderedList) {
        this.context = context;
        this.orderedList = orderedList;
    }

    @NonNull
    @Override
    public OrderedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_ordered, parent, false);
        return new OrderedAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderedAdapter.ViewHolder holder, int position) {
        localStorage = new LocalStorage(context);
        holder.bookTitle.setText(orderedList.get(position).getTitle());
        holder.bookAuthor.setText(orderedList.get(position).getAuthor());
        holder.bookSubtotal.setText(String.valueOf(orderedList.get(position).getSubTotal()));
        holder.bookQuantity.setText(orderedList.get(position).getQuantity() + " vnt.");
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.start();

        Glide.with(context)
                .load(orderedList.get(position).getImage())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.bookIcon);


    }

    @Override
    public int getItemCount() {
        return orderedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle, bookAuthor, bookQuantity, bookSubtotal;
        ImageView  bookIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.textView60);
            bookAuthor = itemView.findViewById(R.id.textView61);
            bookSubtotal = itemView.findViewById(R.id.textView62);
            bookQuantity = itemView.findViewById(R.id.textView56);
            bookIcon = itemView.findViewById(R.id.imageView15);
        }
    }
}
