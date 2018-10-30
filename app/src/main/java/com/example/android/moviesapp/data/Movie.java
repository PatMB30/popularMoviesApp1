package com.example.android.moviesapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "favoriteMovie")
public class Movie implements Parcelable
{
    //Movie ID needed to retrieve additional information
    @PrimaryKey(autoGenerate = true)
    private int movieID;
    //Basic movie characteristics
    private String movieTitle;
    private String movieThumbnail;
    private String movieOverview;
    private String movieAverage;
    private String movieRelease;

    //designates if movie has been added to favorites
    private Boolean isFavorite;

    public Movie(int movieID, String movieTitle, String movieThumbnail, String movieOverview,
                 String movieAverage, String movieRelease, Boolean isFavorite)
    {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieThumbnail = movieThumbnail;
        this.movieOverview = movieOverview;
        this.movieAverage = movieAverage;
        this.movieRelease = movieRelease;
        this.isFavorite = isFavorite;
    }

    @Ignore
    public Movie(Parcel in)
    {
        movieID = in.readInt();
        movieTitle = in.readString();
        movieThumbnail = in.readString();
        movieOverview = in.readString();
        movieAverage = in.readString();
        movieRelease = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public int getMovieID() { return movieID; }

    public void setMovieID(int ID) { this.movieID = ID; }

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

    //public void setMovieReviews(MovieReview mr) { this.movieReview = mr; }

    public Boolean getFavorite() { return isFavorite; }

    public void setFavorite(Boolean favorite) { this.isFavorite = favorite; }

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
        dest.writeInt(movieID);
        dest.writeString(movieTitle);
        dest.writeString(movieThumbnail);
        dest.writeString(movieOverview);
        dest.writeString(movieAverage);
        dest.writeString(movieRelease);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
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
