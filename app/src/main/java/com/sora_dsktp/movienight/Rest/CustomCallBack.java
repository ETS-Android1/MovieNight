/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.sora_dsktp.movienight.Model.JsonObjectResultDescription;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.Screens.MainScreen;
import com.sora_dsktp.movienight.Utils.MoviesAdapter;

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
public class CustomCallBack<J> implements Callback<JsonObjectResultDescription> {
    public static final String DEBUG_TAG = "#CustomCallBack.java";

    private MoviesAdapter mAdapter;
    private RelativeLayout mRelativeLayout;
    public CustomCallBack(MoviesAdapter mAdapter, RelativeLayout linearLayout)
    {
        this.mAdapter = mAdapter;
        this.mRelativeLayout = linearLayout;
    }

    /**
     * Is called when we have response from the API
     * @param call
     * @param response Response object contains the response from the server
     */
    @Override
    public void onResponse(Call<JsonObjectResultDescription> call, Response<JsonObjectResultDescription> response)
    {
        //Get the movies from the call
        ArrayList<Movie> movies = response.body().getResults();
        //If the list is not empty push the data
        //to the adapter and notify him
        if(!movies.isEmpty())
        {
            mAdapter.pushTheData(movies);
            mAdapter.notifyDataSetChanged();
            // Hide the error layout from the user
            mRelativeLayout.setVisibility(View.GONE);
            MainScreen.hideLoadingIndicator();
        }
        Log.d(DEBUG_TAG,"We got a response from the API");
    }

    /**
     * This method is called when there is an error getting a response from the server
     * for example when we have a 404 error
     * @param call
     * @param t The exception we get
     */
    @Override
    public void onFailure(Call<JsonObjectResultDescription> call, Throwable t)
    {
        t.printStackTrace();
        Log.e(DEBUG_TAG,"There was an error fetching data from the API");
    }
}
