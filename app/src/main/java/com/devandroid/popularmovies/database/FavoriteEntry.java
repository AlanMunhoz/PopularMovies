package com.devandroid.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.devandroid.popularmovies.Model.Movie;

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
    private String voteAverage;
    private String releaseDate;
    private String voteCount;
    private String popularity;
    private String overview;

    /**
     * Used by us to create a table on first time
     */
    @Ignore
    public FavoriteEntry(
            String title,
            String posterPath,
            String movieId,
            String voteAverage,
            String releaseDate,
            String voteCount,
            String popularity,
            String overview) {

        this.title = title;
        this.posterPath = posterPath;
        this.movieId = movieId;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
        this.popularity = popularity;
        this.overview = overview;
    }

    /**
     * Used by Room on read time
     */
    public FavoriteEntry(
            int id,
            String title,
            String posterPath,
            String movieId,
            String voteAverage,
            String releaseDate,
            String voteCount,
            String popularity,
            String overview) {

        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.movieId = movieId;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
        this.popularity = popularity;
        this.overview = overview;

    }


    public Movie getMovie() {

        return new Movie(
                movieId,
                title,
                posterPath,
                overview,
                voteAverage,
                releaseDate,
                voteCount,
                popularity,
                "",
                "",
                "",
                "",
                "");
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
