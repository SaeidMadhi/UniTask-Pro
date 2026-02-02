package com.mytest.mytasks;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment secilenSayfa = null;


                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    secilenSayfa = new HomeFragment();
                }
                else if (id == R.id.nav_focus) {
                    secilenSayfa = new FocusFragment();
                }
                else if (id == R.id.nav_settings) {
                    secilenSayfa = new SettingsFragment();
                }


                if (secilenSayfa != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, secilenSayfa)
                            .commit();
                }
                return true;
            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }
}