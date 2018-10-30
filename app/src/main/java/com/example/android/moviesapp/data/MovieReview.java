package com.example.android.moviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReview implements Parcelable
{
    //Movie ID needed to retrieve additional information
    //private int movieID;
    //Basic movie characteristics
    private String reviewAuthor;
    private String reviewContent;
    private String reviewURL;

    public MovieReview()
    {

    }

    public MovieReview(/*int ID,*/ String author, String content, String url)
    {
        //this.movieID = ID;
        this.reviewAuthor = author;
        this.reviewContent = content;
        this.reviewURL = url;
    }

    public MovieReview(Parcel in)
    {
        //movieID = in.readInt();
        reviewAuthor = in.readString();
        reviewContent = in.readString();
        reviewURL = in.readString();
    }

    public MovieReview(String[] movieData)
    {
        reviewAuthor = "Hello";
    }

    //public int getMovieID() { return movieID; }

    //public void setMovieID(int ID) { this.movieID = ID; }

    public String getReviewAuthor()
    {
        return reviewAuthor;
    }

    public void setReviewAuthor(String name)
    {
        this.reviewAuthor = name;
    }

    public String getReviewContent()
    {
        return reviewContent;
    }

    public void setReviewContent(String review)
    {
        this.reviewContent = review;
    }

    public String getReviewURL()
    {
        return reviewURL;
    }

    public void setReviewURL(String url)
    {
        this.reviewURL = url;
    }

    public String getStringReview()
    {
        return this.getReviewAuthor() + "\n\n" + this.getReviewContent() + "\n" +
                this.getReviewURL();
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
        //dest.writeInt(movieID);
        dest.writeString(reviewAuthor);
        dest.writeString(reviewContent);
        dest.writeString(reviewURL);
    }

    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>()
    {
        public MovieReview createFromParcel(Parcel in)
        {
            return new MovieReview(in);
        }

        public MovieReview[] newArray(int size)
        {
            return new MovieReview[size];
        }
    };

}
