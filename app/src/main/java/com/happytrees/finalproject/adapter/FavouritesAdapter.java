package com.happytrees.finalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happytrees.finalproject.R;
import com.happytrees.finalproject.database.ResultDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Boris on 2/22/2018.
 */


//Main class -> create a class that extends RecyclerView.Adapter .put inside the < >  ==> Yourclass.YourInnerClassViewHolder => implement methods
//Inner class -> create inner class  YourInnerClassViewHolder extends RecyclerView.ViewHolder => implement constructor


    //MAIN CLASS
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>{

    //VARIABLES
    public List<ResultDB> favouritesList;
    public Context context;
    //urlPartstart + urlPartfinal + photo_reference ==> URL LINK TO PHOTO
    public String urlPartstart = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    public String urlPartfinal = "&key=AIzaSyDTLvyt5Cry0n5eJDXWJNTluMHRuDYYc5s";

//constructor
    public FavouritesAdapter(List<ResultDB> favouritesList, Context context) {
        this.favouritesList = favouritesList;
        this.context = context;
    }

    //create ViewHolder
    @Override
    public FavouritesAdapter.FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View fView = LayoutInflater.from(context).inflate(R.layout.favourites_item,null);
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

            TextView favouriteName = (TextView)favouriteView.findViewById(R.id.favouriteName);//NAME
            favouriteName.setText(fResultDB.name);

            TextView favouriteAddress = (TextView)favouriteView.findViewById(R.id.favouriteAddress);//ADDRESS
            favouriteAddress.setText(fResultDB.formatted_address);

            //latitude comes before longitude
            TextView favouriteLatitude = (TextView)favouriteView.findViewById(R.id.favouriteLatitude);//LATITUDE
            favouriteLatitude.setText(favouriteLatitude + " ");

            TextView favouriteLongitude = (TextView)favouriteView.findViewById(R.id.favouriteLongitude);//LONGITUDE
            favouriteLongitude.setText(favouriteLongitude + " ");

            TextView favouriteDistance = (TextView)favouriteView.findViewById(R.id.favouriteDistance);//DISTANCE
            //method calculates distances between two points according to their latitude and longitude
        //    Location.distanceBetween(MainActivity.upLatitude,MainActivity.upLongitude,temporaryNLatitude,temporaryNLongitude,nearDistanceResults);// IN METERS
          //  resultNearDistance.setText(nearDistanceResults[0]/1000 + "km");

        }
    }
}
/*
//create a class that extends RecyclerView.Adapter .put inside the < >  ==> Yourclass.YourInnerClassViewHolder => implement methods
//MAIN CLASS
public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {

    //urlPartstart + urlPartfinal + photo_reference ==> URL LINK TO PHOTO
    public String urlPartstart = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    public String urlPartfinal = "&key=AIzaSyDTLvyt5Cry0n5eJDXWJNTluMHRuDYYc5s";
    public ArrayList<NearbyResult> nearResults;//list of places results
    public Context context;
    public float [] nearDistanceResults = new float[10];//10 random number.you need any number higher than 3
    public  String  formatted_address;

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
            nearLongitude.setText(convertedNLongitude);

            TextView resultNearDistance = (TextView)nearView.findViewById(R.id.nearDistance);//DISTANCE
            //method calculates distances between two points according to their latitude and longitude
            Location.distanceBetween(MainActivity.upLatitude,MainActivity.upLongitude,temporaryNLatitude,temporaryNLongitude,nearDistanceResults);// IN METERS
            resultNearDistance.setText(nearDistanceResults[0]/1000 + "km");


            ImageView nImage = (ImageView) nearView.findViewById(R.id.nearbyImage);//IMAGE


            final ProgressBar nProgressBar = (ProgressBar) nearView.findViewById(R.id.nearbyProgress);//PROGRESS BAR
            nProgressBar.setVisibility(View.VISIBLE);////make progress bar visible

            if (nResult.photos == null)
                Log.e("NearbyAdapter", "Photos list of search results is null");//means there no String photo_reference
            else if (nResult.photos.isEmpty())
                Log.e("NearbyAdapter", "Photos list of search results is empty");//there is string but its empty " "
            else {
                String photo_reference = nResult.photos.get(0).photo_reference;//we fetch first image (i = 0) from array of photos
                String urlLinktoPhoto = urlPartstart + photo_reference + urlPartfinal;

                Glide.with(context).load(urlLinktoPhoto).listener(new RequestListener<String, GlideDrawable>() {
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
            //MAKE RECYCLER NEARBY CLICKABLE
            nearView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,nResult.name,Toast.LENGTH_SHORT).show();
                }
            });
            nearView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("which one suits you best?");//dialog message
                    builder.setPositiveButton("Save to Favourites", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          formatted_address = nResult.vicinity;//convert vicinity into formatted_address
                            if (nResult.photos == null ||nResult.photos.isEmpty()) {//there no photo reference
                                ResultDB nAltResultDB = new ResultDB(nResult.name,formatted_address,nResult.geometry.location.lat,nResult.geometry.location.lng,"no photo");
                                nAltResultDB.save();
                            }else {//there is photo reference
                                ResultDB nResultDB = new ResultDB(nResult.name,formatted_address,nResult.geometry.location.lat,nResult.geometry.location.lng,nResult.photos.get(0).photo_reference);
                                nResultDB.save();
                            }

                            Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show();
                        }
                    });



                    builder.setNegativeButton("Share", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"Share",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }

    }
}
/*
TextView resultTxtDistance = (TextView) myView.findViewById(R.id.txtDistance);//DISTANCE
            //method calculates distances between two points according to their latitude and longitude
            Location.distanceBetween(MainActivity.upLatitude,MainActivity.upLongitude,temporaryLatitude,temporaryLongitude,txtDistanceResults);// IN METERS
            resultTxtDistance.setText(txtDistanceResults[0]/1000 + "km");
 */