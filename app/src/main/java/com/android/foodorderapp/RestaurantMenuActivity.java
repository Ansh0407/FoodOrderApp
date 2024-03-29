package com.android.foodorderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.foodorderapp.adapters.MenuListAdapter;
import com.android.foodorderapp.model.Menu;
import com.android.foodorderapp.model.RestaurantModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuActivity extends AppCompatActivity implements MenuListAdapter.MenuListClickListener, NavigationView.OnNavigationItemSelectedListener {
    private List<Menu> menuList = null;
    private MenuListAdapter menuListAdapter;
    private List<Menu> itemsInCartList;
    private int totalItemInCart = 0;
    private TextView buttonCheckout;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        RestaurantModel restaurantModel = getIntent().getParcelableExtra("RestaurantModel");

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout1);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Hostel-Bites");
        }else {
            Log.e("RestaurantMenuActivity", "Action bar is null");
            setTitle("Restaurant List");
        }

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        menuList = restaurantModel.getMenus();
        initRecyclerView();

        buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemsInCartList != null && itemsInCartList.size() <= 0) {
                    Toast.makeText(RestaurantMenuActivity.this, "Please add some items in cart.", Toast.LENGTH_SHORT).show();
                    return;
                }
                restaurantModel.setMenus(itemsInCartList);
                Intent i = new Intent(RestaurantMenuActivity.this, PlaceYourOrderActivity.class);
                i.putExtra("RestaurantModel", restaurantModel);
                startActivityForResult(i, 1000);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        menuListAdapter = new MenuListAdapter(menuList, this);
        recyclerView.setAdapter(menuListAdapter);
    }

    @Override
    public void onAddToCartClick(Menu menu) {
        if(itemsInCartList == null) {
            itemsInCartList = new ArrayList<>();
        }
        itemsInCartList.add(menu);
        totalItemInCart = 0;

        for(Menu m : itemsInCartList) {
            totalItemInCart = totalItemInCart + m.getTotalInCart();
        }
        buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
    }

    @Override
    public void onUpdateCartClick(Menu menu) {
        if(itemsInCartList.contains(menu)) {
            int index = itemsInCartList.indexOf(menu);
            itemsInCartList.remove(index);
            itemsInCartList.add(index, menu);

            totalItemInCart = 0;

            for(Menu m : itemsInCartList) {
                totalItemInCart = totalItemInCart + m.getTotalInCart();
            }
            buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
        }
    }

    @Override
    public void onRemoveFromCartClick(Menu menu) {
        if(itemsInCartList.contains(menu)) {
            itemsInCartList.remove(menu);
            totalItemInCart = 0;

            for(Menu m : itemsInCartList) {
                totalItemInCart = totalItemInCart + m.getTotalInCart();
            }
            buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            //
            finish();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId(); // Get the ID of the clicked item

        switch (id) {
            // Handle your navigation menu item clicks here
            case R.id.home:
                startActivity(new Intent(RestaurantMenuActivity.this, MainActivity.class));
                break;
            case R.id.subscription:
                startActivity(new Intent(RestaurantMenuActivity.this, SubscriptionActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(RestaurantMenuActivity.this, ProfileActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(RestaurantMenuActivity.this, AboutUsActivity.class));
                break;
            case R.id.logout:
                startActivity(new Intent(RestaurantMenuActivity.this, SendOTPActivity.class));
                break;
        }
        drawerLayout.closeDrawers(); // Close the drawer after handling the click
        return true;
    }
}