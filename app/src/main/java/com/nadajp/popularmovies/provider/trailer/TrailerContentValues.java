package com.nadajp.popularmovies.provider.trailer;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.nadajp.popularmovies.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code trailer} table.
 */
public class TrailerContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return TrailerColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where           The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable TrailerSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where           The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable TrailerSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public TrailerContentValues putMoviedbId(@Nullable String value) {
        mContentValues.put(TrailerColumns.MOVIEDB_ID, value);
        return this;
    }

    public TrailerContentValues putMoviedbIdNull() {
        mContentValues.putNull(TrailerColumns.MOVIEDB_ID);
        return this;
    }

    public TrailerContentValues putName(@Nullable String value) {
        mContentValues.put(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerContentValues putNameNull() {
        mContentValues.putNull(TrailerColumns.NAME);
        return this;
    }

    public TrailerContentValues putSource(@Nullable String value) {
        mContentValues.put(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerContentValues putSourceNull() {
        mContentValues.putNull(TrailerColumns.SOURCE);
        return this;
    }
}
