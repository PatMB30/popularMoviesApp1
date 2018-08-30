package com.example.android.moviesapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.android.moviesapp.data.MoviePreferences;
import com.example.android.moviesapp.utils.NetworkUtils;
import com.example.android.moviesapp.utils.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

    //customized listview for the main activity, to add logo to entries
    //private ListView sandwichesWithIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the recyclerview for the main screen
        mainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);

        //create textview for optional error message handling
        mainErrorMessage = (TextView) findViewById(R.id.main_errorMessage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.setHasFixedSize(true);

        myMovieAdapter = new MovieAdapter();

        mainRecyclerView.setAdapter(myMovieAdapter);
        mainProgressBar = (ProgressBar) findViewById(R.id.main_progressBar);

        loadMovieData();
    }

    private void loadMovieData()
    {
        showMovieDataView();

        String URL = MoviePreferences.getDefaultConnectionUrl(this);
        new FetchMovieTask().execute(URL);
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
            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0)
            {
                return null;
            }

            String key = params[0];
            URL movieRequestURL = NetworkUtils.buildUrl(key);

            try
            {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestURL);

                String[] simpleJsonMovieData = OpenMovieJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

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
                myMovieAdapter.setMovieData(movieData);
            }
            else
            {
                showErrorMessage();
            }
        }
    }

    private void launchDetailActivity(int position)
    {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(MovieActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }
}
