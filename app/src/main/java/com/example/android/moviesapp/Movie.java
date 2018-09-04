package com.example.android.moviesapp;

public class Movie
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

}
