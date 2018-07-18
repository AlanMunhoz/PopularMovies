package com.devandroid.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MoviesRequest {

    private final String OWM_MESSAGE_CODE = "cod";
    private final String MOVIES_LIST = "results";

    public class Movie {
        String mStrVoteCount;
        String mStrId;
        String mStrVideo;
        String mStrVoteAverage;
        String mStrTitle;
        String mStrPopularity;
        String mStrPosterPath;
        String mStrOriginalLanguage;
        String mStrOriginalTitle;
        String[] mLstGenreIds;
        String mStrBackdropPath;
        String mStrAdult;
        String mStrOverview;
        String mStrReleaseDate;
    }

    private String mStrPage;
    private String mStrTotalResuts;
    private String mStrTotalPages;
    private ArrayList<Movie> mMovies;

    public MoviesRequest(String JSonString) throws JSONException {

        JSONObject requestJSon = new JSONObject(JSonString);

        if (requestJSon.has(OWM_MESSAGE_CODE)) {
            int errorCode = requestJSon.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return;
                default:
                    /* Server probably down */
                    return;
            }
        }

        JSONArray movieResults = requestJSon.getJSONArray(MOVIES_LIST);
        mMovies = new ArrayList<>();

        for (int i = 0; i < movieResults.length(); i++) {
            JSONObject movieJSon = movieResults.getJSONObject(i);
            Movie movie = new Movie();
            movie.mStrTitle = movieJSon.getString("title");
            movie.mStrPosterPath = movieJSon.getString("poster_path");
            movie.mStrBackdropPath = movieJSon.getString("backdrop_path");
            mMovies.add(movie);
        }
    }

    int getSize() {

        if(mMovies == null) return 0;
        return mMovies.size();
    }

    Movie getItem(int index) {

        if(mMovies == null) return null;
        return mMovies.get(index);
    }
}
