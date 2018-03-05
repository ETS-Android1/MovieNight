package com.sora_dsktp.movienight.Rest;

import android.util.Log;

import com.sora_dsktp.movienight.Model.Movie;
import com.sora_dsktp.movienight.Model.JsonObjectResultDescription;
import com.sora_dsktp.movienight.MoviesAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by SoRa-DSKTP on 23/2/2018.
 */

public class MovieDbClient
{
    private static final String API_KEY = "dc6d8616c929e061cf07b295eb8748a3";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static final String DEBUG_TAG = "#NetWorkUtils.java";
    private static  ArrayList<Movie> MoviesList;


    public interface MoviesDBclient
     {
         //kind is either popular or top_rated
         @GET("movie/{kind}/")
         Call<JsonObjectResultDescription> popularMovies(@Path("kind") String kind, @Query("api_key") String api_key);

     }

     public static void makeRequest(CustomCallBack<JsonObjectResultDescription> callBack,String QueryKey)
     {
         Retrofit.Builder builder = new Retrofit.Builder();


         builder.baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());

         Retrofit retrofit = builder.build();

         MovieDbClient.MoviesDBclient client = retrofit.create(MovieDbClient.MoviesDBclient.class);



         Call<JsonObjectResultDescription> call = client.popularMovies(QueryKey,API_KEY);

         call.enqueue(callBack);

     }


}
