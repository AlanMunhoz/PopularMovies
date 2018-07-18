package com.devandroid.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
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
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements ListAdapter.ListItemClickListener {


    public static final String baseUrl = "https://api.themoviedb.org/3/";
    public static final String searchMostPopular = "movie/popular?api_key=";
    public static final String searchTopRated = "movie/top_rated?api_key=";
    public static final String imageUrl = "https://image.tmdb.org/t/p/";
    public static String apiKey;

    private FrameLayout mFlParentView;

    MoviesRequest moviesRequest;
    ArrayList<ListItem> listMovies;

    private ListAdapter mAdapter;
    private RecyclerView mListMovies;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlParentView = findViewById(R.id.flParentView);

        mFlParentView.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, getResources().getIntArray(R.array.clBackground)));

        apiKey = this.getResources().getString(R.string.MovieDBApiKey);

        mListMovies = findViewById(R.id.rv_list_movies);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, getNCardColumns(this));
        mListMovies.setLayoutManager(layoutManager);
        mListMovies.setHasFixedSize(true);

        makeMTDBSearchQuery(searchMostPopular);

    }

    int getNCardColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int nColumns = (displayMetrics.widthPixels / 250);
        return nColumns;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Context context = MainActivity.this;
        Class destinyActivity = DetailsActivity.class;
        Intent intent = new Intent(context, destinyActivity);
        intent.putExtra(Intent.EXTRA_TEXT, moviesRequest.getItem(clickedItemIndex).mStrBackdropPath);
        intent.putExtra("TITLE", moviesRequest.getItem(clickedItemIndex).mStrTitle);
        intent.putExtra("POSTER_PATH", moviesRequest.getItem(clickedItemIndex).mStrPosterPath);
        intent.putExtra("OVERVIEW", moviesRequest.getItem(clickedItemIndex).mStrOverview);
        intent.putExtra("VOTE_AVERAGE", moviesRequest.getItem(clickedItemIndex).mStrVoteAverage);
        intent.putExtra("RELEASE_DATE", moviesRequest.getItem(clickedItemIndex).mStrReleaseDate);
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
                Toast.makeText(this, "most popular", Toast.LENGTH_SHORT).show();
                makeMTDBSearchQuery(searchMostPopular);
                return true;

            case R.id.top_rated :
                Toast.makeText(this, "top rated", Toast.LENGTH_SHORT).show();
                makeMTDBSearchQuery(searchTopRated);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static URL buildUrl(String search) {

        Uri builtUri = Uri.parse(baseUrl + search + apiKey).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private void makeMTDBSearchQuery(String search) {

        URL mtdbSearchUrl = buildUrl(search);
        Log.d("myLog", mtdbSearchUrl.toString());
        new MtdbQueryTask().execute(mtdbSearchUrl);
    }

    private class MtdbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String SearchResults = null;
            try {
                SearchResults = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return SearchResults;
        }

        @Override
        protected void onPostExecute(String movieResults) {

            showMovieList(movieResults);
        }
    }

    private void showMovieList(String movieResults) {

        if(movieResults!=null) {
            Log.d("myLog", movieResults);
        }

        if (movieResults != null && !movieResults.equals("")) {
            try {
                moviesRequest = new MoviesRequest(movieResults);
                listMovies = new ArrayList<>();
                for(int i=0; i<moviesRequest.getSize(); i++) {
                    listMovies.add(new ListItem(moviesRequest.getItem(i).mStrTitle, imageUrl + "/w185/" + moviesRequest.getItem(i).mStrPosterPath));
                    mAdapter = new ListAdapter(listMovies, MainActivity.this);
                }
                mListMovies.setAdapter(mAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.d("myLog", "Load data errors");
            //showErrorMessage();
        }
    }

}
