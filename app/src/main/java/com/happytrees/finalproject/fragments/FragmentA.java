package com.happytrees.finalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.happytrees.finalproject.R;
import com.happytrees.finalproject.adapter.TxtAdapter;

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

//YOU DON'T HAVE SERIALIZE EVERYTHING ONLY THE OBJECTS YOU WANT TO FETCH IN PARSING.AND YOU DON'T HAVE TO WRITE @SerializedName annotation
//LINK  -->  https://maps.googleapis.com/maps/api/place/textsearch/json?query=pizza%20in%20jerusaelm&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ
//IF THERE IS PROBLEM USE "+" INSTEAD OF "%20" -> https://maps.googleapis.com/maps/api/place/textsearch/json?query=pizza+in+Jerusalem&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ
public class FragmentA extends Fragment {




    String query = "pizza%20in%20jerusalem";
    String key = "AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ";
    String decodedQuery;

    public FragmentA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_a, container, false);

        //URL DECODING
        try {
            decodedQuery = java.net.URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //setting RecyclerView
        final RecyclerView fragArecycler= (RecyclerView) v.findViewById(R.id.recyclerSearch);

        //connect the retrofit class with the interface class
        //generate new instance of the interface and call it service
        final Endpoint apiService = APIClient.getClient().create(Endpoint.class);
        //setting radio group
        RadioGroup radioGroup = (RadioGroup) v .findViewById(R.id.radioGroup);//RadioGroup ensures that only one radio button can be selected at a time.
        //make radio group "listen" to changes in clicked radio buttons
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.radioButtonTxtSearch:
                        //TXT SEARCH
                        Toast.makeText(getContext()," text search selected",Toast.LENGTH_SHORT).show();

                        //text search call
                        Call<TxtResponse> call = apiService.getMyResults(decodedQuery,key);
                        call.enqueue(new Callback<TxtResponse>() {
                            @Override
                            public void onResponse(Call<TxtResponse> call, Response<TxtResponse> response) {
                                ArrayList<TxtResult> myDataSource = new ArrayList<>();
                                myDataSource.clear();//clean old list if there was call from before
                                TxtResponse res =response.body() ;
                                myDataSource.addAll(res.results);

                                fragArecycler.setLayoutManager(new LinearLayoutManager( getActivity()));//LinearLayoutManager, GridLayoutManager ,StaggeredGridLayoutManagerFor defining how single row of recycler view will look .  LinearLayoutManager shows items in horizontal or vertical scrolling list. Don't confuse with type of layout you use in xml
                                //setting txt adapter
                                RecyclerView.Adapter myTxtAdapter = new TxtAdapter(myDataSource,   getActivity());
                                fragArecycler.setAdapter(myTxtAdapter);
                                myTxtAdapter.notifyDataSetChanged();//refresh
                                Log.e("Results", " very good: " + response.body());
                            }

                            @Override
                            public void onFailure(Call<TxtResponse> call, Throwable t) {
                                Log.e("Results", " bad: " + t);
                            }
                        });



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
