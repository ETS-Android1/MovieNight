/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Model;

import java.util.ArrayList;

/**
 This file created by Georgios Kostogloudis on 23/2/2018
 and was last modified on 24/2/2018.
 The name of the project is MovieNight and it was created as part of
 UDACITY ND programm.
 */


/**
 * This class is being used to describe
 * the json response schema
 */
public class JsonMoviesApiModel
{
    private int page;
    private int total_results;
    private int total_pages;
    private ArrayList<Movie> results;

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    /**
     * get method for property results
     * @return Returns an ArrayList<Movie>
     */
    public ArrayList<Movie> getResults() {
        return results;
    }
}
