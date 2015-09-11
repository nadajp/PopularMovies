package com.nadajp.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nadajp.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by nadajp on 7/13/15.
 * Shows Movie details: poster image, synopsis, rating and release date
 */
public class MovieDetailFragment extends Fragment {

    public static final String LOG_TAG = "MovieDetailFragment";
    public static final String BASE_URL = "http://image.tmdb.org/t/p/w342/";

    // UI elements
    ImageView mImgPoster;
    TextView mTxtTitle;
    TextView mTxtReleaseDate;
    TextView mTxtSynopsis;
    TextView mTxtRating;

    private String mTitle;
    private String mReleaseDate;
    private String mSynopsis;
    private String mRating;
    private String mPosterPath;
    private Movie mMovie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mImgPoster = (ImageView) view.findViewById(R.id.imgPoster);
        mTxtTitle = (TextView) view.findViewById(R.id.txtTitle);
        mTxtReleaseDate = (TextView) view.findViewById(R.id.txtReleaseDate);
        mTxtRating = (TextView) view.findViewById(R.id.txtRating);
        mTxtSynopsis = (TextView) view.findViewById(R.id.txtSynopsis);

        if (getArguments() != null) {
            Bundle args = getArguments();
            mMovie = args.getParcelable(Utils.MOVIE_KEY);

            mTitle = mMovie.getTitle();
            mPosterPath = mMovie.getPosterPath();
            mReleaseDate = mMovie.getReleaseDate();
            mRating = mMovie.getRating();
            mSynopsis = mMovie.getSynopsis();

            mTxtTitle.setText(mTitle);
            if (mReleaseDate != null && !mReleaseDate.isEmpty() && !mReleaseDate.matches("null")) {
                Log.i(LOG_TAG, "Release date: " + mReleaseDate);
                String year = mReleaseDate.substring(0, 4);
                mTxtReleaseDate.setText(year);
            } else {
                mTxtReleaseDate.setText(R.string.NA);
            }
            mTxtRating.setText(mRating + "/10");
            if (mSynopsis != null && !mSynopsis.matches("null")) {
                mTxtSynopsis.setText(mSynopsis);
            } else {
                mTxtSynopsis.setText(R.string.no_synopsis);
            }
            Picasso.with(this.getActivity()).
                    load(BASE_URL + mPosterPath).
                    placeholder(R.drawable.loading).
                    error(R.drawable.error).
                    into(mImgPoster);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovie != null) {
            outState.putParcelable(Utils.MOVIE_KEY, mMovie);
        }
    }
}