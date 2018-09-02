package com.devandroid.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.devandroid.popularmovies.database.AppDatabase;
import com.devandroid.popularmovies.database.FavoriteEntry;

import java.util.List;

public class DetailsViewModel extends ViewModel {

    public static final String LOG_TAG = DetailsViewModel.class.getSimpleName();

    private LiveData<List<FavoriteEntry>> favoriteEntry;

    public DetailsViewModel(AppDatabase database, String movieId) {
        Log.d(LOG_TAG, "searchTitle DB");
        favoriteEntry = database.FavoriteDAO().searchTitle(movieId);
    }

    public LiveData<List<FavoriteEntry>> getFavoriteEntry() {
        return favoriteEntry;
    }
}
