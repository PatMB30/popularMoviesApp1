package com.example.android.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class MovieActivity extends AppCompatActivity
{
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private List<Movie> listOfMovies;

    //test TextView for custom adapter
    private TextView movieTitleTextView, movieReleaseTextView, movieAverageTextView,
                    moviePlotTextView;

    private ImageView movieThumbnailImageView;

    //URL connection variables for retrieving thumbnail images
    private String BASE_URL = "http://image.tmdb.org/t/p/";

    //determining the size of the images retrieved
    private String IMAGE_SIZE = "w185";

    //poster_path from JSON movie object response
    private String THUMBNAIL = "";

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

        Intent intent = getIntent();
        if (intent == null)
        {
            closeOnError();
        }

        Bundle bundle = getIntent().getExtras();
        Movie selectedMovie = bundle.getParcelable("selected_movie");

        if (selectedMovie == null)
        {
            // Movie data unavailable
            closeOnError();
            return;
        }

        populateUI(selectedMovie);
        Picasso.with(this)
                .load(BASE_URL + IMAGE_SIZE + selectedMovie.getMovieThumbnail())
                .into(movieThumbnailImageView);

        setTitle(selectedMovie.getMovieTitle());
    }

    private void closeOnError()
    {
        finish();
        Toast.makeText(this, R.string.detail_error_message,
                Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie m)
    {
        //we will use a StringBuilder to format our variables differently, then reset it
        StringBuilder sb = new StringBuilder();

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
        moviePlotTextView.setText(m_moviePlot);
    }

}
