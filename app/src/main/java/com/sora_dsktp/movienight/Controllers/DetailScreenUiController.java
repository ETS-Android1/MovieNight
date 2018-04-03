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
import com.sora_dsktp.movienight.Model.DatabaseContract;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.Model.Review;
import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Rest.ReviewLoader;
import com.sora_dsktp.movienight.Rest.VideoLoader;
import com.sora_dsktp.movienight.Screens.DetailsScreen;

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

    public void getMovieReviews(Movie mMovieClicked)
    {
        int movieID = mMovieClicked.getMovieID();
        LoaderManager loaderManager = mDetailScreen.getSupportLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putInt(mDetailScreen.getString(R.string.MOVIE_ID_BUNDLE_KEY),movieID);
        loaderManager.initLoader(3,bundle,new ReviewLoader(mDetailScreen,this));
    }

    public void getMovieVideos(Movie mMovieClicked)
    {
        int movieID = mMovieClicked.getMovieID();
        LoaderManager loaderManager = mDetailScreen.getSupportLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putInt(mDetailScreen.getString(R.string.MOVIE_ID_BUNDLE_KEY),movieID);
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
                intent.putExtra(mDetailScreen.getResources().getString(R.string.EXTRA_MOVIE_TO_DEL_OBJ), mDetailScreen.getmMovieClicked());
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

    public void setVideoAdapter(VideoAdapter mVideoAdapter) {
        this.mVideoAdapter = mVideoAdapter;
    }

    public VideoAdapter getVideoAdapter() {
        return mVideoAdapter;
    }

    public ReviewAdapter getReviewAdapter() {
        return mReviewAdapter;
    }

    public void setReviewAdapter(ReviewAdapter mReviewAdapter) {
        this.mReviewAdapter = mReviewAdapter;
    }
}
