package com.android.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Payment extends AppCompatActivity implements PaymentResultListener {

    private TextView txtpaymentstatus;
    private Button btnpaynow;
    private EditText editAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        txtpaymentstatus = findViewById(R.id.paymentStatus);
        editAmount = findViewById(R.id.amount);
        btnpaynow = findViewById(R.id.pay_button);

        Checkout.preload(Payment.this);

        btnpaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment(Integer.parseInt(editAmount.getText().toString()));
            }
        });
    }

    public void startPayment(int Amount){
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_uDyxnT2mA1Wctb");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "HOSTEL_BITES_PAYMENT GATEWAY");
            jsonObject.put("description","Enter the Amount to complete payment");
            // Remove or replace this line with a valid image URL
            // jsonObject.put("image", "your_image_url");
            jsonObject.put("theme.color","#FF9D01");
            jsonObject.put("currency","INR");
            jsonObject.put("amount",Amount*100);

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled","true");
            retryObj.put("max_count",4);

            jsonObject.put("retry",retryObj);

            checkout.open(Payment.this,jsonObject);
        }catch (Exception e){
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        txtpaymentstatus.setText(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        txtpaymentstatus.setText("Error:"+s);
    }
}