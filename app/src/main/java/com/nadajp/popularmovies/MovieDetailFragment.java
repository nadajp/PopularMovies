package com.nadajp.popularmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nadajp.popularmovies.provider.movie.MovieColumns;
import com.nadajp.popularmovies.provider.movie.MovieContentValues;
import com.nadajp.popularmovies.provider.movie.MovieSelection;
import com.nadajp.popularmovies.provider.review.ReviewColumns;
import com.nadajp.popularmovies.provider.review.ReviewCursor;
import com.nadajp.popularmovies.provider.review.ReviewSelection;
import com.nadajp.popularmovies.provider.trailer.TrailerColumns;
import com.nadajp.popularmovies.provider.trailer.TrailerCursor;
import com.nadajp.popularmovies.provider.trailer.TrailerSelection;
import com.nadajp.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by nadajp on 7/13/15.
 * Shows Movie details: poster image, synopsis, rating and release date
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = "MovieDetailFragment";
    public static final String BASE_URL = "http://image.tmdb.org/t/p/w342/";
    public static final String BASE_YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    public String mId;                   // movie id, used for fetching data from API
    // UI elements
    ImageView mImgPoster;
    TextView mTxtTitle;
    TextView mTxtReleaseDate;
    TextView mTxtSynopsis;
    TextView mTxtRating;
    LinearLayout mLayoutTrailers;
    LinearLayout mLayoutReviews;
    ImageView mImgFavourite;
    ArrayList<Review> mReviews;
    ArrayList<Trailer> mTrailers;
    private Movie mMovie;                  // movie object contains all movie details
    private Boolean mIsFavourite = false;  // is this movie currently marked as favourite?
    private ContentResolver mResolver;    // used for communicating with the content provider
    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(Utils.TRAILERS_KEY)) {
            mTrailers = new ArrayList<>();
        }
        if (savedInstanceState == null || !savedInstanceState.containsKey(Utils.REVIEWS_KEY)) {
            mReviews = new ArrayList<>();
        }
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
        mLayoutTrailers = (LinearLayout) view.findViewById(R.id.layoutTrailers);
        mLayoutReviews = (LinearLayout) view.findViewById(R.id.layoutReviews);
        mImgFavourite = (ImageView) view.findViewById(R.id.imgFavourite);
        mImgFavourite.setOnClickListener(this);

        mResolver = getActivity().getContentResolver();

        if (savedInstanceState != null) {
            Log.i(LOG_TAG, "Getting movie from saved instance state...");
            mMovie = savedInstanceState.getParcelable(Utils.MOVIE_KEY);
            if (savedInstanceState.containsKey(Utils.TRAILERS_KEY)) {
                mTrailers = savedInstanceState.getParcelableArrayList(Utils.TRAILERS_KEY);
            }
            if (savedInstanceState.containsKey(Utils.REVIEWS_KEY)) {
                mReviews = savedInstanceState.getParcelableArrayList(Utils.REVIEWS_KEY);
            }
            //Log.i(LOG_TAG, "ID: " + mMovie.getId());
            checkFavourites();
        } else if (getArguments() != null) {
            //Log.i(LOG_TAG, "Getting movie from intent...");
            Bundle args = getArguments();
            mMovie = args.getParcelable(Utils.MOVIE_KEY);
            //Log.i(LOG_TAG, "ID from Intent: " + mMovie.getId());

            checkFavourites();
            if (mIsFavourite) {
                // load movie details from database
                loadMovieFromDatabase();
            } else if (isNetworkAvailable()) {
                new DownloadDetailsTask().execute();
            }
        } else {
            RelativeLayout layoutDetails = (RelativeLayout) view.findViewById(R.id.layoutMovieDetails);
            layoutDetails.setVisibility(View.INVISIBLE);
            return view;
        }

        mId = mMovie.getId();
        String title = mMovie.getTitle();
        String posterPath = mMovie.getPosterPath();
        String releaseDate = mMovie.getReleaseDate();
        String rating = mMovie.getRating();
        String synopsis = mMovie.getSynopsis();

        mTxtTitle.setText(title);
        if (releaseDate != null && !releaseDate.isEmpty() && !releaseDate.matches("null")) {
            //Log.i(LOG_TAG, "Release date: " + releaseDate);
            String year = releaseDate.substring(0, 4);
            mTxtReleaseDate.setText(year);
        } else {
            mTxtReleaseDate.setText(R.string.NA);
        }
        mTxtRating.setText(rating + "/10");
        if (synopsis != null && !synopsis.matches("null")) {
            mTxtSynopsis.setText(synopsis);
        } else {
            mTxtSynopsis.setText(R.string.no_synopsis);
        }
        Picasso.with(this.getActivity()).
                load(BASE_URL + posterPath).
                placeholder(R.drawable.loading).
                error(R.drawable.error).
                into(mImgPoster);

        if (mTrailers.size() > 0) {
            showTrailers();
        }

        if (mReviews.size() > 0) {
            showReviews();
        }

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
            this.getActivity().invalidateOptionsMenu();
        }

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

       // if (mTrailers != null && mTrailers.size() > 0) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
       // }
    }

    private Intent createShareMovieIntent() {
        if (mTrailers != null && mTrailers.size() > 0) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, BASE_YOUTUBE_URL + mTrailers.get(0).getSource());
            //Log.i(LOG_TAG, "Share Intent: " + mTrailers.get(0).getSource());
            return shareIntent;
        }
        else return null;
    }

    private void loadMovieFromDatabase() {
        TrailerSelection trailerSel = new TrailerSelection();
        trailerSel.moviedbId(mMovie.getId());
        TrailerCursor tc = trailerSel.query(mResolver);
        int i = 0;
        while (tc.moveToNext()) {
            mTrailers.add(i++, new Trailer(tc.getName(), tc.getSource()));
        }
        tc.close();
        i = 0;
        ReviewSelection reviewSel = new ReviewSelection();
        reviewSel.moviedbId(mMovie.getId());
        ReviewCursor rc = reviewSel.query(mResolver);
        while (rc.moveToNext()) {
            mReviews.add(i++, new Review(rc.getAuthor(), rc.getContent()));
        }
        rc.close();
    }

    private void checkFavourites() {
        MovieSelection where = new MovieSelection();
        where.movieId(mMovie.getId());
        Cursor c = where.query(mResolver);
        if (c.getCount() > 0) {
            mIsFavourite = true;
            mImgFavourite.setImageResource(android.R.drawable.btn_star_big_on);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovie != null) {
            Log.i(LOG_TAG, "Saving movie to saveInstanceState");
            outState.putParcelable(Utils.MOVIE_KEY, mMovie);
        }
        if (mTrailers.size() > 0) {
            outState.putParcelableArrayList(Utils.TRAILERS_KEY, mTrailers);
        }
        if (mReviews.size() > 0) {
            outState.putParcelableArrayList(Utils.REVIEWS_KEY, mReviews);
        }
    }

    public void showTrailers() {
        LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Trailer t : mTrailers) {
            View v = inflater.inflate(R.layout.trailer, null);

            // fill in any details dynamically here
            TextView textView = (TextView) v.findViewById(R.id.textTrailerName);
            textView.setText(t.getName());
            ImageView imgPlay = (ImageView) v.findViewById(R.id.imgPlay);
            imgPlay.setOnClickListener(new ClickPlayListener(t.getSource()));
            mLayoutTrailers.addView(v);
        }
        // Set the share intent if the shareactionprovider has been initialized

    }

    public void showReviews() {
        LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Review r : mReviews) {
            View v = inflater.inflate(R.layout.review, null);
            // fill in any details dynamically here
            TextView author = (TextView) v.findViewById(R.id.textAuthor);
            author.setText(r.getAuthor());
            TextView content = (TextView) v.findViewById(R.id.textContent);
            content.setText(r.getContent());
            mLayoutReviews.addView(v);
        }
    }

    /* This function handles the click on the Favorite button (star).
       It adds/deletes the movie to/from the database using content provider.
     */
    @Override
    public void onClick(View v) {
        mIsFavourite = !mIsFavourite;
        if (mIsFavourite) {
            if (addToFavourites() != -1) {
                Toast.makeText(this.getActivity(), "Added to favourites!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getActivity(), "There was an error while saving to favourites", Toast.LENGTH_SHORT).show();
            }
            mImgFavourite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            // delete
            deleteFromFavourites();
            mImgFavourite.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    private void deleteFromFavourites() {
        MovieSelection where = new MovieSelection();
        where.movieId(mMovie.getId());
        where.delete(mResolver);
        ReviewSelection whereReview = new ReviewSelection();
        whereReview.moviedbId(mMovie.getId());
        whereReview.delete(mResolver);
        TrailerSelection whereTrailer = new TrailerSelection();
        whereTrailer.moviedbId(mMovie.getId());
        whereTrailer.delete(mResolver);
    }

    private long addToFavourites() {
        MovieContentValues values = new MovieContentValues();
        values.putMovieId(mMovie.getId()).
                putTitle(mMovie.getTitle()).
                putPosterPath(mMovie.getPosterPath()).
                putReleaseDate(mMovie.getReleaseDate()).
                putRating(mMovie.getRating()).
                putSynopsis(mMovie.getSynopsis());

        Uri uri = mResolver.insert(MovieColumns.CONTENT_URI, values.values());
        int id = (int) ContentUris.parseId(uri);

        if (id < 0) {
            return id;
        }

        ContentValues[] contentValues = new ContentValues[mTrailers.size()];

        int i = 0;
        for (Trailer t : mTrailers) {
            contentValues[i] = new ContentValues();
            contentValues[i].put(TrailerColumns.MOVIEDB_ID, mId);
            contentValues[i].put(TrailerColumns.NAME, t.getName());
            contentValues[i++].put(TrailerColumns.SOURCE, t.getSource());
        }

        mResolver.bulkInsert(TrailerColumns.CONTENT_URI, contentValues);

        ContentValues[] contentValues1 = new ContentValues[mReviews.size()];

        i = 0;
        for (Review r : mReviews) {
            contentValues1[i] = new ContentValues();
            contentValues1[i].put(ReviewColumns.MOVIEDB_ID, mId);
            contentValues1[i].put(ReviewColumns.AUTHOR, r.getAuthor());
            contentValues1[i++].put(ReviewColumns.CONTENT, r.getContent());
        }
        mResolver.bulkInsert(ReviewColumns.CONTENT_URI, contentValues1);

        return id;
    }

    public class ResultsWrapper {
        ArrayList<Review> mReviews;
        ArrayList<Trailer> mTrailers;

        public ArrayList<Trailer> getTrailers() {
            return mTrailers;
        }

        public void setTrailers(ArrayList<Trailer> mTrailers) {
            this.mTrailers = mTrailers;
        }

        public ArrayList<Review> getReviews() {
            return mReviews;
        }

        public void setReviews(ArrayList<Review> mReviews) {
            this.mReviews = mReviews;
        }
    }

    private class DownloadDetailsTask extends AsyncTask<Void, Void, ResultsWrapper> {

        public static final String API_KEY = "";  // Insert your movie db api key here
        private static final String LOG_TAG = "DownloadDetailsTask";

        /* convert returned result into list of movies */
        private ResultsWrapper getResultsFromJson(String json) throws JSONException {
            //Log.i(LOG_TAG, "JSON: " + json);

            JSONObject objJson = new JSONObject(json);
            JSONObject objTrailers = objJson.getJSONObject("trailers");
            Log.i(LOG_TAG, "Trailers obj: " + objTrailers.toString());
            JSONArray youtubeArray = objTrailers.getJSONArray("youtube");

            ArrayList<Trailer> trailers = new ArrayList();
            for (int i = 0; i < youtubeArray.length(); i++) {
                JSONObject obj = youtubeArray.getJSONObject(i);
                trailers.add(new Trailer(obj.getString("name"),
                        obj.getString("source")));
            }

            JSONObject objReviews = objJson.getJSONObject("reviews");
            JSONArray reviewsArray = objReviews.getJSONArray("results");

            ArrayList<Review> reviews = new ArrayList();

            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject obj = reviewsArray.getJSONObject(i);
                reviews.add(new Review(obj.getString("author"),
                        obj.getString("content")));
            }

            ResultsWrapper result = new ResultsWrapper();
            result.setTrailers(trailers);
            result.setReviews(reviews);
            return result;
        }

        @Override
        protected ResultsWrapper doInBackground(Void... args) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;

            try {
                // Construct the URL for moviedb api
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie").appendPath(mId)
                        .appendQueryParameter("api_key", API_KEY)
                        .appendQueryParameter("append_to_response", "trailers%2Creviews");

                String strUrl = "http://api.themoviedb.org/3/movie/" +
                        mId + "?api_key=" + API_KEY + "&append_to_response="
                        + URLEncoder.encode("trailers,reviews", "UTF-8");

                //URL url = new URL(builder.build().toString());
                URL url = new URL(strUrl);

                Log.i(LOG_TAG, "URI: " + url.toString());
                // Create the request to moviedb api and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error getting movie details: ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

                try {
                    return getResultsFromJson(jsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ResultsWrapper result) {
            if (result != null) {
                mTrailers = result.getTrailers();
                mReviews = result.getReviews();

                if (mTrailers != null) {
                    showTrailers();
                }
                if (mReviews != null) {
                    showReviews();
                }
                if (mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(createShareMovieIntent());
                }
            }
        }
    }

    public class ClickPlayListener implements View.OnClickListener {
        String mSource;

        public ClickPlayListener(String source) {
            this.mSource = source;
        }

        @Override
        public void onClick(View v) {
            String link = BASE_YOUTUBE_URL + mSource;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        }
    }
}