package com.example.internetinisknygynas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;
import com.stripe.android.payments.paymentlauncher.PaymentResult;
import com.stripe.android.view.CardInputWidget;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentCardActivity extends AppCompatActivity {
    private String paymentIntentClientSecret;
    private PaymentLauncher paymentLauncher;
    private final String URL = "http://10.0.2.2/internetinis-knygynas-server/order.php";
    private LocalStorage localStorage;
    private String formattedDate;
    private TextView orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_card);
        localStorage = new LocalStorage(this);

        Date date1 = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = dateFormat.format(date1);
        localStorage.setOrderDate(formattedDate);

        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51MW1hABfI9aZCD1p9cx6EUYFJqV7JenmRWE2TO1yH3mqq6wgrbU2kO6i75OWBGXeKgBpSnFVutoET0PyCgPrERjG00HWt4pxA3"
        );

        final PaymentConfiguration paymentConfiguration
                = PaymentConfiguration.getInstance(getApplicationContext());
        paymentLauncher = PaymentLauncher.Companion.create(
                this,
                paymentConfiguration.getPublishableKey(),
                paymentConfiguration.getStripeAccountId(),
                this::onPaymentResult
        );
        startCheckout();


    }

    private void startCheckout() {
        // ...

        // Hook up the pay button to the card widget
        final Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
//            final CardInputWidget cardInputWidget = view.findViewById(R.id.cardInputWidget);
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            final PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

            if (params != null) {
                String publishableKey = "pk_test_51MW1hABfI9aZCD1p9cx6EUYFJqV7JenmRWE2TO1yH3mqq6wgrbU2kO6i75OWBGXeKgBpSnFVutoET0PyCgPrERjG00HWt4pxA3";
                Stripe stripe = new Stripe(this, publishableKey);
                stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                    @Override
                    public void onSuccess(PaymentMethod paymentMethod) {
                        payment();
                    }
                    @Override
                    public void onError(@NotNull Exception e) {
                        // Error occurred during payment method creation
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Užpildykite kortelės duomenis", Toast.LENGTH_SHORT).show();
            }

//            if (params != null) {
//                final ConfirmPaymentIntentParams confirmParams =
//                        ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
//                                params,
//                                paymentIntentClientSecret
//                        );
//                paymentLauncher.confirm(confirmParams);
//
//
//            } else {
//                Toast.makeText(this, "Įveskite kortelės duomenis", Toast.LENGTH_SHORT).show();
//            }
        });
    }

    private void payment() {
        PaymentCardActivity.this.showCustomDialogPayment();
    }

    private void showCustomDialogPayment() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payment);

        new Handler().postDelayed(() -> {
            dialog.dismiss();
            PaymentCardActivity.this.showCustomDialog();
            PaymentCardActivity.this.postDataToDatabase(formattedDate);
            PaymentCardActivity.this.localStorage.setOrdered(localStorage.getCart());

        }, 5000);


        dialog.show();
    }

    private void showCustomDialog() {
        Dialog dialog2 = new Dialog(this);

        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_confirmation);

        orderNumber = dialog2.findViewById(R.id.textView90);

        TextView closeButton = dialog2.findViewById(R.id.textView91);
        Button button = dialog2.findViewById(R.id.button7);

        closeButton.setOnClickListener(view -> {
            dialog2.dismiss();
            startActivity(new Intent(this, MainMenuActivity.class));
        });

        button.setOnClickListener(view -> {
            dialog2.dismiss();
            startActivity(new Intent(this, OrderStatusActivity.class));
        });


        Date date1 = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = dateFormat.format(date1);
        localStorage.setOrderDate(formattedDate);

        orderNumber.setText("#" + generateOrderNumber());
        localStorage.setOrderNumber(orderNumber.getText().toString().trim());

        dialog2.show();
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


    private void onPaymentResult(PaymentResult paymentResult) {
        String message = "";
        if (paymentResult instanceof PaymentResult.Completed) {
            message = "Completed!";
        } else if (paymentResult instanceof PaymentResult.Canceled) {
            message = "Canceled!";
        } else if (paymentResult instanceof PaymentResult.Failed) {
            // This string comes from the PaymentIntent's error message.
            // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
            message = "Failed: "
                    + ((PaymentResult.Failed) paymentResult).getThrowable().getMessage();
        }

        Toast.makeText(this, "PaymentResult: " + message, Toast.LENGTH_SHORT).show();
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