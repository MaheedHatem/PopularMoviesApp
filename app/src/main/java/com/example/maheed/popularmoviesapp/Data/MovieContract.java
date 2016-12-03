package com.example.maheed.popularmoviesapp.Data;

import android.provider.BaseColumns;

/**
 * Created by Maheed on 11/5/2016.
 */
public class MovieContract {

    public static String MOVIE_DB_API_KEY = "INSERT_API_KEY_HERE";
    public static final class FavoriteMovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorite_movie";

        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
