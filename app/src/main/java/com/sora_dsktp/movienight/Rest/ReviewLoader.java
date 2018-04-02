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

    private final String DEBUG_TAG = "#ReviewLoader.java";
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
        return new MyReviewLoader(mContext);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Review>> loader, ArrayList<Review> data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Review>> loader) {

    }

    private static class MyReviewLoader extends AsyncTaskLoader<ArrayList<Review>> {
        public MyReviewLoader(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public ArrayList<Review> loadInBackground() {
            return null;
        }
    }
}
