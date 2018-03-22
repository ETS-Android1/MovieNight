/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 22/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {


    GridLayoutManager layoutManager;

    public PaginationScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0)
            {
                loadMoreItems();
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

