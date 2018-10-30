package com.example.android.moviesapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviesapp.data.MovieTrailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieTrailerViewHolder>
{
    //variables for Movie title and other information
    private Context trailerAdapterContext;
    private List<MovieTrailer> trailers;

    public interface TrailerAdapterOnClickHandler
    {
        void onClick(MovieTrailer selectedTrailer);
    }

    public TrailerAdapter(Context context, List<MovieTrailer> trailers)
    {
        trailerAdapterContext = context;
        this.trailers = trailers;

        if (trailers != null)
        {
            for (MovieTrailer mt : trailers)
                trailers.add(mt);
        }
    }

    public void setMovieTrailers(List<MovieTrailer> movieTrailers)
    {
        this.trailers = movieTrailers;
        notifyDataSetChanged();
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView trailerName;

        public MovieTrailerViewHolder(View view)
        {
            super(view);
            trailerName = (TextView) view.findViewById(R.id.movie_trailer);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            MovieTrailer trailer = trailers.get(getAdapterPosition());
            String youtubeVideoKey = trailer.getTrailerKey();

            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd:youtube:"
                    + youtubeVideoKey));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://www.youtube.com/watch?v=" + youtubeVideoKey));

            try
            {
                trailerAdapterContext.startActivity(appIntent);
            }
            catch (ActivityNotFoundException ex)
            {
                trailerAdapterContext.startActivity(webIntent);
            }
        }

        void bind(int position)
        {
            if(trailers != null)
            {
                MovieTrailer mt = trailers.get(position);
                trailerName.setText(mt.getTrailerName());
            }
        }
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        trailerAdapterContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_trailers;
        LayoutInflater inflater = LayoutInflater.from(trailerAdapterContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder trailerViewHolder, int position)
    {
        trailerViewHolder.bind(position);
    }

    @Override
    public int getItemCount()
    {
        if (null == trailers) return 0;
        return trailers.size();
    }
}
