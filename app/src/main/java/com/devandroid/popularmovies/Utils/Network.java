package com.devandroid.popularmovies.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class Network {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOST_POPULAR_SEARCH = "movie/popular?api_key=";
    public static final String TOP_RATED_SEARCH = "movie/top_rated?api_key=";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/";
    public static final String IMAGE_POSTER_SIZE_185PX = "/w185/";
    public static final String IMAGE_POSTER_SIZE_780PX = "/w780/";
    public static String API_KEY;

    public static String VIDEOS_URL(String id) { return BASE_URL + "movie/"+id+"/videos"; }
    public static String REVIEWS_URL(String id) { return BASE_URL + "movie/"+id+"/reviews"; }

    /**
     * Stores the user api key
     * @param strKey
     */
    public static void setApiKey(String strKey) {
        API_KEY = strKey;
    }

    /**
     * Builds the URL used to get information about movies.
     * [This code is based on Udacity Nanodegree classes]
     *
     * @param search The path to connect in
     * @return The URL to connect to server
     */
    public static URL buildUrl(String search) {

        Uri builtUri = Uri.parse(BASE_URL + search + API_KEY).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Returns the HTTP response.
     * [This code is based on Udacity Nanodegree classes]
     *
     * @param url to fetch response from HTTP request
     * @return HTTP contents
     * @throws IOException State of request reading
     */
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
}
