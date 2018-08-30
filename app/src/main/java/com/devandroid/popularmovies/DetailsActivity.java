package com.devandroid.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devandroid.popularmovies.Model.Movie;
import com.devandroid.popularmovies.Model.ReviewsRequest;
import com.devandroid.popularmovies.Model.Video;
import com.devandroid.popularmovies.Utils.AppExecutors;
import com.devandroid.popularmovies.Utils.JSON;
import com.devandroid.popularmovies.Utils.Network;
import com.devandroid.popularmovies.Utils.NetworkLoader;
import com.devandroid.popularmovies.database.AppDatabase;
import com.devandroid.popularmovies.database.FavoriteEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, VideoAdapter.ListItemClickListener {

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
    private RecyclerView mRvVideos;

    private VideoAdapter mAdapter;
    private Movie mMovie;
    private ArrayList<Video> mLstVideos;
    private String mSearchUrl;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ivPosterPath = findViewById(R.id.ivPosterPath);
        tvTitle = findViewById(R.id.tvTitle);
        tvVoteAverage = findViewById(R.id.tvVoteAverage);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvOverview = findViewById(R.id.tvOverview);
        tvVoteCount = findViewById(R.id.tvVoteCount);
        tvPopularity = findViewById(R.id.tvPopularity);
        tvReviews = findViewById(R.id.tvReviews);
        mRvVideos = findViewById(R.id.rvVideos);

        mDb = AppDatabase.getInstance(getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvVideos.setLayoutManager(layoutManager);
        mRvVideos.setHasFixedSize(true);

        mAdapter = new VideoAdapter(DetailsActivity.this);
        mRvVideos.setAdapter(mAdapter);


        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.BUNDLE_DETAILS_EXTRA)) {
            Bundle data = intent.getExtras();
            Movie movie = null;
            if(data != null) {
                movie = data.getParcelable(MainActivity.BUNDLE_DETAILS_EXTRA);
            }
            if(movie != null) {

                mMovie = movie;

                tvTitle.setText(movie.getmStrTitle());
                String strMovieId = movie.getmStrPosterPath();
                Picasso.with(this).load(Network.IMAGE_URL + Network.IMAGE_POSTER_SIZE_780PX + strMovieId).into(ivPosterPath);
                tvOverview.setText(movie.getmStrOverview());
                tvVoteAverage.setText(movie.getmStrVoteAverage());
                tvReleaseDate.setText(movie.getmStrReleaseDate());
                tvVoteCount.setText(movie.getmStrVoteCount());
                tvPopularity.setText(movie.getmStrPopularity());

                networkRequest(Network.REVIEWS_URL(movie.getmStrId()));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<FavoriteEntry> favoriteEntries = mDb.FavoriteDAO().searchTitle(mMovie.getmStrId());
                if(favoriteEntries.size()>0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            menu.findItem(R.id.favorite).setIcon(R.drawable.baseline_favorite_white_24);
                        }
                    });
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.favorite:

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<FavoriteEntry> favoriteEntries = mDb.FavoriteDAO().searchTitle(mMovie.getmStrId());
                        if(favoriteEntries.size()>0) {
                            mDb.FavoriteDAO().deleteFavorite(favoriteEntries.get(0));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    item.setIcon(R.drawable.baseline_favorite_border_white_24);
                                    //Toast.makeText(this, "Not favorite :(", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            FavoriteEntry favoriteEntry = new FavoriteEntry(mMovie.getmStrTitle(), mMovie.getmStrPosterPath(), mMovie.getmStrId());
                            mDb.FavoriteDAO().insertFavorite(favoriteEntry);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    item.setIcon(R.drawable.baseline_favorite_white_24);
                                    //Toast.makeText(this, "Favorite ;)", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        String videoUrl = mLstVideos.get(clickedItemIndex).getYoutubeUrl();
        Intent target = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        Intent chooser = Intent.createChooser(target, "Open With");
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
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

            Log.d(LOG_TAG, movieResults);

            if(mSearchUrl.equals(Network.REVIEWS_URL(mMovie.getmStrId()))) {
                showReviewsList(movieResults);
                networkRequest(Network.VIDEOS_URL(mMovie.getmStrId()));
            } else {
                showVideosList(movieResults);
            }
        } else {
            Log.d(LOG_TAG, "Load data errors");
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    private void networkRequest(String search) {

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

    private void showVideosList(String videoResults) {

        try {

            mLstVideos = JSON.getVideosFromJSON(videoResults);

            if(mLstVideos!=null) {
                mRvVideos.setVisibility(RecyclerView.VISIBLE);
                mAdapter.setListAdapter(mLstVideos);
            } else {
                mRvVideos.setVisibility(RecyclerView.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
