/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sora_dsktp.movienight.Adapters.MoviesAdapter;
import com.sora_dsktp.movienight.Controllers.MainScreenUiController;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 27/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public   class DbBroadcastReceiver extends BroadcastReceiver
{
    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();
    private MoviesAdapter mAdapter;


    public DbBroadcastReceiver(MoviesAdapter mAdapter)
    {
        this.mAdapter = mAdapter;
    }


    public static final String ACTION_DATABASE_CHANGED = "com.sora_dsktp.movienight.Utirls.DATABASE_CHANGED";

    /**
     * This method is called when a broadcast with the action ACTION_DATABASE_CHANGED
     * is broadcasted. This method update's the recyclerView by removing the movie that
     * was deleted from the database <b>ONLY</b> if the sort method is favourite movies .Otherwise
     * it does nothing
     * @param context the context from the broadcast was send
     * @param intent the intent that was broadcasted
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(DEBUG_TAG,"ACTION_DATABASE_CHANGED broadcast has been received");
        // Get a reference to the sharedPreferences
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
        // get the SortKey and defaultSort order strings from the resources
        String sortKey = context.getResources().getString(R.string.sort_order_key);
        String defaultSortOrder = context.getResources().getString(R.string.sort_popular_movies_value);
        // get the sort order from the preferences
        String sortOrder = sharedPreferences.getString(sortKey,defaultSortOrder);
        // get the sort favourite string resource
        String sortFavourite = context.getResources().getString(R.string.sort_favourite_movies_value);
        // compare the sort order and if it is equal to the sort favourite method
        // call the adapter's method to remove the movie from the recyclerview
        if (sortOrder.equals(sortFavourite))
        {
            //Get the movie ojbect from the intent
            Movie movie = intent.getParcelableExtra(context.getString(R.string.EXTRA_MOVIE_TO_DEL_OBJ));
            //Get the adapter position of the movie from the intent
            int id = intent.getIntExtra(context.getResources().getString(R.string.EXTRA_KEY_MOVIE_ID),-1);
            if (id!=-1) // if id == -1 means that the extra from the intent does not exist
            {
                // Remove the favourite movie from the adapter
                // since we already removed this movie from the database
                mAdapter.removeItemFromRecyclerView(movie,id);
            }
        }
        else{} // We don't need to remove anything from the recyclerView if the sort method is popular/top rated cause we don't have to.
    }
}
