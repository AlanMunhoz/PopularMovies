package com.devandroid.popularmovies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.net.Uri;
import android.support.annotation.Nullable;
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
import com.devandroid.popularmovies.database.AppDatabase;
import com.devandroid.popularmovies.database.FavoriteEntry;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
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
    private FavoriteEntry mFavoriteEntry;
    private Menu mFavoriteMenu;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.movie_details);
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

        addLiveDataObserver();

        getSupportLoaderManager().initLoader(DETAILS_ACTIVITY_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);

        mFavoriteMenu = menu;
        if(mFavoriteEntry!=null) {
            menu.findItem(R.id.favorite).setIcon(R.drawable.baseline_favorite_white_24);
        } else {
            menu.findItem(R.id.favorite).setIcon(R.drawable.baseline_favorite_border_white_24);
        }
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
                if(mFavoriteEntry!=null) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.FavoriteDAO().deleteFavorite(mFavoriteEntry);
                        }
                    });
                } else {
                    final FavoriteEntry favoriteEntry =
                            new FavoriteEntry(
                                    mMovie.getmStrTitle(),
                                    mMovie.getmStrPosterPath(),
                                    mMovie.getmStrId(),
                                    mMovie.getmStrVoteAverage(),
                                    mMovie.getmStrReleaseDate(),
                                    mMovie.getmStrVoteCount(),
                                    mMovie.getmStrPopularity(),
                                    mMovie.getmStrOverview()
                            );
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.FavoriteDAO().insertFavorite(favoriteEntry);
                        }
                    });
                }
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

    private void networkRequest(String search) {

        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY_URL_EXTRA, search);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(DETAILS_ACTIVITY_LOADER);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(DETAILS_ACTIVITY_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(DETAILS_ACTIVITY_LOADER, bundle, this);
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

    private void addLiveDataObserver() {

        //LiveData will run by default outside of the mainThread
        //LiveData<List<FavoriteEntry>> favoriteEntries = mDb.FavoriteDAO().searchTitle(mMovie.getmStrId());
        //favoriteEntries.observe(this, new Observer<List<FavoriteEntry>>() {

        DetailsViewModelFactory factory = new DetailsViewModelFactory(mDb, mMovie.getmStrId());
        final DetailsViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel.class);
        final Activity appContext = this;

        viewModel.getFavoriteEntry().observe(this, new Observer<List<FavoriteEntry>>() {

            /**
             * onChanged runs on the main thread by default
             */
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                Log.d(DetailsViewModel.LOG_TAG, "onChanged DB");

                if(favoriteEntries!=null && favoriteEntries.size()>0) {
                    mFavoriteEntry = favoriteEntries.get(0);
                    if(mFavoriteMenu != null) {
                        mFavoriteMenu.findItem(R.id.favorite).setIcon(R.drawable.baseline_favorite_white_24);
                        Toast.makeText(appContext, getString(R.string.favorite_selection_on), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mFavoriteEntry = null;
                    if(mFavoriteMenu != null) {
                        mFavoriteMenu.findItem(R.id.favorite).setIcon(R.drawable.baseline_favorite_border_white_24);
                        Toast.makeText(appContext, getString(R.string.favorite_selection_off), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {

        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {

                if (args == null) {
                    return;
                }

                forceLoad();
            }

            @Override
            public String loadInBackground() {

                String SearchResults = null;
                mSearchUrl = args.getString(SEARCH_QUERY_URL_EXTRA);
                URL searchQueryUrlString = Network.buildUrl(mSearchUrl);

                try {
                    Log.d(LOG_TAG, "loadInBackground");
                    SearchResults = Network.getResponseFromHttpUrl(searchQueryUrlString);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return SearchResults;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String movieResults) {

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
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
