package com.android.foodorderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.foodorderapp.adapters.PlaceYourOrderAdapter;
import com.android.foodorderapp.model.Menu;
import com.android.foodorderapp.model.RestaurantModel;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class PlaceYourOrderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaymentResultListener {

    private EditText inputName, inputAddress, inputCity, inputZip;
    private RecyclerView cartItemsRecyclerView;
    private TextView tvSubtotalAmount, tvDeliveryChargeAmount, tvDeliveryCharge, tvTotalAmount, buttonPlaceYourOrder;
    private SwitchCompat switchDelivery;
    private boolean isDeliveryOn;
    private PlaceYourOrderAdapter placeYourOrderAdapter;
    private RestaurantModel restaurantModel; // Declaring restaurantModel here
    private RadioGroup hostelRadioGroup;
    private RadioGroup floorRadioGroup;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private TextView selectfloor;
    private TextView selecthostel;
    private DatabaseHelper dbHelper; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_your_order);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        restaurantModel = getIntent().getParcelableExtra("RestaurantModel"); // Initializing restaurantModel
        hostelRadioGroup = findViewById(R.id.hostelRadioGroup);
        floorRadioGroup = findViewById(R.id.floorRadioGroup);
        toolbar = findViewById(R.id.toolbar);
        selectfloor= findViewById(R.id.floortext);
        selecthostel=findViewById(R.id.hosteltext);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout1);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Hostel-Bites");
        } else {
            Log.e("RestaurantMenuActivity", "Action bar is null");
            setTitle("Restaurant List");
        }

        inputName = findViewById(R.id.inputName);
        inputCity = findViewById(R.id.inputCity);
        inputZip = findViewById(R.id.inputZip);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        buttonPlaceYourOrder = findViewById(R.id.buttonPlaceYourOrder);
        switchDelivery = findViewById(R.id.switchDelivery);

        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);

        buttonPlaceYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrderWithPayment();
            }
        });

        switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show relevant views
                    inputCity.setVisibility(View.VISIBLE);
                    inputZip.setVisibility(View.VISIBLE);
                    tvDeliveryChargeAmount.setVisibility(View.VISIBLE);
                    tvDeliveryCharge.setVisibility(View.VISIBLE);
                    hostelRadioGroup.setVisibility(View.VISIBLE);
                    floorRadioGroup.setVisibility(View.VISIBLE);
                    selectfloor.setVisibility(View.VISIBLE);
                    selecthostel.setVisibility(View.VISIBLE);
                    isDeliveryOn = true;
                    calculateTotalAmount();
                } else {
                    // Hide relevant views
                    inputCity.setVisibility(View.GONE);
                    inputZip.setVisibility(View.GONE);
                    tvDeliveryChargeAmount.setVisibility(View.GONE);
                    tvDeliveryCharge.setVisibility(View.GONE);
                    hostelRadioGroup.setVisibility(View.GONE);
                    selectfloor.setVisibility(View.GONE);
                    selecthostel.setVisibility(View.GONE);
                    floorRadioGroup.setVisibility(View.GONE);
                    isDeliveryOn = false;
                    calculateTotalAmount();
                }
            }
        });


        initRecyclerView();
        calculateTotalAmount();
    }

    private void calculateTotalAmount() {
        float subTotalAmount = 0f;

        for (Menu m : restaurantModel.getMenus()) {
            subTotalAmount += m.getPrice() * m.getTotalInCart();
        }

        tvSubtotalAmount.setText("Rs" + String.format("%.2f", subTotalAmount));
        if (isDeliveryOn) {
            float deliveryCharge = restaurantModel.getDelivery_charge(); // Get delivery charge
            tvDeliveryChargeAmount.setText("Rs" + String.format("%.2f", deliveryCharge));
            subTotalAmount += deliveryCharge;
        }
        tvTotalAmount.setText("Rs" + String.format("%.2f", subTotalAmount));
    }


    private void placeOrderWithPayment() {
        if (TextUtils.isEmpty(inputName.getText().toString())) {
            inputName.setError("Please enter name ");
            return;
        } else if (isDeliveryOn && TextUtils.isEmpty(inputCity.getText().toString())) {
            inputCity.setError("Please choose room ");
            return;
        }
        startRazorpayPayment();
    }

    private void startRazorpayPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_uDyxnT2mA1Wctb");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "HOSTEL_BITES_PAYMENT GATEWAY");
            jsonObject.put("description", "Enter the Amount to complete payment");
            jsonObject.put("theme.color", "#FF9D01");
            jsonObject.put("currency", "INR");
            jsonObject.put("amount", calculateTotalAmount1() * 100);

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", "true");
            retryObj.put("max_count", 4);

            jsonObject.put("retry", retryObj);

            checkout.open(PlaceYourOrderActivity.this, jsonObject);
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateTotalAmount1() {
        float subTotalAmount = 0f;

        for (Menu m : restaurantModel.getMenus()) {
            subTotalAmount += m.getPrice() * m.getTotalInCart();
        }

        if (isDeliveryOn) {
            subTotalAmount += restaurantModel.getDelivery_charge();
        }

        return (int) subTotalAmount;
    }
    private void sendOrderToServer() {
        // Check if restaurantModel is initialized
        if (restaurantModel == null) {
            Toast.makeText(this, "Error: Restaurant data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected items and quantities from the restaurantModel
        List<Menu> selectedItems = new ArrayList<>();
        for (Menu item : restaurantModel.getMenus()) {
            if (item.getTotalInCart() > 0) {
                selectedItems.add(item);
            }
        }

        // Prepare data to send to the server
        String customerName = inputName.getText().toString().trim();
        String deliveryCity = inputCity.getText().toString().trim();
        String restaurantName = restaurantModel.getName();
        JSONArray itemsArray = new JSONArray();
        for (Menu item : selectedItems) {
            JSONObject itemObj = new JSONObject();
            try {
                itemObj.put("name", item.getName());
                itemObj.put("quantity", item.getTotalInCart());
                itemsArray.put(itemObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Calculate total amount
        float totalAmount = calculateTotalAmount1();

        // Save data to SQLite database
        long newRowId = dbHelper.addOrder(customerName, deliveryCity, restaurantName, itemsArray.toString(), totalAmount);

        if (newRowId != -1) {
            Toast.makeText(this, "Order saved to database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving order to database", Toast.LENGTH_SHORT).show();
        }
    }



    private void initRecyclerView() {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeYourOrderAdapter = new PlaceYourOrderAdapter(restaurantModel.getMenus());
        cartItemsRecyclerView.setAdapter(placeYourOrderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1000) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(PlaceYourOrderActivity.this, OrderSucceessActivity.class);
        i.putExtra("RestaurantModel", restaurantModel);
        startActivityForResult(i, 1000);
        sendOrderToServer();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Error: " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId(); // Get the ID of the clicked item

        switch (id) {
            // Handle your navigation menu item clicks here
            case R.id.home:
                startActivity(new Intent(PlaceYourOrderActivity.this, MainActivity.class));
                break;
            case R.id.subscription:
                startActivity(new Intent(PlaceYourOrderActivity.this, SubscriptionActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(PlaceYourOrderActivity.this, ProfileActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(PlaceYourOrderActivity.this, AboutUsActivity.class));
                break;
            case R.id.logout:
                startActivity(new Intent(PlaceYourOrderActivity.this, SendOTPActivity.class));
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }
}

