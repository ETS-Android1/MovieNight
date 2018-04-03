/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

import android.util.Log;

import com.sora_dsktp.movienight.Model.JsonReviewsApiModel;
import com.sora_dsktp.movienight.Model.Review;

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
 * This file created by Georgios Kostogloudis
 * and was last modified on 31/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public class ReviewClient
{
    //Log tag for LogCat usage
    private static  final String DEBUG_TAG = "#ReviewClient.java";


    private interface MovieReviewRetrofit
    {
        //"id" is the id of the movie to get a review
        @GET("movie/{id}/reviews")
        Call<JsonReviewsApiModel> browseMovieReviews(@Path("id") int movie_id, @Query("api_key") String api_key , @Query("page") int page);
    }

    /**
     * Actual method for making a request to the Movie DB API using Retrofit library
     * @param movieID the movie id which we get the reviews for.
     * @param page the page to index on the API
     */
    public static ArrayList<Review> makeRequest(int movieID, int page)
    {
        //Create a retrofit builder
        Retrofit.Builder builder = new Retrofit.Builder();
        //Add the base url of the API and a Gson converted to convert the response
        //into JsonReviewApiModel
        builder.baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());
        // create a retrofit object
        Retrofit retrofit = builder.build();
        ReviewClient.MovieReviewRetrofit client = retrofit.create(ReviewClient.MovieReviewRetrofit.class);
        // create a call object
        Call<JsonReviewsApiModel> call = client.browseMovieReviews(movieID,API_KEY,page);
        //create a new empty ArrayList<Review> object
        ArrayList<Review> reviewArrayList = new ArrayList<>();
        // make a call to the server synchronously
        try
        {
            Response<JsonReviewsApiModel> apiResponse = call.execute();
            int apiCode = apiResponse.code();
            if(apiCode == 200)
            {
                //Successfull response
                 reviewArrayList = apiResponse.body().getResults();
                return reviewArrayList;
            }
            else
            {
                //there was an error
                Log.e(DEBUG_TAG,"The was an error with the response for the reviews...");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.e(DEBUG_TAG,"IO exception catched inside ReviewClient.java");
        }
        return reviewArrayList;
    }


}
