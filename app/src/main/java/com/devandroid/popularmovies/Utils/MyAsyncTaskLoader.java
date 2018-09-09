package com.devandroid.popularmovies.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;


/**
 * AsyncTaskLoader class
 */
public class MyAsyncTaskLoader extends AsyncTaskLoader<String> {

    private final WeakReference<Bundle> args;
    private final WeakReference<String> strBundle;

    public MyAsyncTaskLoader(WeakReference<Activity> ctx, Bundle args, String strBundle) {
        super(ctx.get());

        this.args = new WeakReference<>(args);
        this.strBundle = new WeakReference<>(strBundle);
    }

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
        String searchUrl = args.get().getString(strBundle.get());
        URL searchQueryUrlString = Network.buildUrl(searchUrl);

        try {
            SearchResults = Network.getResponseFromHttpUrl(searchQueryUrlString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SearchResults;
    }

}
