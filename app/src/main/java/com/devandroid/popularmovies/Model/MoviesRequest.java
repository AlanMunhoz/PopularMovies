package com.devandroid.popularmovies.Model;

import java.util.ArrayList;

public class MoviesRequest {

    private String mStrPage;
    private String mStrTotalResuts;
    private String mStrTotalPages;
    private ArrayList<Movie> mMovies;

    public String getmStrPage() { return mStrPage; }
    public void setmStrPage(String mStrPage) { this.mStrPage = mStrPage; }
    public String getmStrTotalResuts() { return mStrTotalResuts; }
    public void setmStrTotalResuts(String mStrTotalResuts) { this.mStrTotalResuts = mStrTotalResuts; }
    public String getmStrTotalPages() { return mStrTotalPages; }
    public void setmStrTotalPages(String mStrTotalPages) { this.mStrTotalPages = mStrTotalPages; }
    public ArrayList<Movie> getmMovies() { return mMovies; }
    public void setmMovies(ArrayList<Movie> mMovies) { this.mMovies = mMovies; }

    public int getSize() {
        if(mMovies == null) return 0;
        return mMovies.size();
    }

    public Movie getItem(int index) {
        if(mMovies == null) return null;
        return mMovies.get(index);
    }
}
