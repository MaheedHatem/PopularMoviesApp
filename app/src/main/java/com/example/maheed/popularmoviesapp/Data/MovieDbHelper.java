package com.example.maheed.popularmoviesapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Maheed on 11/5/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                " UNIQUE (" + MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static boolean isFavorite(Movie movie, Context context) {
        if(movie == null){
            return false;
        }
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(MovieContract.FavoriteMovieEntry.TABLE_NAME, null,
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=" + movie.getId(), null, null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        db.close();
        dbHelper.close();
        return exists;
    }

    public static void addFavoriteMovie(Movie movie, Context context) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_PLOT, movie.getPlot());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_URL, movie.getPosterUrl());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_RATING, movie.getRating());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
        db.close();
        dbHelper.close();
    }

    public static void deleteFavoriteMovie(Movie movie, Context context) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=" + movie.getId(), null);
        db.close();
        dbHelper.close();
    }
    private static String[] FAVORITE_MOVIE_COLUMNS = {MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
            MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE, MovieContract.FavoriteMovieEntry.COLUMN_PLOT,
            MovieContract.FavoriteMovieEntry.COLUMN_POSTER_URL, MovieContract.FavoriteMovieEntry.COLUMN_RATING,
            MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE};
    private static int COLUMN_MOVIE_ID = 0;
    private static int COLUMN_MOVIE_TITLE = 1;
    private static int COLUMN_PLOT = 2;
    private static int COLUMN_POSTER_URL = 3;
    private static int COLUMN_RATING = 4;
    private static int COLUMN_RELEASE_DATE = 5;
    public static ArrayList<Movie> getFavorites(Context context) {
        ArrayList<Movie> favorites = new ArrayList<>();
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(MovieContract.FavoriteMovieEntry.TABLE_NAME, FAVORITE_MOVIE_COLUMNS,
                null, null, null, null, MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID);
        if (c.moveToFirst()) {
            favorites.add(new Movie(
                    c.getString(COLUMN_MOVIE_TITLE),
                    c.getString(COLUMN_RELEASE_DATE),
                    c.getString(COLUMN_PLOT),
                    c.getString(COLUMN_POSTER_URL),
                    c.getDouble(COLUMN_RATING),
                    c.getString(COLUMN_MOVIE_ID)
            ));
        }
        while(c.moveToNext()){
            favorites.add(new Movie(
                    c.getString(COLUMN_MOVIE_TITLE),
                    c.getString(COLUMN_RELEASE_DATE),
                    c.getString(COLUMN_PLOT),
                    c.getString(COLUMN_POSTER_URL),
                    c.getDouble(COLUMN_RATING),
                    c.getString(COLUMN_MOVIE_ID)
            ));
        }
        c.close();
        db.close();
        dbHelper.close();
        return favorites;
    }
}
