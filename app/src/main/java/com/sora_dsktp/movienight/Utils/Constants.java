/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Utils;

/**
 This file created by Georgios Kostogloudis
 and was last modified on 10/3/2018.
 The name of the project is MovieNight and it was created as part of
 UDACITY ND programm.
 */

import com.sora_dsktp.movienight.BuildConfig;

/**
 * Public class containing all the constants used across the project
 */
public class Constants
{
    // Constants used across the Application
    public static final String API_KEY = BuildConfig.THEMOVIEDB_API_KEY;

    public static final String IMAGE_BASE_URL =  "https://image.tmdb.org/t/p/w342/";
    public static final String POPULAR_PATH = "popular";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static final String YOUTUBE_INAGE_BASE_URL = "https://img.youtube.com/vi/";
    public static final String YOUTUBE_SUFFIX_URL = "/0.jpg";

    public static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=";
}
