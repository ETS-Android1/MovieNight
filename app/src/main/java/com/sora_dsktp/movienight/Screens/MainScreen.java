package com.sora_dsktp.movienight.Screens;

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
import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Rest.CustomCallBack;
import com.sora_dsktp.movienight.Rest.MovieDbClient;
import com.sora_dsktp.movienight.Settings.SettingsActivity;
import com.sora_dsktp.movienight.Utils.MoviesAdapter;

import java.util.ArrayList;


public class MainScreen extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    private CustomCallBack customCallBack;
    private static final String POPULAR_PATH = "popular";
    public static final String DEBUG_TAG = "#MainScreen.java";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
        //Get Reference to recyclerView
        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.movies_rv);
        //Instantiate arrayList of movies
        ArrayList<Movie> movies = new ArrayList<>();
        // Create an adapter
        // and a layoutManager
        // Gridlayout to be specific cause we want grid like layout
        // and set them both to recycler View
        MoviesAdapter adapter = new MoviesAdapter(movies,this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        rvMovies.setLayoutManager(mLayoutManager);
        rvMovies.setAdapter(adapter);
        //Instantiate a custom Callback object passing in the adapter to populate
        // with data when we get a response from the Movies DB API
        customCallBack = new CustomCallBack<JsonObjectResultDescription>(adapter);


        //Load Default Sort Order
        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = sharedPreferences.getString(getResources().getString(R.string.sort_order_key),POPULAR_PATH);

        // Make the request passing in the callback object and the sort Order we want
        MovieDbClient.makeRequest(customCallBack,sortOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the Action Bar Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menu item id Clicked
        int id = item.getItemId();
        switch (id)
        {
            // Settings menu clicked
            case R.id.sort_order_option:
            {
                // Start settings Activity
                startActivity(new Intent(this, SettingsActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        // "Sort Order" menu item was changed
        if(key.equals(getResources().getString(R.string.sort_order_key)))
        {
            // Make the request to the API again using the appropriate "sort_order"
            Log.d(DEBUG_TAG,"Key = "+key);
            MovieDbClient.makeRequest(customCallBack,sharedPreferences.getString(key,getResources().getString(R.string.popular_movies)));
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        //Register the shared Preference Listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister the listener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
