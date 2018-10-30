package com.example.android.moviesapp.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.android.moviesapp.AppExecutors;

import java.util.List;

public class MovieRepository
{
    private MovieDAO movieDao;
    private AppExecutors appExecutors;

    private LiveData<List<Movie>> movies;

    public MovieRepository(Application application)
    {
        movieDao = AppDatabase.getInstance(application).movieDao();
        movies = movieDao.loadAllMovies();
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<Movie>> getFavoriteMovies() { return movies; }

    public boolean isFavorite(int movieId) { return movieDao.isFavorite(movieId); }

    public void updateMovieFavorite(int movieId, boolean isFavorite)
    {
        appExecutors.diskIO().execute(() ->
        {
            movieDao.updateMovie(movieId, isFavorite);
        });
    }

    public void addMovieToFavorites(Movie movie)
    {
        appExecutors.diskIO().execute(() ->
        {
            movieDao.insertMovie(movie);
        });
    }

    public void removeMovieFromFavorites(Movie movie)
    {
        appExecutors.diskIO().execute(() ->
        {
            movieDao.deleteMovie(movie);
        });
    }

}
