package com.example.maheed.popularmoviesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle(getString(R.string.movie_details));
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(getString(R.string.movie_extra), getIntent().getSerializableExtra(getString(R.string.movie_extra)));

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, fragment)
                    .commit();
        }

    }
}
