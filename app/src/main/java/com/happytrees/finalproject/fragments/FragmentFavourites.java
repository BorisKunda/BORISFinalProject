package com.happytrees.finalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happytrees.finalproject.R;
import com.happytrees.finalproject.adapter.FavouritesAdapter;
import com.happytrees.finalproject.database.ResultDB;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFavourites extends Fragment {

    public RecyclerView favouriteRecycler;


    public FragmentFavourites() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v3 = inflater.inflate(R.layout.fragment_favourites, container, false);


        //SETTING RECYCLER VIEW LIST
        List<ResultDB> favouritesList = ResultDB.listAll(ResultDB.class);//select all items from favourites database and read them
        //setting RecyclerView
        favouriteRecycler = (RecyclerView) v3.findViewById(R.id.recyclerFavourites);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());//layout manager defines look of RecyclerView -- > grid,list
        favouriteRecycler.setLayoutManager(layoutManager);
        //adapter
        FavouritesAdapter favouritesAdapter = new FavouritesAdapter(favouritesList, getActivity());
        favouriteRecycler.setAdapter(favouritesAdapter);


        return v3;


    }
}
/*

 List<Location> allLocations = Location.listAll(Location.class);
        RecyclerView recyclerView = v.findViewById(R.id.locationRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());//layout manager defines look of RecyclerView -- > grid,list
        recyclerView.setLayoutManager(layoutManager);

        //adapter
        LocationAdapter locationAdapter = new LocationAdapter(allLocations, getActivity());

        recyclerView.setAdapter(locationAdapter);
 */