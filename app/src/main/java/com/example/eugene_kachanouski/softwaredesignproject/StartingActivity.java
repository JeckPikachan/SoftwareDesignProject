package com.example.eugene_kachanouski.softwaredesignproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
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
    private FirebaseUser mUser;
    private FirebaseDatabase database;
    private DatabaseReference profileRef;
    private boolean menuInited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Устанавливает меню в Toolbar (три точки)
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        profileRef = database.getReference("profile");

        ProfileService.setProfileRef(profileRef);
        ProfileService.setUserId(mUser.getUid());

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

        setProfileUpdateListener();
    }

    private void setProfileUpdateListener() {
        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Profile profile = dataSnapshot.child(mUser.getUid()).getValue(Profile.class);
//                ProfileService.setCurrentProfile(profile);
//                String name = profile.getFirstName() == null ? "NONAME" : profile.getFirstName();
                Profile profile = dataSnapshot.child(mUser.getUid()).getValue(Profile.class);
                ProfileService.setCurrentProfile(profile);
                fillNavHeader();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StartingActivity.this, "Failed load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillNavHeader() {
        if (menuInited) {
            ProfileData profileData = ProfileService.getProfileData();
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
        menuInited = true;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.starting, menu);
        fillNavHeader();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        boolean navigated = NavigationUI.onNavDestinationSelected(item, navController);
        return navigated || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.sign_out) {
            mAuth.signOut();
            ProfileService.resetCurrentProfile();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        NavController navController = Navigation.findNavController(this, R.id.fragment);
        boolean navigated = NavigationUI.onNavDestinationSelected(item, navController);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return navigated;
    }

    
}
