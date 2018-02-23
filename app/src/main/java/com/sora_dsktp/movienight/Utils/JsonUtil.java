package com.sora_dsktp.movienight.Utils;

import android.content.Context;
import android.util.Log;

import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SoRa-DSKTP on 23/2/2018.
 */

public class JsonUtil {
    private static final String DEBUG_TAG = "#JsonUtil.java";

    public static ArrayList<Movie> parseJSONResponse(String response, Context context) {
        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject root;
        try
        {
            root = new JSONObject(response);


            JSONArray results = root.getJSONArray(context.getResources().getString(R.string.RESULTS_ARRAY_NODE));

            int arrayResultsLength = results.length();

            for (int i = 0; i < arrayResultsLength; i++) {
                JSONObject currentMovieObj = results.getJSONObject(i);

                int movieRating = currentMovieObj.getInt(context.getResources().getString(R.string.VOTE_AVERAGE_NODE));
                String title = currentMovieObj.getString(context.getResources().getString(R.string.TITLE_NODE));
                String imagePath = currentMovieObj.getString(context.getResources().getString(R.string.POSTER_PATH_NODE));
                String overview = currentMovieObj.getString(context.getResources().getString(R.string.MOVIE_OVERVIEW_NODE));
                String releaseDate = currentMovieObj.getString(context.getResources().getString(R.string.RELEASE_DATE_NODE));

                Movie movie = new Movie(movieRating, title, imagePath, overview, releaseDate);

                movies.add(movie);

            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(DEBUG_TAG,"Error creating json object");
        }
        return movies;
    }
}
