package com.happytrees.finalproject.fragments;

/**
 * Created by Boris on 2/23/2018.
 */

public interface FragmentChanger {
    public void changeFragments(double lat,double lng);//for map fragment only .favourites fragment called from menu .menu  belongs to Main Activity so there no need in using interface for it.
}
