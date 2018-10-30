package com.example.android.moviesapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.data.AppDatabase;
import com.example.android.moviesapp.data.Movie;
import com.example.android.moviesapp.data.MoviePreferences;
import com.example.android.moviesapp.data.MovieReview;
import com.example.android.moviesapp.data.MovieTrailer;
import com.example.android.moviesapp.data.MovieViewModel;
import com.example.android.moviesapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MovieActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>
{
    //movie received from MainActivity
    private Movie displayedMovie;

    //movie ID received from MainActivity
    private int displayedMovieID;

    //movie review for displayed movie
    private List<MovieReview> displayedMovieReviews;

    //movie review for displayed movie
    private List<MovieTrailer> displayedMovieTrailers;

    //test TextView for custom adapter
    private TextView movieTitleTextView, movieReleaseTextView, movieAverageTextView,
                    moviePlotTextView;

    private ImageView movieThumbnailImageView;

    private FloatingActionButton favoritesButton;

    //URL connection variables for retrieving thumbnail images
    private String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/";

    //determining the size of the images retrieved
    private String IMAGE_SIZE = "w185";

    //progress bar for when app loads list of movies
    private ProgressBar movieProgressBar;

    //instance of the database to retrieve information
    private AppDatabase mDb;

    //view model for marking movies as favorites
    private MovieViewModel favoritesViewModel;

    //to determine whether displayed movie has been favorited
    private Boolean isFavorite;

    //to provide user feedback from operations
    private Toast toast;

    //json review data
    private String simpleJsonMovieReviewData;

    //key for loader query
    private static final String SEARCH_MOVIES_URL = "moviesURL";

    //key for review query
    private static final String SEARCH_MOVIE_REVIEW = "movieReview";

    //ID for loader to load movie review
    private static final int SEARCH_REVIEW_LOADER = 23;

    //key for review query
    private static final String SEARCH_MOVIE_TRAILER = "movieTrailer";

    //ID for loader to load movie review
    private static final int SEARCH_TRAILER_LOADER = 24;

    //recycler view for movie reviews
    private RecyclerView reviewRecyclerView;

    //recycler view for movie reviews
    private RecyclerView trailerRecyclerView;

    //adapter for custom reviews view
    private MovieAdapter myMovieAdapter;

    //adapter for custom trailers view
    private TrailerAdapter myTrailerAdapter;

    //bottom navigation menu for main activity
    private BottomNavigationView mMovieMenuNav;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieTitleTextView = (TextView) findViewById(R.id.detailsMovieName);
        movieReleaseTextView = (TextView) findViewById(R.id.detailsMovieRelease);
        movieAverageTextView = (TextView) findViewById(R.id.detailsMovieAverage);
        moviePlotTextView = (TextView) findViewById(R.id.detailsMoviePlot);
        movieThumbnailImageView = (ImageView) findViewById(R.id.detailsMovieThumbnail);
        favoritesButton = (FloatingActionButton) findViewById(R.id.favoritesButton);

        //Progress bar will show when loading the lists of movies
        movieProgressBar = (ProgressBar) findViewById(R.id.movie_progressBar);

        //create the recyclerview for the review part of the screen
        reviewRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);

        //create the recyclerview for the trailer part of the screen
        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recycler_view);

        LinearLayoutManager reviewManager = new LinearLayoutManager(this);

        reviewRecyclerView.setLayoutManager(reviewManager);

        reviewRecyclerView.setHasFixedSize(true);

        LinearLayoutManager trailerManager = new LinearLayoutManager(this);

        trailerRecyclerView.setLayoutManager(trailerManager);

        trailerRecyclerView.setHasFixedSize(true);

        RecyclerViewItemDecoration rvDecoration = new RecyclerViewItemDecoration(this);

        reviewRecyclerView.addItemDecoration(rvDecoration);

        trailerRecyclerView.addItemDecoration(rvDecoration);

        mDb = AppDatabase.getInstance(getApplicationContext());
        favoritesViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        //initialize the main menu bottom navigation menu
        mMovieMenuNav = (BottomNavigationView) findViewById(R.id.movie_nav);

        //by default, show reviews when loading a movie
        trailerRecyclerView.setVisibility(GONE);

        mMovieMenuNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.by_reviews:
                        myMovieAdapter.setMovieReviews(null);
                        swapRecyclerView();
                        loadMovieReviewData();
                        return true;
                    case R.id.by_trailers:
                        myTrailerAdapter.setMovieTrailers(null);
                        swapRecyclerView();
                        loadMovieTrailerData();
                        return true;
                    default:
                        return false;
                }
            }
        });

        if (getIntent() != null)
        {
            if (getIntent().hasExtra(Intent.EXTRA_TEXT))
            {
                //get movie object from intent
                displayedMovie = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
                displayedMovieID = displayedMovie.getMovieID();
                AppExecutors.getInstance().diskIO().execute(() -> {
                    isFavorite = favoritesViewModel.isFavorite(displayedMovieID);
                    if (isFavorite) {
                        displayedMovie = AppDatabase.getInstance(this).movieDao().getMovie(displayedMovieID);
                        favoritesButton.setImageResource(R.drawable.baseline_favorite_black_36dp);
                        AppExecutors.getInstance().diskIO().execute(() ->
                        {
                            loadMovieReviewData();
                        });
                        AppExecutors.getInstance().diskIO().execute(() ->
                        {
                            loadMovieTrailerData();
                        });
                    }
                    else
                    {
                        favoritesButton.setImageResource(R.drawable.baseline_favorite_border_black_36dp);
                        AppExecutors.getInstance().diskIO().execute(() ->
                        {
                            loadMovieReviewData();
                        });
                        AppExecutors.getInstance().diskIO().execute(() ->
                        {
                            loadMovieTrailerData();
                        });
                    }
                });
            }
        }

        favoritesButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                AppExecutors.getInstance().diskIO().execute(() -> {

                    boolean isFavorite = favoritesViewModel.isFavorite(displayedMovie.getMovieID());

                    if(isFavorite)
                    {
                        favoritesViewModel.removeMovieFromFavorites(displayedMovie);
                        runOnUiThread(() -> {
                            favoritesButton.setImageResource(R.drawable.baseline_favorite_border_black_36dp);
                            displayToastMessage(R.string.favorites_removed);
                        });
                    }
                    else
                    {
                        displayedMovie.setFavorite(true);
                        favoritesViewModel.addMovieToFavorites(displayedMovie);
                        runOnUiThread(() -> {
                            favoritesButton.setImageResource(R.drawable.baseline_favorite_black_36dp);
                            displayToastMessage(R.string.favorites_added);
                        });
                    }
                });
            }
        });

        Picasso.with(this)
            .load(THUMBNAIL_BASE_URL + IMAGE_SIZE + displayedMovie.getMovieThumbnail())
            .into(movieThumbnailImageView);

        myMovieAdapter = new MovieAdapter(this, displayedMovieReviews);

        reviewRecyclerView.setAdapter(myMovieAdapter);

        myTrailerAdapter = new TrailerAdapter(this, displayedMovieTrailers);

        trailerRecyclerView.setAdapter(myTrailerAdapter);
    }

    private void swapRecyclerView()
    {
        if(reviewRecyclerView.getVisibility() == GONE)
        {
            reviewRecyclerView.setVisibility(View.VISIBLE);
            trailerRecyclerView.setVisibility(View.GONE);
        }
        else if(trailerRecyclerView.getVisibility() == GONE)
        {
            reviewRecyclerView.setVisibility(View.GONE);
            trailerRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void loadMovieReviewData()
    {
        String URL = MoviePreferences.getDefaultConnectionUrl(this);

        URL movieRequestURL = NetworkUtils.buildUrl(URL, "review",
                displayedMovie.getMovieID());

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_MOVIE_REVIEW, movieRequestURL.toString());

        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<String> movieSearchLoader = loaderManager.getLoader(SEARCH_REVIEW_LOADER);

        if (movieSearchLoader == null)
        {
            loaderManager.initLoader(SEARCH_REVIEW_LOADER, queryBundle, this);
        }
        else
        {
            loaderManager.restartLoader(SEARCH_REVIEW_LOADER, queryBundle, this);
        }
    }

    private void loadMovieTrailerData()
    {
        String URL = MoviePreferences.getDefaultConnectionUrl(this);

        URL movieRequestURL = NetworkUtils.buildUrl(URL, "videos",
                displayedMovie.getMovieID());

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_MOVIE_TRAILER, movieRequestURL.toString());

        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<String> movieSearchLoader = loaderManager.getLoader(SEARCH_TRAILER_LOADER);

        if (movieSearchLoader == null)
        {
            loaderManager.initLoader(SEARCH_TRAILER_LOADER, queryBundle, this);
        }
        else
        {
            loaderManager.restartLoader(SEARCH_TRAILER_LOADER, queryBundle, this);
        }
    }

    private void populateUI(Movie m)
    {
        //Movie Title
        String m_movieTitle = m.getMovieTitle();
        movieTitleTextView.setText(m_movieTitle);

        //Movie Release
        String m_movieRelease = "Release Date: " + m.getMovieRelease();
        movieReleaseTextView.setText(m_movieRelease);

        //Movie Average
        String m_movieAverage = "Average Rating: " + m.getMovieAverage();
        movieAverageTextView.setText(m_movieAverage);

        //Movie Overview/Plot
        String m_moviePlot = m.getMovieOverview();
        moviePlotTextView.setText(m_moviePlot.trim());

        //set movie reviews on adapter
        if(trailerRecyclerView.getVisibility() == GONE)
        {
            if (displayedMovieReviews.size() == 0)
            {
                runOnUiThread(() ->
                {
                    displayToastMessage(R.string.no_reviews_found);
                });
            }
            else
                myMovieAdapter.setMovieReviews(displayedMovieReviews);
        }
        else if (reviewRecyclerView.getVisibility() == GONE)
        {
            if (displayedMovieTrailers.size() == 0)
            {
                runOnUiThread(() ->
                {
                    displayToastMessage(R.string.no_trailers_found);
                });
            }
            else
                myTrailerAdapter.setMovieTrailers(displayedMovieTrailers);
        }
        //remove progress bar from layout
        movieProgressBar.setVisibility(GONE);
    }

    private void displayToastMessage(int messageId)
    {
        cancelToast();

        toast = Toast.makeText(this, messageId, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void cancelToast()
    {
        //dismiss any outstanding toast messages
        if (toast != null)
        {
            toast.cancel();
        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, final Bundle args)
    {
        return new AsyncTaskLoader<String>(this)
        {
            @Override
            protected void onStartLoading()
            {
                super.onStartLoading();
                if (args == null)
                {
                    return;
                }
                forceLoad();
            }

            @Override
            public String loadInBackground()
            {
                String searchListOfMoviesQuery = args.getString(SEARCH_MOVIE_REVIEW);

                if (searchListOfMoviesQuery == null || TextUtils.isEmpty(searchListOfMoviesQuery))
                {
                    String searchListOfTrailers = args.getString(SEARCH_MOVIE_TRAILER);
                    if(searchListOfTrailers != null || !(TextUtils.isEmpty(searchListOfTrailers)))
                    {
                        try
                        {
                            URL movieRequestURL = new URL(searchListOfTrailers);
                            String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);

                            displayedMovieTrailers = parseMovieTrailers(responseFromHttpUrl);

                            return simpleJsonMovieReviewData;
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }
                else
                {
                    try
                    {
                        URL movieRequestURL = new URL(searchListOfMoviesQuery);
                        String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);

                        displayedMovieReviews = parseMovieReview(responseFromHttpUrl);

                        return simpleJsonMovieReviewData;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        return null;
                    }
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s)
    {
        populateUI(displayedMovie);
    }

    @Override
    public void onLoaderReset(Loader<String> loader)
    {

    }

    public ArrayList<MovieReview> parseMovieReview(String movieJsonStr) throws JSONException
    {
        ArrayList<MovieReview> movieReviews = new ArrayList<MovieReview>();
        /* String designating movie results from movieDB */
        final String REV_RESULTS = "results";

        //Movie ID
        final String REV_ID = "id";
        //Movie Title
        final String REV_AUTHOR = "author";
        //Movie Thumbnail Image ID
        final String REV_CONTENT = "content";
        //Movie plot synopsis
        final String REV_URL = "url";

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(REV_RESULTS);

        for (int i = 0; i < movieArray.length(); i++)
        {
            JSONObject movieObject = movieArray.getJSONObject(i);

            //declare the variables we want to use to parse our movie object
            //int movieReviewID;
            String reviewAuthor;
            String reviewContent;
            String reviewURL;

            //movieReviewID = movieObject.getInt(REV_ID);
            reviewAuthor = movieObject.getString(REV_AUTHOR);
            reviewContent = movieObject.getString(REV_CONTENT);
            reviewURL = movieObject.getString(REV_URL);

            MovieReview mr = new MovieReview(/*movieReviewID,*/ reviewAuthor, reviewContent, reviewURL);

            movieReviews.add(mr);
        }

        return movieReviews;
    }

    public ArrayList<MovieTrailer> parseMovieTrailers(String movieJsonStr) throws JSONException
    {
        ArrayList<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();
        /* String designating movie results from movieDB */
        final String TRA_RESULTS = "results";

        //Trailer ID
        final String TRA_ID = "id";
        //Trailer Key
        final String TRA_KEY = "key";
        //Trailer Name
        final String TRA_NAME = "name";

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray trailerArray = movieJson.getJSONArray(TRA_RESULTS);

        for (int i = 0; i < trailerArray.length(); i++)
        {
            JSONObject movieObject = trailerArray.getJSONObject(i);

            //declare the variables we want to use to parse our movie trailer object
            String trailerID;
            String trailerKey;
            String trailerName;

            trailerID = movieObject.getString(TRA_ID);
            trailerKey = movieObject.getString(TRA_KEY);
            trailerName = movieObject.getString(TRA_NAME);

            MovieTrailer mt = new MovieTrailer(trailerID, trailerKey, trailerName);

            movieTrailers.add(mt);
        }

        return movieTrailers;
    }
}
