package com.example.android.moviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>
{
    //variables for Movie title and other information
    private Context movieAdapterContext;
    private List<Movie> movies;

    //onClick handler for Movie adapter
    //private final MovieAdapterOnClickHandler clickHandler;

    //URL connection variables for retrieving thumbnail images
    private String BASE_URL = "http://image.tmdb.org/t/p/";

    //determining the size of the images retrieved
    private String IMAGE_SIZE = "w185";

    //poster_path from JSON movie object response
    private String THUMBNAIL = "";

    public MovieAdapter()
    {

    }

    public interface MovieAdapterOnClickHandler
    {
        void onClick(String selectedMovie);
    }

    public MovieAdapter(Context c, Movie[] listOfMovies, MovieAdapterOnClickHandler cHandler)
    {
        movieAdapterContext = c;

        movies = new ArrayList<Movie>();

        if (listOfMovies != null)
        {
            for (int i = 0; i < listOfMovies.length; i++)
                movies.add(listOfMovies[i]);
        }

        //clickHandler = cHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView movieTitle;
        public final ImageView movieThumbnail;

        public MovieAdapterViewHolder(View view)
        {
            super(view);
            movieTitle = (TextView) view.findViewById(R.id.textViewMovieName);
            movieThumbnail = (ImageView) view.findViewById(R.id.textViewMovieThumbnailIcon);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int adapterPosition = getAdapterPosition();
            String selectedMovieTitle = movies.get(adapterPosition).getMovieTitle();
            //clickHandler.onClick(selectedMovieTitle);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_main;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieViewHolder, int position)
    {
        String selectedMovieName = movies.get(position).getMovieTitle();
        movieViewHolder.movieTitle.setText(selectedMovieName);

        Picasso.with(movieAdapterContext)
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
            for (int i = 0; i < movieData.length; i++)
            {
                movies.add(movieData[i]);
            }
        }

        notifyDataSetChanged();
    }
}
