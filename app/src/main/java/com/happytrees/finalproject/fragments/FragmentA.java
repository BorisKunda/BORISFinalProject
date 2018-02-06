package com.happytrees.finalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.happytrees.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentA extends Fragment {


    public FragmentA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_a, container, false);
        //setting radio group
        RadioGroup radioGroup = (RadioGroup) v .findViewById(R.id.radioGroup);//RadioGroup ensures that only one radio button can be selected at a time.
        //make radio group "listen" to changes in clicked radio buttons
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.radioButtonTxtSearch:
                        Toast.makeText(getContext()," text search selected",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonNearbySearch:
                        Toast.makeText(getContext(),"nearby search selected",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return v;
    }

}
