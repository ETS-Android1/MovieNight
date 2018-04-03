/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

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

public class VideoAdapter extends Adapter<VideoAdapter.MyVideoHolder> {

    private  final String DEBUG_TAG = "#" + this.getClass().getSimpleName();

    private ArrayList<Video> mVideoList;
    private final Context mContext;
    private  videoClickListener mListener;


    public interface videoClickListener
    {
         void onClick(String youtubeKey);
    }


    public VideoAdapter(Context context,ArrayList<Video> videos,videoClickListener listener)
    {
        mContext = context;
        mVideoList = videos;
        mListener = listener;
    }

    @NonNull
    @Override
    public MyVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Log.d(DEBUG_TAG,"Creating normal viewType...");
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.video_item,parent,false);
        return new MyVideoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVideoHolder holder, int position)
    {
        Video video = mVideoList.get(position);
        Log.d(DEBUG_TAG,"Binding item with the followin url :" + YOUTUBE_INAGE_BASE_URL+video.getYoutubeKey()+YOUTUBE_SUFFIX_URL);
        Picasso.with(mContext).load(YOUTUBE_INAGE_BASE_URL+video.getYoutubeKey()+YOUTUBE_SUFFIX_URL).into(holder.videoImage);
    }

    @Override
    public int getItemCount()
    {
        Log.d(DEBUG_TAG,"Video list sie = " + mVideoList.size());
        return mVideoList.size();
    }

    public void addData(ArrayList<Video> data)
    {
        Log.d(DEBUG_TAG,"Adding data to the mVideoList....");
        mVideoList = data;
    }

    public class MyVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView videoImage;


        private MyVideoHolder(View itemView)
        {
            super(itemView);
            videoImage = itemView.findViewById(R.id.iv_movie_video);

            videoImage.setOnClickListener(this);
        }


        @Override
        public void onClick(View v)
        {
            Video videoClicked = mVideoList.get(getAdapterPosition());
            String youtubeVideoKey = videoClicked.getYoutubeKey();
            mListener.onClick(youtubeVideoKey);
        }
    }
}
