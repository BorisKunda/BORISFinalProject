package com.happytrees.finalproject.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.happytrees.finalproject.R;
import com.happytrees.finalproject.activity.MainActivity;
import com.happytrees.finalproject.database.ResultDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Boris on 2/22/2018.
 */


//Main class -> create a class that extends RecyclerView.Adapter .put inside the < >  ==> Yourclass.YourInnerClassViewHolder => implement methods
//Inner class -> create inner class  YourInnerClassViewHolder extends RecyclerView.ViewHolder => implement constructor


//MAIN CLASS
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    //VARIABLES
    public List<ResultDB> favouritesList;
    public Context context;
    //urlPartstart + urlPartfinal + photo_reference ==> URL LINK TO PHOTO
    public String urlPartstart = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    public String urlPartfinal = "&key=AIzaSyDTLvyt5Cry0n5eJDXWJNTluMHRuDYYc5s";
    public float[] fDistanceResults = new float[10];//10 random number.you need any number higher than 3
    public String fPreference = "kilometre";//default value of distance measurement units
    public double roundedFDis;//rounded value of distance (less numbers after dot)


    //constructor
    public FavouritesAdapter(List<ResultDB> favouritesList, Context context) {
        this.favouritesList = favouritesList;
        this.context = context;
    }

    //create ViewHolder
    @Override
    public FavouritesAdapter.FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // generate view and viewHolder
        View fView = LayoutInflater.from(context).inflate(R.layout.favourites_item, null);
        FavouritesViewHolder favouritesViewHolder = new FavouritesViewHolder(fView);
        return favouritesViewHolder;
    }

    @Override
    public void onBindViewHolder(FavouritesAdapter.FavouritesViewHolder holder, int position) {
        //bind data to view holder
        ResultDB dbResult = favouritesList.get(position);
        holder.bindDataFromArrayToView(dbResult);
    }

    @Override
    public int getItemCount() {
        return favouritesList.size();//size of favouritesList array
    }

    //INNER CLASS
    public class FavouritesViewHolder extends RecyclerView.ViewHolder {
        View favouriteView;//define view variable

        //override default constructor
        public FavouritesViewHolder(View itemView) {
            super(itemView);
            favouriteView = itemView;
        }

        // create public  method that binds the data
        public void bindDataFromArrayToView(final ResultDB fResultDB) {

            TextView favouriteName = (TextView) favouriteView.findViewById(R.id.favouriteName);//NAME
            favouriteName.setText(fResultDB.name);

            TextView favouriteAddress = (TextView) favouriteView.findViewById(R.id.favouriteAddress);//ADDRESS
            favouriteAddress.setText(fResultDB.formatted_address);

            //latitude comes before longitude
         //   TextView favouriteLatitude = (TextView) favouriteView.findViewById(R.id.favouriteLatitude);//LATITUDE
            double keptLat = fResultDB.lat;
           // favouriteLatitude.setText(fResultDB.lat + " ");

         //   TextView favouriteLongitude = (TextView) favouriteView.findViewById(R.id.favouriteLongitude);//LONGITUDE
            double keptLng = fResultDB.lng;
         //   favouriteLongitude.setText(fResultDB.lng + " ");

            TextView favouriteDistance = (TextView) favouriteView.findViewById(R.id.favouriteDistance);//DISTANCE
            //method calculates distances between two points according to their latitude and longitude
            Location.distanceBetween(MainActivity.upLatitude, MainActivity.upLongitude, keptLat, keptLng, fDistanceResults);// DEFAULT  IN KILOMETERS


            //FETCH SETTINGS RESULTS FROM SharedPreferences
            //set Shared Preferences (there you save settings values )
            SharedPreferences sharedFPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            //get value from SharedPrefs
           fPreference  = sharedFPreferences.getString("list_preference_units", "kilometre");//list_preference_units is key(id) of preference item in preferences.xml

            //check settings results
            if(fPreference.equals("kilometre") ) {
                roundedFDis =  (double)Math.round( (fDistanceResults[0]/1000 ) * 100d) / 100d;//number of zeros must be same in and outside parenthesis.number of zeroes equals to number of numbers after dot that will remain after rounding up
                favouriteDistance.setText(roundedFDis + " km ");//km
            }else{
                roundedFDis =  (double)Math.round( (((fDistanceResults[0]*0.621371)/1000 ) ) * 100d) / 100d;//number of zeros must be same in and outside parenthesis.number of zeroes equals to number of numbers after dot that will remain after rounding up
               favouriteDistance.setText(roundedFDis + "  miles");//miles
            }


            ImageView fImage = (ImageView) favouriteView.findViewById(R.id.favouriteImage);//IMAGE

            final ProgressBar progressBar = (ProgressBar) favouriteView.findViewById(R.id.progress);//PROGRESS BAR
            progressBar.setVisibility(View.VISIBLE);////make progress bar visible

            //we check items in database which ones have no  photo reference       //PHOTOS

            if (fResultDB.photo_reference.equals("no photo")) {
                //do nothing
            }else{
                String fphoto_reference = fResultDB.photo_reference;////we fetch first image (i = 0) from array of photos
                String furlLinktoPhoto = urlPartstart + fphoto_reference + urlPartfinal;
                //LOADING IMAGE USING GLIDE
                Glide.with(context).load(furlLinktoPhoto).listener(new RequestListener<String, GlideDrawable>() {
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
                }).into(fImage);//SET IMAGE THROUGH GLIDE
            }
            //MAKE RECYCLER FAVOURITES CLICKABLE
            //normal click
            favouriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context,"click f",Toast.LENGTH_SHORT).show();
                }
            });
            //long click
            favouriteView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                  //code
                    return true;
                }
            });


        }
    }
}
/*
/*
    //FETCH SETTINGS RESULTS FROM SharedPreferences
            //set Shared Preferences (there you save settings values )
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            //get value from SharedPrefs
            preference = sharedPreferences.getString("list_preference_units", "kilometre");//list_preference_units is key(id) of preference item in preferences.xml

            //GOOD IDEA IF YOU TRY TO ROUND UP NUMBERS!!!!!!!!!!!!!!!
            if(preference.equals("kilometre") ) {
                roundedDis =  (double)Math.round( (txtDistanceResults[0]/1000 ) * 100d) / 100d;//number of zeros must be same in and outside parenthesis.number of zeroes equals to number of numbers after dot that will remain after rounding up
                  resultTxtDistance.setText(roundedDis + " km ");//km
            }else{
                roundedDis =  (double)Math.round( (((txtDistanceResults[0]*0.621371)/1000 ) ) * 100d) / 100d;//number of zeros must be same in and outside parenthesis.number of zeroes equals to number of numbers after dot that will remain after rounding up
                   resultTxtDistance.setText(roundedDis + "  miles");//miles
            }

 */
