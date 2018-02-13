package com.happytrees.finalproject.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.happytrees.finalproject.R;
import com.happytrees.finalproject.fragments.FragmentA;
import com.happytrees.finalproject.fragments.FragmentB;

//https://www.google.co.il/maps/@32.0662593,34.7698209,15z --> put here your latitude , longitude

public class MainActivity extends AppCompatActivity {

//SAVE INSTANCE ON ROTATION CHANGE --> YET TO COME

    FusedLocationProviderClient mFusedLocationClient;
    public static final int REQUEST_LOCATION_CODE = 99;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////CHECK IF GPS ENABLED
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {//if gps not enabled display dialog warning message
            buildAlertMessageNoGps();
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)//VERSION_CODES.M = Android 6.0    we check if our minimum sdk greater or equal to 6.0 (this when runtime permissions first took place)
        {
            checkLocationPermission();

        }

        //set location provider
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        //get last location
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.i("MAPS", "Latitude " + location.getLatitude() + " Longitude " + location.getLongitude());
                }
            }
        });





        //INIT FRAGMENT A
        FragmentA fragmentA = new FragmentA();//generate a new Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();//call the Fragment Manager
        fragmentManager.beginTransaction().replace(R.id.MainContainer, fragmentA).commit();//add the fragment to the layout



        if(isTablet())//means device used xlarge xml layout of MainActivity,due to device large screen(tablet)
        {
            //INIT FRAGMENT B
            FragmentB fragmentB = new FragmentB();

            fragmentManager.beginTransaction().replace (R.id.ExtraContainer, fragmentB).commit();//we add FragmentB only to sub layout of xlarge MainActivity called ExtraContainer ,and only if its not null(its null if device detected normal size screen)
        }


    }


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
    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)//dialog cant be cancelable with back key
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    //CHECKS IF THERE ARE GRANTED PERMISSIONS ALREADY ,IF NOT ASKS RUNTIME PERMISSION
    public boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED)//check if there already was granted permission
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))//display dialog with explanation(PermissionRationale) why user should grant permission
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return true;
    }

    //CHECKS RESULT OF RUNTIME PERMISSION REQUEST
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
                case REQUEST_LOCATION_CODE:
                    if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                        {

                        }
                    }
                    else
                    {
                        Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                    }
            }


        }
    }

