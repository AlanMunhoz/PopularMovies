package com.devandroid.popularmovies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.devandroid.popularmovies.Model.Movie;
import com.devandroid.popularmovies.Model.ReviewsRequest;
import com.devandroid.popularmovies.Utils.JSON;
import com.devandroid.popularmovies.Utils.Network;
import com.devandroid.popularmovies.Utils.NetworkLoader;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String SEARCH_QUERY_URL_EXTRA = "SearchUrl";
    private static final int DETAILS_ACTIVITY_LOADER = 2;
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private ImageView ivPosterPath;
    private TextView tvTitle;
    private TextView tvVoteAverage;
    private TextView tvReleaseDate;
    private TextView tvOverview;
    private TextView tvVoteCount;
    private TextView tvPopularity;
    private TextView tvReviews;

    private String mSearchUrl;

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
        tvReviews = findViewById(R.id.tvReviews);

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

                makeTMDBSearchQuery(Network.REVIEWS_URL(movie.getmStrId()));
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

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {

        String searchUrl = bundle.getString(SEARCH_QUERY_URL_EXTRA);
        NetworkLoader loader = new NetworkLoader(this, Network.buildUrl(searchUrl));
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String movieResults) {


        if(movieResults!=null) {
            showReviewsList(movieResults);
        } else {
            Log.d(LOG_TAG, "Load data errors");
            //displayErrorMessage(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    private void makeTMDBSearchQuery(String search) {

        mSearchUrl = search;
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY_URL_EXTRA, mSearchUrl);

        NetworkLoader loader = (NetworkLoader) getLoaderManager().getLoader(DETAILS_ACTIVITY_LOADER);
        if(loader!=null && loader.isAlreadyCreated()) {
            getLoaderManager().restartLoader(DETAILS_ACTIVITY_LOADER, bundle, this).forceLoad();
        } else {
            getLoaderManager().initLoader(DETAILS_ACTIVITY_LOADER, bundle, this).forceLoad();
        }
    }

    private void showReviewsList(String reviewResults) {

        try {
            ReviewsRequest reviewsRequest = JSON.getReviewsFromJSON(reviewResults);
            String strText = "";
            for(int i=0; i<reviewsRequest.getSize(); i++) {
                strText += "[" + reviewsRequest.getItem(i).getmAuthor() + "]" + "\n";
                strText += reviewsRequest.getItem(i).getmContent() + "\n";
                strText += reviewsRequest.getItem(i).getmUrl() + "\n";
                strText += "\n\n";
            }
            if(reviewsRequest.getSize()==0){
                tvReviews.setText(getString(R.string.no_reviews));
            } else {
                tvReviews.setText(strText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
