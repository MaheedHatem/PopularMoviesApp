package com.example.maheed.popularmoviesapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.maheed.popularmoviesapp.Data.Movie;
import com.example.maheed.popularmoviesapp.Data.MovieContract;

import java.util.ArrayList;
import java.util.concurrent.Callable;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    private MoviesAdapter mAdapter;
    private String mSortSetting;
    final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_POSITION = "selected_position";
    private GridView mMoviesGridView;

    public interface MovieSelectedCallBack {

        void onItemSelected(Movie movie);
    }

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mMoviesGridView = (GridView) view.findViewById(R.id.gridview_movies);
        mAdapter = new MoviesAdapter(getActivity(), new ArrayList<Movie>());
        mMoviesGridView.setAdapter(mAdapter);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.button_sort);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new SettingsFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null).commit();
            }
        });
        mMoviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MovieSelectedCallBack) getActivity()).onItemSelected((Movie) parent.getAdapter().getItem(position));
                mPosition = position;
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_POSITION)) {
            mPosition = savedInstanceState.getInt(SELECTED_POSITION);
        }
        if(MovieContract.MOVIE_DB_API_KEY.equals("INSERT_API_KEY_HERE")){
            new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setTitle("Error")
                    .setMessage("Please change the API_KEY as specified in the README file")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else{
            updateMovies();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_POSITION, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    private void updateMovies() {
        final ProgressDialog progDialog = ProgressDialog.show(getActivity(),
                "Loading Movies",
                "....please wait....", true);
        FetchMoviesTask task = new FetchMoviesTask(getActivity(), mAdapter, progDialog, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (mPosition != ListView.INVALID_POSITION) {
                    mMoviesGridView.smoothScrollToPosition(mPosition);
                }
                return null;
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSortSetting = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.sort_most_popular_setting));
        String sortOrder = "";
        if (mSortSetting.equals(getString(R.string.sort_highest_rated_setting))) {
            sortOrder = getString(R.string.top_rated);
            getActivity().setTitle(getString(R.string.top_rated_movies));
        } else if (mSortSetting.equals(getString(R.string.sort_favorites_setting))) {
            sortOrder = getString(R.string.favorites);
            getActivity().setTitle(getString(R.string.favorite_movies));
        } else {
            sortOrder = getString(R.string.most_popular);
            getActivity().setTitle(getString(R.string.most_popular_movies));
        }
        task.execute(sortOrder);
    }


    public void onSettingsChanged() {
        updateMovies();
    }
}
