/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

import android.util.Log;

import com.sora_dsktp.movienight.Model.JsonVideosApiModel;
import com.sora_dsktp.movienight.Model.Video;
import com.sora_dsktp.movienight.Utils.Constants;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 31/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */

/**
 * This class has a inner Retrofit interface to define the call to the API.
 * It also has a static method building a retrofit client and making the call to
 * the server synchronously
 */
public class VideoClient
{
    //Log tag for LogCat usage
    private static final String DEBUG_TAG = "#VideoClient.java";

    /**
     * This interface defines a retrofit method using annotations
     */
    private interface MovieVideoRetrofit
    {
        @GET("movie/{movie_id}/videos")
        Call<JsonVideosApiModel> browseMovies(@Path("movie_id")int movieID, @Query("api_key")String API_KEY);
    }

    /**
     * Actual method for making a request to the Movie DB API using Retrofit library
     * @param movieID the movie id which we get the video's for.
     */
    public static ArrayList<Video> makeRequest(int movieID)
    {
        //Create a retrofit object using the base url constant
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // add a converter factory to convert the response to actual JsonApiModel
                .build();

        VideoClient.MovieVideoRetrofit client = retrofit.create(MovieVideoRetrofit.class); // create a client

        Call<JsonVideosApiModel> movieVideosCall = client.browseMovies(movieID,Constants.API_KEY); // create a call object

        ArrayList<Video> results = new ArrayList<>();
        //make the request to the server
        try
        {
            Log.d(DEBUG_TAG,"Making the request for the video....");
            Response<JsonVideosApiModel> apiResponse = movieVideosCall.execute();
            if(apiResponse.isSuccessful()) //Returns true if code() is in the range [200..300)
            {
                Log.d(DEBUG_TAG,"We have a response about the video....");
                results = apiResponse.body().getResults();
            }
            else
            {
                //The response from the API contains errors
                Log.e(DEBUG_TAG,"Error message: " + apiResponse.message());
                Log.e(DEBUG_TAG,"Error code = " + apiResponse.code());
                Log.e(DEBUG_TAG,"Response to string = " + apiResponse.toString());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return results;
    }
}
