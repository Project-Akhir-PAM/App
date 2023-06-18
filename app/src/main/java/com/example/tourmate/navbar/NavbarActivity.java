package com.example.tourmate.navbar;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tourmate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavbarActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.navbar_bottom );

        bottomNavigation = findViewById( R.id.navbar );
        getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new homeFragment() ).commit();

        bottomNavigation.setOnItemReselectedListener( new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.home:
                        selectedFragment = new homeFragment();
                        break;

                    case R.id.search:
                        selectedFragment = new searchFragment();
                        break;

                    case R.id.favorite:
                        selectedFragment = new favoriteFragment();
                        break;

                    case R.id.profile:
                        selectedFragment = new profileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, selectedFragment ).commit();

                return;

            }
        } );
    }
}
