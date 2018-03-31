/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

import android.util.Log;

import com.sora_dsktp.movienight.Model.JsonVideosApiModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 31/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public class MovieVideoRetrofitCallback<T> implements Callback<JsonVideosApiModel>
{
    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();

    @Override
    public void onResponse(Call<JsonVideosApiModel> call, Response<JsonVideosApiModel> response)
    {
        if(response.isSuccessful())
        {
            Log.d(DEBUG_TAG,"We got a response for videos from the API");
        }
        else
        {
            Log.e(DEBUG_TAG,"We did not get a response for videos from the API");
            Log.e(DEBUG_TAG,"Error message:" + response.message());
            Log.e(DEBUG_TAG,"Raw response" +response.raw());
        }

    }

    @Override
    public void onFailure(Call<JsonVideosApiModel> call, Throwable t)
    {
        t.printStackTrace();
        Log.e(DEBUG_TAG,"There was a failure with getting the video's for this movie");
    }
}
