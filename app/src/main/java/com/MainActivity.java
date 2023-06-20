package com;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.view.MotionEvent;
import android.graphics.Rect;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.AboutActivity;
import com.example.myapplication.R;
import com.example.myapplication.ReportErrorActivity;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ui.event.EventFragment;
import com.ui.myEvent.MyEventFragment;
import com.ui.myEvent.MyEventGuestFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ActionBarDrawerToggle drawer_toggle;
    public TextView welcomeText;


    private FirebaseAuth mAuth;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawer_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.getRoot().setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    Rect outRect = new Rect();
                    binding.navigationView.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
            }
            return false;
        });

        replaceFragment(new EventFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.myEvents) {
                if (LoginActivity.isGuestMode) {
                    replaceFragment(new MyEventGuestFragment());
                } else {
                    replaceFragment(new MyEventFragment());
                }
            } else if (id == R.id.events) {
                replaceFragment(new EventFragment());
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        drawer_toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(drawer_toggle);
        drawer_toggle.syncState();


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.logout_id) {
                logout();
            }
            if (id == R.id.buy_ticket_id) {
                buyTicket();
            }
            if (id == R.id.error_id) {
                reportError();
            }
            if (id == R.id.about_id) {
                aboutUs();
            }
            if (id == R.id.home_id) {
                openHomeActivity();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
            if (id == R.id.create_event_id) {
                createEvent();
            }

            return true;
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        if (LoginActivity.isGuestMode) {

            Menu menu = navigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.logout_id);
            menuItem.setTitle("Login");
            menuItem.setIcon(R.drawable.ic_login);
        }
        welcomeText = navigationView.getHeaderView(0).findViewById(R.id.welcomeName);
        setWelcomeName();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("----------------------","back");
    }
    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void createEvent(){
        Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void logout() {
        if (LoginActivity.isGuestMode)
        {
            LoginActivity.isGuestMode = false;
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else {
            mAuth.signOut();

            Toast.makeText(MainActivity.this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void buyTicket() {
        Log.d("BuyTicket", "----HERE------");
        try {
            String url = "https://www.ticketmaster.at";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            //if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            //}
        } catch (Exception e) {
            // Handle the exception here
            Log.d("BuyTicket", "----ERROR-------");
        }
    }

    private void setWelcomeName(){

        if (LoginActivity.isGuestMode)
        {
            TextView welcomeText;
            welcomeText = (TextView)findViewById(R.id.welcomeName);
            if (welcomeText != null)
                welcomeText.setText("Welcome, Guest Mode");
        }
        else{
            String id = mAuth.getCurrentUser().getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("username");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("MainActivity", dataSnapshot.getValue().toString());

                    welcomeText.setText("Welcome, " + dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void aboutUs() {
        // Add your "about us" logic here
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

//    public void error() {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri data = Uri.parse("amela.kurtic@student.tugraz.at");
//        intent.setData(data);
//        startActivity(intent);
//
//    }

    private void openHomeActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void reportError() {
        Intent intent = new Intent(MainActivity.this, ReportErrorActivity.class);
        startActivity(intent);
    }
}