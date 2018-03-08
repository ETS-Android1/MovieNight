package com.sora_dsktp.movienight.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SoRa-DSKTP on 6/3/2018.
 */

public class DetailsScreen extends AppCompatActivity
{
    public static final String DEBUG_TAG = "#DetailsScreen.java";
    private TextView mTitleTextView,mRatingTextView,mReleaseDateTextView,mDescriptionTextView;
    private ImageView mPosterImageView;
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen_layout);

        mPosterImageView = findViewById(R.id.iv_image_poster);
        mRatingTextView = findViewById(R.id.tv_rating_value);
        mReleaseDateTextView = findViewById(R.id.tv_release_date_value);
        mTitleTextView = findViewById(R.id.tv_movie_title_value);
        mDescriptionTextView = findViewById(R.id.tv_description_value);

        Intent passedIntent = getIntent();
        if(passedIntent!= null)
        {
            Log.d(DEBUG_TAG,"We are in business");

            Movie moviePassed = (Movie) passedIntent.getParcelableExtra("data");
            Log.d(DEBUG_TAG,moviePassed.toString());
            Picasso.with(this).load(IMAGE_BASE_URL + moviePassed.getImagePath()).into(mPosterImageView);
            Log.d(DEBUG_TAG,"Movie title = " + moviePassed.getMovieTitle());
            mRatingTextView.setText(String.valueOf(moviePassed.getMovieRating()));
            mTitleTextView.setText(moviePassed.getMovieTitle());
            mReleaseDateTextView.setText(moviePassed.getReleaseDate());
            mDescriptionTextView.setText(moviePassed.getMovieDescription());

            getSupportActionBar().setTitle(moviePassed.getMovieTitle());

        }

    }
}
