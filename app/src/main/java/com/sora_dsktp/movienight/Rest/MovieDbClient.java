/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

import com.sora_dsktp.movienight.Model.JsonObjectResultDescription;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.sora_dsktp.movienight.Utils.Constants.API_KEY;
import static com.sora_dsktp.movienight.Utils.Constants.BASE_URL;

/**
 This file created by Georgios Kostogloudis
 and was last modified on 23/2/2018.
 The name of the project is MovieNight and it was created as part of
 UDACITY ND programm.
 */

public class MovieDbClient
{

    private static final String DEBUG_TAG = "#NetWorkUtils.java";

    /**
     * Interface defining a retrofit call
     */
    public interface RetrofitCallInterface
     {
         //"kind" is either popular or top_rated
         @GET("movie/{sort}/")
         Call<JsonObjectResultDescription> browseMovies(@Path("sort") String sort, @Query("api_key") String api_key , @Query("page") int page);
     }

    /**
     * Actual method for making a request to the Movie DB API using Retrofit library
     * @param callBack callback object to handle the response from the server
     * @param sort_key sort order of getting the movies . Top rated or popular
     */
     public static void makeRequest(CustomCallBack<JsonObjectResultDescription> callBack,String sort_key, int page)
     {
         //Create a retrofit builder
         Retrofit.Builder builder = new Retrofit.Builder();
         //Add the base url of the API and a Gson converted to convert the response
         //into Movie object
         builder.baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());
         // create a retrofit object
         Retrofit retrofit = builder.build();
         RetrofitCallInterface client = retrofit.create(RetrofitCallInterface.class);
         // create a call object
         Call<JsonObjectResultDescription> call = client.browseMovies(sort_key,API_KEY,page);
         // make a call to the server asynchronously
         call.enqueue(callBack);
     }
}
