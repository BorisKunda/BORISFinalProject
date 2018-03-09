package com.happytrees.finalproject.fragments;


import android.app.ProgressDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.happytrees.finalproject.R;
import com.happytrees.finalproject.activity.MainActivity;
import com.happytrees.finalproject.adapter.NearbyAdapter;
import com.happytrees.finalproject.adapter.TxtAdapter;

import com.happytrees.finalproject.model_nearby_search.NearbyResponse;
import com.happytrees.finalproject.model_nearby_search.NearbyResult;
import com.happytrees.finalproject.model_txt_search.TxtResponse;
import com.happytrees.finalproject.model_txt_search.TxtResult;
import com.happytrees.finalproject.rest.Endpoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//YOU DON'T HAVE SERIALIZE EVERYTHING ONLY THE OBJECTS YOU WANT TO FETCH IN PARSING.AND YOU DON'T HAVE TO WRITE @SerializedName annotation
// TEXT LINK  -->  https://maps.googleapis.com/maps/api/place/textsearch/json?query=pizza%20in%20jerusaelm&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ
//NEARBY LINK --> https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&keyword=sushi&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ
//IF THERE IS PROBLEM USE "+" INSTEAD OF "%20" -> https://maps.googleapis.com/maps/api/place/textsearch/json?query=pizza+in+Jerusalem&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ


public class FragmentA extends Fragment {


    //FOR NEARBY SEARCH
    String newNLocation;
    String radius = "500";//Default radius
    //VARIABLES SHARED BOTH BY SEARCHERS
    String key = "AIzaSyC39IysBBweSQw_FJ8qiZIfiZ6pOfLB5DY";//no need in decode
    EditText edtSearch;
    String fromEdtTxt;
    boolean txtChecked,nearChecked =false;//both false by default
    RecyclerView fragArecycler;
    public boolean isOffline = false ;//default value
    public int cacheSize = 10 * 1024 * 1024; // 10 MiB  -> maximum cache size .when it becomes full it refreshes (deletes all stored cache files).MiB is unit of measurement which quite similar to megabytes



