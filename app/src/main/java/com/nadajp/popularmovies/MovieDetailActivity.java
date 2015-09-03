package com.nadajp.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nadajp.popularmovies.utils.Utils;

/**
 * Created by nadajp on 7/13/15.
 * Activity that holds the movie details fragment, used to pass movie data between fragments
 */
public class MovieDetailActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MovieDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_movie_detail);
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        {
            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle args;
            if (savedInstanceState != null) {
                args = savedInstanceState.getParcelable(Utils.MOVIE_KEY);
            } else {
                args = getIntent().getBundleExtra(Utils.MOVIE_KEY);
            }
            fragment.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
}