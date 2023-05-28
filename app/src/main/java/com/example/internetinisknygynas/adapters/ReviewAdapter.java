package com.example.internetinisknygynas.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.activities.RatingUpdateActivity;
import com.example.internetinisknygynas.activities.BookActivity;
import com.example.internetinisknygynas.models.Review;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private LocalStorage localStorage;
    private final Context context;
    private final List<Review> reviewList;
    private final String URLDELETEREVIEW = "http://10.0.2.2/internetinis-knygynas-server/deleteReview.php";

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_review, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        localStorage = new LocalStorage(context);

        holder.ratingBar.setRating(Float.parseFloat(reviewList.get(position).getRating()));
        holder.user.setText(reviewList.get(position).getUser());
        holder.date.setText(reviewList.get(position).getCreatedAt());
        holder.comment.setText(reviewList.get(position).getComment());
        if(!Objects.equals(reviewList.get(position).getUser_id(), localStorage.getCurrentUser()) || Objects.equals(localStorage.getCurrentUser(), "1")) {
            holder.constraintEdit.setVisibility(View.GONE);
            holder.comment.setPadding(0,0,0, 16);
        }
        holder.edit.setOnClickListener(view -> {
            localStorage.setReviewRating(reviewList.get(position).getRating());
            localStorage.setReviewComment(reviewList.get(position).getComment());
            localStorage.setReviewBookId(reviewList.get(position).getId());
            context.startActivity(new Intent(context, RatingUpdateActivity.class));
        });
        holder.delete.setOnClickListener(view -> {
            AlertDialog alertDialog = showConfirmDialog(reviewList.get(position).getId(), position);
            alertDialog.show();
        });
    }

    private AlertDialog showConfirmDialog(String id, int position) {
        AlertDialog confirmationDialog = new AlertDialog.Builder(context)
                .setTitle("Atsiliepimo ištrinimo patvirtinimas")
                .setMessage("Ar tikrai norite ištrinti atsiliepimą?")
                .setPositiveButton("Taip", (dialog, which) -> {
                    deleteReviewFromDb(id);
                    Toast.makeText(context, "Atsiliepimas sėkmingai ištrintas", Toast.LENGTH_SHORT).show();
                    reviewList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, reviewList.size());
                    Gson gson1 = new Gson();
                    String ratingStr = gson1.toJson(reviewList);
                    localStorage.setUserRatedBooks(ratingStr);
                    dialog.dismiss();
                    ((BookActivity)context).finish();
                    context.startActivity(new Intent(context, BookActivity.class));
                })
                .setNegativeButton("Ne", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
        return  confirmationDialog;
    }

    private void deleteReviewFromDb(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDELETEREVIEW, response -> {
            if (response.equals("success")) {

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
                data.put("id", id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView user, date, comment, edit, delete;
        ConstraintLayout constraintEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            user = itemView.findViewById(R.id.textView25);
            date = itemView.findViewById(R.id.textView33);
            comment = itemView.findViewById(R.id.textView47);
            edit = itemView.findViewById(R.id.textView48);
            delete = itemView.findViewById(R.id.textView57);
            constraintEdit = itemView.findViewById(R.id.constraintEdit);
        }
    }
}
