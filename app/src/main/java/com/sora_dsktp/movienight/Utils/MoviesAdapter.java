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
 * Created by SoRa-DSKTP on 24/2/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    public static final String DEBUG_TAG = "#MoviesAdapter.java";
    private ArrayList<Movie> mMovies;
    private Context mContext;

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
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
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
        Log.d(DEBUG_TAG,"Item Size = " + mMovies.size());
        return mMovies.size();
    }



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
