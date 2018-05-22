package com.android.tenchoklang.nycgo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                    selectedFragment = new MapViewFragment();
                    break;
                case R.id.navigation_dashboard:
                    Toast.makeText(MainActivity.this, "DashBoard Clicked", Toast.LENGTH_SHORT).show();
                    selectedFragment = new HologramRecyclerFragment();
//                    Intent intent = new Intent(MainActivity.this, HologramActivity.class);
//                    startActivity(intent);
                    break;
                case R.id.navigation_notifications:
                    Toast.makeText(MainActivity.this, "Notification Clicked", Toast.LENGTH_SHORT).show();
                    selectedFragment = new StepsFragment();
                    break;
            }

            //load the fragment in the frame container
            if(selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new MapViewFragment()).commit();

        SharedPreferences.Editor editor = getSharedPreferences(MapViewFragment.MY_GLOBAL_PREFS, MODE_PRIVATE)
                .edit();
        editor.clear().apply();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


}

