/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest.Loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.sora_dsktp.movienight.Adapters.ReviewAdapter;
import com.sora_dsktp.movienight.Adapters.VideoAdapter;
import com.sora_dsktp.movienight.Controllers.DetailScreenUiController;
import com.sora_dsktp.movienight.Model.Review;
import com.sora_dsktp.movienight.Rest.ReviewClient;
import com.sora_dsktp.movienight.Screens.DetailsScreen;

import java.util.ArrayList;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 1/4/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */

/**
 * Class implementing the LoadeCallbacks interface . We use this class to create and handle the
 * data fetch from the API in a background thread
 */
public class ReviewLoader implements LoaderManager.LoaderCallbacks<ArrayList<Review>>{

    private static final String DEBUG_TAG = "#ReviewLoader.java";
    private static  DetailScreenUiController sController;
    private final DetailsScreen mDetailScreen;
    private final Context mContext;


    public ReviewLoader(DetailsScreen mDetailScreen, DetailScreenUiController controller)
    {
        sController = controller;
        this.mDetailScreen = mDetailScreen;
        mContext = this.mDetailScreen.getApplicationContext();

    }


    /**
     * This is called when LoaderManager.initLoader method is called and
     * creates a MyAsyncLoader Loader object
     * @param id The unique id of the Loader
     * @param args A bundle with arguments inside
     * @return A Loader object
     */
    @NonNull
    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(DEBUG_TAG,"Creating review loader....");
        return new MyReviewLoader(mContext,args);
    }

    /**
     * This method is called when deliverResult is called , meaning that is called
     * when a request is made to the API and a result is ready OR a cached result is ready
     * and is server to this method.
     * @param loader The loader object
     * @param data The ready / cached list with the data from the loader
     */
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Review>> loader, ArrayList<Review> data)
    {
        Log.d(DEBUG_TAG,"OnLoadFinished for the reviews....");
        ReviewAdapter adapter = sController.getReviewAdapter();

        if(data.size() == 0)
        {
            //if there is not review's for the movie
            //show the empty review layout
            sController.showEmptyReviewLayout();
        }
        else
        {
            //there are review's so push them to
            //the adapter , notify the adapter
            //and hide the empty Review layout
            adapter.pushDataToTheRecyclerView(data);
            adapter.notifyDataSetChanged();
            sController.hideEmptyReviewLayout();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Review>> loader)
    {
        Log.d(DEBUG_TAG,"On loader reset");
    }



    /**
     * Inner class extending the AsyncTaskLoader class
     */
    private static class MyReviewLoader extends AsyncTaskLoader<ArrayList<Review>> {

        private ArrayList<Review> mCachedReviewsList = null;
        private final int movieID;


        public MyReviewLoader(@NonNull Context context, Bundle args) {
            super(context);
            movieID = args.getInt("movie_id");
        }

        /**
         * Check's if there is cached result with an if statement
         * and if there is call's deliverResult method . Otherwise
         * it force's the loader to make the request to the API again
         */
        @Override
        protected void onStartLoading()
        {
            if(mCachedReviewsList!=null)
            {
                //there is cached data so deliver them
                //to onLoadFinished() method
                deliverResult(mCachedReviewsList);
            }
            else
            {
                //there aren't any cached data
                //so force the load
                forceLoad();
            }

        }

        /**
         * Does the request to the API in a background thread
         * @return the ArrayList<Movie> containing the movies from the API
         */
        @Nullable
        @Override
        public ArrayList<Review> loadInBackground()
        {
            Log.d(DEBUG_TAG,"loadInBackground for reviews....");
            ArrayList<Review> reviews = ReviewClient.makeRequest(movieID,1);
            return reviews;
        }

        /**
         * Sends the result of the load to the registered listener. In this
         * case the OnLoadFinished() method
         * @param data The result of the load
         */
        @Override
        public void deliverResult(@Nullable ArrayList<Review> data)
        {
            Log.d(DEBUG_TAG,"Caching the reviews list...");
            mCachedReviewsList = data; // cache the data
            super.deliverResult(data);
        }
    }
}
