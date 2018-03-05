package com.sora_dsktp.movienight.Rest;

import android.util.Log;

import com.sora_dsktp.movienight.Model.JsonObjectResultDescription;
import com.sora_dsktp.movienight.Utils.MoviesAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by SoRa-DSKTP on 5/3/2018.
 */

public class CustomCallBack<J> implements Callback<JsonObjectResultDescription> {
    public static final String DEBUG_TAG = "#CustomCallBack.java";

    private MoviesAdapter mAdapter;
    public CustomCallBack(MoviesAdapter mAdapter)
    {
        this.mAdapter = mAdapter;
    }

    @Override
    public void onResponse(Call<JsonObjectResultDescription> call, Response<JsonObjectResultDescription> response)
    {
        Log.e(DEBUG_TAG,call.toString());
        mAdapter.pushTheData(response.body().getResults());
        mAdapter.notifyDataSetChanged();
        Log.d("Debug","Kati ginete");
    }

    @Override
    public void onFailure(Call<JsonObjectResultDescription> call, Throwable t)
    {
        t.printStackTrace();
        Log.e(DEBUG_TAG,"Error on Failuere");

    }
}
