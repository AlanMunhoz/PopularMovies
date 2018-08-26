package com.devandroid.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.GradientDrawable;
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
import com.devandroid.popularmovies.Utils.Network;
import com.devandroid.popularmovies.Utils.NetworkLoader;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, ListAdapter.ListItemClickListener {

    private static final int CEL_WIDTH = 250;
    private static final int MAIN_ACTIVITY_LOADER = 1;
    private static final String SEARCH_QUERY_URL_EXTRA = "SearchUrl";
    public static final String BUNDLE_DETAILS_EXTRA = "Movies";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private FrameLayout mFlParentView;
    private RecyclerView mRvListMovies;
    private ProgressBar mPbProgressbar;
    private TextView mTvNoConnection;
    private ListAdapter mAdapter;

    MoviesRequest moviesRequest;
    ArrayList<ListItem> listMovies;
    private String mSearchUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlParentView = findViewById(R.id.flParentView);
        mRvListMovies = findViewById(R.id.rv_list_movies);
        mPbProgressbar = findViewById(R.id.pbProgressbar);
        mTvNoConnection = findViewById(R.id.tvNoConnection);

        mFlParentView.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, getResources().getIntArray(R.array.clBackground)));

        LinearLayoutManager layoutManager = new GridLayoutManager(this, getNCardColumns(this));
        mRvListMovies.setLayoutManager(layoutManager);
        mRvListMovies.setHasFixedSize(true);

        mAdapter = new ListAdapter(MainActivity.this);
        mRvListMovies.setAdapter(mAdapter);

        Network.setApiKey(this.getResources().getString(R.string.TheMovieDB_ApiKey));

        /**
         * Restore the search string if exists and call request movies
         */
        if (savedInstanceState != null) {
            mSearchUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
        } else {
            mSearchUrl = Network.MOST_POPULAR_SEARCH;
        }
        makeTMDBSearchQuery(mSearchUrl);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SEARCH_QUERY_URL_EXTRA, mSearchUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        switch (itemThatWasClickedId) {

            case R.id.most_popular :
                makeTMDBSearchQuery(Network.MOST_POPULAR_SEARCH);
                return true;

            case R.id.top_rated :
                makeTMDBSearchQuery(Network.TOP_RATED_SEARCH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Context context = MainActivity.this;
        Class destinyActivity = DetailsActivity.class;
        Intent intent = new Intent(context, destinyActivity);
        intent.putExtra(BUNDLE_DETAILS_EXTRA, moviesRequest.getItem(clickedItemIndex));
        startActivity(intent);
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {

        Log.d(LOG_TAG, "onCreateLoader");

        String searchUrl = bundle.getString(SEARCH_QUERY_URL_EXTRA);
        NetworkLoader loader = new NetworkLoader(this, Network.buildUrl(searchUrl));
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String movieResults) {

        /**
         * Hide indicator of loading data
         */
        mPbProgressbar.setVisibility(View.INVISIBLE);

        if(movieResults!=null) {
            Log.d(LOG_TAG, movieResults);
            displayErrorMessage(false);
            showMovieList(movieResults);
        } else {
            Log.d(LOG_TAG, "Load data errors");
            displayErrorMessage(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
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

    private void makeTMDBSearchQuery(String search) {

        mSearchUrl = search;
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY_URL_EXTRA, mSearchUrl);

        NetworkLoader loader = (NetworkLoader) getLoaderManager().getLoader(MAIN_ACTIVITY_LOADER);
        if(loader!=null && loader.isAlreadyCreated()) {
            getLoaderManager().restartLoader(MAIN_ACTIVITY_LOADER, bundle, this).forceLoad();
        } else {
            getLoaderManager().initLoader(MAIN_ACTIVITY_LOADER, bundle, this).forceLoad();
        }

        /**
         * Show indicator of loading data
         */
        mPbProgressbar.setVisibility(View.VISIBLE);
    }

    private void showMovieList(String movieResults) {

        try {
            moviesRequest = JSON.getMoviesFromJSON(movieResults);
            listMovies = new ArrayList<>();
            for(int i=0; i<moviesRequest.getSize(); i++) {
                listMovies.add(new ListItem(moviesRequest.getItem(i).getmStrTitle(), Network.IMAGE_URL + Network.IMAGE_POSTER_SIZE_185PX + moviesRequest.getItem(i).getmStrPosterPath()));
            }
            mAdapter.setListAdapter(listMovies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
