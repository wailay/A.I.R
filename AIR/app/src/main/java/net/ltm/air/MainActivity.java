package net.ltm.air;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_camera, R.id.nav_near_trash, R.id.nav_eco_points,
                R.id.nav_register_bin, R.id.nav_github, R.id.nav_our_team)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.bringToFront();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home_camera:
                        Intent intent = new Intent(MainActivity.this, CameraHomeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_near_trash:
                        Intent intent2 = new Intent(MainActivity.this, NearestMapActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_eco_points:
                        Intent intent3 = new Intent(MainActivity.this, EcoActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_register_bin:
                        Intent intent4 = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.nav_github:
                        Intent intent5 = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent5);
                        break;

                    case R.id.nav_our_team:
                        Intent intent6 = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent6);
                        break;
                }

                drawer.closeDrawers();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
