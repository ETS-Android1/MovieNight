package com.sora_dsktp.movienight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sora_dsktp.movienight.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SoRa-DSKTP on 24/2/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

//    / Store a member variable for the contacts
    private ArrayList<Movie> mMovies;
    // Store the context for easy access
    private Context mContext;
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/";



    public MoviesAdapter(ArrayList<Movie> mMovies, Context mContext) {
        this.mMovies = mMovies;
        this.mContext = mContext;
    }

    public void pushTheData(ArrayList<Movie> data)
    {
        this.mMovies = data;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_movie,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position)
    {
        // Get the data model based on position
        Movie movie = mMovies.get(position);

        // Set item views based on your views and data model
        holder.mMovieTitle.setText(movie.getMovieTitle());
        Picasso.with(mContext).load(IMAGE_BASE_URL+movie.getImagePath()).into(holder.mMoviePoster);
    }



    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public TextView mMovieTitle;
        public ImageView mMoviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mMovieTitle = (TextView) itemView.findViewById(R.id.item_movie_title);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.item_movie_poster);
        }
    }

}
