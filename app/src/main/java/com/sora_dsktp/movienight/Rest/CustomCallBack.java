package com.sora_dsktp.movienight.Rest;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.sora_dsktp.movienight.Model.JsonObjectResultDescription;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.Utils.MoviesAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by SoRa-DSKTP on 5/3/2018.
 */

public class CustomCallBack<J> implements Callback<JsonObjectResultDescription> {
    public static final String DEBUG_TAG = "#CustomCallBack.java";

    private MoviesAdapter mAdapter;
    private RelativeLayout mRelativeLayout;
    public CustomCallBack(MoviesAdapter mAdapter, RelativeLayout linearLayout)
    {
        this.mAdapter = mAdapter;
        this.mRelativeLayout = linearLayout;
    }

    @Override
    public void onResponse(Call<JsonObjectResultDescription> call, Response<JsonObjectResultDescription> response)
    {
        //Get the movies from the call
        ArrayList<Movie> movies = response.body().getResults();
        //If the list is not empty push the data
        //to the adapter and notify him
        if(!movies.isEmpty())
        {
            mAdapter.pushTheData(movies);
            mAdapter.notifyDataSetChanged();
            // Hide the error layout from the user
            mRelativeLayout.setVisibility(View.GONE);
        }
        Log.d("Debug","We got a response from the API");
    }

    @Override
    public void onFailure(Call<JsonObjectResultDescription> call, Throwable t)
    {
        t.printStackTrace();
        Log.e(DEBUG_TAG,"There was an error fetching data from the API");

    }
}
