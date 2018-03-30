/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

import android.util.Log;

import com.sora_dsktp.movienight.Model.JsonMoviesApiModel;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.Adapters.MoviesAdapter;
import com.sora_dsktp.movienight.Controllers.MainScreenUiController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 This file created by Georgios Kostogloudis on 23/2/2018
 and was last modified on 5/3/2018.
 The name of the project is MovieNight and it was created as part of
 UDACITY ND programm.
 */


/**
 * This class is used to implement a callback method
 * which is called when we get a response from the movie db API
 * @param <J>
 */
public class CustomCallBack<J> implements Callback<JsonMoviesApiModel> {
    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();

    private MoviesAdapter mAdapter;
    private MainScreenUiController mController;
    public CustomCallBack(MoviesAdapter mAdapter)
    {
        this.mAdapter = mAdapter;
    }

    /**
     * Is called when we have response from the API
     * @param call
     * @param response Response object contains the response from the server
     */
    @Override
    public void onResponse(Call<JsonMoviesApiModel> call, Response<JsonMoviesApiModel> response)
    {
        //Get the object from the response body
        JsonMoviesApiModel result = response.body();
        //increment the page to index on the next call
        mController.incrementAPIindex();
        //Get the movies from the call
        ArrayList<Movie> movies = result.getResults();
        //If the list is not empty push the data
        //to the adapter and notify him
        if(!movies.isEmpty())
        {
            // hide the loading indicator
            mController.hideLoadingIndicator();
            // notify the adapter
            mAdapter.pushTheDataToTheAdapter(movies);
            mAdapter.notifyDataSetChanged();
        }
        mController.setLoading(false);
        Log.d(DEBUG_TAG,"We got a response from the API");
    }

    /**
     * This method is called when there is an error getting a response from the server
     * for example when we have a 404 error
     * @param call
     * @param t The exception we get
     */
    @Override
    public void onFailure(Call<JsonMoviesApiModel> call, Throwable t)
    {
        t.printStackTrace();
        Log.e(DEBUG_TAG,"There was an error fetching data from the API");
    }

    /**
     * Setter method
     * @param controller the Ui controller to use
     */
    public void setUIcontroller(MainScreenUiController controller)
    {
        this.mController = controller;
    }
}
