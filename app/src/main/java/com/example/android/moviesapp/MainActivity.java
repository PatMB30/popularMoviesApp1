package com.example.android.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.android.moviesapp.data.MoviePreferences;
import com.example.android.moviesapp.utils.NetworkUtils;
import com.example.android.moviesapp.utils.OpenMovieJsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler
{
    //recycler view for main screen
    private RecyclerView mainRecyclerView;

    //tool bar for main screen
    private Toolbar mainToolBar;

    //adapter for custom movie view
    private MovieAdapter myMovieAdapter;

    //progress bar for when app loads list of movies
    private ProgressBar mainProgressBar;

    //error message shown if app has trouble loading list of movies
    private TextView mainErrorMessage;

    //default string containing filter for main menu
    private String menuFilter;

    //Array containing list of movie objects to be displayed
    private Movie[] listOfMoviesToDisplay;

    //customized listview for the main activity, to add logo to entries
    //private ListView sandwichesWithIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //make the main screen ranked on popularity by default
        menuFilter = "popular";

        //initialize array of movie objects to display
        listOfMoviesToDisplay = null;

        //create the recyclerview for the main screen
        mainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);

        //create textview for optional error message handling
        mainErrorMessage = (TextView) findViewById(R.id.main_errorMessage);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mainRecyclerView.setLayoutManager(gridLayoutManager);

        mainRecyclerView.setHasFixedSize(true);

        myMovieAdapter = new MovieAdapter(this, listOfMoviesToDisplay, this);

        mainRecyclerView.setAdapter(myMovieAdapter);

        //Progress bar will show when loading the lists of movies
        mainProgressBar = (ProgressBar) findViewById(R.id.main_progressBar);

        loadMovieData();

    }

    private void loadMovieData()
    {
        showMovieDataView();

        String URL = MoviePreferences.getDefaultConnectionUrl(this);
        new FetchMovieTask().execute(URL);
    }

    @Override
    public void onClick(Movie selectedMovie)
    {
        Intent intent = new Intent(this, MovieActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("selected_movie", selectedMovie);
        intent.putExtras(bundle);
        this.startActivity(intent);
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

    public class FetchMovieTask extends AsyncTask<String, Void, String[]>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mainProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params)
        {
            if (params.length == 0)
            {
                return null;
            }

            String key = params[0];
            URL movieRequestURL = NetworkUtils.buildUrl(key, menuFilter);

            try
            {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestURL);

                String[] simpleJsonMovieData = OpenMovieJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                listOfMoviesToDisplay = convertToListOfMovies(jsonMovieResponse);

                return simpleJsonMovieData;

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData)
        {
            mainProgressBar.setVisibility(View.INVISIBLE);
            if (movieData != null)
            {
                showMovieDataView();
                myMovieAdapter.setMovieData(listOfMoviesToDisplay);
            }
            else
            {
                showErrorMessage();
            }
        }
    }

    private Movie[] convertToListOfMovies(String jsonResponse)
            throws JSONException {

        /* String designating movie results from movieDB */
        final String MOV_RESULTS = "results";

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

        final String OWM_MESSAGE_CODE = "cod";

        Movie[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(jsonResponse);

        JSONArray movieArray = movieJson.getJSONArray(MOV_RESULTS);

        parsedMovieData = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++)
        {
            /* Get the JSON object representing one movie */
            JSONObject movieObject = movieArray.getJSONObject(i);

            //declare the variables we want to use to parse our movie object
            String movieTitle;
            String movieThumbnail;
            String movieOverview;
            String movieAverage;
            String movieRelease;

            movieTitle = movieObject.getString(MOV_TITLE);
            movieThumbnail = movieObject.getString(MOV_THUMB);
            movieOverview = movieObject.getString(MOV_PLOT);
            movieAverage = movieObject.getString(MOV_RAT);
            movieRelease = movieObject.getString(MOV_DATE);

            Movie m = new Movie(movieTitle, movieThumbnail, movieOverview,
                    movieAverage, movieRelease);

            parsedMovieData[i] = m;
        }

        return parsedMovieData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.movie, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.by_popularity)
        {
            menuFilter = "popular";
            myMovieAdapter.setMovieData(null);
            loadMovieData();
            return true;
        }
        if (id == R.id.by_rating)
        {
            menuFilter = "top_rated";
            myMovieAdapter.setMovieData(null);
            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}