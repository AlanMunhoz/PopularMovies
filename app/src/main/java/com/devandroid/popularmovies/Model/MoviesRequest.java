package com.devandroid.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MoviesRequest implements Parcelable {

    private String mStrPage;
    private String mStrTotalResuts;
    private String mStrTotalPages;
    private ArrayList<Movie> mMovies;

    public static final Creator<MoviesRequest> CREATOR = new Creator<MoviesRequest>() {
        @Override
        public MoviesRequest createFromParcel(Parcel in) {
            return new MoviesRequest(in);
        }

        @Override
        public MoviesRequest[] newArray(int size) {
            return new MoviesRequest[size];
        }
    };

    public MoviesRequest(Parcel in) {
        mStrPage = in.readString();
        mStrTotalResuts = in.readString();
        mStrTotalPages = in.readString();
        mMovies = in.createTypedArrayList(Movie.CREATOR);
    }

    public MoviesRequest() {
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mStrPage);
        parcel.writeString(mStrTotalResuts);
        parcel.writeString(mStrTotalPages);
        parcel.writeTypedList(mMovies);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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
