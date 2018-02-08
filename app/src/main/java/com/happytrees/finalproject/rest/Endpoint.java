package com.happytrees.finalproject.rest;



import com.happytrees.finalproject.model_nearby_search.NearbyResponse;
import com.happytrees.finalproject.model_txt_search.TxtResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface Endpoint {

    //TXTSEARCH
    @GET("/maps/api/place/textsearch/json")
    Call<TxtResponse> getMyResults(@Query("query") String query, @Query("key") String key);

    //NEARBYSEARCH
    @GET("/maps/api/place/textsearch/json")
    Call<NearbyResponse>getNearbyResults(@Query("location") String location, @Query("radius") String radius,@Query("keyword") String keyword,@Query("key") String key);
}

/*
DISPLAY IMAGE ACCORDING TO PHOTOREFERENCE
////////////////////////////////////////////
https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=INSERT_PHOTOREFERENCE&key=AIzaSyDTLvyt5Cry0n5eJDXWJNTluMHRuDYYc5s
 */