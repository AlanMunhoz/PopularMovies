package com.devandroid.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String mStrVoteCount;
    private String mStrId;
    private String mStrVideo;
    private String mStrVoteAverage;
    private String mStrTitle;
    private String mStrPopularity;
    private String mStrPosterPath;
    private String mStrOriginalLanguage;
    private String mStrOriginalTitle;
    private String[] mLstGenreIds;
    private String mStrBackdropPath;
    private String mStrAdult;
    private String mStrOverview;
    private String mStrReleaseDate;
    private String mStrFullPosterPathUrl;

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in) {

        mStrId = in.readString();
        mStrTitle = in.readString();
        mStrPosterPath = in.readString();
        mStrOverview = in.readString();
        mStrVoteAverage = in.readString();
        mStrReleaseDate = in.readString();
        mStrVoteCount = in.readString();
        mStrPopularity = in.readString();
        mStrVideo = in.readString();
        mStrOriginalLanguage = in.readString();
        mStrOriginalTitle = in.readString();
        mStrBackdropPath = in.readString();
        mStrAdult = in.readString();
    }

    public Movie(
            String strId,
            String strTitle,
            String strPosterPath,
            String strOverview,
            String strVoteAverage,
            String strReleaseDate,
            String strVoteCount,
            String strPopularity,
            String strVideo,
            String strOriginalLanguage,
            String strOriginalTitle,
            String strBackdropPath,
            String strAdult
            ) {

        mStrId = strId;
        mStrTitle = strTitle;
        mStrPosterPath = strPosterPath;
        mStrOverview = strOverview;
        mStrVoteAverage = strVoteAverage;
        mStrReleaseDate = strReleaseDate;
        mStrVoteCount = strVoteCount;
        mStrPopularity = strPopularity;
        mStrVideo = strVideo;
        mStrOriginalLanguage = strOriginalLanguage;
        mStrOriginalTitle = strOriginalTitle;
        mStrBackdropPath = strBackdropPath;
        mStrAdult = strAdult;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mStrId);
        parcel.writeString(mStrTitle);
        parcel.writeString(mStrPosterPath);
        parcel.writeString(mStrOverview);
        parcel.writeString(mStrVoteAverage);
        parcel.writeString(mStrReleaseDate);
        parcel.writeString(mStrVoteCount);
        parcel.writeString(mStrPopularity);
        parcel.writeString(mStrVideo);
        parcel.writeString(mStrOriginalLanguage);
        parcel.writeString(mStrOriginalTitle);
        parcel.writeString(mStrBackdropPath);
        parcel.writeString(mStrAdult);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public String getmStrVoteCount() { return mStrVoteCount; }
    public void setmStrVoteCount(String mStrVoteCount) { this.mStrVoteCount = mStrVoteCount; }
    public String getmStrId() { return mStrId; }
    public void setmStrId(String mStrId) { this.mStrId = mStrId; }
    public String getmStrVideo() { return mStrVideo; }
    public void setmStrVideo(String mStrVideo) { this.mStrVideo = mStrVideo; }
    public String getmStrVoteAverage() { return mStrVoteAverage; }
    public void setmStrVoteAverage(String mStrVoteAverage) { this.mStrVoteAverage = mStrVoteAverage; }
    public String getmStrTitle() { return mStrTitle; }
    public void setmStrTitle(String mStrTitle) { this.mStrTitle = mStrTitle; }
    public String getmStrPopularity() { return mStrPopularity; }
    public void setmStrPopularity(String mStrPopularity) { this.mStrPopularity = mStrPopularity; }
    public String getmStrPosterPath() { return mStrPosterPath; }
    public void setmStrPosterPath(String mStrPosterPath) { this.mStrPosterPath = mStrPosterPath; }
    public String getmStrOriginalLanguage() { return mStrOriginalLanguage; }
    public void setmStrOriginalLanguage(String mStrOriginalLanguage) { this.mStrOriginalLanguage = mStrOriginalLanguage; }
    public String getmStrOriginalTitle() { return mStrOriginalTitle; }
    public void setmStrOriginalTitle(String mStrOriginalTitle) { this.mStrOriginalTitle = mStrOriginalTitle; }
    public String[] getmLstGenreIds() { return mLstGenreIds; }
    public void setmLstGenreIds(String[] mLstGenreIds) { this.mLstGenreIds = mLstGenreIds; }
    public String getmStrBackdropPath() { return mStrBackdropPath; }
    public void setmStrBackdropPath(String mStrBackdropPath) { this.mStrBackdropPath = mStrBackdropPath; }
    public String getmStrAdult() { return mStrAdult; }
    public void setmStrAdult(String mStrAdult) { this.mStrAdult = mStrAdult; }
    public String getmStrOverview() { return mStrOverview; }
    public void setmStrOverview(String mStrOverview) { this.mStrOverview = mStrOverview; }
    public String getmStrReleaseDate() { return mStrReleaseDate; }
    public void setmStrReleaseDate(String mStrReleaseDate) { this.mStrReleaseDate = mStrReleaseDate; }
    public String getmStrFullPosterPathUrl() { return mStrFullPosterPathUrl; }
    public void setmStrFullPosterPathUrl(String mStrFullPosterPathUrl) { this.mStrFullPosterPathUrl = mStrFullPosterPathUrl; }
}
