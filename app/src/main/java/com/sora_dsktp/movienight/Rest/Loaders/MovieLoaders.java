/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest.Loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.sora_dsktp.movienight.Adapters.MoviesAdapter;
import com.sora_dsktp.movienight.Controllers.MainScreenUiController;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.Rest.MovieClient;
import com.sora_dsktp.movienight.Screens.MainScreen;

import java.util.ArrayList;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 31/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */

/**
 * Class implementing the LoadeCallbacks interface . We use this class to create and handle the
 * data fetch from the API in a background thread
 */
public  class MovieLoaders implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static final String DEBUG_TAG = "#MovieLoaders.java";
    private static MainScreenUiController sController;
    private MainScreen mainScreen;
    private Context mContext;

    public MovieLoaders(MainScreen mainScreen,MainScreenUiController controller)
    {
        this.mainScreen = mainScreen;
        sController = controller;
        mContext = this.mainScreen.getApplicationContext();
    }

    /**
     * This is called when LoaderManager.initLoader method is called and
     * creates a MyAsyncLoader Loader object
     * @param id The unique id of the Loader
     * @param args A bundle with arguments inside
     * @return A Loader object
     */
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args)
    {
        Log.d(DEBUG_TAG,"Creating loaders....");
        return new MyAsyncLoader(mContext);
    }

    /**
     * This method is called when deliverResult is called , meaning that is called
     * when a request is made to the API and a result is ready OR a cached result is ready
     * and is server to this method.
     * @param loader The loader object
     * @param data The ready / cached list with the data from the loader
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data)
    {
        Log.d(DEBUG_TAG,"onLoadFinished getting the results....");
        MoviesAdapter moviesAdapterAdapter = sController.getAdapter();

        //If the list is not empty push the data
        //to the adapter and notify him
        if((!data.isEmpty()) && (sController.UIneedsToBeUpdated()))
        {
            // notify the adapter
            moviesAdapterAdapter.setMoviesToTheAdapter(data);
            moviesAdapterAdapter.notifyDataSetChanged();

            // restore the scroll position if a device configuration occured
            sController.restoreScrollPostition();
        }
        // hide the loading indicator
        sController.hideLoadingIndicator();
        sController.setUIneedsUpdate(false);
        Log.d(DEBUG_TAG,"We got a response from the API");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader)
    {
        Log.d(DEBUG_TAG,"On Loader reset....");
    }


    /**
     * Inner class extending the AsyncTaskLoader class
     */
    private static class MyAsyncLoader extends AsyncTaskLoader<ArrayList<Movie>> {

        private ArrayList<Movie> mMovies = new ArrayList<>();
        private boolean updateCache = false;

        public MyAsyncLoader(Context context)
        {
           super(context);

        }

        /**
         * Check's if there is cached result with an if statement
         * and if there is call's deliverResult method . Otherwise
         * it force's the loader to make the request to the API again
         */
        @Override
        protected void onStartLoading() {
            Log.d(DEBUG_TAG,"onStartLoading called....");
            if (!mMovies.isEmpty())
            {
                Log.d(DEBUG_TAG,"Delivering the results.....");
                if(!isStarted()) deliverResult(mMovies); // if the loader isn't running deliver the result's
                sController.setLoading(false);
                sController.hideLoadingIndicator();
            }
            else
            {
                Log.d(DEBUG_TAG,"Executing the loadInBackground method again...");
                sController.setLoading(true);
                sController.showLoadingIndicator();
                forceLoad();
            }

        }

        /**
         * Does the request to the API in a background thread
         * @return the ArrayList<Movie> containing the movies from the API
         */
        @Override
        public synchronized ArrayList<Movie> loadInBackground()
        {
            Log.d(DEBUG_TAG,"loadInBackground called....");
            ArrayList<Movie> movies = MovieClient.makeRequest(sController.getSortOrder(), sController.getPageToIndex());
            if(!movies.isEmpty())
            {
                //increment the page to index on the next call
                sController.incrementAPIindex();
                sController.setLoading(false); // results from background thread is here so set the loading variable to false
                updateCache = true; // we need to update our cache variable cause we got new data
            }
            return movies;
        }

        /**
         * Sends the result of the load to the registered listener.
         *
         * @param data The result of the load
         */
        @Override
        public synchronized void deliverResult(ArrayList<Movie> data)
        {
            //Caching the movies...
            Log.d(DEBUG_TAG,"Caching the results.....");
            //if there is need we update the cache list
            if(updateCache)
            {
                mMovies.addAll(data);
                updateCache = false;
            }
            super.deliverResult(mMovies);
        }




    }
}
