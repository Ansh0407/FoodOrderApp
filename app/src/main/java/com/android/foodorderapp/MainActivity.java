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

import com.android.foodorderapp.adapters.RestaurantListAdapter;
import com.android.foodorderapp.model.RestaurantModel;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RestaurantListAdapter.RestaurantListClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            drawerLayout = findViewById(R.id.drawerLayout);
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

            NavigationView navigationView = findViewById(R.id.navigationView);
            navigationView.setNavigationItemSelectedListener(this);

        List<RestaurantModel> restaurantModelList = getRestaurantData();
        initRecyclerView(restaurantModelList);
    }

    private void initRecyclerView(List<RestaurantModel> restaurantModelList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RestaurantListAdapter adapter = new RestaurantListAdapter(restaurantModelList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<RestaurantModel> getRestaurantData() {
        InputStream is = getResources().openRawResource(R.raw.restaurent);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = writer.toString();
        Gson gson = new Gson();
        RestaurantModel[] restaurantModels = gson.fromJson(jsonStr, RestaurantModel[].class);
        return Arrays.asList(restaurantModels);
    }

    @Override
    public void onItemClick(RestaurantModel restaurantModel) {
        Intent intent = new Intent(MainActivity.this, RestaurantMenuActivity.class);
        intent.putExtra("RestaurantModel", restaurantModel);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId(); // Get the ID of the clicked item

        switch (id) {
            // Handle your navigation menu item clicks here
            case R.id.home:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.subscription:
                startActivity(new Intent(MainActivity.this, SubscriptionActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout CLicked !!", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawers(); // Close the drawer after handling the click
        return true;
    }
}
