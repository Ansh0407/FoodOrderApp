package com.android.foodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Html;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SubscriptionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;
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
            Log.e("SubscriptionActivity", "Action bar is null");
            setTitle("Subscription");
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
                radioButton3.setBackgroundColor(ContextCompat.getColor(SubscriptionActivity.this, android.R.color.transparent));
                radioButton4.setBackgroundColor(ContextCompat.getColor(SubscriptionActivity.this, android.R.color.transparent));
                radioButton5.setBackgroundColor(ContextCompat.getColor(SubscriptionActivity.this, android.R.color.transparent));

                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    selectedRadioButton.setBackgroundColor(ContextCompat.getColor(SubscriptionActivity.this, android.R.color.holo_orange_light));
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SubscriptionActivity.this, "Please select a subscription option!", Toast.LENGTH_SHORT).show();
                } else {
                    // Retrieve the selected radio button text and extract the amount
                    RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                    String amountText = selectedRadioButton.getText().toString();
                    // Remove any non-numeric characters from the string
                    String numericAmountString = amountText.replaceAll("[^\\d]", "");
                    // Parse the remaining string as an integer
                    int amount = Integer.parseInt(numericAmountString);
                    Intent intent = new Intent(SubscriptionActivity.this, Payment.class);
                    intent.putExtra("AMOUNT", amount);
                    startActivity(intent);
                }
            }
        });
        TextView radioButton3Details = findViewById(R.id.radioButton3_details);
        radioButton3Details.setText(Html.fromHtml(getString(R.string.subscription_basic_details)));

        TextView radioButton4Details = findViewById(R.id.radioButton4_details);
        radioButton4Details.setText(Html.fromHtml(getString(R.string.subscription_basic_plus_details)));

        TextView radioButton5Details = findViewById(R.id.radioButton5_details);
        radioButton5Details.setText(Html.fromHtml(getString(R.string.subscription_premium_details)));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId(); // Get the ID of the clicked item

        switch (id) {
            case R.id.login:
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is logged in
                    Toast.makeText(this, "User already logged in", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(SubscriptionActivity.this, SendOTPActivity.class));
                }
                break;
            case R.id.home:
                startActivity(new Intent(SubscriptionActivity.this, MainActivity.class));
                break;
            case R.id.subscription:
                // No need to navigate to the same activity again
                break;
            case R.id.profile:
                startActivity(new Intent(SubscriptionActivity.this, ProfileActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(SubscriptionActivity.this, AboutUsActivity.class));
                break;
            case R.id.logout:
                mAuth.signOut();
                sessionManager.clearSession();
                Intent intent = new Intent(SubscriptionActivity.this, SendOTPActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawers(); // Close the drawer after handling the click
        return true;
    }

}
