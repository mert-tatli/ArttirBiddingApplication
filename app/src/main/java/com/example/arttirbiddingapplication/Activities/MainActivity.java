package com.example.arttirbiddingapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.arttirbiddingapplication.Fragments.DashboardFragment;
import com.example.arttirbiddingapplication.Fragments.MyAuctionsFragment;
import com.example.arttirbiddingapplication.Fragments.MessageFragment;
import com.example.arttirbiddingapplication.Fragments.ProfileFragment;
import com.example.arttirbiddingapplication.Fragments.SellFragment;
import com.example.arttirbiddingapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    String messageFragment="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_menu);

        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("message"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessageFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_notification);
        }
        else{
            if (fUser != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
            } else {
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
                finish();
            }
        }



        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.bottom_nav_dashboard:
                    if (fUser != null) {
                        fragment = new DashboardFragment();
                    } else {
                        Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(register);
                        finish();
                        return false;
                    }

                    break;
                case R.id.bottom_nav_sell:
                    if (fUser != null) {
                        fragment = new SellFragment();
                    } else {
                        Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(register);
                        finish();
                        return false;
                    }
                    break;
                case R.id.bottom_nav_auctions:
                    if (fUser != null) {
                        fragment = new MyAuctionsFragment();
                    } else {
                        Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(register);
                        finish();
                        return false;
                    }
                    break;
                case R.id.bottom_nav_notification:
                    if (fUser != null) {
                        fragment = new MessageFragment();
                    } else {
                        Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(register);
                        finish();
                        return false;


                    }
                    break;
                case R.id.bottom_nav_profile:
                    if (fUser != null) {
                        fragment = new ProfileFragment();
                    } else {
                        Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(register);
                        finish();
                        return false;
                    }
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            return true;
        }
    };


}