package com.example.eugene_kachanouski.softwaredesignproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class StartingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Устанавливает меню в Toolbar (три точки)
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.fragment);

        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        // TODO: Разобраться как это работает
//        NavigationUI.setupActionBarWithNavController(this, navController, drawer);

    }

    private void fillNavHeader() {
        ProfileData profileData = ProfileService.getProfileData(this);
        ((TextView) findViewById(R.id.nav_user_name)).setText(profileData.firstName + ' ' + profileData.lastName);
        ((TextView) findViewById(R.id.nav_user_email)).setText(profileData.email);

        final Activity activity = this;

        findViewById(R.id.nav_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(activity, R.id.fragment);
                navController.navigate(R.id.profileFragment);
                DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.starting, menu);
        fillNavHeader();
//        setSignOutOnClickListener();
        return true;
    }

    private void setSignOutOnClickListener() {
        findViewById(R.id.sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        boolean navigated = NavigationUI.onNavDestinationSelected(item, navController);
        return navigated || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.sign_out) {
            mAuth.signOut();
//            Toast.makeText(StartingActivity.this,  "Authentication failed.",
//                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
//
//
//        Toast.makeText(StartingActivity.this,  "Authentication suck.",
//                Toast.LENGTH_SHORT).show();

        NavController navController = Navigation.findNavController(this, R.id.fragment);
        boolean navigated = NavigationUI.onNavDestinationSelected(item, navController);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return navigated;
//        return true;
    }

    
}
