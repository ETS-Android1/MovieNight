/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 23/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */


/**
 * This class extends the ContentProvider class to create a Content Provider helper
 * class for accessing our local SQLite database.It has methods for CRUD operation's
 */
public class FavouritesContentProvider extends ContentProvider {

    private FavouritesDbHelper mDbHelper;
    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();

    public static final int FAV_MOVIES = 100;
    public static final int FAV_MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Method for creating a URI matcher for our URI's
     * a uri for path favourite movies and one path for favourite movies with id
     * @return the UriMatcher
     */
    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // uri matcher for the whole directory
        uriMatcher.addURI(DatabaseContract.AUTHORITY,DatabaseContract.PATH_FAVOURITE_MOVIES,FAV_MOVIES);
        // single movie uri matcher
        uriMatcher.addURI(DatabaseContract.AUTHORITY,DatabaseContract.PATH_FAVOURITE_MOVIES + "/#",FAV_MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate()
    {
        Context context = getContext();
        mDbHelper = new FavouritesDbHelper(context);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        int id = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor resultCursor = null;

        switch (id)
        {
            case FAV_MOVIES:
            {
                Log.d(DEBUG_TAG,"Querying the whole database ....");
                 try
                 {
                     resultCursor = db.query(DatabaseContract.FavouriteMovies.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                     Log.d(DEBUG_TAG,"Row count = " + resultCursor.getCount());
                 }
                 catch (SQLException e)
                 {
                     e.printStackTrace();
                 }
                return resultCursor;
            }
            default:
            {
                throw new UnsupportedOperationException("Unknown Uri = " + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        int id = sUriMatcher.match(uri);
        Uri uriToReturn;

        switch (id)
        {
            case FAV_MOVIES:
            {
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                long rows = db.insert(DatabaseContract.FavouriteMovies.TABLE_NAME,null,values);
                if(rows>0)
                {
                    // successful insertion to the db
                    uriToReturn = ContentUris.withAppendedId(DatabaseContract.FavouriteMovies.CONTENT_URI,rows);
                    // notify the contentResolver that the uri has changed
                    getContext().getContentResolver().notifyChange(uri,null);
                    return uriToReturn;
                }

                break;
            }
            default:
            {
                // implement default case to thorw unsupported exception
                throw new UnsupportedOperationException("Unknown uri = " + uri);
            }

        }
        return null;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        int id = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int  rowsDeleted = 0;

        switch (id)
        {
            case FAV_MOVIES:
            {
                Log.d(DEBUG_TAG,"Deleting items without id....");
                try
                {
                    rowsDeleted = db.delete(DatabaseContract.FavouriteMovies.TABLE_NAME,selection, selectionArgs);
                    Log.d(DEBUG_TAG,"Deleted items  count = " + rowsDeleted);
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
                return rowsDeleted;
            }
            default:
            {
                throw new UnsupportedOperationException("Unknown uri = + " + uri);
            }
        }
    }

    /**
     * Update operation
     * @param uri the Uri pointing to the database path
     * @param values The ContentValues object containing the new(updated) values
     * @param selection The selection of the columns
     * @param selectionArgs The selection arguments of the Columns
     * @return The row's affected the update
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
