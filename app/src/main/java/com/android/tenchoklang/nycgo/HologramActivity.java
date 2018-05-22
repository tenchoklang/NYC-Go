package com.android.tenchoklang.nycgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class HologramActivity extends AppCompatActivity {

//    String photoUrl = "https://maps.googleapis.com/maps/api/streetview?size=400x400&location=40.720032,-73.988354&size=20x20&key=AIzaSyBhb4XTDgK8ODRMKQsK5gBX038F6ygy3BQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hologram);

        Intent intent = getIntent();
        String photoUrl = intent.getExtras().getString("PHOTO");


        ImageView thumbnail = findViewById(R.id.hologramBottom);
        ImageView thumbnail1 = findViewById(R.id.hologramLeft);
        ImageView thumbnail2 = findViewById(R.id.hologramRight);
        ImageView thumbnail3 = findViewById(R.id.hologramTop);

        Picasso.with(this).load(photoUrl)
                .into(thumbnail);
        Picasso.with(this).load(photoUrl)
                .into(thumbnail1);
        Picasso.with(this).load(photoUrl)
                .into(thumbnail2);
        Picasso.with(this).load(photoUrl)
                .into(thumbnail3);
    }
}
