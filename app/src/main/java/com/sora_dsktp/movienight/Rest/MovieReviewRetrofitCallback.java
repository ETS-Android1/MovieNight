/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

import android.util.Log;

import com.sora_dsktp.movienight.Model.JsonReviewsApiModel;

import javax.crypto.spec.DESedeKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 31/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public class MovieReviewRetrofitCallback<J> implements Callback<JsonReviewsApiModel> {

    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();

    @Override
    public void onResponse(Call<JsonReviewsApiModel> call, Response<JsonReviewsApiModel> response)
    {
        if(response.isSuccessful())
        {
            Log.d(DEBUG_TAG,"We got a response for reviews from the API");
        }
        else
        {
            Log.e(DEBUG_TAG,"We did not get a response for reviews from the API");
        }
    }

    @Override
    public void onFailure(Call<JsonReviewsApiModel> call, Throwable t)
    {
        t.printStackTrace();
        Log.e(DEBUG_TAG,"There was an error retrieving the reviews for this movies");
    }
}
