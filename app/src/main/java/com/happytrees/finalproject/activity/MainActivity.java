package com.happytrees.finalproject.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.happytrees.finalproject.R;
import com.happytrees.finalproject.fragments.FragmentA;
import com.happytrees.finalproject.fragments.FragmentB;

//SAVE INSTANCE ON ROTATION CHANGE --> YET TO COME
//USE ANOTHER DATABASE THAN SUGAR ORM FOR INSTANCE RXJAVA
//DON'T ASK ME AGAIN OPTION WILL APPEAR IF USER DECLINED PERMISSION AT LEAST ONCE
//ADD BROADCAST RECEIVER FOR GPS AND BATTERY
//pizza+tel-aviv has long txt results good for checking design
//Services?MyLooperLooper
//MAP WITH MARKER
//FAVOURITES
//DATABASE
//CHECK IF GPS ENABLED BEFORE CONDUCTING NEARBY SEARCH
//EXPENDABLE LAYOUT


public class MainActivity extends AppCompatActivity {
//https://www.google.co.il/maps/@32.0662593,34.7698209,15z --> put here your latitude , longitude

    //VARIABLES
    public FusedLocationProviderClient mFusedLocationClient;
    public LocationCallback mLocationCallback;
    LocationRequest mLocationRequest;
    public static final int REQUEST_CODE_LOCATION = 4;
    public static double upLatitude,upLongitude;//updated current position's latitude and longitude made as static in order to be accessible in both  Adapter java classes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create an instance of the Fused Location Provider Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);//alternatively you can use Location manager which has different Location Listener

        //create location request + set requirements for it
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);//10 second . setInterval() - This method sets the rate in milliseconds at which your app prefers to receive location updates.
        mLocationRequest.setFastestInterval(5000);//5 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //getting result from location update
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {//callback on request of location updates made by method : requestLocationUpdates()
                for (Location location : locationResult.getLocations()) {
                    Log.e("CURRENT LOCATION", " Latitude " + location.getLatitude() + " Longitude " + location.getLongitude());
                    upLatitude = location.getLatitude();//KEEP CURRENT LOCATION
                    upLongitude = location.getLongitude();//KEEP CURRENT LOCATION

                }
            }
        };

        //PERMISSIONS CHECK IF ANDROID VERSION IS 6.0 OR ABOVE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)//VERSION_CODES.M = Android 6.0  --> we check if our minimum sdk greater or equal to 6.0 (this when runtime permissions first took place)
        {
            checkLocationPermission();

        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }

        //GOOGLE PLAY SERVICES CHECK
        googlePlayCheck();

        //GPS CHECK
        gpsCheck();


        //INIT FRAGMENT A
        FragmentA fragmentA = new FragmentA();//generate a new Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();//call the Fragment Manager
        fragmentManager.beginTransaction().replace(R.id.MainContainer, fragmentA).commit();//add the fragment to the layout


        if (isTablet())//means device used xlarge xml layout of MainActivity,due to device large screen(tablet)
        {
            //INIT FRAGMENT B
            FragmentB fragmentB = new FragmentB();

            fragmentManager.beginTransaction().replace(R.id.ExtraContainer, fragmentB).commit();//we add FragmentB only to sub layout of xlarge MainActivity called ExtraContainer ,and only if its not null(its null if device detected normal size screen)
        }


    }

    ////////////////////////////////////////////////////////////////////////METHODS////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //METHOD CHECKS IF DEVICE HAS XLARGE SCREEN.IF IT DOES IT PUTS BOTH FRAGMENT A AND B INSIDE SCREEN
    public boolean isTablet() {
        {
            boolean isTab = false;
            LinearLayout ExtraContainer = findViewById(R.id.ExtraContainer);
            if (ExtraContainer != null)//if device chose xlarge xml layout of MainActivity then its sub layout  ExtraContainer wont be null
            {
                isTab = true;
            }
            return isTab;
        }
    }

    //METHOD CHECKS LOCATION PERMISSION
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//if there wasn't already permission granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {// shouldShowRequestPermissionRationale() -->  this method returns true if the app has requested this permission previously and the user denied the request.
                //HERE WE WILL WRITE CODE EXPLAINED WHY GRANTING THIS SPECIFIC PERMISSION IS WORTH IT
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());//called if permission was already granted
        }
    }

    //METHOD CHECK RESULT OF PERMISSION REQUEST
    @SuppressLint("MissingPermission")
//no need in permission check cause one was already performed above
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {//received permission result for location permission

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// If request is cancelled, the result arrays are empty.
                Log.e("LOCATION PERMISSION", "GRANTED");
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                Log.e("LOCATION PERMISSION", "DENIED");
            }
        }
    }

    //GOOGLE PLAY SERVICES CHECK
    public void googlePlayCheck() {
        Log.d("GOOGLE PLAY CHECK ", "checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.e("GOOGLE PLAY CHECK ", "isServicesOK: Google Play Services is working");
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d("GOOGLE PLAY CHECK ", " an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, 1);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
    }

    //GPS CHECK
    public void gpsCheck() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);//dialog warning that gps disabled
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();//don't forget otherwise dialog wont show
        } else {
            Log.e("GPS", "ENABLED");
        }
    }

    //STOPPING LOCATION UPDATES ON PAUSE
    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }else{
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());//update only if there is permission
        }
    }
}

