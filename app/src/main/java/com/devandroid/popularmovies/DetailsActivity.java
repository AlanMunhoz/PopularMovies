package com.devandroid.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.devandroid.popularmovies.Model.Movie;
import com.devandroid.popularmovies.Utils.Network;
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

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivPosterPath = findViewById(R.id.ivPosterPath);
        tvTitle = findViewById(R.id.tvTitle);
        tvVoteAverage = findViewById(R.id.tvVoteAverage);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvOverview = findViewById(R.id.tvOverview);
        tvVoteCount = findViewById(R.id.tvVoteCount);
        tvPopularity = findViewById(R.id.tvPopularity);

        Intent intent = getIntent();

        if(intent.hasExtra(MainActivity.BUNDLE_DETAILS_EXTRA)) {
            Bundle data = intent.getExtras();
            Movie movie = null;
            if(data != null) {
                movie = data.getParcelable(MainActivity.BUNDLE_DETAILS_EXTRA);
            }
            if(movie != null){
                tvTitle.setText(movie.getmStrTitle());
                String strMovieId = movie.getmStrPosterPath();
                Picasso.with(this).load(Network.IMAGE_URL + Network.IMAGE_POSTER_SIZE_780PX + strMovieId).into(ivPosterPath);
                tvOverview.setText(movie.getmStrOverview());
                tvVoteAverage.setText(movie.getmStrVoteAverage());
                tvReleaseDate.setText(movie.getmStrReleaseDate());
                tvVoteCount.setText(movie.getmStrVoteCount());
                tvPopularity.setText(movie.getmStrPopularity());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
