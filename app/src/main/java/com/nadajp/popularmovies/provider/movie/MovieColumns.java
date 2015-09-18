package com.nadajp.popularmovies.provider.movie;

import android.net.Uri;
import android.provider.BaseColumns;

import com.nadajp.popularmovies.provider.MoviesProvider;

/**
 * Columns for the {@code movie} table.
 */
public class MovieColumns implements BaseColumns {
    public static final String TABLE_NAME = "movie";
    public static final Uri CONTENT_URI = Uri.parse(MoviesProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String MOVIE_ID = "movie_id";

    public static final String TITLE = "title";

    public static final String POSTER_PATH = "poster_path";

    public static final String RELEASE_DATE = "release_date";

    public static final String SYNOPSIS = "synopsis";

    public static final String RATING = "rating";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." + _ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[]{
            _ID,
            MOVIE_ID,
            TITLE,
            POSTER_PATH,
            RELEASE_DATE,
            SYNOPSIS,
            RATING
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MOVIE_ID) || c.contains("." + MOVIE_ID)) return true;
            if (c.equals(TITLE) || c.contains("." + TITLE)) return true;
            if (c.equals(POSTER_PATH) || c.contains("." + POSTER_PATH)) return true;
            if (c.equals(RELEASE_DATE) || c.contains("." + RELEASE_DATE)) return true;
            if (c.equals(SYNOPSIS) || c.contains("." + SYNOPSIS)) return true;
            if (c.equals(RATING) || c.contains("." + RATING)) return true;
        }
        return false;
    }

}
