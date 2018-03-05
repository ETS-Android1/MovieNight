package com.sora_dsktp.movienight.Model;

import java.util.ArrayList;

/**
 * Created by SoRa-DSKTP on 24/2/2018.
 */



public class JsonObjectResultDescription
{
    private int page;
    private int total_results;
    private int total_pages;
    private ArrayList<Movie> results;


    public ArrayList<Movie> getResults() {
        return results;
    }
}
