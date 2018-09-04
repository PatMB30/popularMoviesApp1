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

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION)
        {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }


    }

    private void closeOnError()
    {
        finish();
        Toast.makeText(this, R.string.detail_error_message,
                Toast.LENGTH_SHORT).show();
    }
}
