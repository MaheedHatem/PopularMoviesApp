package com.example.maheed.popularmoviesapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Maheed on 10/22/2016.
 */
public class FetchMoviesDataTask extends AsyncTask<String, Void, ArrayList<Pair<String, String>>> {
    private Context mContext;
    private ArrayAdapter mAdapter;
    public static final String VIDEOS_PARAM = "videos";
    public static final String REVIEWS_PARAM = "reviews";
    final private String LOG_TAG = FetchMoviesDataTask.class.getSimpleName();

    FetchMoviesDataTask(Context context, ArrayAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
    }

    @Override
    protected ArrayList<Pair<String, String>> doInBackground(String... params) {
        if (!isOnline()) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;
        final String MOVIE_ID = params[0];
        final String PATH_PARAM = params[1];
        try {
            final String MOVIES_DB_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String API_KEY_PARAM = "api_key";
            Uri builtUri = Uri.parse(MOVIES_DB_BASE_URL).buildUpon().appendPath(MOVIE_ID).
                    appendPath(PATH_PARAM).
                    appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY).build();
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            StringBuffer buffer = new StringBuffer();
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            line = reader.readLine();
            while (line != null) {
                buffer.append(line).append("\n");
                line = reader.readLine();
            }
            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();
            Log.v(LOG_TAG, "MoviesJsonString: " + jsonStr);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            return getTrailersDataFromJson(jsonStr, PATH_PARAM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Pair<String, String>> getTrailersDataFromJson(String jsonStr, final String pathParam) throws JSONException {
        final String MDB_RESULTS = "results";
        final String MDB_VIDEO_NAME = "name";
        final String MDB_VIEDO_KEY = "key";
        final String MDB_REVIEW_AUTHOR = "author";
        final String MDB_REVIEW_CONTENT = "content";
        final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch";
        final String YOUTUBE_V_PARAM = "v";
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray dataResults = jsonObject.getJSONArray(MDB_RESULTS);
        ArrayList<Pair<String, String>> results = new ArrayList<>();
        for (int i = 0; i < dataResults.length(); i++) {
            JSONObject objet = dataResults.getJSONObject(i);
            if (pathParam.equals(VIDEOS_PARAM)) {
                String posterUrl = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                        .appendQueryParameter(YOUTUBE_V_PARAM, objet.getString(MDB_VIEDO_KEY)).build().toString();
                String videoName = objet.getString(MDB_VIDEO_NAME);
                results.add(new Pair<String, String>(videoName, posterUrl));
            } else if (pathParam.equals(REVIEWS_PARAM)) {
                String reviewName = objet.getString(MDB_REVIEW_AUTHOR);
                String reviewContent = objet.getString(MDB_REVIEW_CONTENT);
                results.add(new Pair<String, String>(reviewName, reviewContent));
            }
        }
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<Pair<String, String>> results) {
        mAdapter.clear();
        if (results != null) {
            for (Pair<String, String> result : results) {
                mAdapter.add(result);
            }
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
