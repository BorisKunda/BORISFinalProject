package com.happytrees.finalproject.adapter;

import android.content.Context;
import android.location.Location;
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


    //constructor
    public FavouritesAdapter(List<ResultDB> favouritesList, Context context) {
        this.favouritesList = favouritesList;
        this.context = context;
    }

    //create ViewHolder
    @Override
    public FavouritesAdapter.FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View fView = LayoutInflater.from(context).inflate(R.layout.favourites_item, null);
        FavouritesViewHolder favouritesViewHolder = new FavouritesViewHolder(fView);
        return favouritesViewHolder;
    }

    @Override
    public void onBindViewHolder(FavouritesAdapter.FavouritesViewHolder holder, int position) {
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

        //custom method we need to define
        public void bindDataFromArrayToView(final ResultDB fResultDB) {

            TextView favouriteName = (TextView) favouriteView.findViewById(R.id.favouriteName);//NAME
            favouriteName.setText(fResultDB.name);

            TextView favouriteAddress = (TextView) favouriteView.findViewById(R.id.favouriteAddress);//ADDRESS
            favouriteAddress.setText(fResultDB.formatted_address);

            //latitude comes before longitude
            TextView favouriteLatitude = (TextView) favouriteView.findViewById(R.id.favouriteLatitude);//LATITUDE
            double keptLat = fResultDB.lat;
            favouriteLatitude.setText(fResultDB.lat + " ");

            TextView favouriteLongitude = (TextView) favouriteView.findViewById(R.id.favouriteLongitude);//LONGITUDE
            double keptLng = fResultDB.lng;
            favouriteLongitude.setText(fResultDB.lng + " ");

            TextView favouriteDistance = (TextView) favouriteView.findViewById(R.id.favouriteDistance);//DISTANCE
            //method calculates distances between two points according to their latitude and longitude
            Location.distanceBetween(MainActivity.upLatitude, MainActivity.upLongitude, keptLat, keptLng, fDistanceResults);// IN METERS
            favouriteDistance.setText(fDistanceResults[0] / 1000 + "km");

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
                    Toast.makeText(context,"click f",Toast.LENGTH_SHORT).show();
                }
            });
            //long click
            favouriteView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //REMOVE ITEM FROM DATABASE THEN FROM RECYCLER VIEW
                    //remove item from database
                    ResultDB resultDB = ResultDB.findById(ResultDB.class,favouritesList.get(getAdapterPosition()).getId());//we used  "getAdapterPosition()" to get item  position (int).getId used to get id cause in sugar orm you need id in order to remove item
                    resultDB.delete();
                    //standard code for removing item from recycler view -> we remove item from list after we removed it from database
                    favouritesList.remove(getAdapterPosition());//we used  "getAdapterPosition()" to get item  position (int)
                    notifyItemRemoved(getAdapterPosition());//we used  "getAdapterPosition()" to get item  position (int)
                    notifyItemRangeChanged(getAdapterPosition(), favouritesList.size());//we used  "getAdapterPosition()" to get item  position (int)

                    Toast.makeText(context,"item removed",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });


        }
    }
}
