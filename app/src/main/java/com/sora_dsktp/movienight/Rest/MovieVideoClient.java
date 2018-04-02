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
public class MovieVideoClient
{
    //Log tag for LogCat usage
    private static final String DEBUG_TAG = "#MovieVideoClient.java";

    private interface MovieVideoRetrofit
    {
        @GET("movie/{movie_id}/videos")
        Call<JsonVideosApiModel> browseMovies(@Path("movie_id")int movieID, @Query("api_key")String API_KEY);
    }

    public static ArrayList<Video> makeRequest(int movieID)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieVideoClient.MovieVideoRetrofit client = retrofit.create(MovieVideoRetrofit.class);

        Call<JsonVideosApiModel> movieVideosCall = client.browseMovies(movieID,Constants.API_KEY);

        ArrayList<Video> results = new ArrayList<>();
        try
        {
            Log.d(DEBUG_TAG,"Making the request for the video....");
            Response<JsonVideosApiModel> apiResponse = movieVideosCall.execute();
            if(apiResponse.isSuccessful())
            {
                Log.d(DEBUG_TAG,"We have a response about the video....");
                results = apiResponse.body().getResults();
            }
            else
            {
                //The response from the API contains errors
                Log.e(DEBUG_TAG,"Error message: " + apiResponse.message());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return results;
    }
}
