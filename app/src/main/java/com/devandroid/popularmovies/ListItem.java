package com.devandroid.popularmovies;

public class ListItem {

    private String mURL;

    ListItem(String text, String urlFigure) {
        mURL = urlFigure;
    }

    String getFigure() {
        return mURL;
    }
}
