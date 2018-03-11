/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Screens.DetailsScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.sora_dsktp.movienight.Utils.Constants.IMAGE_BASE_URL;

/**
 This file created by Georgios Kostogloudis
 and was last modified on 24/2/2018.
 The name of the project is MovieNight and it was created as part of
 UDACITY ND programm.
 */

/**
 * Adapter for the recyclerView
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    public static final String DEBUG_TAG = "#MoviesAdapter.java";
    private ArrayList<Movie> mMovies;
    private Context mContext;

    /**
     * Default constructor for the adapter
     * @param mMovies ArrayList of movie objects
     * @param mContext Context object used to get reference to resources
     */
    public MoviesAdapter(ArrayList<Movie> mMovies, Context mContext) {
        this.mMovies = mMovies;
        this.mContext = mContext;
    }

    /**
     * Setter method for mMovies field
     * @param data the data we want to set
     */
    public void pushTheData(ArrayList<Movie> data)
    {
        this.mMovies = data;
    }

    /**
     * Creation of the ViewHolder objects
     * @param parent
     * @param viewType
     * @return a viewHolder object.
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //Get a layout inflate from the context
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // Get a view from inflating the item_movie.xml
        View view = inflater.inflate(R.layout.item_movie,parent,false);
        // Create a ViewHolder object from the view we just inflated
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    /**
     * Binds the ViewHolder object's with actual data
     * from the ArrayList of movies
     * @param holder ViewHolder object containing the ImageView and the TextView we want to populate with data
     * @param position The position fo the current element
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position)
    {
        // Get the data model based on position
        Movie movie = mMovies.get(position);

        // Set item views based on your views and data model
        holder.mMovieTitle.setText(movie.getMovieTitle());
        Picasso.with(mContext).load(IMAGE_BASE_URL+movie.getImagePath()).into(holder.mMoviePoster);
    }

    /**
     * Get method for recyclerview size
     * @return the size of the mMovies ArrayList
     */
    @Override
    public int getItemCount() {
        Log.d(DEBUG_TAG,"Item Size = " + mMovies.size());
        return mMovies.size();
    }


    /**
     * Class for wrapping the actual fields in this case a ImageView for the poster
     * and a TextView for the movie title
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mMovieTitle;
        public ImageView mMoviePoster;

        public MovieViewHolder(View itemView)
        {
            super(itemView);
            mMovieTitle = (TextView) itemView.findViewById(R.id.item_movie_title);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.item_movie_poster);

            itemView.setOnClickListener(this);
        }

        /**
         * OnClick method for when a user click a movie
         * @param v the View object that was clicked
         */
        @Override
        public void onClick(View v)
        {
            // Start the activity containing
            // the movie user clicked from the list
            Intent openDetailScreen = new Intent(mContext, DetailsScreen.class);
            Movie movieToSend = mMovies.get(getAdapterPosition());
            if(movieToSend == null) Log.e(DEBUG_TAG,"Movie is empty ");
            Log.d(DEBUG_TAG,movieToSend.toString());
            openDetailScreen.putExtra(mContext.getString(R.string.EXTRA_KEY),movieToSend);
            mContext.startActivity(openDetailScreen);
        }
    }

}
