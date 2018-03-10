package com.happytrees.finalproject.model_txt_search;

import java.util.List;

/**
 * Created by Boris on 2/7/2018.
 */

public class TxtResult {
    public    String formatted_address;
    public    TxtGeometry geometry;
    public String name;
    public List<TxtPhoto> photos ;
    public String place_id;

    @Override
    public String toString() {
        return  name ;
    }
}

