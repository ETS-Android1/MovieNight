/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Rest;

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
import com.sora_dsktp.movienight.Screens.DetailsScreen;

import java.util.ArrayList;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 1/4/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
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

    @NonNull
    @Override
    public Loader<ArrayList<Video>> onCreateLoader(int id, @Nullable Bundle args)
    {
        Log.d(DEBUG_TAG,"Creating a video loader...");
        // return a newly created Loader
        return new MyVideoLoader(mContext,args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Video>> loader, ArrayList<Video> data)
    {
        //Load is finished do something with the results
        Log.d(DEBUG_TAG,"onLoadFinished for videos...");
        VideoAdapter adapter = sController.getVideoAdapter();

        if(data.size() == 0)
        {
            sController.showEmptyVideosLayout();
        }
        else
        {
            adapter.addData(data);
            adapter.notifyDataSetChanged();
            sController.hideEmptyVideosLayout();
            sController.setFirstTrailerURL(data.get(0).getYoutubeKey());
        }



    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Video>> loader) {

    }

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

        @Nullable
        @Override
        public ArrayList<Video> loadInBackground()
        {
            Log.d(DEBUG_TAG,"loadInBackgournd is executed for Videos....");
            return VideoClient.makeRequest(mArgs.getInt("movie_id"));
        }

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
