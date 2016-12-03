package com.example.maheed.popularmoviesapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.example.maheed.popularmoviesapp.Data.Movie;
import com.example.maheed.popularmoviesapp.Data.MovieContract;
import com.example.maheed.popularmoviesapp.Data.MovieDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Created by Maheed on 10/21/2016.
 */
public class FetchMoviesTask extends AsyncTask<String,Void,ArrayList<Movie>> {
    private Context mContext;
    private MoviesAdapter mAdapter;
    ProgressDialog mProgDialog;
    Callable<Void> mCallback;
    final private String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    FetchMoviesTask(Context context , MoviesAdapter adapter , ProgressDialog progDialog , Callable<Void> callback){
        mContext = context;
        mAdapter = adapter;
        mProgDialog = progDialog;
        mCallback = callback;
    }
    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        final String SORT_ORDER = params[0];
        if(SORT_ORDER.equals(mContext.getString(R.string.favorites))){
            return MovieDbHelper.getFavorites(mContext);
        }
        if (!isOnline()) {

            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;
        try {
            final String MOVIES_DB_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String API_KEY_PARAM = "api_key";
            Uri builtUri = Uri.parse(MOVIES_DB_BASE_URL).buildUpon().appendPath(SORT_ORDER).
                    appendQueryParameter(API_KEY_PARAM , MovieContract.MOVIE_DB_API_KEY).build();
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            StringBuffer buffer = new StringBuffer();
            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream == null){
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            line = reader.readLine();
            while(line != null){
                buffer.append(line).append("\n");
                line = reader.readLine();
            }
            if(buffer.length() == 0){
                return null;
            }
            moviesJsonStr = buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection!= null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Movie> getMoviesDataFromJson (String moviesJsonStr) throws JSONException {
        final String MDB_RESULTS = "results";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342/";
        final String MDB_TITLE = "original_title";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_PLOT = "overview";
        final String MDB_ID = "id";
        final String MDB_RATING = "vote_average";
        JSONObject jsonObject = new JSONObject(moviesJsonStr);
        JSONArray movieResults = jsonObject.getJSONArray(MDB_RESULTS);
        ArrayList<Movie> results = new ArrayList<>();
        for(int i = 0; i < movieResults.length(); i++){
            JSONObject movieObject = movieResults.getJSONObject(i);
            String posterUrl = MDB_POSTER_BASE_URL + movieObject.getString(MDB_POSTER_PATH);
            String title = movieObject.getString(MDB_TITLE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date releaseDate = null;
            try {
                releaseDate = simpleDateFormat.parse(movieObject.getString(MDB_RELEASE_DATE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String plot = movieObject.getString(MDB_PLOT);
            double rating = movieObject.getDouble(MDB_RATING);
            String id = movieObject.getString(MDB_ID);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(releaseDate);
            Movie movie = new Movie(title, Integer.toString(calendar.get(Calendar.YEAR)), plot, posterUrl, rating , id);
            results.add(movie);
        }
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> results) {
        mAdapter.clear();
        if(results!=null){
            for(Movie result :results){
                mAdapter.add(result);
            }
        }
        else{
            mProgDialog.dismiss();
            new AlertDialog.Builder(mContext)
                    .setTitle("Info")
                    .setMessage("Internet not available. Please check your internet connectivity.")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        mProgDialog.dismiss();
        try {
            mCallback.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
