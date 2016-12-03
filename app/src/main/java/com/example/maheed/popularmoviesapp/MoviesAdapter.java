package com.example.maheed.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.maheed.popularmoviesapp.Data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Maheed on 10/21/2016.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {
    public MoviesAdapter(Context context , ArrayList<Movie> moviesList) {
        super(context, R.layout.grid_item_view , moviesList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView = convertView==null?inflater.inflate(R.layout.grid_item_view , parent , false):convertView;
        ImageView movieIconImageView = (ImageView)gridView.findViewById(R.id.movie_icon);
        Picasso.with(getContext()).load(getItem(position).getPosterUrl()).into(movieIconImageView);
        return gridView;
    }
}
