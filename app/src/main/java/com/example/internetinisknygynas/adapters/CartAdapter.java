package com.example.internetinisknygynas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.activities.CartActivity;
import com.example.internetinisknygynas.models.Cart;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private LocalStorage localStorage;
    private final Context context;
    private final List<Cart> cartList;
    private String subtotal;
    private Gson gson;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_checkout, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        localStorage = new LocalStorage(context);
        gson = new Gson();

        holder.bookTitle.setText(cartList.get(position).getTitle());
        holder.bookAuthor.setText(cartList.get(position).getAuthor());
        subtotal = String.format(Locale.US, "%.2f", Double.parseDouble(cartList.get(position).getPrice()) * Double.parseDouble(cartList.get(position).getQuantity()));
        holder.bookSubtotal.setText(subtotal);
        holder.bookQuantity.setText(cartList.get(position).getQuantity());
        holder.bookTotalLeft.setText("Likutis sandėlyje " + cartList.get(position).getAvailable_quantity() + " vnt.");
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.start();

        Glide.with(context)
                .load(cartList.get(position).getImage())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.bookIcon);

        holder.plusIcon.setOnClickListener(view -> {
            for (int i = 0;  i < cartList.size(); i++) {
                if(Objects.equals(cartList.get(i).getId(), cartList.get(position).getId())) {
                    holder.minusIcon.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(cartList.get(position).getQuantity());
                    if (count < Integer.parseInt(cartList.get(position).getAvailable_quantity())) {
                        count++;
                    } else {
                        Toast.makeText(context, "Viršijamas knygų likutis sandėlyje!", Toast.LENGTH_SHORT).show();
                    }
                    String totalQuantity = "" + count;
                    holder.bookQuantity.setText(totalQuantity);
                    cartList.get(position).setQuantity(totalQuantity);

                    subtotal = String.format(Locale.US, "%.2f", Double.parseDouble(cartList.get(position).getPrice()) * Double.parseDouble(cartList.get(position).getQuantity()));
                    holder.bookSubtotal.setText(subtotal);
                    cartList.get(position).setSubTotal(Double.parseDouble(subtotal));

                    String cartString = gson.toJson(cartList);
                    localStorage.setCart(cartString);
                    ((CartActivity) context).updateOrderPrice();
                }
            }
        });

        holder.minusIcon.setOnClickListener(view -> {
            for (int i = 0;  i < cartList.size(); i++) {
                if(Objects.equals(cartList.get(i).getId(), cartList.get(position).getId())) {
                    int count = Integer.parseInt(cartList.get(position).getQuantity());
                    if (count <= 1) {
                        count = 1;
                    } else count--;
                    String totalQuantity = "" + count;
                    holder.bookQuantity.setText(totalQuantity);
                    cartList.get(position).setQuantity(totalQuantity);

                    subtotal = String.format(Locale.US, "%.2f", Double.parseDouble(cartList.get(position).getPrice()) * Double.parseDouble(cartList.get(position).getQuantity()));
                    holder.bookSubtotal.setText(subtotal);
                    cartList.get(position).setSubTotal(Double.parseDouble(subtotal));

                    String cartString = gson.toJson(cartList);
                    localStorage.setCart(cartString);
                    ((CartActivity) context).updateOrderPrice();
                }
            }
        });

        holder.deleteIcon.setOnClickListener(view -> {
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
            Gson gson1 = new Gson();
            String cartStr = gson1.toJson(cartList);
            localStorage.setCart(cartStr);
            ((CartActivity) context).updateOrderPrice();
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle, bookAuthor, bookQuantity, bookSubtotal, bookTotalLeft;
        ImageView plusIcon, minusIcon, deleteIcon, bookIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.textView60);
            bookAuthor = itemView.findViewById(R.id.textView61);
            bookSubtotal = itemView.findViewById(R.id.textView62);
            bookQuantity = itemView.findViewById(R.id.textView63);
            plusIcon = itemView.findViewById(R.id.imageView40);
            minusIcon = itemView.findViewById(R.id.imageView41);
            bookTotalLeft = itemView.findViewById(R.id.textView128);
            deleteIcon = itemView.findViewById(R.id.imageView8);
            bookIcon = itemView.findViewById(R.id.imageView15);
        }
    }
}
