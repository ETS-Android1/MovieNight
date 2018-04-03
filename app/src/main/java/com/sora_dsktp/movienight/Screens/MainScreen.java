/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Screens;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sora_dsktp.movienight.BroadcastReceivers.InternetBroadcastReceiver;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Settings.SettingsActivity;
import com.sora_dsktp.movienight.BroadcastReceivers.DbBroadcastReceiver;
import com.sora_dsktp.movienight.Adapters.MoviesAdapter;
import com.sora_dsktp.movienight.Listeners.PaginationScrollListener;
import com.sora_dsktp.movienight.Controllers.MainScreenUiController;

import java.util.ArrayList;

import static com.sora_dsktp.movienight.Utils.Constants.POPULAR_PATH;


public class MainScreen extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,MoviesAdapter.OnMovieClickedInterface{

    //Log tag for LogCat usage
    private final String DEBUG_TAG = "#" + getClass().getSimpleName();
    private static final int SPAN_COUNT = 3;   //How many movies in each row
    private BroadcastReceiver mBroadcastReceiver;
    private MoviesAdapter mAdapter;
    private MainScreenUiController mController;
    private static final int FORECAST_LOADER_ID = 0;
    private DbBroadcastReceiver mDatabaseReceiver;
    private Parcelable listState; // this Parcelable contain's the bundle from the recyclerView's layout manager onSavedInstanceState and is used to restore the scroll position
    private RecyclerView mRvMovies;


    /**
     * This method is called when the activity is created
     * @param savedInstanceState a Bundle object containing data that was saved when the activity was destroyed
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);

        final ArrayList<Movie> movies = new ArrayList<>(); // ArrayList to use to store the movies
        mAdapter = new MoviesAdapter(movies,this,this); // Adapter for the movies


        // create an instance of UI controller
        mController = new MainScreenUiController(this);
        // set the adapter to the UI controller
        mController.setAdapter(mAdapter);

        // set the mainScreenUicontroller to the adapter
        mAdapter.setUiController(mController);

        setUpRecyclerView();
        // Set the toolbar title
        setActionBarTitle();
        //Create and register the Connectivity broadcast receiver
        instantiateBroadcastReceivers();


        //if favourite mode is enabled load the movies
        if(mController.favouritesMode()) mController.fetchFavouriteMovies();


        //Check the savedInstanceState bundle
        if(savedInstanceState!=null)
        {
            // get the parcelable from the bundle
            listState = savedInstanceState.getParcelable(getString(R.string.list_state));
            // we need to restore the scroll so set the boolean to true
            mController.setScrollPositionToPreferences(true);
            // set the following fields to the UI controller for later use in onLoadFinished() method of the MovieLoader
            mController.setRecyclerView(mRvMovies);
            mController.setListState(listState);

        }

    }

    /**
     * This method is executed when activity's OnDestroy() is called .
     * This method is used to save the scroll position of the recyclerView
     * so we can restore it on Activity's recreation
     * @param outState The bundle which will be send when the activity is re-created .
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save in the outState bundle the layoutManager's  onSaveInstanceState bundle
        outState.putParcelable(getString(R.string.list_state), mRvMovies.getLayoutManager().onSaveInstanceState());
        //Save a boolean indicating that a restore scroll position is required
        outState.putBoolean(getString(R.string.restore_scroll_boolean),true);
    }

    /**
     * Setup method for cleaner code in onCreate method
     */
    private void setUpRecyclerView()
    {
        //Get Reference to recyclerView
        mRvMovies = findViewById(R.id.movies_rv);
        //improves performance
        mRvMovies.setHasFixedSize(true);
        //Create a layout manager
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        // set methods
        mRvMovies.setLayoutManager(mLayoutManager); // sets the layoutmanager
        mRvMovies.setAdapter(mAdapter); // set's the recyclerView's adapter
        mRvMovies.setOnScrollListener(new PaginationScrollListener(mLayoutManager) {
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
                    //show the loading indicator
                    mController.showLoadingIndicator();
                    // pass as a parameter the page to index the API
                    Log.d(DEBUG_TAG,"fetching movies for load more method");
                    mController.fetchMovies(true,false);
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
    private void instantiateBroadcastReceivers()
    {
        mBroadcastReceiver= new InternetBroadcastReceiver(mController);
        //create and register db change broadcast receiver
        mDatabaseReceiver = new DbBroadcastReceiver(mAdapter);
        //register the internet receiver
        this.registerReceiver(mBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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

            // if the sort preference is "favourite"
            if(mController.favouritesMode())
            {
                // fetch offline favourite movies
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
                mController.resetAPIindex();
                Log.d(DEBUG_TAG,"Fetching movies for sharedPreferences listener.....");
                // restart the loader to make a new request with the sort preference
                mController.fetchMovies(false,true);
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        //Register the shared Preference Listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        //register database broadcast receiver

        registerReceiver(mDatabaseReceiver,new IntentFilter(DbBroadcastReceiver.ACTION_DATABASE_CHANGED));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister the listener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        //Destroy Internet broadcast Receiver
        unregisterReceiver(mBroadcastReceiver);
        //unregister Database receiver
        unregisterReceiver(mDatabaseReceiver);
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
        openDetailScreen.putExtra(this.getString(R.string.EXTRA_KEY_MOVIE_OBJ),movieClicked);
        openDetailScreen.putExtra(getString(R.string.EXTRA_KEY_MOVIE_ID),moviePosition);
        startActivity(openDetailScreen);
    }

}
