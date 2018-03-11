package com.happytrees.finalproject.database;

import com.orm.SugarRecord;

/**
 * Created by Boris on 3/9/2018.
 */

public class LastSearch extends SugarRecord {

    public  String name;
    public  String formatted_address;
    public double lat;
    public double lng;


    //required empty constructor
    public LastSearch() {
    }

    public LastSearch(String name, String formatted_address, double lat, double lng) {
        this.name = name;
        this.formatted_address = formatted_address;
        this.lat = lat;
        this.lng = lng;
    }
}
