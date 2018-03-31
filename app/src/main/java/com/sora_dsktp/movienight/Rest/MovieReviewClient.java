/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

import com.sora_dsktp.movienight.Model.JsonReviewsApiModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.sora_dsktp.movienight.Utils.Constants.API_KEY;
import static com.sora_dsktp.movienight.Utils.Constants.BASE_URL;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 31/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public class MovieReviewClient
{
    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();


    private interface MovieReviewRetrofit
    {
        //"id" is the id of the movie to get a review
        @GET("movie/{id}/reviews")
        Call<JsonReviewsApiModel> browseMovieReviews(@Path("id") String movie_id, @Query("api_key") String api_key , @Query("page") int page);
    }

    /**
     * Actual method for making a request to the Movie DB API using Retrofit library
     * @param callBack callback object to handle the response from the server
     * @param movieID the movie id which we get the reviews for.
     */
    public static void makeRequest(MovieReviewRetrofitCallback<JsonReviewsApiModel> callBack, String movieID, int page)
    {
        //Create a retrofit builder
        Retrofit.Builder builder = new Retrofit.Builder();
        //Add the base url of the API and a Gson converted to convert the response
        //into Movie object
        builder.baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());
        // create a retrofit object
        Retrofit retrofit = builder.build();
        MovieReviewClient.MovieReviewRetrofit client = retrofit.create(MovieReviewClient.MovieReviewRetrofit.class);
        // create a call object
        Call<JsonReviewsApiModel> call = client.browseMovieReviews(movieID,API_KEY,page);
        // make a call to the server asynchronously
        call.enqueue(callBack);
    }


}
