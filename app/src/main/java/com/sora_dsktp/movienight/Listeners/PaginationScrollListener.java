/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Listeners;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 22/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();
    private GridLayoutManager mLayoutManager;

    public PaginationScrollListener(GridLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

        // If the user scroll down
        if(dy>0)
        {
            // if there is nothing loading from the API
            if (!isLoading())
            {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                }
            }
        }
    }

    /**
     * method to fetch the next page
     */
    protected abstract void loadMoreItems();


    /**
     * method for checking if we are still loading data from a previous request
     * to the API
     * @return true if we are waiting for a response otherwise false
     */
    public abstract boolean isLoading();
}

