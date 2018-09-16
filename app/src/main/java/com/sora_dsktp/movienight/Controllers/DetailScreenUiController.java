/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Controllers;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 29/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sora_dsktp.movienight.Adapters.ReviewAdapter;
import com.sora_dsktp.movienight.Adapters.VideoAdapter;
import com.sora_dsktp.movienight.Model.DAO.DatabaseContract;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Network.Loaders.ReviewLoader;
import com.sora_dsktp.movienight.Network.Loaders.VideoLoader;
import com.sora_dsktp.movienight.Screens.DetailsScreen;
import com.sora_dsktp.movienight.Utils.Constants;

import static com.sora_dsktp.movienight.BroadcastReceivers.DbBroadcastReceiver.ACTION_DATABASE_CHANGED;

/**
 * Helper class containing methods to update the UI
 * in Detail Screen
 */
public class DetailScreenUiController
{
    private final DetailsScreen mDetailScreen;
    private final AsyncQueryHelper mQueryHelper;
    private  VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;
    private boolean mIsFavourite = false;
    private final String DEBUG_TAG = getClass().getSimpleName();

    private String mFirstMovieVideo = ""; // This field is used to store the YoutubeURL of the first trailer


    /**
     * Default constructor
     * @param detailsScreen the DetailScreen object
     */
    public DetailScreenUiController(DetailsScreen detailsScreen)
    {
        this.mDetailScreen = detailsScreen;
        mQueryHelper = new AsyncQueryHelper(mDetailScreen.getContentResolver());
    }

    /**
     * Helper method for packing the clicked movie inside
     * a contentValues object and starting a asynchronously insert into the database
     * @param movieClicked The movie object to add to the database
     */
    public void addTheMovieToTheDatabase(Movie movieClicked)
    {
        // Create content values to put the movie object inside
        final ContentValues cv = new ContentValues();

        cv.put(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_TITLE, movieClicked.getMovieTitle());
        cv.put(DatabaseContract.FavouriteMovies.COLUMN_RELEASE_DATE, movieClicked.getReleaseDate());
        cv.put(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_DESCRIPTION, movieClicked.getMovieDescription());
        cv.put(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_RATING, movieClicked.getMovieRating());
        cv.put(DatabaseContract.FavouriteMovies.COLUMN_POSTER_PATH, movieClicked.getImagePath());
        cv.put(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_ID, movieClicked.getMovieID());
        // Save the movie to the database using the Content uri with the contentValues

        //Database operation must be on a background thread
        mQueryHelper.startInsert(2,null, DatabaseContract.FavouriteMovies.CONTENT_URI,cv);
    }

    /**
     * Helper method calling a delete method asynchronously to the database for the movie
     * clicked
     * @param movieClicked The movie object to delete from the database
     */
    public void deleteTheMovieFromDatabase(Movie movieClicked)
    {
        final String selection = DatabaseContract.FavouriteMovies.COLUMN_MOVIE_TITLE + "=?";
        final String[] selectionArgs = new String[]{movieClicked.getMovieTitle()};

        // Database operation must run on a background thread
        mQueryHelper.startDelete(-1,null,DatabaseContract.FavouriteMovies.CONTENT_URI,selection,selectionArgs);
    }

    /**
     * This method creates a loader responsible for making a request in a background
     * thread to the Movies DB API for getting the reviews for a movie
     * @param mMovieClicked The Movie object that was clicked
     */
    public void getMovieReviews(Movie mMovieClicked)
    {
        //get the id of the movie
        int movieID = mMovieClicked.getMovieID();
        LoaderManager loaderManager = mDetailScreen.getSupportLoaderManager();
        //add the id of the movie into a bundle
        Bundle bundle = new Bundle();
        bundle.putInt(mDetailScreen.getString(R.string.MOVIE_ID_BUNDLE_KEY),movieID);
        //start the loader and send the id of the movie inside the bundle
        loaderManager.initLoader(3,bundle,new ReviewLoader(mDetailScreen,this));
    }

    /**
     * This method creates a loader that make's a request to the Movies DB API in a
     * background thread requesting the videos related to the movie that was clicked
     * @param mMovieClicked The Movie object that was clicked
     */
    public void getMovieVideos(Movie mMovieClicked)
    {
        //get the movie id
        int movieID = mMovieClicked.getMovieID();
        LoaderManager loaderManager = mDetailScreen.getSupportLoaderManager();
        //put the movie id inside a bundle object
        Bundle bundle = new Bundle();
        bundle.putInt(mDetailScreen.getString(R.string.MOVIE_ID_BUNDLE_KEY),movieID);
        //start the loader passing the bundle alongside.
        loaderManager.initLoader(2,bundle,new VideoLoader(mDetailScreen,this));
    }

    /**
     * This method displays e message layout to the user
     * telling him that no video's are available for this movie
     */
    public void showEmptyVideosLayout()
    {
        mDetailScreen.findViewById(R.id.empty_videos_layout).setVisibility(View.VISIBLE);

    }

    /**
     * This method HIDES the empty videos layout from the
     * user
     */
    public void hideEmptyVideosLayout()
    {
        mDetailScreen.findViewById(R.id.empty_videos_layout).setVisibility(View.GONE);

    }

    /**
     * This method HIDES the empty reviews layout from the
     * user
     */
    public void hideEmptyReviewLayout() {
        mDetailScreen.findViewById(R.id.empty_reviews_layout).setVisibility(View.GONE);
    }

    /**
     * This method displays e message layout to the user
     * telling him that no review's are available for this movie
     */
    public void showEmptyReviewLayout()
    {
        mDetailScreen.findViewById(R.id.empty_reviews_layout).setVisibility(View.VISIBLE);
    }




    /**
     * Helper class to query asynchronously the database
     */
    private class AsyncQueryHelper extends AsyncQueryHandler
    {

