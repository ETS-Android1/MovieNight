package com.sora_dsktp.movienight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sora_dsktp.movienight.Model.JsonObjectResultDescription;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.Rest.CustomCallBack;
import com.sora_dsktp.movienight.Rest.MovieDbClient;
import com.sora_dsktp.movienight.Settings.SettingsActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private MoviesAdapter adapter;
    private CustomCallBack customCallBack;
    private static final String POPULAR_PATH = "popular";
    public static final String DEBUG_TAG = "#MainActivity.java";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.sort_order_option:
            {
                startActivity(new Intent(this, SettingsActivity.class));
            }
        }


        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        // Lookup the recyclerview in activity layout
        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.movies_rv);

        ArrayList<Movie> movies = new ArrayList<>();
        // Create adapter passing in the sample user data
        MoviesAdapter adapter = new MoviesAdapter(movies,this);
        // Attach the adapter to the recyclerview to populate items
        rvMovies.setAdapter(adapter);

        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        //Register the listener to update the UI in case a preference change occurs
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        String sortOrder = sharedPreferences.getString(getResources().getString(R.string.sort_order_key),POPULAR_PATH);
        customCallBack = new CustomCallBack<JsonObjectResultDescription>(adapter);
        MovieDbClient.makeRequest(customCallBack,sortOrder);
        // Set layout manager to position the items
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);

        rvMovies.setLayoutManager(mLayoutManager);
        // That's all!


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(key.equals(getResources().getString(R.string.sort_order_key)))
        {
            Log.d(DEBUG_TAG,"Key = "+key);
            MovieDbClient.makeRequest(customCallBack,sharedPreferences.getString(key,getResources().getString(R.string.popular_movies)));
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister the listener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
