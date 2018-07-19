package com.devandroid.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private ImageView ivPosterPath;
    private TextView tvTitle;
    private TextView tvVoteAverage;
    private TextView tvReleaseDate;
    private TextView tvOverview;
    private TextView tvVoteCount;
    private TextView tvPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivPosterPath = findViewById(R.id.ivPosterPath);
        tvTitle = findViewById(R.id.tvTitle);
        tvVoteAverage = findViewById(R.id.tvVoteAverage);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvOverview = findViewById(R.id.tvOverview);
        tvVoteCount = findViewById(R.id.tvVoteCount);
        tvPopularity = findViewById(R.id.tvPopularity);

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)) {
            String strMovieId = intent.getStringExtra(Intent.EXTRA_TEXT);

        }
        if(intent.hasExtra("TITLE")) {
            tvTitle.setText(intent.getStringExtra("TITLE"));
        }
        if(intent.hasExtra("POSTER_PATH")) {
            String strMovieId = intent.getStringExtra("POSTER_PATH");
            Picasso.with(this).load("https://image.tmdb.org/t/p/" + "/w780/" + strMovieId).into(ivPosterPath);
        }
        if(intent.hasExtra("OVERVIEW")) {
            tvOverview.setText(intent.getStringExtra("OVERVIEW"));
        }
        if(intent.hasExtra("VOTE_AVERAGE")) {
            tvVoteAverage.setText(intent.getStringExtra("VOTE_AVERAGE"));
        }
        if(intent.hasExtra("RELEASE_DATE")) {
            tvReleaseDate.setText(intent.getStringExtra("RELEASE_DATE"));
        }
        if(intent.hasExtra("VOTE_COUNT")) {
            tvVoteCount.setText(intent.getStringExtra("VOTE_COUNT"));
        }
        if(intent.hasExtra("POPULARITY")) {
            tvPopularity.setText(intent.getStringExtra("POPULARITY"));
        }

    }
}
