/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
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
public class FavouritesContentProvider extends ContentProvider {

    private FavouritesDbHelper mDbHelper;

    public static final int FAV_MOVIES = 100;
    public static final int FAV_MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

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

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        int id = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        switch (id)
        {
            case FAV_MOVIES:
            {
                Cursor resultCursor = db.query(DatabaseContract.FavouriteMovies.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                return resultCursor;
            }
            case FAV_MOVIE_WITH_ID:
            {
                Cursor resultCursor = db.query(DatabaseContract.FavouriteMovies.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                return resultCursor;
            }
        }
        return null;
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
                Log.d("tset",db.getClass().toString());
                long rows = db.insert(DatabaseContract.FavouriteMovies.TABLE_NAME,null,values);
                if(rows>0)
                {
                    uriToReturn = ContentUris.withAppendedId(DatabaseContract.FavouriteMovies.CONTENT_URI,rows);
                    getContext().getContentResolver().notifyChange(uri,null);
                    return uriToReturn;
                }

                break;
            }
            // implement default case to thorw unsupported exception
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
