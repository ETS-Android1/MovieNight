package com.sora_dsktp.movienight.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by SoRa-DSKTP on 23/2/2018.
 */

public class Movie implements Parcelable {

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

    @Override
    public String toString() {
        return "This movie has a title: " +
                getMovieTitle() +
                " With a rating of " +
                getMovieRating() +
                " and the following description: " +
                getMovieDescription() +
                " and it was release on:" +
                getReleaseDate();
    }

    public float getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(float movieRating) {
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

    @Override
    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel out, int flags) {
        out.writeFloat(getMovieRating());
        out.writeString(getMovieTitle());
        out.writeString(getMovieDescription());
        out.writeString(getImagePath());
        out.writeString(getReleaseDate());

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        setMovieRating(in.readFloat());
        setMovieTitle(in.readString());
        setMovieDescription( in.readString());
        setImagePath(in.readString());
        setReleaseDate( in.readString());
    }
}
