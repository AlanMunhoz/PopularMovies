package com.devandroid.popularmovies.Model;

public class Review {

    private String mAuthor;
    private String mContent;
    private String mId;
    private String mUrl;

    public Review(String strAuthor, String strContent, String strId, String strUrl) {

        mAuthor = strAuthor;
        mContent = strContent;
        mId = strId;
        mUrl = strUrl;
    }

    public String getmAuthor() { return mAuthor; }
    public String getmContent() { return mContent; }
    public String getmId() { return mId; }
    public String getmUrl() { return mUrl; }
}
