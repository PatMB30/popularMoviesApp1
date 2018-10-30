package com.example.android.moviesapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.moviesapp.data.AppDatabase;
import com.example.android.moviesapp.data.MainViewModel;
import com.example.android.moviesapp.data.Movie;
import com.example.android.moviesapp.data.MoviePreferences;
import com.example.android.moviesapp.utils.NetworkUtils;
import com.example.android.moviesapp.utils.OpenMovieJsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MainAdapter.MainAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String>
{
    //flag for differentiating URL request
    private int reviewsFlag = 0;

    //to save instance state
    private static final String MAIN_MENU_STATE = "state";

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    //key for loader query
    private static final String SEARCH_MOVIES_URL = "moviesURL";

    //key for review query
    private static final String SEARCH_MOVIE_REVIEW = "movieReview";

    //ID for loader to load movies
    private static final int SEARCH_MOVIES_LOADER = 22;

    //bottom navigation menu for main activity
    private BottomNavigationView mMainMenuNav;

    //recycler view for main screen
    private RecyclerView mainRecyclerView;

    //adapter for custom movie view
    private MainAdapter myMainAdapter;

    //progress bar for when app loads list of movies
    private ProgressBar mainProgressBar;

    //error message shown if app has trouble loading list of movies
    private TextView mainErrorMessage;

    //default string containing filter for main menu
    private String menuFilter;

    //JSON string received from API calls
    private String simpleJsonMovieData;

    //Array containing list of movie objects to be displayed
    private List<Movie> listOfMoviesToDisplay;

    //instance of the database to retrieve information
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
        {
            if(savedInstanceState.containsKey(MAIN_MENU_STATE))
            {
                String savedMenuState = savedInstanceState.getString(MAIN_MENU_STATE);
                if(savedMenuState != null)
                    menuFilter = savedMenuState;
                else
                {
                    //make the main screen ranked on popularity by default
                    menuFilter = "popular";
                }
            }
        }
        else
        {
            //make the main screen ranked on popularity by default
            menuFilter = "popular";
        }

        //default value
        simpleJsonMovieData = "";

        //initialize the main menu bottom navigation menu
        mMainMenuNav = (BottomNavigationView) findViewById(R.id.main_nav);

        mMainMenuNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.by_popularity:
                        mMainMenuNav.setItemBackgroundResource(R.color.colorPopular);
                        menuFilter = "popular";
                        myMainAdapter.setMovieData(null);
                        loadMovieData();
                        return true;
                    case R.id.by_rating:
                        //mMainMenuNav.setItemBackgroundResource(R.color.colorRating);
                        menuFilter = "top_rated";
                        myMainAdapter.setMovieData(null);
                        loadMovieData();
                        return true;
                    case R.id.by_favorites:
                        //mMainMenuNav.setItemBackgroundResource(R.color.colorFavorite);
                        menuFilter = "favorite";
                        myMainAdapter.setMovieData(null);
                        loadMovieData();
                        return true;
                    default:
                        return false;
                }
            }
        });

        //initialize array of movie objects to display
        listOfMoviesToDisplay = new ArrayList<Movie>();

        //create the recyclerview for the main screen
        mainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);

        //create textview for optional error message handling
        mainErrorMessage = (TextView) findViewById(R.id.main_errorMessage);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns());

        mainRecyclerView.setLayoutManager(gridLayoutManager);

        mainRecyclerView.setHasFixedSize(true);

        myMainAdapter = new MainAdapter(this, listOfMoviesToDisplay);

        mainRecyclerView.setAdapter(myMainAdapter);

        //RecyclerViewItemDecoration rvDecoration = new RecyclerViewItemDecoration(this);

        //mainRecyclerView.addItemDecoration(rvDecoration);

        //Progress bar will show when loading the lists of movies
        mainProgressBar = (ProgressBar) findViewById(R.id.main_progressBar);

        getSupportLoaderManager().initLoader(SEARCH_MOVIES_LOADER, null, this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        loadMovieData();
    }

    private int numberOfColumns()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 500;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void loadMovieData()
    {
        showMovieDataView();

        if(menuFilter != "favorite")
        {
            String URL = MoviePreferences.getDefaultConnectionUrl(this);

            URL movieRequestURL = NetworkUtils.buildUrl(URL, menuFilter,
                    -1);

            Bundle queryBundle = new Bundle();
            queryBundle.putString(SEARCH_MOVIES_URL, movieRequestURL.toString());

            LoaderManager loaderManager = getSupportLoaderManager();

            Loader<String> movieSearchLoader = loaderManager.getLoader(SEARCH_MOVIES_LOADER);

            if (movieSearchLoader == null)
            {
                loaderManager.initLoader(SEARCH_MOVIES_LOADER, queryBundle, this);
            }
            else
            {
                loaderManager.restartLoader(SEARCH_MOVIES_LOADER, queryBundle, this);
            }
        }
        else
        {
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

            viewModel.getMovies().observe(this, new Observer<List<Movie>>()
            {
                @Override
                public void onChanged(@Nullable List<Movie> movies)
                {
                    Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                    myMainAdapter.setMovies(movies);
                }
            });
        }
    }

    @Override
    public void onClick(Movie selectedMovie)
    {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(SEARCH_MOVIE_REVIEW, selectedMovie);
        startActivity(intent);
    }

    private void showMovieDataView()
    {
        mainErrorMessage.setVisibility(View.INVISIBLE);
        mainRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage()
    {
        mainRecyclerView.setVisibility(View.INVISIBLE);
        mainErrorMessage.setVisibility(View.VISIBLE);
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
                mainProgressBar.setVisibility(View.VISIBLE);

                forceLoad();

                mainProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public String loadInBackground()
            {
                String searchListOfMoviesQuery = args.getString(SEARCH_MOVIES_URL);

                if (searchListOfMoviesQuery == null || TextUtils.isEmpty(searchListOfMoviesQuery))
                    return null;

                try
                {
                    URL movieRequestURL = new URL(searchListOfMoviesQuery);
                    String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);

                    simpleJsonMovieData = OpenMovieJsonUtils
                            .getSimpleMovieStringsFromJson(MainActivity.this,
                                    responseFromHttpUrl);

                    parseListOfMovies(simpleJsonMovieData);

                    return simpleJsonMovieData;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    //mainProgressBar.setVisibility(View.INVISIBLE);
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s)
    {
        mainProgressBar.setVisibility(View.INVISIBLE);
        if (s != null || menuFilter != null)
        {
            showMovieDataView();
            myMainAdapter.setMovies(listOfMoviesToDisplay);
        }
        else
        {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    public void parseListOfMovies(String movieJsonStr) throws JSONException
    {
        listOfMoviesToDisplay.clear();
        /* String designating movie results from movieDB */
        final String MOV_RESULTS = "results";

        //Movie ID
        final String MOV_ID = "id";
        //Movie Title
        final String MOV_TITLE = "title";
        //Movie Thumbnail Image ID
        final String MOV_THUMB = "poster_path";
        //Movie plot synopsis
        final String MOV_PLOT = "overview";
        //Movie user rating
        final String MOV_RAT = "vote_average";
        //Movie release date
        final String MOV_DATE = "release_date";

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(MOV_RESULTS);

        for (int i = 0; i < movieArray.length(); i++)
        {
            JSONObject movieObject = movieArray.getJSONObject(i);

            //declare the variables we want to use to parse our movie object
            int movieID;
            String movieTitle;
            String movieThumbnail;
            String movieOverview;
            String movieAverage;
            String movieRelease;

            movieID = movieObject.getInt(MOV_ID);
            movieTitle = movieObject.getString(MOV_TITLE);
            movieThumbnail = movieObject.getString(MOV_THUMB);
            movieOverview = movieObject.getString(MOV_PLOT);
            movieAverage = movieObject.getString(MOV_RAT);
            movieRelease = movieObject.getString(MOV_DATE);

            Movie m = new Movie(movieID, movieTitle, movieThumbnail, movieOverview, movieAverage,
                    movieRelease, false);

            listOfMoviesToDisplay.add(m);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        String mainMenuState = menuFilter;
        outState.putString(MAIN_MENU_STATE, mainMenuState);
    }
}