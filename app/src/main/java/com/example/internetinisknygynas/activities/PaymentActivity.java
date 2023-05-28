package com.example.internetinisknygynas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.utils.LocalStorage;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Address;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.order.Shipping;
import com.paypal.checkout.paymentbutton.PayPalButton;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {
    private TextView orderNumber;
    private final String URL = "http://10.0.2.2/internetinis-knygynas-server/order.php";
    private LocalStorage localStorage;
    private String formattedDate;
    private PayPalButton payPalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        localStorage = new LocalStorage(this);

        Date date1 = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = dateFormat.format(date1);
        localStorage.setOrderDate(formattedDate);



        payPalButton = findViewById(R.id.paypalButton);
        payPalButton.setup(
                new CreateOrder() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void create(@NonNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        Address address = new Address("Gatvė: " + localStorage.getLocationAddress(), "Namo nr: " + localStorage.getLocationNumber(), null, localStorage.getLocationCity(), null, "LT");
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.EUR)
                                                        .value(String.format(Locale.US,"%.2f", localStorage.getTotalPrice()))
                                                        .build()
                                        )
                                        .shipping(
                                                new Shipping()
                                                        .copy(address, null)
                                        )
                                        .build()
                        );
                        Order order = new Order(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NonNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                showCustomDialog();
                                postDataToDatabase(formattedDate);
                                localStorage.setOrdered(localStorage.getCart());
                            }
                        });
                    }
                }
        );
    }

    private void postDataToDatabase(String formattedDate) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if (response.equals("success")) {
                localStorage.deleteCart();
            } else if (response.equals("failure")) {
                Toast.makeText(getApplicationContext(), "Įvyko klaida, bandykite dar kartą", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("user_id", localStorage.getCurrentUser());
                data.put("orderNumber", orderNumber.getText().toString().trim());
                data.put("date", formattedDate);
                data.put("status", "Ruošiama");
                data.put("cart", localStorage.getCart());
                data.put("street", localStorage.getLocationAddress());
                data.put("street_number", localStorage.getLocationNumber());
                data.put("city", localStorage.getLocationCity());
                data.put("payment", localStorage.getPaymentMethod());
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirmation);

        orderNumber = dialog.findViewById(R.id.textView90);

        TextView closeButton = dialog.findViewById(R.id.textView91);
        Button button = dialog.findViewById(R.id.button7);

        closeButton.setOnClickListener(view -> {
            startActivity(new Intent(this, MainMenuActivity.class));
        });

        button.setOnClickListener(view -> {
            startActivity(new Intent(this, OrderStatusActivity.class));
        });


        Date date1 = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = dateFormat.format(date1);
        localStorage.setOrderDate(formattedDate);

        orderNumber.setText("#" + generateOrderNumber());
        localStorage.setOrderNumber(orderNumber.getText().toString().trim());

        dialog.show();
    }

    private String generateOrderNumber() {
        final String characters = "abcdefghijklmnopqrstuvwxyz123456789";
        StringBuilder result = new StringBuilder();
        int count = 0;
        while (count < 6) {
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            count++;
        }
        return result.toString();
    }

}