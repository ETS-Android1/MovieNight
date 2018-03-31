/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Model;

import java.util.ArrayList;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 31/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public class JsonVideosApiModel
{
    private int movie_id;
    private ArrayList<Video> results;

    public int getMovie_id()
    {
        return movie_id;
    }

    public ArrayList<Video> getResults()
    {
        return results;
    }
}
