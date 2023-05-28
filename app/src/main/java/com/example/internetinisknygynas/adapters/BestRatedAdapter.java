package com.example.internetinisknygynas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.models.Book;
import com.example.internetinisknygynas.utils.LocalStorage;

import java.util.List;

public class BestRatedAdapter extends RecyclerView.Adapter<BestRatedAdapter.ViewHolder>{
    private final Context context;
    private final List<Book> bestRatedList;
    private LocalStorage localStorage;

    public BestRatedAdapter(Context context, List<Book> bestRatedList) {
        this.context = context;
        this.bestRatedList = bestRatedList;
    }

    @NonNull
    @Override
    public BestRatedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_new, parent, false);
        return new BestRatedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestRatedAdapter.ViewHolder holder, int position) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.start();
        Glide.with(context)
                .load(bestRatedList.get(position).getImage())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return bestRatedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView15);
        }
    }
}
