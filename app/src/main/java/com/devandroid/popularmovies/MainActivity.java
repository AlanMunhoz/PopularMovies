package com.devandroid.popularmovies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devandroid.popularmovies.Model.MoviesRequest;
import com.devandroid.popularmovies.Utils.JSON;
import com.devandroid.popularmovies.Utils.MyAsyncTaskLoader;
import com.devandroid.popularmovies.Utils.Network;
import com.devandroid.popularmovies.database.FavoriteEntry;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, ListAdapter.ListItemClickListener {

    /**
     * Constants
     */
    private static final int CEL_WIDTH = 250;
    private static final int MAIN_ACTIVITY_LOADER = 1;
    public static final String SEARCH_QUERY_URL_EXTRA = "SearchQuery";
    private static final String LAST_SELECTION_EXTRA = "LastSelection";
    public static final String MOVIE_REQUEST_EXTRA = "MovieRequest";
    public static final String BUNDLE_DETAILS_EXTRA = "Movies";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    //
    private static final int MOST_POPULAR = 0;
    private static final int TOP_RATED = 1;
    private static final int FAVORITES = 2;

    /**
     * UI components
     */
    private FrameLayout mFlParentView;
    private RecyclerView mRvListMovies;
    private ProgressBar mPbProgressbar;
    private TextView mTvNoConnection;

    /**
     * Data
     */
    private ListAdapter mAdapter;
    private MoviesRequest mMoviesRequest;
    private ArrayList<ListItem> mLstMovieItems;
    private ArrayList<ListItem> mLstFavoriteMovieItems;
    private List<FavoriteEntry> mLstFavoriteEntries;
    private int mLastSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlParentView = findViewById(R.id.flParentView);
        mRvListMovies = findViewById(R.id.rv_list_movies);
        mPbProgressbar = findViewById(R.id.pbProgressbar);
        mTvNoConnection = findViewById(R.id.tvNoConnection);

        Network.setApiKey(this.getResources().getString(R.string.TheMovieDB_ApiKey));

        mFlParentView.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, getResources().getIntArray(R.array.clBackground)));

        LinearLayoutManager layoutManager = new GridLayoutManager(this, getNCardColumns(this));
        mRvListMovies.setLayoutManager(layoutManager);
        mRvListMovies.setHasFixedSize(true);

        mAdapter = new ListAdapter(MainActivity.this);
        mRvListMovies.setAdapter(mAdapter);

        /**
         * Restore the search string if exists and call request movies
         */
        if (savedInstanceState != null) {
            mLastSelection = savedInstanceState.getInt(LAST_SELECTION_EXTRA);
            mMoviesRequest = savedInstanceState.getParcelable(MOVIE_REQUEST_EXTRA);
        }

        if(mLastSelection == FAVORITES) {
            showFavoriteList();
        } else {
            if(mMoviesRequest != null) {
                showMovieList();
            } else {
                getSupportLoaderManager().initLoader(MAIN_ACTIVITY_LOADER, null, this);
                mLastSelection = MOST_POPULAR;
                networkRequest();
            }
        }
        setTitleActionBar();
        addLiveDataObserver();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LAST_SELECTION_EXTRA, mLastSelection);
        outState.putParcelable(MOVIE_REQUEST_EXTRA, mMoviesRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        switch (itemThatWasClickedId) {

            case R.id.most_popular:
                mLastSelection = MOST_POPULAR;
                networkRequest();
                setTitleActionBar();
                return true;

            case R.id.top_rated:
                mLastSelection = TOP_RATED;
                networkRequest();
                setTitleActionBar();
                return true;

            case R.id.favorite:
                displayErrorMessage(false);
                mLastSelection = FAVORITES;
                setTitleActionBar();
                showFavoriteList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Context context = MainActivity.this;
        Class destinyActivity = DetailsActivity.class;
        Intent intent = new Intent(context, destinyActivity);

        if(mLastSelection == FAVORITES) {
            intent.putExtra(BUNDLE_DETAILS_EXTRA, mLstFavoriteEntries.get(clickedItemIndex).getMovie());
        } else {
            intent.putExtra(BUNDLE_DETAILS_EXTRA, mMoviesRequest.getItem(clickedItemIndex));
        }
        startActivity(intent);
    }

    /**
     * calculate and define how many columns will be present in recycler view
     */
    private int getNCardColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int nColumns = (displayMetrics.widthPixels / CEL_WIDTH);
        return nColumns;
    }

    private void displayErrorMessage(Boolean show) {
        if(show) {
            mTvNoConnection.setVisibility(View.VISIBLE);
            mRvListMovies.setVisibility(View.INVISIBLE);
        } else {
            mTvNoConnection.setVisibility(View.INVISIBLE);
            mRvListMovies.setVisibility(View.VISIBLE);
        }
    }

    private void networkRequest() {

        String searchUrl = "";
        switch (mLastSelection) {
            case MOST_POPULAR: searchUrl = Network.MOST_POPULAR_SEARCH; break;
            case TOP_RATED: searchUrl = Network.TOP_RATED_SEARCH; break;
        }

        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY_URL_EXTRA, searchUrl);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(MAIN_ACTIVITY_LOADER);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(MAIN_ACTIVITY_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(MAIN_ACTIVITY_LOADER, bundle, this);
        }

    }

    private void setTitleActionBar() {

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {

            switch (mLastSelection) {

                case MOST_POPULAR:
                    actionBar.setTitle(getString(R.string.most_popular_title));
                    break;

                case TOP_RATED:
                    actionBar.setTitle(getString(R.string.top_rated_title));
                    break;

                case FAVORITES:
                    actionBar.setTitle(getString(R.string.favorite_title));
                    break;

                default:
                    actionBar.setTitle(getString(R.string.most_popular_title));
                    break;
            }
        }
    }

    private void showMovieList() {

        mLstMovieItems = new ArrayList<>();
        for(int i=0; i<mMoviesRequest.getSize(); i++) {
            //moviesRequest.getItem(i).setmStrFullPosterPathUrl(Network.IMAGE_URL + Network.IMAGE_POSTER_SIZE_185PX + moviesRequest.getItem(i).getmStrPosterPath());
            mLstMovieItems.add(new ListItem(mMoviesRequest.getItem(i).getmStrTitle(), Network.IMAGE_URL + Network.IMAGE_POSTER_SIZE_185PX + mMoviesRequest.getItem(i).getmStrPosterPath()));
        }
        mAdapter.setListAdapter(mLstMovieItems);
    }

    private void showFavoriteList() {

        mAdapter.setListAdapter(mLstFavoriteMovieItems);
    }

    /**
     * Adding observer to database, this way allow us to know when database changes occurs.
     */
    private void addLiveDataObserver() {

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        /**
         * onChanged runs on the main thread by default
         */
        viewModel.getFavoriteEntries().observe(this, new Observer<List<FavoriteEntry>>() {

            //onChanged runs on the main thread by default
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {

                Log.d(MainViewModel.LOG_TAG, "onChanged DB");
                mLstFavoriteEntries = favoriteEntries;
                mLstFavoriteMovieItems = new ArrayList<>();
                for(int i=0;i<favoriteEntries.size();i++) {
                    mLstFavoriteMovieItems.add(new ListItem(
                            favoriteEntries.get(i).getTitle(),
                            Network.IMAGE_URL + Network.IMAGE_POSTER_SIZE_185PX + favoriteEntries.get(i).getPosterPath()));
                }
                if(mLastSelection == FAVORITES) {
                    showFavoriteList();
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {

        /**
         * Show indicator of loading data
         */
        mPbProgressbar.setVisibility(View.VISIBLE);

        return new MyAsyncTaskLoader(new WeakReference<Activity>(this), args, SEARCH_QUERY_URL_EXTRA);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String movieResults) {

        /**
         * Hide indicator of loading data
         */
        mPbProgressbar.setVisibility(View.INVISIBLE);

        if(movieResults!=null) {
            Log.d(LOG_TAG, movieResults);
            displayErrorMessage(false);
            try {
                mMoviesRequest = JSON.getMoviesFromJSON(movieResults);
                if(mLastSelection != FAVORITES) {
                    showMovieList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(LOG_TAG, "Load data errors");
            displayErrorMessage(true);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }
}
