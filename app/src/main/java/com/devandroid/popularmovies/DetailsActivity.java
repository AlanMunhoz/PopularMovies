package com.devandroid.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private ImageView ivBackdropPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivBackdropPath = findViewById(R.id.iv_backdrop_path);

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)) {
            String strMovieId = intent.getStringExtra(Intent.EXTRA_TEXT);
            Picasso.with(this).load("https://image.tmdb.org/t/p/" + "/w780/" + strMovieId).into(ivBackdropPath);
        }

    }
}
