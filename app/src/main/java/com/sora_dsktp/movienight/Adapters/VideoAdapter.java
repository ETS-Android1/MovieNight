/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sora_dsktp.movienight.Model.Video;
import com.sora_dsktp.movienight.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.sora_dsktp.movienight.Utils.Constants.IMAGE_BASE_URL;

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
    public VideoAdapter(Context context,ArrayList<Video> videos)
    {
        mContext = context;
        mVideoList = videos;
    }

    @NonNull
    @Override
    public MyVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.video_item,parent,false);
        return new MyVideoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVideoHolder holder, int position)
    {
        String youtubeURL = "https://img.youtube.com/vi/";
        String lastPart = "/0.jpg";
        Video video = mVideoList.get(position);
        Log.d(DEBUG_TAG,"Binding item with the followin url :" + youtubeURL+video.getYoutubeKey()+lastPart);
        Picasso.with(mContext).load(youtubeURL+video.getYoutubeKey()+lastPart).into(holder.videoImage);
    }

    @Override
    public int getItemCount()
    {
        Log.d(DEBUG_TAG,"Video list sie = " + mVideoList.size());
        return mVideoList.size();
    }

    public void addData(ArrayList<Video> data) {
        mVideoList = data;
    }

    public class MyVideoHolder extends RecyclerView.ViewHolder
    {
        public ImageView videoImage;


        private MyVideoHolder(View itemView)
        {
            super(itemView);
            videoImage = itemView.findViewById(R.id.iv_movie_video);
        }
    }
}
