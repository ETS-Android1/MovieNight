package com.sora_dsktp.movienight;

import android.content.Context;
import android.content.res.Resources;
import android.test.mock.MockContext;

import com.sora_dsktp.movienight.Model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class JsonUtilsTest {

    @Mock
    private Context mockApplicationContext;
    @Mock
    private Resources mockContextResources;

    @Before
    public void setupTests() {
        // Specify what to mock here

        when(mockApplicationContext.getResources()).thenReturn(mockContextResources);

        when(mockContextResources.getString(R.string.RESULTS_ARRAY_NODE)).thenReturn("results");
        when(mockContextResources.getString(R.string.VOTE_AVERAGE_NODE)).thenReturn("vote_average");
        when(mockContextResources.getString(R.string.TITLE_NODE)).thenReturn("title");
        when(mockContextResources.getString(R.string.POSTER_PATH_NODE)).thenReturn("poster_path");
        when(mockContextResources.getString(R.string.MOVIE_OVERVIEW_NODE)).thenReturn("overview");
        when(mockContextResources.getString(R.string.RELEASE_DATE_NODE)).thenReturn("release_date");

    }

    @Test
    public void parseJSONResponse() throws Exception {


        String response = "{\"page\":1,\"total_results\":8139,\"total_pages\":407,\"results\":[{\"vote_count\":1051,\"id\":19404,\"video\":false," +
                "\"vote_average\":9.1,\"title\":\"Dilwale Dulhania Le Jayenge\",\"popularity\":13.986863,\"poster_path\":\"/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg\"," +
                "\"original_language\":\"hi\",\"original_title\":\"Dilwale Dulhania Le Jayenge\",\"genre_ids\":[35,18,10749],\"backdrop_path\":\"/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg\"" +
                ",\"adult\":false,\"overview\":\"Raj is a rich, carefree, happy-go-lucky second generation NRI. Simran is the daughter of Chaudhary Baldev Singh, who in spite of being an NRI is very strict about adherence to Indian values. Simran has left for India to be married to her childhood fiancé. Raj leaves for India with a mission at his hands, to claim his lady love under the noses of her whole family. Thus begins a saga.\"" +
                ",\"release_date\":\"1995-10-20\"},{\"vote_count\":81,\"id\":20532,\"video\":false,\"vote_average\":8.7,\"title\":\"Sansho the Bailiff\",\"popularity\":8.807246,\"poster_path\":\"/deBjt3LT3UQHRXiNX1fu28lAtK6.jpg\",\"original_language\":\"ja\",\"original_title\":\"山椒大夫\",\"genre_ids\":[18],\"backdrop_path\":\"/keaFMNUr1OpdHzPWJ0qeDP8hrO8.jpg\",\"adult\":false,\"overview\":\"In medieval Japan a compassionate governor is sent into exile. His wife and children try to join him, but are separated, and the children grow up amid suffering and oppression.\"" +
                ",\"release_date\":\"1954-03-31\"},{\"vote_count\":9494,\"id\":278,\"video\":false,\"vote_average\":8.5,\"title\":\"The Shawshank Redemption\",\"popularity\":28.249349,\"poster_path\":\"/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg\",\"original_language\":\"en\",\"original_title\":\"The Shawshank Redemption\",\"genre_ids\":[18,80],\"backdrop_path\":\"/xBKGJQsAIeweesB79KC89FpBrVr.jpg\",\"adult\":false,\"overview\":\"Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison," +
                " where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.\",\"release_date\":\"1994-09-23\"},{\"vote_count\":1902,\"id\":372058,\"video\":false,\"vote_average\":8.5,\"title\":\"Your Name.\",\"popularity\":21.768725,\"" +
                "poster_path\":\"/xq1Ugd62d23K2knRUx6xxuALTZB.jpg\",\"original_language\":\"ja\",\"original_title\":\"君の名は。\",\"genre_ids\":[10749,16,18],\"backdrop_path\":\"/6vkhRvsRvWpmaRVyCXaxTkIEb7j.jpg\",\"adult\":false,\"overview\":" +
                "\"High schoolers Mitsuha and Taki are complete strangers living separate lives. But one night, they suddenly switch places. Mitsuha wakes up in Taki’s body, and he in hers. This bizarre occurrence continues to happen randomly, and the two must adjust their lives around each other.\",\"release_date\":\"2016-08-26\"}]}";

        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject root = new JSONObject(response);

        JSONArray results = root.getJSONArray(mockApplicationContext.getResources().getString(R.string.RESULTS_ARRAY_NODE));

        int arrayResultsLength = results.length();

        for(int i=0;i<arrayResultsLength;i++)
        {
            JSONObject currentMovieObj = results.getJSONObject(i);

            int movieRating = currentMovieObj.getInt(mockApplicationContext.getResources().getString(R.string.VOTE_AVERAGE_NODE));
            String title =  currentMovieObj.getString(mockApplicationContext.getResources().getString(R.string.TITLE_NODE));
            String imagePath = currentMovieObj.getString(mockApplicationContext.getResources().getString(R.string.POSTER_PATH_NODE));
            String overview = currentMovieObj.getString(mockApplicationContext.getResources().getString(R.string.MOVIE_OVERVIEW_NODE));
            String releaseDate = currentMovieObj.getString(mockApplicationContext.getResources().getString(R.string.RELEASE_DATE_NODE));

            Movie movie = new Movie(movieRating,title,imagePath,overview,releaseDate);

            movies.add(movie);

        }

        // First Movie Title as you can see at the RAW response String
        String expectedFirstMovieTitle = "Dilwale Dulhania Le Jayenge";
        String actualFirstMovieTitle = movies.get(0).getMovieTitle();
        assertEquals(expectedFirstMovieTitle,actualFirstMovieTitle);

        // Second Movie Title as you can see at the RAW response String
        String expectedSecondMovieTitle = "Sansho the Bailiff";
        String actualSecondMovieTitle = movies.get(1).getMovieTitle();
        assertEquals(expectedFirstMovieTitle,actualFirstMovieTitle);

        // Third Movie Title as you can see at the RAW response String
        String expectedThirdMovieTitle = "The Shawshank Redemption";
        String actualThirdMovieTitle = movies.get(2).getMovieTitle();
        assertEquals(expectedFirstMovieTitle,actualFirstMovieTitle);

    }
}