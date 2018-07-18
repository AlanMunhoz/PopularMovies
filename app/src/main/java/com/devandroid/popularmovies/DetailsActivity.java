package com.devandroid.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private ImageView ivBackdropPath;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivBackdropPath = findViewById(R.id.iv_backdrop_path);
        tvTitle = findViewById(R.id.tvTitle);

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)) {
            String strMovieId = intent.getStringExtra(Intent.EXTRA_TEXT);
            Picasso.with(this).load("https://image.tmdb.org/t/p/" + "/w780/" + strMovieId).into(ivBackdropPath);
        }
        if(intent.hasExtra("TITLE")) {
            tvTitle.setText(intent.getStringExtra("TITLE"));
        }
        if(intent.hasExtra("POSTER_PATH")) {
            //intent.putExtra("POSTER_PATH", moviesRequest.getItem(clickedItemIndex).mStrPosterPath);
        }
        if(intent.hasExtra("OVERVIEW")) {
            //intent.putExtra("OVERVIEW", moviesRequest.getItem(clickedItemIndex).mStrOverview);
        }
        if(intent.hasExtra("VOTE_AVERAGE")) {
            //intent.putExtra("VOTE_AVERAGE", moviesRequest.getItem(clickedItemIndex).mStrVoteAverage);
        }
        if(intent.hasExtra("RELEASE_DATE")) {
            //intent.putExtra("RELEASE_DATE", moviesRequest.getItem(clickedItemIndex).mStrReleaseDate);
        }

    }
}
