package com.sora_dsktp.movienight.Rest;

import com.sora_dsktp.movienight.Model.JsonObjectResultDescription;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.sora_dsktp.movienight.Utils.Constants.API_KEY;
import static com.sora_dsktp.movienight.Utils.Constants.BASE_URL;

/**
 * Created by SoRa-DSKTP on 23/2/2018.
 */

public class MovieDbClient
{

    private static final String DEBUG_TAG = "#NetWorkUtils.java";

    public interface MoviesDBclient
     {
         //"kind" is either popular or top_rated
         @GET("movie/{kind}/")
         Call<JsonObjectResultDescription> browseMovies(@Path("kind") String kind, @Query("api_key") String api_key);
     }

     public static void makeRequest(CustomCallBack<JsonObjectResultDescription> callBack,String QueryKey)
     {
         Retrofit.Builder builder = new Retrofit.Builder();

         builder.baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());

         Retrofit retrofit = builder.build();

         MovieDbClient.MoviesDBclient client = retrofit.create(MovieDbClient.MoviesDBclient.class);

         Call<JsonObjectResultDescription> call = client.browseMovies(QueryKey,API_KEY);

         call.enqueue(callBack);
     }
}
