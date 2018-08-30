package com.example.android.moviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MovieAdapter extends BaseAdapter
{
    //variables for Movie title and other information
    private Context movieAdapterContext;
    private List<Movie> movies;

    public MovieAdapter()
    {

    }

    public MovieAdapter(Context context, List<Movie> listOfMovies)
    {
        movieAdapterContext = context;
        movies = listOfMovies;
    }

    public int getCount()
    {
        // TODO Auto-generated method stub
        return movies.size();
    }

    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        return movies.get(arg0);
    }

    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return movies.indexOf(getItem(position));
    }

    private class ViewHolder
    {
        ImageView movieThumbnail;
        TextView movieName;
    }


    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;

        //inflate layout by using our created sandwich_main.xml adapter
        LayoutInflater inflater = (LayoutInflater) movieAdapterContext
                .getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.movie_main, null);
            holder = new ViewHolder();

            holder.movieThumbnail = (ImageView) convertView.findViewById(R.id.movieThumbnailIcon);
            holder.movieName = (TextView) convertView.findViewById(R.id.movieName);

            Movie m = movies.get(position);

            holder.movieThumbnail.setImageResource(m.getImageID());
            holder.movieName.setText(m.getMainName());
        }

        return convertView;
    }
}
