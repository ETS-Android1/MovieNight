/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.sora_dsktp.movienight.Controllers.MainScreenUiController;
import com.sora_dsktp.movienight.R;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 30/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */

/**
 * This class extends the BroadcastReceiver class and overrides the onReceive method
 * which is called when a CONNECTIVITY_CHANGE action is broadcasted through the application
 */
public class InternetBroadcastReceiver  extends BroadcastReceiver
{

    private final MainScreenUiController mMainScreenUiController;
    private final String DEBUG_TAG = this.getClass().getSimpleName();

    public InternetBroadcastReceiver(MainScreenUiController controller)
    {
        this.mMainScreenUiController = controller;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork;
        // Get the network info
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(activeNetwork != null)
        {
            // If we have Internet connectivity
            // check to see if we need to update the ui
            if(activeNetwork.isConnected())
            {
                mMainScreenUiController.setWeHaveInternet(true);
                if(!mMainScreenUiController.favouritesMode())
                {
                    Log.d(DEBUG_TAG,"Fetching movies from the broadcast receiver on receive method.....");
                    mMainScreenUiController.fetchMovies();
                }
            }
        }
        // If we lose Internet connectivity show a Toast message
        else
        {
            Toast.makeText(context, R.string.no_connection_message,Toast.LENGTH_LONG).show();
            mMainScreenUiController.setWeHaveInternet(false);
        }

    }
}
