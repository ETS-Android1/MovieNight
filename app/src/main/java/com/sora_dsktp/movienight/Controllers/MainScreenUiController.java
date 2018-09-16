/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Controllers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.sora_dsktp.movienight.Model.DAO.DatabaseContract;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Network.Loaders.MovieLoaders;
import com.sora_dsktp.movienight.Screens.MainScreen;
import com.sora_dsktp.movienight.Utils.Constants;
import com.sora_dsktp.movienight.Adapters.MoviesAdapter;

import java.util.ArrayList;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 22/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */


/**
 * Helper class containing method's for updating the
 * UI in MainScreen.java
 */
public class MainScreenUiController {
    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();
    private final MainScreen mainScreen;
    //Decision Variables for updating the UI with data
    private boolean WeHaveInternet = true;
    private boolean FirstTimeFetch = true;
    private boolean UIneedsUpdate = true;

    // Pagination variables
    private int pageToIndex = 1;
    private boolean isLoading = false;

    private MoviesAdapter mAdapter;
    //The following fields is used to restore the scroll position of the recyclerView
    private RecyclerView mRecyclerView;
    private Parcelable listState;


    /**
     * Defaults constructor
     * @param mainScreen a reference to the mainScree activity
     */
    public MainScreenUiController(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    /**
     * This method is a getter method from the DefaultSharedPreferences
     * @return
     */
    public boolean getScrollPosNeedsRestoreFromPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mainScreen);
        boolean restoreSroll = preferences.getBoolean(mainScreen.getResources().getString(R.string.restore_scroll_boolean),false);
        return restoreSroll;
    }

    /**
     * This method is a setter method for a boolean value to the DefaultSharedPreferences.
     * This boolean value is used to restore the scroll position of the recyclerView
     * @param needsRestore the boolean value to save on the preferences
     */
    public void setScrollPositionToPreferences(boolean needsRestore)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mainScreen);
        preferences.edit().putBoolean(mainScreen.getResources().getString(R.string.restore_scroll_boolean),needsRestore).apply();
    }

    /**
     * Increments the current page we are indexing on the moviesAPI
     */
    public void incrementAPIindex() {
        pageToIndex++;
    }

    /**
     * Reset's the page index to the first page of the movieAPI
     */
    public void resetAPIindex() {
        pageToIndex = 1;
    }



    /**
     * Method for restoring the scroll position of the movies recyclerView
     */
    public void restoreScrollPostition()
    {
        //Get the value from the preferences
        boolean ScrollPosNeedsRestore = getScrollPosNeedsRestoreFromPref();
        //if the list state isn't null and the scroll position needs to be restored
        // then restore the scroll position
        if(getListState()!=null && ScrollPosNeedsRestore)
        {
            //restore the scroll position
            getRecyclerView().getLayoutManager().onRestoreInstanceState(listState);
            //the scroll boolean needs to be set to false until there is a device configuration
            setScrollPositionToPreferences(false);
        }
    }



    /**
     * Helper class for querying the database in
     * a background thread
     */
    private class AsyncQueryHelper extends AsyncQueryHandler {

        // integer token used to identify the query action
        public static final int TOKEN_QUERY_DATABASE = 1;

        /**
         * Default constructor
         * @param cr the content resolver to use for querying the database
         */

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
            // determine the action according to the token variable
            switch (token)
            {
                case TOKEN_QUERY_DATABASE:
                {
                    ArrayList<Movie> movies = new ArrayList<>();
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            //Retrieve the indexes of the columns we need to query
                            int titleColumnIndex = cursor.getColumnIndex(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_TITLE);
                            int descriptionColumnIndex = cursor.getColumnIndex(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_DESCRIPTION);
                            int ratingColumnIndex = cursor.getColumnIndex(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_RATING);
                            int releaseDateColumnIndex = cursor.getColumnIndex(DatabaseContract.FavouriteMovies.COLUMN_RELEASE_DATE);
                            int posterPathColumnIndex = cursor.getColumnIndex(DatabaseContract.FavouriteMovies.COLUMN_POSTER_PATH);
                            int movieIDPathColumnIndex = cursor.getColumnIndex(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_ID);

                            //first set of results
                            String movieTitle = cursor.getString(titleColumnIndex);
                            String movieDesc = cursor.getString(descriptionColumnIndex);
                            String movieDate = cursor.getString(releaseDateColumnIndex);
                            String posterPath = cursor.getString(posterPathColumnIndex);
                            int movieRating = cursor.getInt(ratingColumnIndex);
                            int movieID = cursor.getInt(movieIDPathColumnIndex);
                            // create a new object Movie and add it to the ArrayList
                            movies.add(new Movie(movieRating, movieTitle, posterPath, movieDesc, movieDate,movieID));

                            //Continue to query through the next set of results
                            while (cursor.moveToNext()) {
                                movieTitle = cursor.getString(titleColumnIndex);
                                movieDesc = cursor.getString(descriptionColumnIndex);
                                movieDate = cursor.getString(releaseDateColumnIndex);
                                posterPath = cursor.getString(posterPathColumnIndex);
                                movieRating = cursor.getInt(ratingColumnIndex);
                                movieID = cursor.getInt(movieIDPathColumnIndex);

                                // create a new object Movie and add it to the ArrayList
                                movies.add(new Movie(movieRating, movieTitle, posterPath, movieDesc, movieDate,movieID));
                            }
                        }
                        //Close the cursor to avoid memory leak
                        cursor.close();
                        //check if there are favourite movies by checking the list size
                        if (movies.size() > 0)
                        {
                            //There are favourite movies
                            mAdapter.clearData();
                            mAdapter.pushTheDataToTheAdapter(movies);
                            mAdapter.notifyDataSetChanged();
                            // hide the loading indicator
                            hideLoadingIndicator();
                            //restore the scroll position
                            restoreScrollPostition();
                        }
                        else
                        {
                            // no favourite movies
                            mAdapter.clearData();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown token = " + token);
            }
        }
    }

    /**
     * Method for getting the movies from the local database
     * This method use's a AsyncQuery object to do the query to the
     * database asynchronously
     */
    public void fetchFavouriteMovies()
    {
        Log.d(DEBUG_TAG, "Fetching favourite movies.....");
        // Create a object of AsyncQueryHelper
        AsyncQueryHelper queryHandler = new AsyncQueryHelper(mainScreen.getContentResolver());
        // start querying the database
        queryHandler.startQuery(AsyncQueryHelper.TOKEN_QUERY_DATABASE,null, DatabaseContract.FavouriteMovies.CONTENT_URI,null,null,null,null);
    }


    /**
     * Method for hiding the Empty View for favourite movies
     */
    public void hideEmptyFavouriteMoviesLayout() {
        mainScreen.findViewById(R.id.no_favourite_movies_layout).setVisibility(View.GONE);
    }
    /**
     * Method for showing the Empty View for favourite movies
     */
    public void showEmptyFavouriteMoviesLayout() {
        mainScreen.findViewById(R.id.no_favourite_movies_layout).setVisibility(View.VISIBLE);
    }




    /**
     * This method makes the request to the Movies db
     * if needed
     */
    public void fetchMovies(boolean forceUpdate,boolean restartLoader)
    {
        if (UIneedsToBeUpdated() && pageToIndex != 1000) // page = 1000 is the last page so don't make any call to the API
        {
            LoaderManager loaderManager = mainScreen.getSupportLoaderManager();
            Log.d(DEBUG_TAG, "Fetching movies from the API.....");

            //show the loading indicator indicating
            //that data is being loaded
            this.showLoadingIndicator();
            //if the load JUST need's to start
            if(!forceUpdate && !restartLoader )loaderManager.initLoader(1,null,new MovieLoaders(mainScreen,this));

            //if the loader need's to be forced
            if(forceUpdate)
            {
                Log.d(DEBUG_TAG,"Loader is force loaded....");
                if(loaderManager.getLoader(1) != null )  loaderManager.getLoader(1).forceLoad();
            }
            //if the loader need's to restart
            if(restartLoader)
            {
                Log.d(DEBUG_TAG,"Loader is restarted..");
                loaderManager.restartLoader(1,null,new MovieLoaders(mainScreen,this));
            }
        }
    }


    /**
     * Method for determining if we need to UpdateTheUi
     *
     * @return true or false depending the outcome of the if/else statements
     */
    public boolean UIneedsToBeUpdated() {
        Log.d(DEBUG_TAG, "Value of mWeHaveInternet = " + WeHaveInternet +
                "\n" + "Value of mUiNeedsUpdate = " + UIneedsUpdate + "\n" +
                "Value of mFirstTimeFetch = " + FirstTimeFetch);
        // If we have internet and the Ui needs update and its the first time the user opens the app
        // the return true and set the mFirstTimeFetch variable to false.
        if (isWeHaveInternet() && UIneedsUpdate() && isFirstTimeFetch()) {
            setFirstTimeFetch(false);
            return true;
        }
        // If we wave internet and the UI needs  update then return true
        else if (UIneedsUpdate() && isWeHaveInternet()) return true;
            // In any other case return false
        else return false;
    }

    /**
     * Show's the empty layout when data isn't available
     */
    public void showErrorLayout() {
        RelativeLayout errorLayout = mainScreen.findViewById(R.id.error_display_layout);
        errorLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Hide's the empty layout when data is available
     */
    public void hideErrorLayout() {
        RelativeLayout errorLayout = mainScreen.findViewById(R.id.error_display_layout);
        errorLayout.setVisibility(View.GONE);
    }

    /**
     * Display's on screen a loading circle
     */
    public void showLoadingIndicator() {
        ProgressBar progressBar = mainScreen.findViewById(R.id.main_screen_loading_indicator);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide's the loading circle from the screen when
     * loading data is finished
     */
    public void hideLoadingIndicator() {
        ProgressBar progressBar = mainScreen.findViewById(R.id.main_screen_loading_indicator);
        progressBar.setVisibility(View.GONE);
    }



    /**
     * Getter method for sort preference
     *
     * @return the sort preference from the user
     */
    public String getSortOrder() {
        return PreferenceManager.getDefaultSharedPreferences(mainScreen).getString(mainScreen.getResources().getString(R.string.sort_order_key), Constants.POPULAR_PATH);

    }

    /**
     * Getter method for variable WeHaveInternet
     *
     * @return true if we have internet otherwise false
     */
    public boolean isWeHaveInternet() {
        return WeHaveInternet;
    }

    /**
     * Setter method
     *
     * @param weHaveInternet
     */
    public void setWeHaveInternet(boolean weHaveInternet) {
        this.WeHaveInternet = weHaveInternet;
    }

    /**
     * Getter method
     *
     * @return true if its the first time we open the app otherwise false
     */
    public boolean isFirstTimeFetch() {
        return FirstTimeFetch;
    }

    /**
     * Setter method
     *
     * @param firstTimeFetch
     */
    public void setFirstTimeFetch(boolean firstTimeFetch) {
        this.FirstTimeFetch = firstTimeFetch;
    }

    /**
     * Getter method
     *
     * @return true if the UI needs update otherwise false
     */
    public boolean UIneedsUpdate() {
        return UIneedsUpdate;
    }

    /**
     * Setter method
     *
     * @param UIneedsUpdate
     */
    public void setUIneedsUpdate(boolean UIneedsUpdate) {
        this.UIneedsUpdate = UIneedsUpdate;
    }

    /**
     * Getter method
     *
     * @return the current page of the API index
     */
    public int getPageToIndex() {
        return pageToIndex;
    }

    /**
     * Setter method
     *
     * @param pageToIndex
     */
    public void setPageToIndex(int pageToIndex) {
        this.pageToIndex = pageToIndex;
    }

    /**
     * Getter method
     *
     * @return true if we are <b>still</b> loading data from the API otherwise false
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * Setter method
     *
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    /**
     * Setter method for field MoviesAdapter mAdapter
     *
     * @param mAdapter
     */
    public void setAdapter(MoviesAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    /**
     * Method for checking if the sort order is favourites
     *
     * @return true if the sort order is "favourite movies" otherwise false
     */
    public boolean favouritesMode() {
        return getSortOrder().equals(mainScreen.getResources().getString(R.string.sort_favourite_movies_value));
    }

    /**
     * Setter method for field listState
     * @param listState the listState object to set equal to the field listState
     */
    public void setListState(Parcelable listState) {
        this.listState = listState;
    }

    /**
     * Getter method for listState object
     * @return the Parcelable object ListState
     */
    public Parcelable getListState() {
        return listState;
    }

    /**
     * Getter method for the field mRecyclerView
     * @return the RecyclerView object
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Setter method for the field mRecyclerView
     * @param mRecyclerView the recyclerView object to set equal to the field mRecyclerView
     */
    public void setRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    /**
     * Getter method for the field mAdapter
     * @return the MoviesAdapter object
     */
    public MoviesAdapter getAdapter() {
        return mAdapter;
    }
}


