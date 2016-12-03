package com.example.maheed.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * Created by Maheed on 10/22/2016.
 */
public class TrailersAdapter extends ArrayAdapter<Pair<String,String>> {
    private static final String LOG_TAG = TrailersAdapter.class.getSimpleName();
    public TrailersAdapter(Context context) {
        super(context ,R.layout.trailer_list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView == null ? inflater.inflate(R.layout.trailer_list_item, parent, false) : convertView;
        String trailerName = getItem(position).first;
        final String trailerUrl = getItem(position).second;
        TextView trailerNameTextView = (TextView) view.findViewById(R.id.textview_trailer_name);
        trailerNameTextView.setText(trailerName);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl)));
                Log.v(LOG_TAG , trailerUrl);
            }
        });
        return view;
    }

}
