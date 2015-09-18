package com.nadajp.popularmovies.provider.trailer;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.nadajp.popularmovies.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code trailer} table.
 */
public class TrailerCursor extends AbstractCursor implements TrailerModel {
    public TrailerCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(TrailerColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code moviedb_id} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getMoviedbId() {
        String res = getStringOrNull(TrailerColumns.MOVIEDB_ID);
        return res;
    }

    /**
     * Get the {@code name} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getName() {
        String res = getStringOrNull(TrailerColumns.NAME);
        return res;
    }

    /**
     * Get the {@code source} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getSource() {
        String res = getStringOrNull(TrailerColumns.SOURCE);
        return res;
    }
}
