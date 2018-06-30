/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Screens;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sora_dsktp.movienight.Adapters.ReviewAdapter;
import com.sora_dsktp.movienight.Adapters.VideoAdapter;
import com.sora_dsktp.movienight.Controllers.DetailScreenUiController;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.Model.Review;
import com.sora_dsktp.movienight.Model.Video;
import com.sora_dsktp.movienight.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.sora_dsktp.movienight.Utils.Constants.IMAGE_BASE_URL;
import static com.sora_dsktp.movienight.Utils.Constants.YOUTUBE_VIDEO_URL;

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
public class DetailsScreen extends AppCompatActivity implements VideoAdapter.videoClickListener
{
    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();
    private TextView mTitleTextView,mRatingTextView,mReleaseDateTextView,mDescriptionTextView;
    private ImageView mPosterImageView;
    private Movie mMovieClicked;
    private int mMovieAdapterPosition;
    private DetailScreenUiController mController;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;


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
        setUpReferences();
        // Get the intent passed from main screen
        // and check it for null
        Intent passedIntent = getIntent();
        if(passedIntent!= null)
        {
            //set the activity view's with the detail's of the movie
            //from the intent
            extractMovieDetails(passedIntent);
            // Set the toolbar title to the movie title
            getSupportActionBar().setTitle(mMovieClicked.getMovieTitle());
            //check to see if the movie is already marked as favourite
            mController.checkTheMovieOnDatabase(mMovieClicked);

            //set up the video rv
            setUpVideoRecyclerView();

            mController.setVideoAdapter(mVideoAdapter);
            //set up the review rv
            setUpReviewRecyclerView();

            mController.setReviewAdapter(mReviewAdapter);

            mController.getMovieReviews(mMovieClicked); // load the movie review's
            mController.getMovieVideos(mMovieClicked); // load the movie video's
        }

    }

    /**
     * This method extract's and set's the movie detail's to the
     * appropriate textView's
     * @param passedIntent
     */
    private void extractMovieDetails(Intent passedIntent) {
        Log.d(DEBUG_TAG,"We have a non null Intent");
        //Get the movie from the extras
        mMovieClicked = (Movie) passedIntent.getParcelableExtra(getString(R.string.EXTRA_KEY_MOVIE_OBJ));
        //get the movie's adapter position
        mMovieAdapterPosition = passedIntent.getIntExtra(getResources().getString(R.string.EXTRA_KEY_MOVIE_ID),-1);
        Log.d(DEBUG_TAG, mMovieClicked.toString());
        // Set the values to the appropriate fields
        Picasso.get().load(IMAGE_BASE_URL + mMovieClicked.getImagePath()).into(mPosterImageView);
        Log.d(DEBUG_TAG,"Movie title = " + mMovieClicked.getMovieTitle());
        String movieRating = String.valueOf(mMovieClicked.getMovieRating()) + "/10";
        mRatingTextView.setText(movieRating);
        mTitleTextView.setText(mMovieClicked.getMovieTitle());
        mReleaseDateTextView.setText(mMovieClicked.getReleaseDate());
        mDescriptionTextView.setText(mMovieClicked.getMovieDescription());
    }

    /**
     * Method for getting the reference to the appropriate view
     */
    private void setUpReferences() {
        mPosterImageView = findViewById(R.id.iv_image_poster);
        mRatingTextView = findViewById(R.id.tv_rating_value);
        mReleaseDateTextView = findViewById(R.id.tv_release_date_value);
        mTitleTextView = findViewById(R.id.tv_movie_title_value);
        mDescriptionTextView = findViewById(R.id.tv_description_value);
    }

    /**
     * Set up method for creating and setting the adapter
     * to the recyclerView
     */
    private void setUpReviewRecyclerView() {
        ArrayList<Review> reviewList = new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(this,reviewList);
        RecyclerView rvReviews = findViewById(R.id.rv_reviews);

        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(mReviewAdapter);
    }
    /**
     * Set up method for creating and setting the adapter
     * to the recyclerView
     */
    private void setUpVideoRecyclerView() {
        ArrayList<Video> videoList = new ArrayList<>();
        mVideoAdapter = new VideoAdapter(this,videoList,this);
        RecyclerView rvVideos = findViewById(R.id.rv_videos);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvVideos.setLayoutManager(manager);
        rvVideos.setAdapter(mVideoAdapter);
    }

    /**
     * This method is called to create the toolbar menu
     * options
     * @param menu
     * @return true if it created otherwise false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_screen_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method handle's the click event's on
     * the menu items
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemID = item.getItemId();
        switch (itemID)
        {
            case R.id.action_share_movie_trailer:
            {
                //Share button clicked

                //Create an intent and share the first movie trailer if it exists
                if(TextUtils.isEmpty(mController.getFirstTrailerURL()))
                {
                    Toast.makeText(this,"No video to share found",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Create a share intent
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND); // action send to share the url
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mController.getFirstTrailerURL()); // the Youtube trailer key
                    sendIntent.setType("text/plain"); // type of text
                    startActivity(sendIntent);
                }

                return true;
            }
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Getter method for field mMovieClicked
     * @return the Movie object of the movie clicked
     */
    public Movie getMovieClicked() {
        return mMovieClicked;
    }

    /**
     * Getter method for field MovieAdapterPosition
     * @return the adapter position
     */
    public int getmMovieAdapterPosition() {
        return mMovieAdapterPosition;
    }

    /**
     * This method handle's the click on the heart button
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

    /**
     * OnClick method for the video.This method is triggered when the user
     * click's on a video , then we create an Intent object and we start to play the video
     * either on Youtube App or the browser
     * @param youtubeKey
     */
    @Override
    public void onClick(String youtubeKey)
    {
        String videoURL = YOUTUBE_VIDEO_URL + youtubeKey;
        //Intent for the Youtube App
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
        youtubeIntent.setData( Uri.parse("vnd.youtube:" + youtubeKey)); //set the data equal to the video url on the youtube
        //Intent for the Browser App
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(videoURL)); //set the data equal to the video url on the youtube
        //If the Youtube application is not found start the browser intent
        try
        {
            startActivity(youtubeIntent);
        }
        catch (ActivityNotFoundException ex)
        {
            startActivity(browserIntent);
        }
    }
}
