/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sora_dsktp.movienight.Controllers.MainScreenUiController;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
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

    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();
    private ArrayList<Movie> mMovies;
    private Context mContext;
    private OnMovieClickedInterface mClickListener;
    private final customObserver mObserver = new customObserver();
    private MainScreenUiController mMainScreenUiController;

    public void setUiController(MainScreenUiController uiController) {
        this.mMainScreenUiController = uiController;
    }

    // Simple interface to implement on main activity
    // to delegate the click on a movie
    public interface OnMovieClickedInterface{
         void onMovieClicked(int moviePosition,Movie movie);
    }

    /**
     * This class is used as a data observer
     */
    private class customObserver extends RecyclerView.AdapterDataObserver
    {
        /**
         * This method is triggered when a change happen's
         * to the recyclerView . We use this method to show/hide
         * empty/no favourite movies layout's for informing the user
         */
        @Override
        public void onChanged() {
            super.onChanged();
            Log.e(DEBUG_TAG,"onChanged occured......");
            // if the recyclerview has no items show the appropriate layout
            if(getItemCount() == 0)
            {
                if(mMainScreenUiController.favouritesMode())
                {
                    mMainScreenUiController.showEmptyFavouriteMoviesLayout();
                }
                else
                {
                    mMainScreenUiController.showErrorLayout();
                }
            }
            // the itemCount of the recyclerView is >0 so , hide the layout's
            else
            {
                mMainScreenUiController.hideErrorLayout();
                mMainScreenUiController.hideEmptyFavouriteMoviesLayout();
            }
        }

    }

    /**
     * Default constructor for the adapter
     * @param mMovies ArrayList of movie objects
     * @param mContext Context object used to get reference to resources
     */
    public MoviesAdapter(ArrayList<Movie> mMovies, Context mContext, OnMovieClickedInterface listener) {
        this.mMovies = mMovies;
        this.mContext = mContext;
        this.mClickListener = listener;
        registerAdapterDataObserver(mObserver);
    }

    /**
     * Setter method for mMovies field
     * @param data the data we want to set
     */
    public void pushTheDataToTheAdapter(ArrayList<Movie> data)
    {
        this.mMovies.addAll(data);
    }

    /**
     * Method for clearing the movies list from the recyclerview
     */
    public void clearData() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    /**
     * Getter method
     * @return the ArrayList<Movie> object
     */
    public ArrayList<Movie>  getData()
    {
        return mMovies;
    }

    /**
     * Method for removing one item (movie) from
     * the recyclerview
     */
    public boolean removeItemFromRecyclerView(Movie movie, int adapterPosition)
    {
        Log.d(DEBUG_TAG,"Removing movie from the adaptor with title = " + movie.getMovieTitle());
        // remove the movie from the recyclerview using the
        // adapter position of the movie
        Movie deletedMovie = mMovies.remove(adapterPosition);
        // check if the movie that was deleted is equal to the movie object
        // we want to delete by comparing their toString() methods for equality
        if(deletedMovie.toString().equals(movie.toString()))
        {
            //Removed sucessfully from the recyclerview
            Log.d(DEBUG_TAG,"Movie removed from the adapter with no errors");
            notifyDataSetChanged();
            return true;
        }
        Log.e(DEBUG_TAG,"Could'n delete the movie from the adapter");
        return false;
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
        // check the size and hide/show the empty layout's
        if(mMovies.size() == 0)
        {
            if(mMainScreenUiController.favouritesMode())
            {
                mMainScreenUiController.showEmptyFavouriteMoviesLayout();
            }
            else
            {
                mMainScreenUiController.showErrorLayout();
            }
        }
        else
        {
            mMainScreenUiController.hideErrorLayout();
            mMainScreenUiController.hideEmptyFavouriteMoviesLayout();

        }
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
            // Sent the movie clicked via the listener
            mClickListener.onMovieClicked(getAdapterPosition(),mMovies.get(getAdapterPosition()));
        }
    }

}