    public FragmentA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_a, container, false);


        //setting RecyclerView
       fragArecycler = (RecyclerView) v.findViewById(R.id.recyclerSearch);





        //GO BUTTON
        Button goBtn = (Button)v.findViewById(R.id.goBtn);

        //EditText
        edtSearch = (EditText) v.findViewById(R.id.editTextSearch);
        //GET STRING VALUE FROM EDIT TEXT

           //PROGRESS BAR
        // Set up progress before call
       final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("ProgressDialog bar ");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        //FETCH SETTINGS RESULTS FROM SharedPreferences
        //set Shared Preferences (there you save settings values )
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //get value from SharedPrefs
        radius = sharedPreferences.getString("list_preference_radius", "500");//list_preference_radius is key(id) of preference item in preferences.xml




        //code checks if network available and user  connected to it (then isConnected is true)
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        //check gps
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // if gps ,network or both disabled isOffline declared true
        if(!isConnected||!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isOffline = true;
        }

        //DEALING WITH CACHING -> caching will load previously searched entries faster.and when offline searching these specific entries will give you previously called results

        //getCacheDir()  cant be used  directly without root ->grants cache files  path to the application specific cache directory on the filesystem.(its size defined in this example  by cacheSize variable)
        //So we used alternative :
        File httpCacheDirectory = new File(getActivity().getCacheDir(), "responses");

        //create cache object
        Cache cache = new Cache(httpCacheDirectory,10 * 1024 * 1024);

        //create okhttp object
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).addInterceptor(new Interceptor() {
            @Override //Interceptor will intercept Retrofit's response(call)
            public okhttp3.Response intercept( Chain chain)  {//in case there will be exception it dealt further in code
                Request request = chain.request();
                if(isOffline) {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                    request = request
                            .newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
                try {
                    return chain.proceed(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        })
                .build();




        //instead of using  code inside APIClient  we use one belowe
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        //connect the retrofit class with the interface class
        //generate new instance of the interface and call it service
        Retrofit retrofit = builder.build();
        final Endpoint apiService = retrofit.create(Endpoint.class);//instead previously used : final Endpoint apiService = APIClient.getClient().create(Endpoint.class);

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code checks if network available and user  connected to it (then isConnected is true)
                ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                //if network provider disabled ask from user re-enable it
                if(!isConnected) {
                    Toast.makeText(getActivity(), "enable network provider", Toast.LENGTH_SHORT).show();
                }

                //check if location provider enabled if not ask user re-enable it
                LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
               if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                   Toast.makeText(getActivity(), "please enable gps", Toast.LENGTH_SHORT).show();
                }

                        //check if edit text empty
                        if (edtSearch.length() != 0) {
                            fromEdtTxt = edtSearch.getText().toString();//keep txt written in EditText inside fromEdtTxt variable

                            //NOTHING SELECTED
                            if (!txtChecked && !nearChecked) {
                                Toast.makeText(getActivity(), "please choose an search type", Toast.LENGTH_SHORT).show();
                                //TXT SELECTED
                            } else if (txtChecked && !nearChecked) {
                                //text search call
                                Call<TxtResponse> call = apiService.getMyResults(fromEdtTxt, key);
                                progressDoalog.show();//SHOW PROGRESS BAR BEFORE CALL
                                call.enqueue(new Callback<TxtResponse>() {
                                    @Override
                                    public void onResponse(Call<TxtResponse> call, Response<TxtResponse> response) {
                                        final ArrayList<TxtResult> myDataSource = new ArrayList<>();
                                        myDataSource.clear();//clean old list if there was call from before
                                        if (response.body() == null) {
                                            Log.i("OFFLINE ", "No Results");
                                            progressDoalog.dismiss();
                                        } else {
                                            TxtResponse res = response.body();

                                            Log.i("Connection", "OFFLINE can't display results");

                                            myDataSource.addAll(res.results);

                                            if (myDataSource.isEmpty()) {
                                                Toast.makeText(getActivity(), "No Results", Toast.LENGTH_SHORT).show();//TOAST MESSAGE IF WE HAVE JSON WITH ZERO RESULTS
                                            }

                                            fragArecycler.setLayoutManager(new LinearLayoutManager(getActivity()));//LinearLayoutManager, GridLayoutManager ,StaggeredGridLayoutManagerFor defining how single row of recycler view will look .  LinearLayoutManager shows items in horizontal or vertical scrolling list. Don't confuse with type of layout you use in xml
                                            //setting txt adapter
                                            RecyclerView.Adapter myTxtAdapter = new TxtAdapter(myDataSource, getActivity());
                                            fragArecycler.setAdapter(myTxtAdapter);
                                            myTxtAdapter.notifyDataSetChanged();//refresh
                                            progressDoalog.dismiss();//dismiss progress bar after call was completed
                                            Log.e("TxtResults", " very good: " + response.body());

                                        }
                                    }


                                    @Override
                                    public void onFailure(Call<TxtResponse> call, Throwable t) {
                                        progressDoalog.dismiss();//dismiss progress bar after call was completed
                                        Log.e("TxtResults", " bad: " + t);
                                    }
                                });



                                //NEARBY SELECTED
                            } else if (!txtChecked && nearChecked) {
                                Log.i("SEARCH", "NearbySearch");
                                //FETCHED LATITUDE AND LONGITUDE FROM MAIN ACTIVITY
                                double fUpLatitude = MainActivity.upLatitude;//fetch current position's latitude from Main Activity
                                double fUpLongitude = MainActivity.upLongitude; //fetch current position's Longitude from Main Activity
                                String convertedFUpLatitude = String.valueOf(fUpLatitude);//convert double to String
                                String convertedFUpLongitude = String.valueOf(fUpLongitude);//convert double to String
                                String comma = ",";
                                newNLocation = convertedFUpLatitude + comma + convertedFUpLongitude;
                                //nearby search call
                                Call<NearbyResponse> nCall = apiService.getNearbyResults(newNLocation, radius, fromEdtTxt, key);
                                progressDoalog.show();//SHOW PROGRESS BAR BEFORE CALL
                                nCall.enqueue(new Callback<NearbyResponse>() {
                                    @Override
                                    public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                                        //  Toast.makeText(getContext(),"nearby search selected",Toast.LENGTH_SHORT).show();
                                        final ArrayList<NearbyResult> nDataSource = new ArrayList<>();
                                        nDataSource.clear();//clean old list if there was call from before
                                        NearbyResponse nRes = response.body();
                                        nDataSource.addAll(nRes.results);

                                        if (nDataSource.isEmpty()) {
                                            Toast.makeText(getActivity(), "No Results", Toast.LENGTH_SHORT).show();//TOAST MESSAGE IF WE HAVE JSON WITH ZERO RESULTS
                                        }

                                        fragArecycler.setLayoutManager(new LinearLayoutManager(getActivity()));//LinearLayoutManager, GridLayoutManager ,StaggeredGridLayoutManagerFor defining how single row of recycler view will look .  LinearLayoutManager shows items in horizontal or vertical scrolling list. Don't confuse with type of layout you use in xml
                                        //setting txt adapter
                                        RecyclerView.Adapter myNearAdapter = new NearbyAdapter(nDataSource, getActivity());
                                        fragArecycler.setAdapter(myNearAdapter);
                                        myNearAdapter.notifyDataSetChanged();//refresh

                                        progressDoalog.dismiss();//dismiss progress bar after call was completed

                                        Log.e("TxtResults", " very good: " + response.body());

                                    }

                                    @Override
                                    public void onFailure(Call<NearbyResponse> call, Throwable t) {
                                        progressDoalog.dismiss();//dismiss progress bar after call was completed
                                        Log.e("NearResults", " bad: " + t);

                                    }
                                });

                            }

                        } else {
                            Toast.makeText(getActivity(), "please write something", Toast.LENGTH_SHORT).show();
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

                        txtChecked = false;
                        nearChecked = true;
                        break;


                }


            }

        });
        return v;
    }

}
/*
    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
                   snackbar.show();
 */