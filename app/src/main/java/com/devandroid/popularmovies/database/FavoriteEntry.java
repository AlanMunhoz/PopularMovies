package com.devandroid.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * POJO - Plain Old Java Object
 */

@Entity(tableName = "Favorite")
public class FavoriteEntry {

    /**
     * Columns of table
     */
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String posterPath;
    private String movieId;

    /**
     * Used by us to create a table on first time
     */
    @Ignore
    public FavoriteEntry(String title, String posterPath, String movieId) {

        this.title = title;
        this.posterPath = posterPath;
        this.movieId = movieId;
    }

    /**
     * Used by Room on read time
     */
    public FavoriteEntry(int id, String title, String posterPath, String movieId) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.movieId = movieId;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return posterPath; }

    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getMovieId() { return movieId; }

    public void setMovieId(String movieId) { this.movieId = movieId; }
}
