package com.devandroid.popularmovies.Model;

public class Video {

    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    private String mId;
    private String mIso_639_1;
    private String mIso_3166_1;
    private String mKey;
    private String mName;
    private String mSite;
    private String mSize;
    private String mType;

    public Video(
            String strId,
            String strIso_639_1,
            String strIso_3166_1,
            String strKey,
            String strName,
            String strSite,
            String strSize,
            String strType
    ) {

        mId = strId;
        mIso_639_1 = strIso_639_1;
        mIso_3166_1 = strIso_3166_1;
        mKey = strKey;
        mName = strName;
        mSite = strSite;
        mSize = strSize;
        mType = strType;
    }

    public String getName() {
        return mName;
    }

    public String getYoutubeUrl() {

        if(mSite.equals("YouTube")) {
            return BASE_YOUTUBE_URL + mKey;
        }
        return "";
    }

}
