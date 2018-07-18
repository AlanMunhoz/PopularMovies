package com.devandroid.popularmovies;

public class ListItem {

    private String mTitle;
    private String mURL;

    ListItem(String text, String urlFigure) {
        mTitle = text;
        mURL = urlFigure;
    }

    String getTitle() {
        return mTitle;
    }

    String getFigure() {
        return mURL;
    }
}
