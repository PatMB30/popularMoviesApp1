package com.example.android.moviesapp.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class MovieViewModel extends AndroidViewModel
{

    private MovieRepository movieRepository;

    public MovieViewModel(@NonNull Application application)
    {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    public boolean isFavorite(int movieId)
    {
        return movieRepository.isFavorite(movieId);
    }

    public void updateMovieFavorite(int movieId, boolean isFavorite)
    {
        movieRepository.updateMovieFavorite(movieId, isFavorite);
    }

    public void addMovieToFavorites(Movie movie)
    {
        movieRepository.addMovieToFavorites(movie);
    }

    public void removeMovieFromFavorites(Movie movie)
    {
        movieRepository.removeMovieFromFavorites(movie);
    }

}
