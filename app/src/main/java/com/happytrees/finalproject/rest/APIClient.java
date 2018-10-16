package com.happytrees.finalproject.rest;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {//class dedicated to Retrofit instance

    private static final String BASE_URL = "https://maps.googleapis.com";
    private static Retrofit retrofit;

    //private constructor
    private APIClient() {
    }

    public static Retrofit getClient() {
        if (retrofit==null) {//create singleton instance of retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}