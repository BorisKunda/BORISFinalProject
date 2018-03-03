package com.happytrees.finalproject.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.happytrees.finalproject.R;

/**
 * Created by Boris on 3/3/2018.
 */

public class MyPreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.my_preferences);
    }
}
