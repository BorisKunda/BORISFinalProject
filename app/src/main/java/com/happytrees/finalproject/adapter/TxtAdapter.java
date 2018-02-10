package com.happytrees.finalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.happytrees.finalproject.R;
import com.happytrees.finalproject.model_txt_search.TxtResult;

import java.util.ArrayList;

//create a class that extends RecyclerView.Adapter .put inside the < >  ==> Yourclass.YourInnerClassViewHolder

public class TxtAdapter extends RecyclerView.Adapter<TxtAdapter.TxtHolder> {

    //urlPartstart + urlPartfinal + photo_reference ==> URL LINK TO PHOTO
    public String urlPartstart = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    public String urlPartfinal = "&key=AIzaSyDTLvyt5Cry0n5eJDXWJNTluMHRuDYYc5s";


    public ArrayList<TxtResult> txtResults;//list of places results
    public Context context;

    //constructor
    public TxtAdapter(ArrayList<TxtResult> txtResults,  Context context) {
        this.txtResults = txtResults;
        this.context = context;
    }

    @Override
    public TxtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.txt_result_item_layout,null);//getContext refers to get value of context variable
        TxtHolder txtHolder = new TxtHolder(view);
        return  txtHolder;//In the onCreateViewHolder() method we inflate the row layout as a View and return as ViewHolder object.
    }

    @Override
    public void onBindViewHolder(TxtHolder holder, int position) {
        TxtResult txtResultobject = txtResults.get(position);
        holder.bindDataFromArrayToView(txtResultobject);
    }

    @Override
    public int getItemCount() {
        return txtResults.size();
    }

    //create inner class  YourInnerClassViewHolder extends RecyclerView.ViewHolder => implement constructor
    class TxtHolder extends RecyclerView.ViewHolder {
        View myView;

        //override default constructor
        public TxtHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void bindDataFromArrayToView(final TxtResult txtResultCurrent) {



            TextView resultName = (TextView) myView.findViewById(R.id.resultName);//NAME
            resultName.setText(txtResultCurrent.name);

            TextView resultAddress = (TextView) myView.findViewById(R.id.resultAddress);//ADDRESS
            resultAddress.setText(txtResultCurrent.formatted_address);

            //latitude comes before longitude

            TextView resultLatitude = (TextView)myView.findViewById(R.id.txtLatitude);//LATITUDE
            double temporaryLatitude  =txtResultCurrent.geometry.location.lat;
            String convertedLatitude = String.valueOf(temporaryLatitude);//you cant setText on double so you need convert it first to String
            resultLatitude.setText(convertedLatitude);

            TextView resultLongitude = (TextView)myView.findViewById(R.id.txtLongitude);//LONGITUDE
            double temporaryLongitude = txtResultCurrent.geometry.location.lng;
            String convertedLongitude = String.valueOf(temporaryLongitude);//you cant setText on double so you need convert it first to String
            resultLongitude.setText(convertedLongitude);



            ImageView resultImage = (ImageView) myView.findViewById(R.id.resultImage);//IMAGE

            final ProgressBar progressBar = (ProgressBar) myView.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);////make progress bar visible

            if (txtResultCurrent.photos == null)
                Log.e("TxtAdapter", "Photos list of search results is null");
            else if (txtResultCurrent.photos.isEmpty())
                Log.e("TxtAdapter", "Photos list of search results is empty");
            else {
                String photo_reference = txtResultCurrent.photos.get(0).photo_reference;////we fetch first image (i = 0) from array of photos
                String urlLinktoPhoto = urlPartstart + photo_reference + urlPartfinal;


                Glide.with(context).load(urlLinktoPhoto).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);//removes progress bar if there was exception
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);////removes progress bar if picture finished loading
                        return false;
                    }
                }).into(resultImage);//SET IMAGE THROUGH GLIDE
            }
        }
    }
}


