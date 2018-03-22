/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Rest.CustomCallBack;
import com.sora_dsktp.movienight.Rest.MovieDbClient;
import com.sora_dsktp.movienight.Screens.MainScreen;

import static com.sora_dsktp.movienight.Utils.Constants.POPULAR_PATH;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 22/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public class UiController
{
    private static final String DEBUG_TAG = "#UiController.java";
    private  final MainScreen mainScreen;
    private final CustomCallBack mCallBack;
    //Decision Variables for updating the UI with data
    private  boolean WeHaveInternet = true;
    private  boolean FirstTimeFetch = true;
    private  boolean UIneedsUpdate = true;

    // Pagination variables
    private int pageToIndex = 1;
    private boolean isLoading = false;

    /**
     * Constructor
     * @param mainScreen the MainScreen object we need to access Activity method's
     * @param callBack the CustomCallBack object we need to make a request to the API
     */
    public UiController(MainScreen mainScreen, CustomCallBack callBack)
    {
        this.mainScreen = mainScreen;
        this.mCallBack = callBack;
    }

    /**
     * Increments the current page we are indexing on the API
     */
    public void incrementAPIindex()
    {
        pageToIndex++;
    }

    /**
     * Reset's the page index to the first page
     */
    public void resetAPIindex()
    {
        pageToIndex = 1;
    }


    /**
     * Show's the empty layout when data isn't available
     */
    public void showErrorLayout()
    {
        RelativeLayout errorLayout = mainScreen.findViewById(R.id.error_display_layout);
        errorLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Hide's the empty layout when data is available
     */
    public void hideErrorLayout()
    {
        RelativeLayout errorLayout = mainScreen.findViewById(R.id.error_display_layout);
        errorLayout.setVisibility(View.GONE);
    }

    /**
     * Display's on screen a loading circle
     */
    public void showLoadingIndicator()
    {
        ProgressBar progressBar = mainScreen.findViewById(R.id.main_screen_loading_indicator);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide's the loading circle from the screen when
     * loading data is finished
     */
    public void hideLoadingIndicator()
    {
        ProgressBar progressBar = mainScreen.findViewById(R.id.main_screen_loading_indicator);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * This method makes the request to the Movies db
     * if needed
     */
    public void fetchMovies()
    {
        //Load Default Sort Order
        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(mainScreen);
        String sortOrder = sharedPreferences.getString(mainScreen.getResources().getString(R.string.sort_order_key),POPULAR_PATH);
        if(UIneedsToBeUpdated() && pageToIndex != 1000) // page = 1000 is the last page so don't make any call to the API
        {
            isLoading = true;
            if(isLoading) showLoadingIndicator();
            MovieDbClient.makeRequest(mCallBack, sortOrder, pageToIndex);
            UIneedsUpdate = false;
        }
    }


    /**
     * Method for determining if we need to UpdateTheUi
     * @return true or false depending the outcome of the if/else statements
     */
    public boolean UIneedsToBeUpdated()
    {
        Log.d(DEBUG_TAG,"Value of mWeHaveInternet = " + WeHaveInternet +
                "\n" + "Value of mUiNeedsUpdate = " + UIneedsUpdate + "\n" +
                "Value of mFirstTimeFetch = " + FirstTimeFetch);
        // If we have internet and the Ui needs update and its the first time the user opens the app
        // the return true and set the mFirstTimeFetch variable to false.
        if(WeHaveInternet && UIneedsUpdate && FirstTimeFetch)
        {
            FirstTimeFetch = false;
            return true;
        }
        // If we wave internet and the UI needs  update then return true
        else if(WeHaveInternet && UIneedsUpdate) return true;
            // In any other case return false
        else return false;
    }

    /**
     * Getter method for variable WeHaveInternet
     * @return true if we have internet otherwise false
     */
    public boolean isWeHaveInternet() {
        return WeHaveInternet;
    }

    /**
     * Setter method
     * @param weHaveInternet
     */
    public void setWeHaveInternet(boolean weHaveInternet) {
        this.WeHaveInternet = weHaveInternet;
    }

    /**
     * Getter method
     * @return true if its the first time we open the app otherwise false
     */
    public boolean isFirstTimeFetch() {
        return FirstTimeFetch;
    }

    /**
     * Setter method
     * @param firstTimeFetch
     */
    public void setFirstTimeFetch(boolean firstTimeFetch) {
        this.FirstTimeFetch = firstTimeFetch;
    }

    /**
     * Getter method
     * @return true if the UI needs update otherwise false
     */
    public boolean isUIneedsUpdate() {
        return UIneedsUpdate;
    }

    /**
     * Setter method
     * @param UIneedsUpdate
     */
    public void setUIneedsUpdate(boolean UIneedsUpdate) {
        this.UIneedsUpdate = UIneedsUpdate;
    }

    /**
     * Getter method
     * @return the current page of the API index
     */
    public int getPageToIndex() {
        return pageToIndex;
    }

    /**
     * Setter method
     * @param pageToIndex
     */
    public void setPageToIndex(int pageToIndex) {
        this.pageToIndex = pageToIndex;
    }

    /**
     * Getter method
     * @return true if we are <b>still</b> loading data from the API otherwise false
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * Setter method
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
