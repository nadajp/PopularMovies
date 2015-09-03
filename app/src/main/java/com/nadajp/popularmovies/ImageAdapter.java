package com.nadajp.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nadajp on 7/13/15.
 * Custom Adapter for inserting movie images into a grid
 */
public class ImageAdapter extends BaseAdapter {
    public static final String BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String LOG_TAG = "ImageAdapter";
    private Context mContext;
    private ArrayList<Movie> mMovies = null;

    public ImageAdapter(Context c, ArrayList<Movie> movies) {
        mContext = c;
        mMovies = movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        mMovies = movies;
    }

    public int getCount() {
        if (mMovies == null) {
            return 0;
        }
        return mMovies.size();
    }

    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    public long getItemId(int position) {
        return Long.valueOf(mMovies.get(position).getId());
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).
                load(BASE_URL + getItem(position).getPosterPath()).
                placeholder(R.drawable.loading).
                error(R.drawable.error).
                into(imageView);

        return imageView;
    }
}
