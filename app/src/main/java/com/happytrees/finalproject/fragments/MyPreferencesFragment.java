package com.happytrees.finalproject.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.happytrees.finalproject.R;
import com.happytrees.finalproject.database.ResultDB;

import java.util.List;

/**
 * Created by Boris on 3/3/2018.
 */

public class MyPreferencesFragment extends PreferenceFragment {

    public Preference myPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.my_preferences);

        myPreference = findPreference("remove_favourites");

        //LISTEN TO CLICKS ON SPECIFIC PREFERENCE ITEM
        myPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
            //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure ? ( All favourites will be lost)");//dialog message
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete all favourites
                        List<ResultDB> favouritesList = ResultDB.listAll(ResultDB.class);//select all favourites
                        ResultDB.deleteAll(ResultDB.class);
                        Toast.makeText(getActivity(),"Favourites removed",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//closes dialog
                    }
                });
                builder.show();//makes dialog window visible
                return true;
            }
        });
    }
}
