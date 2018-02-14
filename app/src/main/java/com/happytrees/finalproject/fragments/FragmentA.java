package com.happytrees.finalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.happytrees.finalproject.R;
import com.happytrees.finalproject.adapter.NearbyAdapter;
import com.happytrees.finalproject.adapter.TxtAdapter;

import com.happytrees.finalproject.model_nearby_search.NearbyResponse;
import com.happytrees.finalproject.model_nearby_search.NearbyResult;
import com.happytrees.finalproject.model_txt_search.TxtResponse;
import com.happytrees.finalproject.model_txt_search.TxtResult;
import com.happytrees.finalproject.rest.APIClient;
import com.happytrees.finalproject.rest.Endpoint;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TASKS:
//edit text listener
//fish in moscow error


//YOU DON'T HAVE SERIALIZE EVERYTHING ONLY THE OBJECTS YOU WANT TO FETCH IN PARSING.AND YOU DON'T HAVE TO WRITE @SerializedName annotation
// TEXT LINK  -->  https://maps.googleapis.com/maps/api/place/textsearch/json?query=pizza%20in%20jerusaelm&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ
//NEARBY LINK --> https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&keyword=sushi&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ
//IF THERE IS PROBLEM USE "+" INSTEAD OF "%20" -> https://maps.googleapis.com/maps/api/place/textsearch/json?query=pizza+in+Jerusalem&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ


public class FragmentA extends Fragment {


    //FOR NEARBY SEARCH
    String nLocation = "-33.8670522,151.1957362";
    String radius = "500";
    //VARIABLES SHARED BOTH BY SEARCHERS
    String key = "AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ";//no need in decode
    EditText edtSearch;
    String fromEdtTxt;
    boolean txtChecked,nearChecked =false;//both false by default


    public FragmentA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_a, container, false);




        //GO BUTTON
        Button goBtn = (Button)v.findViewById(R.id.goBtn);

        //EditText
        edtSearch = (EditText) v.findViewById(R.id.editTextSearch);
        //GET STRING VALUE FROM EDIT TEXT


        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromEdtTxt = edtSearch.getText().toString();//keep txt written in EditText inside fromEdtTxt variable
                //NOTHING SELECTED
                if(!txtChecked&&!nearChecked) {
                    Toast.makeText(getActivity(),"please choose an option",Toast.LENGTH_SHORT).show();
                    //TXT SELECTED
                }else if (txtChecked&&!nearChecked) {
                    Log.e("TAG", fromEdtTxt+"A");

                    //NEARBY SELECTED
                }else if (!txtChecked&&nearChecked){
                    Log.e("TAG",fromEdtTxt+"B");
                }
            }
        });

        //CLEAN BUTTON
        Button cleanBtn = (Button)v.findViewById(R.id.cleanBtn);
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText(" ");
            }
        });


        //setting RecyclerView
        final RecyclerView fragArecycler = (RecyclerView) v.findViewById(R.id.recyclerSearch);

        //connect the retrofit class with the interface class
        //generate new instance of the interface and call it service
        final Endpoint apiService = APIClient.getClient().create(Endpoint.class);
        //setting radio group
        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);//RadioGroup ensures that only one radio button can be selected at a time.
        //make radio group "listen" to changes in clicked radio buttons


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {   // checkedId is the RadioButton selected


                switch (checkedId) {

                    case R.id.radioButtonTxtSearch:
                        txtChecked =true;
                        nearChecked = false;
                        break;


                    case R.id.radioButtonNearbySearch:
                        //NEARBY SEARCH -- NEEDS TO BE FIXED

                        txtChecked = false;
                        nearChecked = true;
                        break;


                }


            }

        });
        return v;
    }

}
