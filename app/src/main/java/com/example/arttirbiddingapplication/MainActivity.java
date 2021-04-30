package com.example.arttirbiddingapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
        fAuth=FirebaseAuth.getInstance();
        fUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore= FirebaseFirestore.getInstance();

    }

       private BottomNavigationView.OnNavigationItemSelectedListener navListener= new  BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               Fragment fragment = null;
               switch (item.getItemId()) {
                   case R.id.bottom_nav_dashboard:
                       fragment = new DashboardFragment();
                       break;
                   case R.id.bottom_nav_sell:
                       if (fUser!=null)
                       {
                           fragment = new SellFragment();
                       }
                       else {
                           Intent register=new Intent(MainActivity.this,RegisterActivity.class);
                           startActivity(register);
                           finish();
                           return false;
                       }
                       break;
                   case R.id.bottom_nav_auctions:
                       if (fUser!=null)
                       {
                           fragment = new MyAuctionsFragment();
                       }
                       else {
                           Intent register=new Intent(MainActivity.this,RegisterActivity.class);
                           startActivity(register);
                           finish();
                           return false;
                       }
                       break;
                   case R.id.bottom_nav_notification:
                       if (fUser!=null)
                       {

                       }
                       else {
                           Intent register=new Intent(MainActivity.this,RegisterActivity.class);
                           startActivity(register);
                           finish();
                           return false;


                       }
                       break;
                   case R.id.bottom_nav_profile:
                       if (fUser!=null)
                       {
                           fragment = new ProfileFragment();
                       }
                       else {
                           Intent register=new Intent(MainActivity.this,RegisterActivity.class);
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