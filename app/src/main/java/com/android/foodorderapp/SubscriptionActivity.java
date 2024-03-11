package com.android.foodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SubscriptionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private Button okButton;
    private RadioGroup radioGroup;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Toolbar toolbar = findViewById(R.id.toolbar);
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

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton5 = findViewById(R.id.radioButton5);
        okButton = findViewById(R.id.button);
        radioGroup = findViewById(R.id.radioGroup);

        // Handle RadioButton selection change
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Reset background color of all RadioButtons
                radioButton3.setBackgroundColor(ContextCompat.getColor(SubscriptionActivity.this, android.R.color.transparent));
                radioButton4.setBackgroundColor(ContextCompat.getColor(SubscriptionActivity.this, android.R.color.transparent));
                radioButton5.setBackgroundColor(ContextCompat.getColor(SubscriptionActivity.this, android.R.color.transparent));

                // Set background color for the selected RadioButton
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    selectedRadioButton.setBackgroundColor(ContextCompat.getColor(SubscriptionActivity.this, android.R.color.holo_orange_light));
                }
            }
        });



        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if any RadioButton is selected
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    // No RadioButton selected, show error message
                    Toast.makeText(SubscriptionActivity.this, "Please select a subscription option!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SubscriptionActivity.this, "Done successful! Proceed to Payment", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubscriptionActivity.this, Payment.class);
                    startActivity(intent);
                }
            }
        });
    }

    // Function to darken a color
    private int darkenColor(int color) {
        float factor = 0.8f;
        int alpha = android.graphics.Color.alpha(color);
        int red = (int) (android.graphics.Color.red(color) * factor);
        int green = (int) (android.graphics.Color.green(color) * factor);
        int blue = (int) (android.graphics.Color.blue(color) * factor);
        return android.graphics.Color.argb(alpha, red, green, blue);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId(); // Get the ID of the clicked item

        switch (id) {
            // Handle your navigation menu item clicks here
            case R.id.home:
                startActivity(new Intent(SubscriptionActivity.this, MainActivity.class));
                break;
            case R.id.subscription:
                startActivity(new Intent(SubscriptionActivity.this, SubscriptionActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(SubscriptionActivity.this, ProfileActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(SubscriptionActivity.this, AboutUsActivity.class));
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout CLicked !!", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawers(); // Close the drawer after handling the click
        return true;
    }
}
