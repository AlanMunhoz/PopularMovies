package com.devandroid.popularmovies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.devandroid.popularmovies.database.AppDatabase;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final String movieId;

    public DetailsViewModelFactory(AppDatabase database, String movieId) {
        mDb = database;
        this.movieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new DetailsViewModel(mDb, movieId);
    }
}
