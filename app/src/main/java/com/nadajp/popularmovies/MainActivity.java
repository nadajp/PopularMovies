package com.nadajp.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.nadajp.popularmovies.utils.Utils;

/**
 * Created by nadajp on 7/13/15.
 * Main Activity, holds the movie grid fragment
 */
public class MainActivity extends AppCompatActivity implements MovieGridFragment.OnMovieSelectedListener {

    private static final String MOVIEDETAILFRAGMENT_TAG = "MOVIEDETAILTAG";
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayShowTitleEnabled(true);


        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), MOVIEDETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onMovieSelected(Movie movie) {
        Bundle extras = new Bundle();
        extras.putParcelable(Utils.MOVIE_KEY, movie);
        if (mTwoPane) {
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(extras);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIEDETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(Utils.MOVIE_KEY, extras);
            startActivity(intent);
        }
    }
}
