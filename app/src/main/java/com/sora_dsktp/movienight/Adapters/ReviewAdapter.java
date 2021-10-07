/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Adapters;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 3/4/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sora_dsktp.movienight.Model.Review;
import com.sora_dsktp.movienight.R;

import java.util.ArrayList;

/**
 * This class is responsible for binding viewHolder item's on the recyclerView . The viewHolder item's
 * is reviews about the movie that was clicked and it contains the author and the content of the review
 */
public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.MyReviewHolder>
{
    private final String DEBUG_TAG = "#ReviewAdapter.java";
    private ArrayList<Review> mReviewsList;
    private Context mContext;

    public ReviewAdapter(Context context ,ArrayList<Review> mReviewsList)
    {
        this.mReviewsList = mReviewsList;
        this.mContext = context;
    }

    /**
     * This method create's  a viewHolder item of type MyReviewHolder. It inflates
     * the view into the screen
     * @param parent
     * @param viewType
     * @return view of type MyReviewHolder
     */
    @NonNull
    @Override
    public MyReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.item_review,parent,false);
        return new MyReviewHolder(inflatedView);
    }

    /**
     * This method is used to bind the MyReviewHolder objects with actual values.
     * We populate the viewHolder object with the author and content values from our
     * Review object list
     * @param holder The ViewHolder object to bind to the UI
     * @param position The relative position of the RecyclerView to be populated
     */
    @Override
    public void onBindViewHolder(@NonNull MyReviewHolder holder, int position) {
        //Get current review object
        Review review = mReviewsList.get(position);
        //Bind the values from the review object to the ViewHolder fields
        holder.mTextViewContent.setText(review.getContent());
        String author = mContext.getString(R.string.review_author_text) + " " + review.getAuthor(); // e.g "by George Kostogloudis"
        holder.mTextViewAuthor.setText(author);
    }



    /**
     * This method is called several times as the ViewHolder item's are
     * rendered on the screen .
     * @return the size of the ReviewList containing the review's about the movie clicked
     */
    @Override
    public int getItemCount()
    {
        Log.d(DEBUG_TAG,"Review's count about this movie = " + mReviewsList.size());
        return mReviewsList.size();
    }

    /**
     * This method set's the Reviews list when it get's results from the
     * API server
     * @param data
     */
    public void pushDataToTheRecyclerView(ArrayList<Review> data)
    {
        mReviewsList = data;
    }

    /**
     * This inner class is used to represent the ViewHolder item's.
     * In this class we get a reference to the actual layout fields
     */
    public class MyReviewHolder extends RecyclerView.ViewHolder {

        //These field's is part of the ViewHolder item
        //populated to the RecyclerView
        public TextView mTextViewAuthor;
        public TextView mTextViewContent;

        public MyReviewHolder(View itemView)
        {
            super(itemView);
            mTextViewAuthor = itemView.findViewById(R.id.tv_review_author);
            mTextViewContent = itemView.findViewById(R.id.tv_review_content);
        }
    }
}
