package com.happytrees.finalproject.database;

import com.happytrees.finalproject.model_txt_search.TxtGeometry;
import com.happytrees.finalproject.model_txt_search.TxtPhoto;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Boris on 2/20/2018.
 */

public class ResultDB extends SugarRecord{

    public    String formatted_address;//vicinity
    public TxtGeometry geometry;
    public String name;
    public List<TxtPhoto> photos ;

    //required empty constructor
    public ResultDB() {
    }

    public ResultDB(String formatted_address, TxtGeometry geometry, String name, List<TxtPhoto> photos) {
        this.formatted_address = formatted_address;
        this.geometry = geometry;
        this.name = name;
        this.photos = photos;
    }
}
