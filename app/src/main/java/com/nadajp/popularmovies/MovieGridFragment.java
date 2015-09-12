package com.nadajp.popularmovies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.nadajp.popularmovies.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nadajp on 7/13/15.
 * Main fragment, which displays the movie image grid
 */
public class MovieGridFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String LOG_TAG = "MovieGridFragment";
    public ImageAdapter mAdapter;      // Custom adapter for loading poster images into grid view
    public int mSortType;              // SORT_POPULAR = 0, SORT_RATING = 1
    ArrayList<Movie> mMovies = null;   // List to store all downloaded movie data
    GridView mGrid;                    // Grid view for displaying movie posters
    LinearLayout mLayoutNoNetwork;     // Layout that is displayed if there is no available network
    Button mBtnRefresh;                // Button that the user can click to refresh data after
    // network was unavailable
    boolean mbHasNetwork;              // is network available

    public MovieGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSortType = Utils.getSortType(this.getActivity(), 0);
        if (savedInstanceState == null || !savedInstanceState.containsKey(Utils.MOVIES_KEY)) {
            mMovies = new ArrayList<>();
        } else {
            mMovies = savedInstanceState.getParcelableArrayList(Utils.MOVIES_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mLayoutNoNetwork = (LinearLayout) rootView.findViewById(R.id.layoutNoNetwork);
        mBtnRefresh = (Button) rootView.findViewById(R.id.btnRefresh);
        mGrid = (GridView) rootView.findViewById(R.id.gridView);
        mAdapter = new ImageAdapter(this.getActivity(), mMovies);
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(this);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });
        if (savedInstanceState == null) {
            refreshData();
        } else {
            mbHasNetwork = savedInstanceState.getBoolean(Utils.HAS_NETWORK);
            if (mbHasNetwork) {
                mLayoutNoNetwork.setVisibility(View.GONE);
            } else {
                mLayoutNoNetwork.setVisibility(View.VISIBLE);
            }
        }
        return rootView;
    }

    public void refreshData() {
        if (isNetworkAvailable()) {
            Log.i(LOG_TAG, "Network available, downloading movies...");
            mLayoutNoNetwork.setVisibility(View.GONE);
            mbHasNetwork = true;
            new DownloadMoviesTask().execute();
        } else {
            Log.i(LOG_TAG, "Network NOT available, display alert...");
            displayNoNetworkAlert(this.getActivity());
            //mGrid.setVisibility(View.INVISIBLE);
            mbHasNetwork = false;
            mLayoutNoNetwork.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(getTitle());
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem sort = menu.findItem(R.id.action_sort);
        sort.setTitle(getSettingsText());
    }

    private String getSettingsText() {
        return mSortType == 0 ? getString(R.string.action_sort_rating)
                : getString(R.string.action_sort_popular);
    }

    private String getTitle() {
        return mSortType == 0 ? getString(R.string.title_most_popular)
                : getString(R.string.title_top_rated);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            mSortType = mSortType == 0 ? 1 : 0;
            item.setTitle(getSettingsText());
            ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            ab.setTitle(getTitle());
            refreshData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayNoNetworkAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Network Unavailable.");
        builder.setCancelable(true);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getActivity(), MovieDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Utils.MOVIE_KEY, mMovies.get(position));
        intent.putExtra(Utils.MOVIE_KEY, extras);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        Utils.setSortType(this.getActivity(), mSortType);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovies != null && mMovies.size() > 0) {
            outState.putParcelableArrayList(Utils.MOVIES_KEY, mMovies);
        }
        outState.putBoolean(Utils.HAS_NETWORK, mbHasNetwork);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }

    private class DownloadMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        public static final String API_KEY = "942a26a880fe217a46e11613b4ec7059";  // Insert your movie db api key here
        private static final String LOG_TAG = "DownloadMoviesTask";

        /* convert returned result into list of movies */
        private ArrayList<Movie> getResultsFromJson(String json) throws JSONException {
            //Log.i(LOG_TAG, "JSON: " + json);

            JSONObject urlJson = new JSONObject(json);
            JSONArray resultsArray = urlJson.getJSONArray("results");

            final String ID = "id";
            final String TITLE = "original_title";
            final String POSTER = "poster_path";
            final String RELEASE_DATE = "release_date";
            final String SYNOPSIS = "overview";
            final String RATING = "vote_average";

            ArrayList<Movie> movies = new ArrayList();

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject obj = resultsArray.getJSONObject(i);
                movies.add(new Movie(obj.getString(ID),
                        obj.getString(TITLE),
                        obj.getString(POSTER),
                        obj.getString(RELEASE_DATE),
                        obj.getString(SYNOPSIS),
                        obj.getString(RATING)));
            }

            return movies;
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... args) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;

            try {
                // Construct the URL for moviedb api
                URL url;
                if (mSortType == 0) {
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="
                            + API_KEY);
                } else {
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key="
                            + API_KEY);
                }

                //Log.i(LOG_TAG, "URI: " + url.toString());
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
                Log.e(LOG_TAG, "Error getting movie data: ", e);
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

        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                mMovies = movies;
                mAdapter.setMovies(movies);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}