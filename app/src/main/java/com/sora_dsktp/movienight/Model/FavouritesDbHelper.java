/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 23/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public class FavouritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourite.db";
    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();

    private static final int DB_VERSION = 1;


    public FavouritesDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_FAVOURITE_TABLE = "CREATE TABLE " +
                DatabaseContract.FavouriteMovies.TABLE_NAME + " (" +
                DatabaseContract.FavouriteMovies.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                DatabaseContract.FavouriteMovies.COLUMN_MOVIE_DESCRIPTION + " TEXT NOT NULL, " +
                DatabaseContract.FavouriteMovies.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                DatabaseContract.FavouriteMovies.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                DatabaseContract.FavouriteMovies.COLUMN_MOVIE_RATING + " REAL NOT NULL  " +  ");";

        db.execSQL(SQL_CREATE_FAVOURITE_TABLE);

        Log.d(DEBUG_TAG,"Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //This is called when the database schema is updated
    }
}
