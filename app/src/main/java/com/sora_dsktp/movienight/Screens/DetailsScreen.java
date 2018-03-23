/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Screens;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora_dsktp.movienight.Model.DatabaseContract;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.squareup.picasso.Picasso;

import static com.sora_dsktp.movienight.Utils.Constants.IMAGE_BASE_URL;

/**
 This file created by Georgios Kostogloudis
 and was last modified on 6/3/2018.
 The name of the project is MovieNight and it was created as part of
 UDACITY ND programm.
 */

/**
 * This class represent the detail screen of a movie
 * get's a movie trought intent
 * and set's the values of the textview's and the imageview on
 * this layout
 */
public class DetailsScreen extends AppCompatActivity
{
    private static final String DEBUG_TAG = "#DetailsScreen.java";
    private TextView mTitleTextView,mRatingTextView,mReleaseDateTextView,mDescriptionTextView;
    private ImageView mPosterImageView;
    private Movie mMovieClicked;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen_layout);

        //Show the back button on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Get reference to the textviews
        mPosterImageView = findViewById(R.id.iv_image_poster);
        mRatingTextView = findViewById(R.id.tv_rating_value);
        mReleaseDateTextView = findViewById(R.id.tv_release_date_value);
        mTitleTextView = findViewById(R.id.tv_movie_title_value);
        mDescriptionTextView = findViewById(R.id.tv_description_value);
        // Get the intent passed from main screen
        // and check it for null
        Intent passedIntent = getIntent();
        if(passedIntent!= null)
        {
            Log.d(DEBUG_TAG,"We have a non null Intent");
            //Get the movie from the extras
            mMovieClicked = (Movie) passedIntent.getParcelableExtra(getString(R.string.EXTRA_KEY));
            Log.d(DEBUG_TAG, mMovieClicked.toString());
            // Set the values to the appropriate fields
            Picasso.with(this).load(IMAGE_BASE_URL + mMovieClicked.getImagePath()).into(mPosterImageView);
            Log.d(DEBUG_TAG,"Movie title = " + mMovieClicked.getMovieTitle());
            mRatingTextView.setText(String.valueOf(mMovieClicked.getMovieRating()));
            mTitleTextView.setText(mMovieClicked.getMovieTitle());
            mReleaseDateTextView.setText(mMovieClicked.getReleaseDate());
            mDescriptionTextView.setText(mMovieClicked.getMovieDescription());
            // Set the toolbar title to the movie title
            getSupportActionBar().setTitle(mMovieClicked.getMovieTitle());
        }

    }


    public void favouriteButtonClicked(View view)
    {
        FloatingActionButton button = (FloatingActionButton) view;
        ColorStateList list ;
        list = button.getBackgroundTintList();
        if(Color.TRANSPARENT == list.getDefaultColor())
        {

            ContentValues cv = new ContentValues();

            cv.put(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_TITLE,mMovieClicked.getMovieTitle());
            cv.put(DatabaseContract.FavouriteMovies.COLUMN_RELEASE_DATE,mMovieClicked.getReleaseDate());
            cv.put(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_DESCRIPTION,mMovieClicked.getMovieDescription());
            cv.put(DatabaseContract.FavouriteMovies.COLUMN_MOVIE_RATING,mMovieClicked.getMovieRating());
            cv.put(DatabaseContract.FavouriteMovies.COLUMN_POSTER_PATH,mMovieClicked.getImagePath());

            Uri uri = getContentResolver().insert(DatabaseContract.FavouriteMovies.CONTENT_URI,cv);
            if( uri != null)  Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show();
            button.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            button.setBackgroundTintMode(null);
        }
        else
        {
            Toast.makeText(this,"It's favourite",Toast.LENGTH_SHORT).show();
            button.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
