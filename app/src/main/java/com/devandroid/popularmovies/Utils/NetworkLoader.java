package com.devandroid.popularmovies.Utils;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.net.URL;

public class NetworkLoader extends AsyncTaskLoader {

    private Boolean mAlreadyCreated = false;
    private URL mUrl;

    public NetworkLoader(Context context, URL url) {
        super(context);

        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        mAlreadyCreated = true;
    }

    @Override
    public String loadInBackground() {

        String SearchResults = null;

        try {
            SearchResults = Network.getResponseFromHttpUrl(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SearchResults;
    }

    public Boolean isAlreadyCreated() {
        return mAlreadyCreated;
    }
}
