package com.example.maheed.popularmoviesapp;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maheed.popularmoviesapp.Data.Movie;
import com.example.maheed.popularmoviesapp.Data.MovieDbHelper;
import com.squareup.picasso.Picasso;



/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        final Movie movie = (Movie) getArguments().getSerializable(getContext().getString(R.string.movie_extra));

        TextView movieTitleTextView = (TextView) view.findViewById(R.id.textview_movie_title);
        ImageView movieIconImageView = (ImageView) view.findViewById(R.id.movie_icon);
        TextView dateTextView = (TextView) view.findViewById(R.id.textview_date);
        TextView ratingTextView = (TextView) view.findViewById(R.id.textview_rating);
        TextView plotTextView = (TextView) view.findViewById(R.id.textview_plot);
        ListView trailersListView = (NonScrollListView) view.findViewById(R.id.listview_trailers);
        ListView reviewsListView = (NonScrollListView) view.findViewById(R.id.listview_reviews);
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.button_favorite);

        movieTitleTextView.setText(movie.getTitle());
        Picasso.with(getContext()).load(movie.getPosterUrl()).into(movieIconImageView);
        dateTextView.setText(movie.getReleaseDate());
        ratingTextView.setText(String.format(getString(R.string.format_rating), movie.getRating()));
        plotTextView.setText(movie.getPlot());
        TrailersAdapter trailersAdapter = new TrailersAdapter(getActivity());
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity());
        trailersListView.setAdapter(trailersAdapter);
        reviewsListView.setAdapter(reviewsAdapter);

        if(!isOnline()){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Info")
                    .setMessage("Internet not available. Please check your internet connectivity.")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else{
            FetchMoviesDataTask fetchTask = new FetchMoviesDataTask(getContext(), trailersAdapter );
            FetchMoviesDataTask fetchReviewsTask = new FetchMoviesDataTask(getContext(), reviewsAdapter);
            fetchTask.execute(movie.getId(), FetchMoviesDataTask.VIDEOS_PARAM);
            fetchReviewsTask.execute(movie.getId(), FetchMoviesDataTask.REVIEWS_PARAM);
        }
        if(MovieDbHelper.isFavorite(movie,getActivity())) {
            fab.setImageResource(R.drawable.ic_favorited);
        }
        else{
            fab.setImageResource(R.drawable.ic_unfavorited);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MovieDbHelper.isFavorite(movie,getActivity())) {
                    MovieDbHelper.deleteFavoriteMovie(movie, getActivity());
                    fab.setImageResource(R.drawable.ic_unfavorited);
                }
                else{
                    MovieDbHelper.addFavoriteMovie(movie, getActivity());
                    fab.setImageResource(R.drawable.ic_favorited);
                }
            }
        });
        return view;
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}

