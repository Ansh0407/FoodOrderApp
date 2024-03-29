package com.android.foodorderapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.foodorderapp.adapters.OrderAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout1);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Hostel-Bites");
        } else {
            Log.e("ProfileActivity", "Action bar is null");
            setTitle("Profile");
        }

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerViewOrderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter();
        recyclerView.setAdapter(orderAdapter);

        // Add sample orders
        // Add sample orders
        List<Order> orders = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            String orderId = "Order ID: #" + (100000 + random.nextInt(900000)); // Generate random 6-digit order IDs
            String customerName = "Customer " + (i + 1);
            String foodItem = "Food Item " + (i + 1);
            String deliveryAddress = "Address " + (i + 1);
            orders.add(new Order(orderId, customerName, foodItem, deliveryAddress));
        }
        orderAdapter.setOrders(orders);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                break;
            case R.id.subscription:
                startActivity(new Intent(ProfileActivity.this, SubscriptionActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(ProfileActivity.this, AboutUsActivity.class));
                break;
            case R.id.logout:
                startActivity(new Intent(ProfileActivity.this, SendOTPActivity.class));
                break;
        }

        drawerLayout.closeDrawers();
        return true;
    }
}
