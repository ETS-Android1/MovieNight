/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 23/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public class DatabaseContract
{
    private DatabaseContract()  { }

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.sora_dsktp.movienight.Model.FavouritesContentProvider";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_FAVOURITE_MOVIES = "favourite_movies";


    public static final class FavouriteMovies implements BaseColumns
    {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIES).build();

        public static final String TABLE_NAME = "fav_movies";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_RATING = "movie_rating";
        public static final String COLUMN_MOVIE_DESCRIPTION = "movie_description";
        public static final String COLUMN_POSTER_PATH = "movie_poster_path";


    }


}
