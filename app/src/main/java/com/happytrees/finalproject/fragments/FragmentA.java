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





//YOU DON'T HAVE SERIALIZE EVERYTHING ONLY THE OBJECTS YOU WANT TO FETCH IN PARSING.AND YOU DON'T HAVE TO WRITE @SerializedName annotation
// TEXT LINK  -->  https://maps.googleapis.com/maps/api/place/textsearch/json?query=pizza%20in%20jerusaelm&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ
//NEARBY LINK --> https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&keyword=sushi&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ
//IF THERE IS PROBLEM USE "+" INSTEAD OF "%20" -> https://maps.googleapis.com/maps/api/place/textsearch/json?query=pizza+in+Jerusalem&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ
public class FragmentA extends Fragment {


    //FOR TEXT SEARCH
    String query = "pizza%20in%20jerusalem";//needs decoding
    String decodedQuery;
    //FOR NEARBY SEARCH
    String nLocation = "-33.8670522,151.1957362";
    String radius = "500";
    String keyword = "sushi";

    String key = "AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ";//no need in decode




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
                                Log.e("TxtResults", " very good: " + response.body());
                            }

                            @Override
                            public void onFailure(Call<TxtResponse> call, Throwable t) {
                                Log.e("TxtResults", " bad: " + t);
                            }
                        });

                        break;


                    case R.id.radioButtonNearbySearch:
                        //NEARBY SEARCH -- NEEDS TO BE FIXED
                       Toast.makeText(getContext(),"nearby search selected",Toast.LENGTH_SHORT).show();
                        //nearby search call
                        Call<NearbyResponse>nCall = apiService.getNearbyResults(nLocation,radius,keyword,key);
                        nCall.enqueue(new Callback<NearbyResponse>() {
                            @Override
                            public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                              //  Toast.makeText(getContext(),"nearby search selected",Toast.LENGTH_SHORT).show();
                                ArrayList<NearbyResult> nDataSource = new ArrayList<>();
                                nDataSource.clear();//clean old list if there was call from before
                                NearbyResponse nRes =response.body() ;
                                nDataSource.addAll(nRes.results);

                                fragArecycler.setLayoutManager(new LinearLayoutManager( getActivity()));//LinearLayoutManager, GridLayoutManager ,StaggeredGridLayoutManagerFor defining how single row of recycler view will look .  LinearLayoutManager shows items in horizontal or vertical scrolling list. Don't confuse with type of layout you use in xml
                                //setting txt adapter
                                RecyclerView.Adapter myNearAdapter = new NearbyAdapter(nDataSource,   getActivity());
                                fragArecycler.setAdapter(myNearAdapter);
                                myNearAdapter.notifyDataSetChanged();//refresh

                                Log.e("TxtResults", " very good: " + response.body());

                            }

                            @Override
                            public void onFailure(Call<NearbyResponse> call, Throwable t) {
                                Log.e("NearResults", " bad: " + t);
                                Toast.makeText(getContext(),"failure",Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                }
            }
        });
        return v;
    }

}
