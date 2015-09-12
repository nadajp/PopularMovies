package com.nadajp.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
public class MovieDetailFragment extends Fragment {

    public static final String LOG_TAG = "MovieDetailFragment";
    public static final String BASE_URL = "http://image.tmdb.org/t/p/w342/";

    // UI elements
    ImageView mImgPoster;
    TextView mTxtTitle;
    TextView mTxtReleaseDate;
    TextView mTxtSynopsis;
    TextView mTxtRating;
    ArrayList<Review> mReviews;
    ArrayList<Trailer> mTrailers;
    private String mId;
    private String mTitle;
    private String mReleaseDate;
    private String mSynopsis;
    private String mRating;
    private String mPosterPath;
    private Movie mMovie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mTrailers = new ArrayList<>();
            mReviews = new ArrayList<>();
        } else {
            mTrailers = savedInstanceState.getParcelableArrayList(Utils.TRAILERS_KEY);
            mReviews = savedInstanceState.getParcelableArrayList(Utils.REVIEWS_KEY);
        }
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

            mId = mMovie.getId();
            new DownloadDetailsTask().execute();

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
            outState.putParcelable(Utils.MOVIE_KEY, mMovie);
        }
    }

    public class ResultsWrapper {
        ArrayList<Review> mReviews;
        ArrayList<Trailer> mTrailers;

        public ArrayList<Trailer> getmTrailers() {
            return mTrailers;
        }

        public void setmTrailers(ArrayList<Trailer> mTrailers) {
            this.mTrailers = mTrailers;
        }

        public ArrayList<Review> getmReviews() {
            return mReviews;
        }

        public void setmReviews(ArrayList<Review> mReviews) {
            this.mReviews = mReviews;
        }
    }

    private class DownloadDetailsTask extends AsyncTask<Void, Void, ResultsWrapper> {

        public static final String API_KEY = "942a26a880fe217a46e11613b4ec7059";  // Insert your movie db api key here
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
            result.setmTrailers(trailers);
            result.setmReviews(reviews);
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
                mTrailers = result.getmTrailers();
                mReviews = result.getmReviews();
                //mAdapter.setMovies(movies);
                //mAdapter.notifyDataSetChanged();
            }
        }
    }
}