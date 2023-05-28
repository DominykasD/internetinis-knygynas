package com.example.internetinisknygynas.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internetinisknygynas.activities.OrderStatusActivity;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.models.Order;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.google.gson.Gson;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private final Context context;
    private List<Order> orderList;
    private LocalStorage localStorage;
    private Gson gson;

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_orders, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        localStorage = new LocalStorage(context);
        gson = new Gson();
        holder.orderNumber.setText(orderList.get(position).getOrderNumber());
        holder.orderDate.setText(orderList.get(position).getDate());
        holder.orderStatus.setText(orderList.get(position).getStatus());
        holder.orderPlaciau.setOnClickListener(view -> {
            localStorage.setOrderNumber(orderList.get(position).getOrderNumber());
            localStorage.setOrderDate(orderList.get(position).getDate());
            localStorage.setLocationAddress(orderList.get(position).getAddress());
            localStorage.setLocationCity(orderList.get(position).getCity());
            localStorage.setClickedPlačiau("TRUE");
            localStorage.setOrdered(orderList.get(position).getCart());
            context.startActivity(new Intent(context, OrderStatusActivity.class));
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber, orderDate, orderStatus, orderPlaciau;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderNumber = itemView.findViewById(R.id.textView107);
            orderDate = itemView.findViewById(R.id.textView108);
            orderStatus = itemView.findViewById(R.id.textView109);
            orderPlaciau = itemView.findViewById(R.id.textView110);
        }
    }
}
