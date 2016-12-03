package com.example.maheed.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Maheed on 10/22/2016.
 */
public class SettingsAdapter extends ArrayAdapter<String> {
    public SettingsAdapter(Context context) {
        super(context , R.layout.settings_list_item
                , context.getResources().getStringArray(R.array.sort_settings_array));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView = convertView==null?inflater.inflate(R.layout.settings_list_item , parent , false):convertView;
        String settingText = getItem(position);
        ImageView settingsIcon = (ImageView)gridView.findViewById(R.id.settings_icon);

        if(settingText.equals(getContext().getString(R.string.sort_most_popular_setting))){
            settingsIcon.setImageResource(R.drawable.ic_most_popular);
        }
        else if(settingText.equals(getContext().getString(R.string.sort_highest_rated_setting))){
            settingsIcon.setImageResource(R.drawable.ic_highest_rated);
        }
        else if(settingText.equals(getContext().getString(R.string.sort_favorites_setting))){
            settingsIcon.setImageResource(R.drawable.ic_favorites);
        }
        TextView settingsTextView = (TextView) gridView.findViewById(R.id.textview_setting);
        settingsTextView.setText(settingText);
        return gridView;
    }
}
