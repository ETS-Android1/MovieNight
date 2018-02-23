package com.sora_dsktp.movienight.Model;

/**
 * Created by SoRa-DSKTP on 23/2/2018.
 */

public class Movie
{
    private int movieRating;
    private String movieTitle;
    private String imagePath;
    private String movieDescription;
    private String releaseDate;

    public Movie(int movieRating, String movieTitle, String imagePath, String movieDescription, String releaseDate) {
        this.movieRating = movieRating;
        this.movieTitle = movieTitle;
        this.imagePath = imagePath;
        this.movieDescription = movieDescription;
        this.releaseDate = releaseDate;
    }

    public int getMovieRating() {
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
