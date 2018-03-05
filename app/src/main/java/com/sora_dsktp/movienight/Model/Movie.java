package com.sora_dsktp.movienight.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SoRa-DSKTP on 23/2/2018.
 */

public class Movie
{
    @SerializedName("vote_average")
    private float movieRating;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("poster_path")
    private String imagePath;
    @SerializedName("overview")
    private String movieDescription;
    @SerializedName("release_date")
    private String releaseDate;

    public Movie(int movieRating, String movieTitle, String imagePath, String movieDescription, String releaseDate) {
        this.movieRating = movieRating;
        this.movieTitle = movieTitle;
        this.imagePath = imagePath;
        this.movieDescription = movieDescription;
        this.releaseDate = releaseDate;
    }

    public float getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(int movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
