package com.example.maheed.popularmoviesapp.Data;

import java.io.Serializable;

/**
 * Created by Maheed on 10/21/2016.
 */
public class Movie implements Serializable{
    private String mTitle;
    private String mPosterUrl;
    private String mPlot;
    private double mRating;
    private String mReleaseDate;
    private String mId;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getPlot() {
        return mPlot;
    }

    public Movie(String title , String releaseDate, String plot, String posterUrl, double rating , String id) {
        this.mReleaseDate = releaseDate;
        this.mPlot = plot;
        this.mPosterUrl = posterUrl;
        this.mRating = rating;
        this.mTitle = title;
        this.mId = id;
    }

    public void setPlot(String mPlot) {
        this.mPlot = mPlot;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(String mPosterURL) {
        this.mPosterUrl = mPosterURL;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
