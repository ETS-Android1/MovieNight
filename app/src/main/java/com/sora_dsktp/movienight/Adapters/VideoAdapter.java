/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sora_dsktp.movienight.Model.Video;
import com.sora_dsktp.movienight.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.sora_dsktp.movienight.Utils.Constants.YOUTUBE_INAGE_BASE_URL;
import static com.sora_dsktp.movienight.Utils.Constants.YOUTUBE_SUFFIX_URL;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 1/4/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */

/**
 * This class is the adapter for the recyclerView that holds
 * the related video's of a movie clicked . It contain's method to populate
 * the screen with ViewHolder items . It also handle's the click event's on the video
 */
public class VideoAdapter extends Adapter<VideoAdapter.MyVideoHolder> {

    private  final String DEBUG_TAG = "#" + this.getClass().getSimpleName();

    private ArrayList<Video> mVideoList;
    private final Context mContext;
    private  videoClickListener mListener;

    /**
     * This is a custom interface to delegate
     * the click event to the DetailScreen activity
     */
    public interface videoClickListener
    {
         void onClick(String youtubeKey);
    }

    /**
     * Custom constructor
     * @param context The context of the activity that use's the adapter
     * @param videos The videoList with the actual video data
     * @param listener The click event listener
     */
    public VideoAdapter(Context context,ArrayList<Video> videos,videoClickListener listener)
    {
        mContext = context;
        mVideoList = videos;
        mListener = listener;
    }

    /**
     * This method is used to create a new ViewHolder object
     * @param parent The parent ViewGroup
     * @param viewType The viewType of the ViewHolder object if there are more than one type of views
     * @return the viewHolder object
     */
    @NonNull
    @Override
    public MyVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Log.d(DEBUG_TAG,"Creating normal viewType...");
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.video_item,parent,false);
        return new MyVideoHolder(inflatedView);
    }

    /**
     * This method is used to bind the ViewHolder object with some data
     * @param holder The viewHolder item to populate data
     * @param position The position relative to the RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull MyVideoHolder holder, int position)
    {
        Video video = mVideoList.get(position);
        Log.d(DEBUG_TAG,"Binding item with the followin url :" + YOUTUBE_INAGE_BASE_URL+video.getYoutubeKey()+YOUTUBE_SUFFIX_URL);
        Picasso.get().load(YOUTUBE_INAGE_BASE_URL+video.getYoutubeKey()+YOUTUBE_SUFFIX_URL).into(holder.videoImage);
    }

    /**
     * This method is used to count the number of items
     * that the recyclerView has any given moment
     * @return The count of the item's on the VideoList
     */
    @Override
    public int getItemCount()
    {
        Log.d(DEBUG_TAG,"Video list sie = " + mVideoList.size());
        return mVideoList.size();
    }

    /**
     * This method set's the videoList with actual data
     * @param data the data to populate with
     */
    public void addData(ArrayList<Video> data)
    {
        Log.d(DEBUG_TAG,"Adding data to the mVideoList....");
        mVideoList = data;
    }

    /**
     * This inner class is used to get a reference to the actual fields at the layout. In this case
     * it get's a reference to the ImageView that will hold the image thumbnail of each video on the
     * list.
     */
    public class MyVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView videoImage;

        /**
         * Default constructor
         */
        private MyVideoHolder(View itemView)
        {
            super(itemView);
            videoImage = itemView.findViewById(R.id.iv_movie_video);
            //set the onClick listener of the imageView
            videoImage.setOnClickListener(this);
        }

        /**
         * On Click method handler.This method is used to delegate
         * the click item to the detailScreen activity by calling the
         * mListener.OnClick method
         * @param v The View object that was clicked
         */
        @Override
        public void onClick(View v)
        {
            Video videoClicked = mVideoList.get(getAdapterPosition());
            String youtubeVideoKey = videoClicked.getYoutubeKey();
            //delegate the click handling to the DetailScreen activity.
            mListener.onClick(youtubeVideoKey);
        }
    }
}
