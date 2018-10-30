package com.example.android.moviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.moviesapp.data.MovieReview;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>
{
    //variables for Movie title and other information
    private Context movieAdapterContext;
    private List<MovieReview> reviews;

    public MovieAdapter(Context context, List<MovieReview> reviews)
    {
        movieAdapterContext = context;
        this.reviews = reviews;

        if (reviews != null)
        {
            for (MovieReview mr : reviews)
                reviews.add(mr);
        }
    }

    public void setMovieReviews(List<MovieReview> movieReviews)
    {
        this.reviews = movieReviews;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder
    {
        //public final TextView movieTitle;
        public final TextView movieReviewAuthor;
        public final TextView movieReviewContent;
        public final TextView movieReviewSite;

        public MovieAdapterViewHolder(View view)
        {
            super(view);
            movieReviewAuthor = (TextView) view.findViewById(R.id.movie_review_author);
            movieReviewContent = (TextView) view.findViewById(R.id.movie_review_content);
            movieReviewSite = (TextView) view.findViewById(R.id.movie_review_site);
        }

        void bind(int position)
        {
            if(reviews != null)
            {
                MovieReview mr = reviews.get(position);
                movieReviewAuthor.setText(mr.getReviewAuthor());
                movieReviewContent.setText(mr.getReviewContent());
                movieReviewSite.setText(mr.getReviewURL());
            }
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        movieAdapterContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_reviews;
        LayoutInflater inflater = LayoutInflater.from(movieAdapterContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieViewHolder, int position)
    {
        movieViewHolder.bind(position);
    }

    @Override
    public int getItemCount()
    {
        if (null == reviews) return 0;
        return reviews.size();
    }
}
