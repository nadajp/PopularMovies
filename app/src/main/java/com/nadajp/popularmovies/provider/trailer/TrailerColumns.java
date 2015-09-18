package com.nadajp.popularmovies.provider.trailer;

import android.net.Uri;
import android.provider.BaseColumns;

import com.nadajp.popularmovies.provider.MoviesProvider;

/**
 * Columns for the {@code trailer} table.
 */
public class TrailerColumns implements BaseColumns {
    public static final String TABLE_NAME = "trailer";
    public static final Uri CONTENT_URI = Uri.parse(MoviesProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String MOVIEDB_ID = "movieDb_id";

    public static final String NAME = "name";

    public static final String SOURCE = "source";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." + _ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[]{
            _ID,
            MOVIEDB_ID,
            NAME,
            SOURCE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MOVIEDB_ID) || c.contains("." + MOVIEDB_ID)) return true;
            if (c.equals(NAME) || c.contains("." + NAME)) return true;
            if (c.equals(SOURCE) || c.contains("." + SOURCE)) return true;
        }
        return false;
    }

}
