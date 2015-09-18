package com.nadajp.popularmovies.provider.review;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.nadajp.popularmovies.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code review} table.
 */
public class ReviewCursor extends AbstractCursor implements ReviewModel {
    public ReviewCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(ReviewColumns._ID);
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
        String res = getStringOrNull(ReviewColumns.MOVIEDB_ID);
        return res;
    }

    /**
     * Get the {@code author} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getAuthor() {
        String res = getStringOrNull(ReviewColumns.AUTHOR);
        return res;
    }

    /**
     * Get the {@code content} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getContent() {
        String res = getStringOrNull(ReviewColumns.CONTENT);
        return res;
    }
}