        public AsyncQueryHelper(ContentResolver cr) {
            super(cr);
        }

        /**
         * This method is called when a query has been completed
         * @param token This is used for identification
         * @param cookie This is a object passed from startQuery method
         * @param cursor The Cursor object holding the data from the query
         */
        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);

            Movie movieClciked = (Movie) cookie;
            if (cursor.getCount() > 0) // if the cursor has results that means that the movie is favourite
            {
                Log.d(DEBUG_TAG,"Query completed and a movie found in the database with title = " + movieClciked.getMovieTitle());
                paintTheHeartButton();
                mIsFavourite = true;
            }
            else Log.d(DEBUG_TAG,"Query completed found no movie with that title = " + movieClciked.getMovieTitle());

        }

        /**
         * This method is called when a Insert query to the database is completed.
         * @param token This is used for identification
         * @param cookie This is a object passed from startInsert
         * @param uri The new Uri pointing the path that was inserted
         */
        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            super.onInsertComplete(token, cookie, uri);

            //check to see if the insert was success by checking the Uri object
            if( uri != null)
            {
                //Added successfully
                Toast.makeText(mDetailScreen.getApplication(),"Added to your favourite movies",Toast.LENGTH_SHORT).show();
                //Paint the heart button to red
                paintTheHeartButton();
                mIsFavourite = true;
            }
        }

        /**
         * This method is called when a delete query to the database is completed.
         * @param token This is used for identification
         * @param cookie This is a object that was passed from startDelete method
         * @param itemsDeleted The number of rows that the query deleted from the database
         */
        @Override
        protected void onDeleteComplete(int token, Object cookie, int itemsDeleted) {
            super.onDeleteComplete(token, cookie, itemsDeleted);

            if(itemsDeleted == 1) // the itemsDeleted must equal to 1 otherwise something has gone wrong
            {
                //successfully removed from the favourites
                mIsFavourite = false;
                // inform the user using a toast message
                Toast.makeText(mDetailScreen.getApplicationContext(),"Removed from favourites",Toast.LENGTH_SHORT).show();
                unPaintTheHeartButton(); // call this method to unpaint the heart to indicate that the movie is not favourite anymore
                //send broadcast to update the UI
                Intent intent = new Intent(ACTION_DATABASE_CHANGED);
                // put in extra the movie object to delete with the adapter position of the movie
                // to the recyclerView.
                intent.putExtra(mDetailScreen.getResources().getString(R.string.EXTRA_MOVIE_TO_DEL_OBJ), mDetailScreen.getMovieClicked());
                intent.putExtra(mDetailScreen.getResources().getString(R.string.EXTRA_KEY_MOVIE_ID), mDetailScreen.getmMovieAdapterPosition());
                // send a broadcast that the database has been changed
                mDetailScreen.sendBroadcast(intent);
            }
        }
    }

    /**
     * Helper method to check if this movie is already a favourite by checking
     * the local database using a mQuery Helper startQuery() method
     */
    public void checkTheMovieOnDatabase(Movie movieClicked)
    {
        final String selection = DatabaseContract.FavouriteMovies.COLUMN_MOVIE_TITLE + "=?";
        final String [] SelectionArgs = new String[]{movieClicked.getMovieTitle()};
        // execute the query in a background thread
        mQueryHelper.startQuery(1,movieClicked, DatabaseContract.FavouriteMovies.CONTENT_URI,null,selection,SelectionArgs,null);
    }



    /**
     * Helper method that get's a reference to the imageButton
     * and set's the color to RED
     */
    public void paintTheHeartButton()
    {
        //mark the movie as favourite
        //by painting the button
        FloatingActionButton button = mDetailScreen.findViewById(R.id.fab_favourite);
        button.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        button.setBackgroundTintMode(null);
    }

    /**
     * Helper method that get's a reference to the imageButton
     * and set's the color to TRANSPARENT
     */
    public void unPaintTheHeartButton()
    {
        //mark the movie as not favourite
        //by repainting the heart button
        FloatingActionButton button = mDetailScreen.findViewById(R.id.fab_favourite);
        button.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        button.setBackgroundTintMode(null);
    }

    /**
     * Getter method for mIsFavourite field
     * @return the value of mIsFavourite field
     */
    public boolean getIsFavourite() {
        return mIsFavourite;
    }

    /**
     * Setter method for the VideoAdapter variable
     * @param mVideoAdapter The videoAdapter object
     */
    public void setVideoAdapter(VideoAdapter mVideoAdapter) {
        this.mVideoAdapter = mVideoAdapter;
    }

    /**
     * Getter method for the VideoAdapter variable
     * @return The videoAdapter object
     */
    public VideoAdapter getVideoAdapter() {
        return mVideoAdapter;
    }

    /**
     * Getter method for the variable ReviewAdapter
     * @return The ReviewAdapter object
     */
    public ReviewAdapter getReviewAdapter() {
        return mReviewAdapter;
    }

    /**
     * Setter method for the ReviewAdapter variable
     * @param mReviewAdapter The ReviewAdapter object
     */
    public void setReviewAdapter(ReviewAdapter mReviewAdapter) {
        this.mReviewAdapter = mReviewAdapter;
    }

    /**
     * Getter method for the FirstMovieVideo variable
     * @return the String object mFirstMovieVideo
     */
    public String getFirstTrailerURL()
    {
        return Constants.YOUTUBE_VIDEO_URL + this.mFirstMovieVideo;
    }

    /**
     * Setter method for the FirstMovieVideo variable
     * @param mFirstMovieVideo The FirstMovieVideo String object
     */
    public void setFirstTrailerURL(String mFirstMovieVideo) {
        this.mFirstMovieVideo = mFirstMovieVideo;
    }
}
