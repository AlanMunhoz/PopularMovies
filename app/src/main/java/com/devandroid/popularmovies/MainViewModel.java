package com.devandroid.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.devandroid.popularmovies.database.AppDatabase;
import com.devandroid.popularmovies.database.FavoriteEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteEntry>> favoriteEntries;

    public MainViewModel(@NonNull Application application) {
        super(application);

        Log.d("02092018", "loadFavorites from DB");
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favoriteEntries = database.FavoriteDAO().loadFavorites();
    }

    public LiveData<List<FavoriteEntry>> getFavoriteEntries() {
        return favoriteEntries;
    }
}
