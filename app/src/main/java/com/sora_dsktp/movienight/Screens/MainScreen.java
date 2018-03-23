/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sora_dsktp.movienight.Model.JsonObjectResultDescription;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Rest.CustomCallBack;
import com.sora_dsktp.movienight.Settings.SettingsActivity;
import com.sora_dsktp.movienight.Utils.MoviesAdapter;
import com.sora_dsktp.movienight.Utils.PaginationScrollListener;
import com.sora_dsktp.movienight.Utils.UiController;

import java.util.ArrayList;

import static com.sora_dsktp.movienight.Utils.Constants.POPULAR_PATH;


public class MainScreen extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,MoviesAdapter.OnMovieClickedInterface{


    public static final int SPAN_COUNT = 3;   //How many movies in each row
    private CustomCallBack mMoviesCallBack;
    private BroadcastReceiver mBroadcastReceiver;
    private MoviesAdapter mAdapter;
    private IntentFilter mIntentFilter;
    private UiController mController;
    private static final String DEBUG_TAG = "#MainScreen.java";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
        final ArrayList<Movie> movies = new ArrayList<>(); // ArrayList to use to store the movies
        mAdapter = new MoviesAdapter(movies,this,this); // Adapter for the movies

        //Instantiate a custom Callback object passing in the adapter to populate
        // with data when we get a response from the Movies DB API
        mMoviesCallBack = new CustomCallBack<JsonObjectResultDescription>(mAdapter);
        // create an instance of UI controller
        mController = new UiController(this, mMoviesCallBack);
        // set the adapter to the UI controller
        mController.setAdapter(mAdapter);
        // set ui controller on callback from API
        mMoviesCallBack.setUIcontroller(mController);

        setUpRecyclerView();
        // Set the toolbar title
        setActionBarTitle();
        //Create and register the Connectivity broadcast receiver
        createInternetBroadcastReceiver();

        //if favourite mode is enabled load the movies
        if(mController.favouritesMode()) mController.fetchFavouriteMovies();
    }


    /**
     * Setup method for cleaner onCreate method
     */
    private void setUpRecyclerView()
    {
        //Get Reference to recyclerView
        RecyclerView rvMovies = findViewById(R.id.movies_rv);
        //Create a layout manager
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        // set methods
        rvMovies.setLayoutManager(mLayoutManager); // sets the layoutmanager
        rvMovies.setAdapter(mAdapter); // set's the recyclerView's adapter
        rvMovies.setOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems()
            {
                // check if the app is
                // loading still data
                // from a previous request
                if(!isLoading()&& !mController.favouritesMode())
                {
                    // set the variable to true
                    // so we can fetch the movies
                    mController.setUIneedsUpdate(true);
                    // pass as a parameter the page to index the API
                    Log.d("#UiController.java","fetching movies for load more method");
                    mController.fetchMovies();
                }
            }

            /**
             * Simple method for checking if a request to the API is still active
             * @return the state of loading
             */
            @Override
            public boolean isLoading()
            {
                return mController.isLoading();
            }
        }); // OnScrollListener for pagination
    }

    /**
     * This method create's and registers the broadcast receiver.
     * This broadcast is called when a connectivity change occurs
     */
    private void createInternetBroadcastReceiver() {
        mBroadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork;
                // Get the network info
                activeNetwork = connectivityManager.getActiveNetworkInfo();
                if(activeNetwork != null)
                {
                    // If we have Internet connectivity
                    // check to see if we need to update the ui
                    if(activeNetwork.isConnected())
                    {
                        mController.setWeHaveInternet(true);
                        if(!mController.favouritesMode())
                        {
                            Log.d("#UiCo","Fetching movies from the broadcast receiver.....");
                            mController.fetchMovies();
                        }
                    }
                }
                // If we lose Internet connectivity show a Toast message
                else
                {
                    Toast.makeText(context, R.string.no_connection_message,Toast.LENGTH_LONG).show();
                    mController.setWeHaveInternet(false);
                }

            }
        };
        // Create the intent filter for Connectivity Change
        // and register the receiver
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(mBroadcastReceiver,mIntentFilter);
    }

    /**
     * This method inflates the toolbar menu items
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the Action Bar Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    /**
     * This method is called when a toolbar item is clicked
     * @param item The item which was clicked
     * @return
     */
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

    /**
     * This method is called whenever a preference  change it's value
     * for example if a user select's top rated movies/popular sort
     * @param sharedPreferences sharedPreferences object
     * @param key The key to find which preference has changed
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        // "Sort Order" menu item was changed
        if(key.equals(getResources().getString(R.string.sort_order_key)))
        {
            //Remember to change the toolbar title
            setActionBarTitle();

            String sortOrder = mController.getSortOrder();

            if(mController.favouritesMode())
            {
                // fetch offline favourite movies
                Toast.makeText(getApplicationContext(),"Coming soon!",Toast.LENGTH_SHORT).show();
                mController.fetchFavouriteMovies();
            }
            else
            {
                // fetch from the server
                // Tell the Ui it needs update
                // clear the data from the adapter
                // reset the indexing page for the API
                mController.setUIneedsUpdate(true);
                mAdapter.clearData();
                mController.showErrorLayout(); // In case we don't have internet show the empty dataset layout
                mController.resetAPIindex();
                Log.d("#UiCo","Fetching movies for sharedPreferences listener.....");
                // Make the request to the API again using the appropriate "sort_order"
                mController.fetchMovies();
            }
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
        //Destroy Internet broadcast Receiver
        this.unregisterReceiver(mBroadcastReceiver);
        Log.d(DEBUG_TAG,"On Destroy called");
    }

    /**
     * Method for setting the ToolBar title
     * according to the sort order preference
     */
    public void setActionBarTitle() {
        //Load Default Sort Order
        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        String SortOrder = sharedPreferences.getString(getResources().getString(R.string.sort_order_key),POPULAR_PATH);
        //Set the title according to the sort order
        switch (SortOrder)
        {
            case "popular":
            {
                getSupportActionBar().setTitle("Popular Movies");
                break;
            }
            case "top_rated":
            {
                getSupportActionBar().setTitle("Top Rated Movies");
                break;
            }
            case "favourites":
            {
                getSupportActionBar().setTitle("Favourite Movies");
            }
        }
    }

    /**
     * Method for handling click's on RecyclerView
     * It starts the details view
     * with the appropriate data
     * @param moviePosition adapter position clicked
     * @param movieClicked Movie object to send in details Activity
     */
    @Override
    public void onMovieClicked(int moviePosition,Movie movieClicked) {
        //Start the activity containing
        //the movie user clicked from the list
        Intent openDetailScreen = new Intent(getApplicationContext(), DetailsScreen.class);
        if(movieClicked == null) Log.e(DEBUG_TAG,"Movie is empty ");
        Log.d(DEBUG_TAG,movieClicked.toString());
        openDetailScreen.putExtra(this.getString(R.string.EXTRA_KEY),movieClicked);
        startActivity(openDetailScreen);
    }

}
