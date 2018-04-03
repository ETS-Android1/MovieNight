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

import com.sora_dsktp.movienight.Adapters.ReviewAdapter;
import com.sora_dsktp.movienight.Adapters.VideoAdapter;
import com.sora_dsktp.movienight.Controllers.DetailScreenUiController;
import com.sora_dsktp.movienight.Model.Review;
import com.sora_dsktp.movienight.Screens.DetailsScreen;

import java.util.ArrayList;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 1/4/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
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


    @NonNull
    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(DEBUG_TAG,"Creating review loader....");
        return new MyReviewLoader(mContext,args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Review>> loader, ArrayList<Review> data)
    {
        Log.d(DEBUG_TAG,"OnLoadFinished for the reviews....");
        ReviewAdapter adapter = sController.getReviewAdapter();

        if(data.size() == 0)
        {
            sController.showEmptyReviewLayout();
        }
        else
        {
            adapter.pushDataToTheRecyclerView(data);
            adapter.notifyDataSetChanged();
            sController.hideEmptyReviewLayout();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Review>> loader)
    {

    }




    private static class MyReviewLoader extends AsyncTaskLoader<ArrayList<Review>> {

        private ArrayList<Review> mCachedReviewsList = null;
        private final int movieID;


        public MyReviewLoader(@NonNull Context context, Bundle args) {
            super(context);
            movieID = args.getInt("movie_id");
        }

        @Override
        protected void onStartLoading()
        {
            if(mCachedReviewsList!=null)
            {
                deliverResult(mCachedReviewsList);
            }
            else
            {
                forceLoad();
            }

        }

        @Nullable
        @Override
        public ArrayList<Review> loadInBackground()
        {
            Log.d(DEBUG_TAG,"loadInBackground for reviews....");
            ArrayList<Review> reviews = ReviewClient.makeRequest(movieID,1);
            return reviews;
        }


        @Override
        public void deliverResult(@Nullable ArrayList<Review> data)
        {
            Log.d(DEBUG_TAG,"Caching the reviews list...");
            mCachedReviewsList = data;
            super.deliverResult(data);
        }
    }
}
