/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

import com.sora_dsktp.movienight.Model.JsonVideosApiModel;
import com.sora_dsktp.movienight.Utils.Constants;

import retrofit2.Call;
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
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();

    private interface MovieVideoRetrofit
    {
        @GET("movie/{movie_id}/videos")
        Call<JsonVideosApiModel> browseMovies(@Path("movie_id")String movieID, @Query("api_key")String API_KEY);
    }

    public static void makeRequest(String movieID,MovieVideoRetrofitCallback<JsonVideosApiModel> callback)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieVideoClient.MovieVideoRetrofit client = retrofit.create(MovieVideoRetrofit.class);

        Call<JsonVideosApiModel> movieVideosCall = client.browseMovies(movieID,Constants.API_KEY);

        movieVideosCall.enqueue(callback);
    }
}
