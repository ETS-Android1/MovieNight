/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Network;

import android.util.Log;

import com.sora_dsktp.movienight.Model.JsonMoviesApiModel;
import com.sora_dsktp.movienight.Model.Movie;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
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

/**
 * This class has a inner Retrofit interface to define the call to the API.
 * It also has a static method building a retrofit client and making the call to
 * the server synchronously
 */
public class MovieClient
{

    //Log tag for LogCat usage
    private  static  String DEBUG_TAG = "#MovieClient.java" ;

    /**
     * Interface defining a retrofit call
     */
    public interface MovieRetrofit
     {
         //"kind" is either popular or top_rated
         @GET("movie/{sort}/")
         Call<JsonMoviesApiModel> browseMovies(@Path("sort") String sort, @Query("api_key") String api_key , @Query("page") int page);
     }

    /**
     * Actual method for making a request to the Movie DB API using Retrofit library
     * @param sort_key sort order of getting the movies . Top rated or popular
     */
     public static ArrayList<Movie> makeRequest(String sort_key, int page)
     {
         ArrayList<Movie> movies = new ArrayList<>();
         //Create a retrofit builder
         Retrofit.Builder builder = new Retrofit.Builder();
         //Add the base url of the API and a Gson converted to convert the response
         //into JsonMoviesApiModel object
         builder.baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());
         // create a retrofit object
         Retrofit retrofit = builder.build();
         MovieRetrofit client = retrofit.create(MovieRetrofit.class);
         // create a call object
         Call<JsonMoviesApiModel> call = client.browseMovies(sort_key,API_KEY,page);
         // make a call to the server synchronously
         Log.d(DEBUG_TAG,"Making the request.........");
         try
         {
             Log.d(DEBUG_TAG,"Got the response inside MovieClient.java");
             Response<JsonMoviesApiModel> moviesApiModelResponse = call.execute();
             if(moviesApiModelResponse.isSuccessful()) //Returns true if code() is in the range [200..300)
             {
                 // the request hasn't any errors so save the
                 // results inside movie list
                 movies = moviesApiModelResponse.body().getResults();
                 return movies;
             }
         }
         catch (IOException e)
         {
             e.printStackTrace();
             Log.e(DEBUG_TAG,"There was an error with making the request to the movie API");
         }
         return movies;
     }
}
