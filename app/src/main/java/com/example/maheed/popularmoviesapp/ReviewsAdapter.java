package com.example.maheed.popularmoviesapp;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maheed on 10/22/2016.
 */
public class ReviewsAdapter extends ArrayAdapter<Pair<String,String>> {
    public ReviewsAdapter(Context context) {
        super(context, R.layout.review_list_item
                , new ArrayList<Pair<String, String>>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView == null ? inflater.inflate(R.layout.review_list_item, parent, false) : convertView;
        String reviewAuthor = getItem(position).first;
        TextView reviewAuthorTextView = (TextView) view.findViewById(R.id.textview_review_author);
        reviewAuthorTextView.setText(reviewAuthor);
        String reviewContent = getItem(position).second;
        final TextView reviewContentTextView = (TextView) view.findViewById(R.id.textview_review_content);
        reviewContentTextView.setText(reviewContent);
        return view;
    }
}
