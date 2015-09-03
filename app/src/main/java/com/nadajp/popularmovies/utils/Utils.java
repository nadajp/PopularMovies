package com.nadajp.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nadajp on 7/30/15.
 * Utility class for using SharedPreferences
 */
public class Utils {

    public static final String MOVIE_KEY = "movie";
    public static final String MOVIES_KEY = "movies";
    public static final String SHARED_PREFS_FILENAME = "com.nadajp.popularmovies.shared_prefs";
    public static final String SORT_TYPE = "sort_type";
    public static final int SORT_POPULAR = 0;
    public static final int SORT_RATING = 1;


    public static void setSortType(Context context, int type) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(SORT_TYPE, type);
        editor.commit();
    }

    public static int getSortType(Context context, int defaultSort) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        return sharedPrefs.getInt(SORT_TYPE, defaultSort);
    }
}
