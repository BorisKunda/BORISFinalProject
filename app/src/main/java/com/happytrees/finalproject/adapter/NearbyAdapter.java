package com.happytrees.finalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.happytrees.finalproject.model_nearby_search.NearbyResult;

import java.util.ArrayList;

/**
 * Created by Boris on 2/8/2018.
 */
//create a class that extends RecyclerView.Adapter .put inside the < >  ==> Yourclass.YourInnerClassViewHolder => implement methods
//MAIN CLASS
public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {

    //urlPartstart + urlPartfinal + photo_reference ==> URL LINK TO PHOTO
    public String urlPartstart = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    public String urlPartfinal = "&key=AIzaSyDTLvyt5Cry0n5eJDXWJNTluMHRuDYYc5s";

    public ArrayList<NearbyResult> nearResults;//list of places results
    public Context context;

    //constructor
    public NearbyAdapter(ArrayList<NearbyResult> nearResults, Context context) {
        this.nearResults = nearResults;
        this.context = context;
    }

    //create ViewHolder
    @Override
    public NearbyAdapter.NearbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nView = LayoutInflater.from(context).inflate(R.layout.nearby_result_item_layout, null);//getContext refers to get value of context variable
        NearbyViewHolder nearbyViewHolder = new NearbyViewHolder(nView);
        return nearbyViewHolder;//In the onCreateViewHolder() method we inflate the row layout as a View and return as ViewHolder object.
    }

    @Override
    public void onBindViewHolder(NearbyAdapter.NearbyViewHolder holder, int position) {
        NearbyResult nearbyResultObj = nearResults.get(position);
        holder.bindDataFromArrayToView(nearbyResultObj);
    }

    @Override
    public int getItemCount() {
        return nearResults.size();//size of nearResults array
    }

    //INNER CLASS
    //create inner class  YourInnerClassViewHolder extends RecyclerView.ViewHolder => implement constructor
    public class NearbyViewHolder extends RecyclerView.ViewHolder {

        View nearView;

        //override default constructor
        public NearbyViewHolder(View itemView) {
            super(itemView);
            nearView = itemView;
        }

        public void bindDataFromArrayToView(final NearbyResult nResult) {

            TextView nearbyName = (TextView) nearView.findViewById(R.id.nearbyName);//NAME
            nearbyName.setText(nResult.name);

            TextView nearbyAddress = (TextView) nearView.findViewById(R.id.nearbyAddress);//ADDRESS
            nearbyAddress.setText(nResult.vicinity);

            //latitude comes before longitude
            TextView nearLatitude = (TextView) nearView.findViewById(R.id.nearbyLatitude);//LATITUDE
            double temporaryNLatitude = nResult.geometry.location.lat;
            String convertedNLatitude = String.valueOf(temporaryNLatitude);//you cant setText on double so you need convert it first to String
            nearLatitude.setText(convertedNLatitude);

            TextView nearLongitude = (TextView) nearView.findViewById(R.id.nearbyLongitude);//LONGITUDE
            double temporaryNLongitude = nResult.geometry.location.lng;
            String convertedNLongitude = String.valueOf(temporaryNLongitude);//you cant setText on double so you need convert it first to String
            nearLatitude.setText(convertedNLongitude);

            ImageView nImage = (ImageView) nearView.findViewById(R.id.nearbyImage);//IMAGE


            final ProgressBar nProgressBar = (ProgressBar) nearView.findViewById(R.id.nearbyProgress);//PROGRESS BAR
            nProgressBar.setVisibility(View.VISIBLE);////make progress bar visible

            String photo_reference = nResult.photos.get(0).photo_reference;//keeping photo_reference under  String  photo_reference variable
            String urlLinktoPhoto = urlPartstart + photo_reference + urlPartfinal;

            Glide.with(context).load( urlLinktoPhoto).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                   nProgressBar.setVisibility(View.GONE);//removes progress bar if there was exception
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                     nProgressBar.setVisibility(View.GONE);////removes progress bar if picture finished loading
                    return false;
                }
            }).into(nImage);//SET IMAGE THROUGH GLIDE


        }
    }

}
/*



public class TxtAdapter extends RecyclerView.Adapter<TxtAdapter.TxtHolder> {

    //urlPartstart + urlPartfinal + photo_reference ==> URL LINK TO PHOTO
    public String urlPartstart = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    public String urlPartfinal = "&key=AIzaSyDTLvyt5Cry0n5eJDXWJNTluMHRuDYYc5s";


    public ArrayList<TxtResult> txtResults;//list of places results
    public Context context;

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

    //INNER CLASS EXTENDS RecyclerView.ViewHolder
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

            TextView resultLongitude = (TextView)myView.findViewById(R.id.txtLongitude);
            double temporaryLongitude = txtResultCurrent.geometry.location.lng;
            String convertedLongitude = String.valueOf(temporaryLongitude);//you cant setText on double so you need convert it first to String
            resultLongitude.setText(convertedLongitude);



            ImageView resultImage = (ImageView) myView.findViewById(R.id.resultImage);

            final ProgressBar progressBar = (ProgressBar) myView.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);////make progress bar visible

            String photo_reference = txtResultCurrent.photos.get(0).photo_reference;//keeping photo_reference under  String  photo_reference variable
            String urlLinktoPhoto = urlPartstart + photo_reference + urlPartfinal;



            Glide.with(context).load( urlLinktoPhoto).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                   progressBar .setVisibility(View.GONE);//removes progress bar if there was exception
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar .setVisibility(View.GONE);////removes progress bar if picture finished loading
                    return false;
                }
            }).into(resultImage);//SET IMAGE THROUGH GLIDE
        }
    }
}


 */
