/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sora_dsktp.movienight.Controllers.DetailScreenUiController;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Rest.MovieReviewClient;
import com.sora_dsktp.movienight.Rest.MovieReviewRetrofitCallback;
import com.sora_dsktp.movienight.Rest.MovieVideoClient;
import com.sora_dsktp.movienight.Rest.MovieVideoRetrofitCallback;
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
    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();
    private TextView mTitleTextView,mRatingTextView,mReleaseDateTextView,mDescriptionTextView;
    private ImageView mPosterImageView;
    private Movie mMovieClicked;
    private int mMovieAdapterPosition;
    private DetailScreenUiController mController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen_layout);
        //Show the back button on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //create a UI controller
        mController = new DetailScreenUiController(this);
        // Get reference to the textView's
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
            mMovieClicked = (Movie) passedIntent.getParcelableExtra(getString(R.string.EXTRA_KEY_MOVIE_OBJ));
            //get the movie's adapter position
            mMovieAdapterPosition = passedIntent.getIntExtra(getResources().getString(R.string.EXTRA_KEY_MOVIE_ID),-1);
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
            //check to see if the movie is already marked as favourite
            mController.checkTheMovieOnDatabase(mMovieClicked);

            getReviews(mMovieClicked);
            getVideos(mMovieClicked);
        }

    }

    private void getVideos(Movie mMovieClicked) {
        MovieVideoRetrofitCallback callback = new MovieVideoRetrofitCallback();
        MovieVideoClient.makeRequest(String.valueOf(mMovieClicked.getMovieID()),callback);
    }

    private void getReviews(Movie mMovieClicked)
    {
        MovieReviewRetrofitCallback callback = new MovieReviewRetrofitCallback();
        MovieReviewClient.makeRequest(callback, (String.valueOf(mMovieClicked.getMovieID())),1);

    }

    public Movie getmMovieClicked() {
        return mMovieClicked;
    }

    public int getmMovieAdapterPosition() {
        return mMovieAdapterPosition;
    }

    /**
     * This method handle's the click on the heart button
     * It creates a content values object to store a Movie object inside it
     * Then in a background thread using a runnable object it call's the content's
     * resolver method insert to store the movie object to  the local SQlite database and
     * if the insert is successful it calss the paintHeartButton to make the heart RED indicating that
     * the movie is now a "favourite movie" . If the heart button is already RED meaning the movie is already favourite
     * then it removes the movie from the database using again content's resolver delete() method in a background thread
     * @param view The view that was clicked , in this case an ImageButton
     */
    public void favouriteButtonClicked(View view)
    {
           if(!mController.getIsFavourite())
           {
               mController.addTheMovieToTheDatabase(mMovieClicked);
           }
           else mController.deleteTheMovieFromDatabase(mMovieClicked);
    }

}
