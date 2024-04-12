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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);
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


        loadOrdersFromDatabase(); // Load orders from SQLite database


    }

    private void loadOrdersFromDatabase() {
        List<Order> orderList = dbHelper.getAllOrders(); // Retrieve orders from DBHelper
        orderAdapter.setOrders(orderList); // Update the adapter with retrieved orders
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.login:
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is logged in
                    Toast.makeText(this, "User already logged in", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(ProfileActivity.this, SendOTPActivity.class));
                }
                break;

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
                mAuth.signOut();
                sessionManager.clearSession();
                Intent intent = new Intent(ProfileActivity.this, SendOTPActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        drawerLayout.closeDrawers();
        return true;
    }
}
