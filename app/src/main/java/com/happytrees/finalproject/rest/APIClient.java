package com.happytrees.finalproject.rest;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {//class dedicated to Retrofit instance
    public static final String BASE_URL = "https://maps.googleapis.com";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {//if instance of Retrofit is null build new one using GSON  ".addConverterFactory(GsonConverterFactory.create())"  and base url ".baseUrl(BASE_URL)"
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}