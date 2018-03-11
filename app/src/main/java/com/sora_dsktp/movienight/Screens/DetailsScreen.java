package com.sora_dsktp.movienight.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.sora_dsktp.movienight.Utils.Constants.IMAGE_BASE_URL;

/**
 * Created by SoRa-DSKTP on 6/3/2018.
 */

public class DetailsScreen extends AppCompatActivity
{
    public static final String DEBUG_TAG = "#DetailsScreen.java";
    private TextView mTitleTextView,mRatingTextView,mReleaseDateTextView,mDescriptionTextView;
    private ImageView mPosterImageView;



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
            Movie moviePassed = (Movie) passedIntent.getParcelableExtra(getString(R.string.EXTRA_KEY));
            Log.d(DEBUG_TAG,moviePassed.toString());
            // Set the values to the appropriate fields
            Picasso.with(this).load(IMAGE_BASE_URL + moviePassed.getImagePath()).into(mPosterImageView);
            Log.d(DEBUG_TAG,"Movie title = " + moviePassed.getMovieTitle());
            mRatingTextView.setText(String.valueOf(moviePassed.getMovieRating()));
            mTitleTextView.setText(moviePassed.getMovieTitle());
            mReleaseDateTextView.setText(moviePassed.getReleaseDate());
            mDescriptionTextView.setText(moviePassed.getMovieDescription());
            // Set the toolbar title to the movie title
            getSupportActionBar().setTitle(moviePassed.getMovieTitle());

        }

    }

}
