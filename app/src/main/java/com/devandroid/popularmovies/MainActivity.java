package com.devandroid.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements ListAdapter.ListItemClickListener {

    private static final int CEL_WIDTH = 250;

    private FrameLayout mFlParentView;
    private RecyclerView mRvListMovies;
    private ProgressBar mPbProgressbar;
    private TextView mTvNoConnection;
    private ListAdapter mAdapter;

    MoviesRequest moviesRequest;
    ArrayList<ListItem> listMovies;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

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

        if (mAdapter.getItemCount() == 0) {
            makeTMDBSearchQuery(Network.MOST_POPULAR_SEARCH);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Context context = MainActivity.this;
        Class destinyActivity = DetailsActivity.class;
        Intent intent = new Intent(context, destinyActivity);
        intent.putExtra("Movie", moviesRequest.getItem(clickedItemIndex));
        startActivity(intent);
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

        URL tmdbSearchUrl = Network.buildUrl(search);
        Log.d(LOG_TAG, tmdbSearchUrl.toString());
        new TMDBQueryTask().execute(tmdbSearchUrl);
    }

    private class TMDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPbProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String SearchResults = null;
            try {
                SearchResults = Network.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return SearchResults;
        }

        @Override
        protected void onPostExecute(String movieResults) {
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
    }

    private void showMovieList(String movieResults) {

        try {
            moviesRequest = JSON.getModelFromJSON(movieResults);
            listMovies = new ArrayList<>();
            for(int i=0; i<moviesRequest.getSize(); i++) {
                listMovies.add(new ListItem(moviesRequest.getItem(i).getmStrTitle(), Network.IMAGE_URL + "/w185/" + moviesRequest.getItem(i).getmStrPosterPath()));
            }
            mAdapter.setListAdapter(listMovies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
