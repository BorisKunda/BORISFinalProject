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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.happytrees.finalproject.R;
import com.happytrees.finalproject.fragments.FragmentA;
import com.happytrees.finalproject.fragments.FragmentB;

//SAVE INSTANCE ON ROTATION CHANGE --> YET TO COME
//USE ANOTHER DATABASE THAN SUGAR ORM FOR INSTANCE RXJAVA


public class MainActivity extends AppCompatActivity  {
//https://www.google.co.il/maps/@32.0662593,34.7698209,15z --> put here your latitude , longitude


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
}

