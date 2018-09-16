/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Network.Loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.sora_dsktp.movienight.Adapters.VideoAdapter;
import com.sora_dsktp.movienight.Controllers.DetailScreenUiController;
import com.sora_dsktp.movienight.Model.Video;
import com.sora_dsktp.movienight.Network.VideoClient;
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
public class VideoLoader implements LoaderManager.LoaderCallbacks<ArrayList<Video>> {

    private static final String DEBUG_TAG = "#VideoLoader.java";

    private final Context mContext;
    private static  DetailScreenUiController sController;
    public VideoLoader(DetailsScreen detailsScreen,DetailScreenUiController controller)
    {
        mContext = detailsScreen;
        sController = controller;
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
    public Loader<ArrayList<Video>> onCreateLoader(int id, @Nullable Bundle args)
    {
        Log.d(DEBUG_TAG,"Creating a video loader...");
        // return a newly created Loader
        return new MyVideoLoader(mContext,args);
    }

    /**
     * This method is called when deliverResult is called , meaning that is called
     * when a request is made to the API and a result is ready OR a cached result is ready
     * and is server to this method.
     * @param loader The loader object
     * @param data The ready / cached list with the data from the loader
     */
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Video>> loader, ArrayList<Video> data)
    {
        //Load is finished do something with the results
        Log.d(DEBUG_TAG,"onLoadFinished for videos...");
        VideoAdapter adapter = sController.getVideoAdapter();

        if(data.size() == 0)
        {
            //There aren't any video to display
            //show the empty video layout
            sController.showEmptyVideosLayout();
        }
        else
        {
            //there are video to display
            //add them to the adapter
            adapter.addData(data);
            //notify the adapter
            adapter.notifyDataSetChanged();
            //hide the empty layout
            sController.hideEmptyVideosLayout();
            //save the first video url to share it later
            sController.setFirstTrailerURL(data.get(0).getYoutubeKey());
        }



    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Video>> loader) {

    }

    /**
     * Inner class extending the AsyncTaskLoader class
     */
    private static class MyVideoLoader extends AsyncTaskLoader<ArrayList<Video>>
    {
        // variable to store the cached result
        private ArrayList<Video> cachedResult = null;
        private Bundle mArgs;

        public MyVideoLoader(@NonNull Context context, Bundle args)
        {
            super(context);
            mArgs = args;
        }

        /**
         * Does the request to the API in a background thread
         * @return the ArrayList<Movie> containing the movies from the API
         */
        @Nullable
        @Override
        public ArrayList<Video> loadInBackground()
        {
            Log.d(DEBUG_TAG,"loadInBackgournd is executed for Videos....");
            return VideoClient.makeRequest(mArgs.getInt("movie_id"));
        }

        /**
         * Check's if there is cached result with an if statement
         * and if there is call's deliverResult method . Otherwise
         * it force's the loader to make the request to the API again
         */
        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if(cachedResult == null)
            {
                //no cache force load
                forceLoad();
            }
            else
            {
                // there is cached data
                // deliver it
                deliverResult(cachedResult);
            }
        }

        @Override
        public void deliverResult(@Nullable ArrayList<Video> data) {
            super.deliverResult(data);
        }
    }
}
