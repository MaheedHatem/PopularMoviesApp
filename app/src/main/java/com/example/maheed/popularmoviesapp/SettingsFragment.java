package com.example.maheed.popularmoviesapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    public interface OnSettingsChangedListener {
        void onSettingsChanged();
    }

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        FloatingActionButton closeButton = (FloatingActionButton) view.findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        ListView settingsListView = (ListView)view.findViewById(R.id.listview_settings);
        settingsListView.setAdapter(new SettingsAdapter(getContext()));
        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                String sortSetting = (String)parent.getAdapter().getItem(position);
                editor.putString(getString(R.string.pref_sort_key) , sortSetting);
                editor.apply();
                ((OnSettingsChangedListener)getActivity()).onSettingsChanged();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

}
