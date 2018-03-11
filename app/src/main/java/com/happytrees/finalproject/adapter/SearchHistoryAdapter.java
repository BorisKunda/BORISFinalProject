package com.happytrees.finalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happytrees.finalproject.R;
import com.happytrees.finalproject.database.LastSearch;

import java.util.List;


//create a class that extends RecyclerView.Adapter .put inside the < >  ==> Yourclass.YourInnerClassViewHolder


public class SearchHistoryAdapter extends RecyclerView.Adapter <SearchHistoryAdapter.SearchHistoryViewHolder>{


//VARIABLES
    public List<LastSearch> lastSearchResults;//list of places results
    public Context context;

    public SearchHistoryAdapter(List<LastSearch> lastSearchResults, Context context) {
        this.lastSearchResults = lastSearchResults;
        this.context = context;
    }

    @Override
    public SearchHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // generate view and viewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.lastsearch_item_layout, null);
        SearchHistoryViewHolder searchHistoryViewHolder = new SearchHistoryViewHolder(view);
        return searchHistoryViewHolder;
    }



    @Override
    public void onBindViewHolder(SearchHistoryViewHolder holder, int position) {
        //bind data to view holder
        LastSearch lastSearch = lastSearchResults.get(position);
        holder.bindDataFromArrayToView(lastSearch);
    }

    @Override
    public int getItemCount() {
        return lastSearchResults.size();
    }



    //INNER CLASS
    //create inner class  YourInnerClassViewHolder extends RecyclerView.ViewHolder => implement constructor

    public class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
        View myHView;



        //constructor
        public SearchHistoryViewHolder(View itemView) {
            super(itemView);
            myHView = itemView;
        }


        public void bindDataFromArrayToView(final LastSearch currentLastSearch) {

            TextView historyName = (TextView) myHView.findViewById(R.id.historyName);//NAME
            historyName.setText(currentLastSearch.name);




        }


    }




}
