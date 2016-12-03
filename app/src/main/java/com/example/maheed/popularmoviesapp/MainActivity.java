package com.example.maheed.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.maheed.popularmoviesapp.Data.Movie;

public class MainActivity extends AppCompatActivity implements SettingsFragment.OnSettingsChangedListener , MoviesFragment.MovieSelectedCallBack {

    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static final String MOVIESFRAGMENT_TAG = "MVTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.container,new MoviesFragment() , MOVIESFRAGMENT_TAG).commit();
        }
        mTwoPane = findViewById(R.id.details_container)!= null;
    }

    @Override
    public void onSettingsChanged() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ((MoviesFragment)fragmentManager.findFragmentByTag(MOVIESFRAGMENT_TAG)).onSettingsChanged();
        Fragment fragment = fragmentManager.findFragmentByTag(DETAILFRAGMENT_TAG);
        if(fragment!=null){
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if(mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putSerializable(getString(R.string.movie_extra), movie);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_container, fragment , DETAILFRAGMENT_TAG)
                    .commit();
        }
        else{
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(getString(R.string.movie_extra), movie);
            startActivity(intent);
        }
    }
}
