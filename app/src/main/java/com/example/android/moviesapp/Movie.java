package com.example.android.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable
{
    private String movieTitle;
    private String movieThumbnail;
    private String movieOverview;
    private String movieAverage;
    private String movieRelease;
    //private int imageID;

    public Movie()
    {

    }

    public Movie(String title, String thumbnail, String overview,
                 String average, String release)
    {
        this.movieTitle = title;
        this.movieThumbnail = thumbnail;
        this.movieOverview = overview;
        this.movieAverage = average;
        this.movieRelease = release;
    }

    public Movie(Parcel in)
    {
        movieTitle = in.readString();
        movieThumbnail = in.readString();
        movieOverview = in.readString();
        movieAverage = in.readString();
        movieRelease = in.readString();
    }

    public String getMovieTitle()
    {
        return movieTitle;
    }

    public void setMovieTitle(String name)
    {
        this.movieTitle = name;
    }

    public String getMovieThumbnail()
    {
        return movieThumbnail;
    }

    public void setMovieThumbnail(String thumbnail)
    {
        this.movieThumbnail = thumbnail;
    }

    public String getMovieOverview()
    {
        return movieOverview;
    }

    public void setMovieOverview(String overview)
    {
        this.movieOverview = overview;
    }

    public String getMovieAverage()
    {
        return movieAverage;
    }

    public void setMovieAverage(String average)
    {
        this.movieAverage = average;
    }

    public String getMovieRelease()
    {
        return movieRelease;
    }

    public void setMovieRelease(String release)
    {
        this.movieRelease = release;
    }

    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1)
    {
        // TODO Auto-generated method stub
        dest.writeString(movieTitle);
        dest.writeString(movieThumbnail);
        dest.writeString(movieOverview);
        dest.writeString(movieAverage);
        dest.writeString(movieRelease);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

}
