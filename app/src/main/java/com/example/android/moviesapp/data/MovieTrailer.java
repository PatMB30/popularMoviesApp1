package com.example.android.moviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieTrailer implements Parcelable
{
    private String trailerID;
    private String youtubeTrailerKey;
    private String trailerName;

    public MovieTrailer()
    {

    }

    public MovieTrailer(String ID, String key, String name)
    {
        this.trailerID = ID;
        this.youtubeTrailerKey = key;
        this.trailerName = name;
    }

    public MovieTrailer(Parcel in)
    {
        trailerID = in.readString();
        youtubeTrailerKey = in.readString();
        trailerName = in.readString();
    }

    public String getTrailerID() { return trailerID; }

    public void setTrailerID(String ID) { this.trailerID = ID; }

    public String getTrailerKey() { return youtubeTrailerKey; }

    public void setTrailerKey(String name) { this.youtubeTrailerKey = name; }

    public String getTrailerName() { return trailerName; }

    public void setTrailerName(String trailerName) { this.trailerName = trailerName; }

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
        dest.writeString(trailerID);
        dest.writeString(youtubeTrailerKey);
        dest.writeString(trailerName);
    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>()
    {
        public MovieTrailer createFromParcel(Parcel in)
        {
            return new MovieTrailer(in);
        }

        public MovieTrailer[] newArray(int size)
        {
            return new MovieTrailer[size];
        }
    };

}
