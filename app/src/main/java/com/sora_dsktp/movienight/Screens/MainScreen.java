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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sora_dsktp.movienight.Model.JsonObjectResultDescription;
import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;
import com.sora_dsktp.movienight.Rest.CustomCallBack;
import com.sora_dsktp.movienight.Rest.MovieDbClient;
import com.sora_dsktp.movienight.Settings.SettingsActivity;
import com.sora_dsktp.movienight.Utils.MoviesAdapter;

import java.util.ArrayList;

import static com.sora_dsktp.movienight.Utils.Constants.POPULAR_PATH;


public class MainScreen extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    private CustomCallBack mCustomCallBack;
    private BroadcastReceiver mBroadcastReceiver;
    private MoviesAdapter mAdapter;
    private IntentFilter mIntentFilter;
    private  String mSortOrder;
    private  boolean mWeHaveInternet = true;
    private  boolean mFirstTimeFetch = true;
    private  boolean mUineedsUpdate = true;
    private static final String DEBUG_TAG = "#MainScreen.java";


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
        mAdapter = new MoviesAdapter(movies,this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        rvMovies.setLayoutManager(mLayoutManager);
        rvMovies.setAdapter(mAdapter);
        //Instantiate a custom Callback object passing in the adapter to populate
        // with data when we get a response from the Movies DB API
        mCustomCallBack = new CustomCallBack<JsonObjectResultDescription>(mAdapter, (RelativeLayout) findViewById(R.id.error_display_layout));

        // Set the toolbar title
        setActionBarTitle();

        //Create and register the Connectivity broadcast receiver
        createInternetBroadcastReceiver();
        this.registerReceiver(mBroadcastReceiver,mIntentFilter);

    }

    private void createInternetBroadcastReceiver() {
        mBroadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork;
                activeNetwork = connectivityManager.getActiveNetworkInfo();
                if(activeNetwork != null)
                {
                    if(activeNetwork.isConnected())
                    {
                        Toast.makeText(getApplicationContext(),"We have internet",Toast.LENGTH_SHORT).show();
                        mWeHaveInternet = true;
                        //Check to See if we have internet and then fetch the data
                        if(UIneedsToBeUpdated()) fetchMovies();
                    }
                }
                else
                {
                    Toast.makeText(context, R.string.no_connection_message,Toast.LENGTH_LONG).show();
                    mWeHaveInternet = false;
                }

            }
        };
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);


    }
    public void fetchMovies()
    {
        // If we have internet connection
        // make the request passing in the callback object and the sort Order we want
        //Load Default Sort Order
        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        mSortOrder = sharedPreferences.getString(getResources().getString(R.string.sort_order_key),POPULAR_PATH);
        if(UIneedsToBeUpdated())
        {
            MovieDbClient.makeRequest(mCustomCallBack, mSortOrder);
            mUineedsUpdate = false;
        }
    }

    public boolean UIneedsToBeUpdated()
    {
        Log.d(DEBUG_TAG,"Value of mWeHaveInternet = " + mWeHaveInternet +
                "\n" + "Value of mUiNeedsUpdate = " + mUineedsUpdate + "\n" +
                      "Value of mFirstTimeFetch = " + mFirstTimeFetch);
        if(mWeHaveInternet && mUineedsUpdate && mFirstTimeFetch)
        {
            mFirstTimeFetch = false;
            return true;
        }
        else if(mWeHaveInternet && mUineedsUpdate) return true;
        else return false;
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
            mUineedsUpdate = true;
            // Make the request to the API again using the appropriate "sort_order"
            fetchMovies();
            //Remember to change the toolbar title
            setActionBarTitle();
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

    public void setActionBarTitle() {
        //Load Default Sort Order
        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        mSortOrder = sharedPreferences.getString(getResources().getString(R.string.sort_order_key),POPULAR_PATH);
        //Set the title according to the sort order
        switch (mSortOrder)
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
        }
    }
}
