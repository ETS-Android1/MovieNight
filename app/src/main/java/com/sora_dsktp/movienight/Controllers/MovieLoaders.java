/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.sora_dsktp.movienight.Adapters.MoviesAdapter;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.Rest.MovieClient;
import com.sora_dsktp.movienight.Screens.MainScreen;

import java.util.ArrayList;

import javax.crypto.spec.DESedeKeySpec;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 31/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public  class MovieLoaders implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static final String DEBUG_TAG = "#MovieLoaders.java";
    private static MainScreenUiController mController;
    private MainScreen mainScreen;
    private Context mContext;

    public MovieLoaders(MainScreen mainScreen,MainScreenUiController controller)
    {
        this.mainScreen = mainScreen;
        mController = controller;
        mContext = this.mainScreen.getApplicationContext();
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args)
    {
        Log.d(DEBUG_TAG,"Creating loaders....");
        return new MyAsyncLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data)
    {
        Log.d(DEBUG_TAG,"onLoadFinished getting the results....");
        MoviesAdapter mAdapter = mController.getmAdapter();

        //If the list is not empty push the data
        //to the adapter and notify him
        if(!data.isEmpty() && mController.UIneedsToBeUpdated())
        {
            // notify the adapter
            mAdapter.pushTheDataToTheAdapter(data);
            mAdapter.notifyDataSetChanged();
        }
        // hide the loading indicator
        mController.hideLoadingIndicator();
        mController.setLoading(false);
        mController.setUIneedsUpdate(false);
//        mController.setLoading(false);
        Log.d(DEBUG_TAG,"We got a response from the API");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader)
    {
        Log.d(DEBUG_TAG,"On Load reset....");

    }



    private static class MyAsyncLoader extends AsyncTaskLoader<ArrayList<Movie>> {

        private ArrayList<Movie> mMovies = null;

        public MyAsyncLoader(Context context)
        {
           super(context);

        }

        @Override
        protected void onStartLoading() {
            if (mMovies != null)
            {
                Log.d(DEBUG_TAG,"Delivering the results.....");
                deliverResult(mMovies);
                mController.setLoading(false);
                mController.hideLoadingIndicator();
            }
            else
            {
                Log.d(DEBUG_TAG,"Executing the loadInBackground method again...");
                mController.setLoading(true);
                mController.showLoadingIndicator();
                forceLoad();
            }

        }

        @Override
        public ArrayList<Movie> loadInBackground()
        {
            ArrayList<Movie> movies = MovieClient.makeRequest(mController.getSortOrder(),mController.getPageToIndex());
            if(!movies.isEmpty())
            {
                //increment the page to index on the next call
                mController.incrementAPIindex();
            }
            return movies;
        }

        /**
         * Sends the result of the load to the registered listener.
         *
         * @param data The result of the load
         */
        public void deliverResult(ArrayList<Movie> data)
        {
            //Caching the movies...
            Log.d(DEBUG_TAG,"Caching the results.....");
            mMovies = mController.getmAdapter().getData();
            super.deliverResult(data);
        }


    }
}
