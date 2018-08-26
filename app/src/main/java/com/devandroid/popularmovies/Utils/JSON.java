package com.devandroid.popularmovies.Utils;

import com.devandroid.popularmovies.Model.Movie;
import com.devandroid.popularmovies.Model.MoviesRequest;
import com.devandroid.popularmovies.Model.Review;
import com.devandroid.popularmovies.Model.ReviewsRequest;
import com.devandroid.popularmovies.Model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public final class JSON {

    private static final String MESSAGE_CODE = "cod";
    private static final String PAGE = "page";
    private static final String TOTAL_RESULTS = "total_results";
    private static final String TOTAL_PAGES = "total_pages";
    private static final String LIST_RESULTS = "results";
    private static final String VOTE_COUNT = "vote_count";
    private static final String ID = "id";
    private static final String VIDEO = "video";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String TITLE = "title";
    private static final String POPULARITY = "popularity";
    private static final String POSTER_PATH = "poster_path";
    private static final String ORIG_LANGUAGE = "original_language";
    private static final String ORIG_TITLE = "original_title";
    private static final String LIST_IDS = "genre_ids";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String ADULT = "adult";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";

    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String URL = "url";

    private static final String ISO_639 = "iso_639_1";
    private static final String ISO_3166 = "iso_3166_1";
    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String SITE = "site";
    private static final String SIZE = "size";
    private static final String TYPE = "type";

    /**
     * Parses JSON from web response to MovieRequest object
     * [This code is based on Udacity Nanodegree classes]
     *
     * @param JSonString JSON string from server
     * @return MovieRequest object
     * @throws JSONException if JSON data can't be parsed
     */
    public static MoviesRequest getMoviesFromJSON(String JSonString) throws JSONException {

        JSONObject requestJSon = new JSONObject(JSonString);
        MoviesRequest moviesRequest = new MoviesRequest();

        if (requestJSon.has(MESSAGE_CODE)) {
            int errorCode = requestJSon.getInt(MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    /* Server sent valid data */
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* URL wrong, skip method */
                    return null;
                default:
                    /* Server didn't respond, skip method */
                    return null;
            }
        }

        JSONArray movieResults = requestJSon.getJSONArray(LIST_RESULTS);
        ArrayList<Movie> mMovies;
        mMovies = new ArrayList<>();

        moviesRequest.setmStrPage(requestJSon.getString(PAGE));
        moviesRequest.setmStrTotalResuts(requestJSon.getString(TOTAL_RESULTS));
        moviesRequest.setmStrTotalPages(requestJSon.getString(TOTAL_PAGES));

        for (int i = 0; i < movieResults.length(); i++) {

            JSONObject movieJSon = movieResults.getJSONObject(i);
            Movie movie = new Movie(
                    movieJSon.getString(ID),
                    movieJSon.getString(TITLE),
                    movieJSon.getString(POSTER_PATH),
                    movieJSon.getString(OVERVIEW),
                    movieJSon.getString(VOTE_AVERAGE),
                    movieJSon.getString(RELEASE_DATE),
                    movieJSon.getString(VOTE_COUNT),
                    movieJSon.getString(POPULARITY),
                    movieJSon.getString(VIDEO),
                    movieJSon.getString(ORIG_LANGUAGE),
                    movieJSon.getString(ORIG_TITLE),
                    movieJSon.getString(BACKDROP_PATH),
                    movieJSon.getString(ADULT)
            );
            mMovies.add(movie);
        }
        moviesRequest.setmMovies(mMovies);

        return moviesRequest;
    }

    /**
     * Parses JSON from web response to getReviewsFromJSON object
     * [This code is based on Udacity Nanodegree classes]
     *
     * @param JSonString JSON string from server
     * @return ReviewsRequest object
     * @throws JSONException if JSON data can't be parsed
     */
    public static ReviewsRequest getReviewsFromJSON(String JSonString) throws JSONException {

        JSONObject requestJSon = new JSONObject(JSonString);
        ReviewsRequest reviewsRequest = new ReviewsRequest();

        if (requestJSon.has(MESSAGE_CODE)) {
            int errorCode = requestJSon.getInt(MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    /* Server sent valid data */
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* URL wrong, skip method */
                    return null;
                default:
                    /* Server didn't respond, skip method */
                    return null;
            }
        }

        JSONArray reviewsResults = requestJSon.getJSONArray(LIST_RESULTS);
        ArrayList<Review> mReviews;
        mReviews = new ArrayList<>();

        reviewsRequest.setmId(requestJSon.getString(ID));
        reviewsRequest.setmPages(requestJSon.getString(PAGE));

        for (int i = 0; i < reviewsResults.length(); i++) {

            JSONObject movieJSon = reviewsResults.getJSONObject(i);
            Review review = new Review(
                    movieJSon.getString(AUTHOR),
                    movieJSon.getString(CONTENT),
                    movieJSon.getString(ID),
                    movieJSon.getString(URL)
            );
            mReviews.add(review);
        }
        reviewsRequest.setReviews(mReviews);

        return reviewsRequest;
    }

    /**
     * Parses JSON from web response to getVideosFromJSON object
     * [This code is based on Udacity Nanodegree classes]
     *
     * @param JSonString JSON string from server
     * @return getVideosFromJSON object
     * @throws JSONException if JSON data can't be parsed
     */
    public static ArrayList<Video> getVideosFromJSON(String JSonString) throws JSONException {

        JSONObject requestJSon = new JSONObject(JSonString);

        if (requestJSon.has(MESSAGE_CODE)) {
            int errorCode = requestJSon.getInt(MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    /* Server sent valid data */
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* URL wrong, skip method */
                    return null;
                default:
                    /* Server didn't respond, skip method */
                    return null;
            }
        }

        JSONArray lstVideos = requestJSon.getJSONArray(LIST_RESULTS);
        ArrayList<Video> mVideos;
        mVideos = new ArrayList<>();

        for (int i = 0; i < lstVideos.length(); i++) {

            JSONObject videosJSon = lstVideos.getJSONObject(i);
            Video video = new Video(
                    videosJSon.getString(ID),
                    videosJSon.getString(ISO_639),
                    videosJSon.getString(ISO_3166),
                    videosJSon.getString(KEY),
                    videosJSon.getString(NAME),
                    videosJSon.getString(SITE),
                    videosJSon.getString(SIZE),
                    videosJSon.getString(TYPE)
            );
            mVideos.add(video);
        }
        return mVideos;
    }

}
