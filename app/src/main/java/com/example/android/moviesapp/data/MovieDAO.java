package com.example.android.moviesapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDAO
{
    @Query("SELECT * FROM favoriteMovie ORDER BY movieTitle")
    LiveData<List<Movie>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Query("UPDATE favoriteMovie SET isFavorite = :isFavorite WHERE movieID = :movieID" )
    void updateMovie(int movieID, boolean isFavorite);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM favoriteMovie WHERE movieID = :movieID")
    LiveData<Movie> loadMovieById(int movieID);

    @Query("SELECT isFavorite FROM favoriteMovie WHERE movieID = :movieID")
    boolean isFavorite(int movieID);

    @Query("SELECT * FROM favoriteMovie WHERE movieID = :movieID")
    Movie getMovie(int movieID);
}
