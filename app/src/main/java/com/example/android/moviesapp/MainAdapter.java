package com.example.android.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesapp.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainAdapterViewHolder>
{
    //variables for Movie title and other information
    private Context mainAdapterContext;
    private List<Movie> movies;

    //URL connection variables for retrieving thumbnail images
    private String BASE_URL = "http://image.tmdb.org/t/p/";

    //determining the size of the images retrieved
    private String IMAGE_SIZE = "w185";

    //poster_path from JSON movie object response
    private String THUMBNAIL = "";

    public interface MainAdapterOnClickHandler
    {
        void onClick(Movie selectedMovie);
    }

    public MainAdapter(Context context, List<Movie> movies)
    {
        mainAdapterContext = context;
        this.movies = movies;

        if (movies != null)
        {
            for (Movie m : movies)
                movies.add(m);
        }
    }

    public class MainAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //public final TextView movieTitle;
        public final ImageView movieThumbnail;

        public MainAdapterViewHolder(View view)
        {
            super(view);
            //movieTitle = (TextView) view.findViewById(R.id.textViewMovieName);
            movieThumbnail = (ImageView) view.findViewById(R.id.textViewMovieThumbnailIcon);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Movie movie = movies.get(getAdapterPosition());
            Intent intent = new Intent(mainAdapterContext, MovieActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movie);
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public MainAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        mainAdapterContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_main;
        LayoutInflater inflater = LayoutInflater.from(mainAdapterContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        return new MainAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainAdapterViewHolder movieViewHolder, int position)
    {
        //String selectedMovieName = movies.get(position).getMovieTitle();
        //movieViewHolder.movieTitle.setText(selectedMovieName);

        Picasso.with(mainAdapterContext)
                .load(BASE_URL + IMAGE_SIZE + movies.get(position).getMovieThumbnail())
                .into(movieViewHolder.movieThumbnail);
    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    public List<Movie> getMovieData()
    {
        return movies;
    }

    public void setMovieData(Movie[] movieData)
    {
        movies = new ArrayList<Movie>();

        if(movieData != null)
        {
            for (Movie m : movieData)
            {
                movies.add(m);
            }
        }

        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movies)
    {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
