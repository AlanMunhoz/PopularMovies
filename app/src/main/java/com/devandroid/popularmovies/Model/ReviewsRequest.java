package com.devandroid.popularmovies.Model;

import java.util.ArrayList;

public class ReviewsRequest {

    private String mId;
    private String mPages;
    private ArrayList<Review> mReviews;

    public ArrayList<Review> getReviews() { return mReviews; }
    public void setReviews(ArrayList<Review> mReviews) { this.mReviews = mReviews; }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setmPages(String mPages) {
        this.mPages = mPages;
    }

    public int getSize() {
        if(mReviews == null) return 0;
        return mReviews.size();
    }

    public Review getItem(int index) {
        if(mReviews == null) return null;
        return mReviews.get(index);
    }
}
