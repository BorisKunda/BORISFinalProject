package com.happytrees.finalproject.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        final List<ResultDB> favouritesList = ResultDB.listAll(ResultDB.class);//select all items from favourites database and read them
        //setting RecyclerView
        favouriteRecycler = (RecyclerView) v3.findViewById(R.id.recyclerFavourites);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());//layout manager defines look of RecyclerView -- > grid,list
        favouriteRecycler.setLayoutManager(layoutManager);
        //adapter
        final FavouritesAdapter favouritesAdapter = new FavouritesAdapter(favouritesList, getActivity());
        favouriteRecycler.setAdapter(favouritesAdapter);

        /**ALLOWING LISTENING TO SWEEP ACTIONS*/
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {//enables sweep left <-> right
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //fetch item position
                int position = viewHolder.getAdapterPosition();
                //remove item from database
                ResultDB resultDB = ResultDB.findById(ResultDB.class,favouritesList.get(position).getId());
                resultDB.delete();
                //standard code for removing item from recycler view -> we remove item from list after we removed it from database
                favouritesList.remove(position);
                favouritesAdapter.notifyItemRemoved(position);
                favouritesAdapter.notifyItemRangeChanged(position, favouritesList.size());

                Toast.makeText(getActivity(),"item removed",Toast.LENGTH_SHORT).show();


            }
        };
        //attach ItemTouchHelper instance  to recycler view through ItemTouchHelper.SimpleCallback instance
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(favouriteRecycler); //set swipe to recylcerview





        return v3;


    }
}
