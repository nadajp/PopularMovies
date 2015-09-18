package com.nadajp.popularmovies.provider.movie;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.nadajp.popularmovies.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code movie} table.
 */
public class MovieContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return MovieColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where           The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable MovieSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where           The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable MovieSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public MovieContentValues putMovieId(@Nullable String value) {
        mContentValues.put(MovieColumns.MOVIE_ID, value);
        return this;
    }

    public MovieContentValues putMovieIdNull() {
        mContentValues.putNull(MovieColumns.MOVIE_ID);
        return this;
    }

    public MovieContentValues putTitle(@Nullable String value) {
        mContentValues.put(MovieColumns.TITLE, value);
        return this;
    }

    public MovieContentValues putTitleNull() {
        mContentValues.putNull(MovieColumns.TITLE);
        return this;
    }

    public MovieContentValues putPosterPath(@Nullable String value) {
        mContentValues.put(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public MovieContentValues putPosterPathNull() {
        mContentValues.putNull(MovieColumns.POSTER_PATH);
        return this;
    }

    public MovieContentValues putReleaseDate(@Nullable String value) {
        mContentValues.put(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public MovieContentValues putReleaseDateNull() {
        mContentValues.putNull(MovieColumns.RELEASE_DATE);
        return this;
    }

    public MovieContentValues putSynopsis(@Nullable String value) {
        mContentValues.put(MovieColumns.SYNOPSIS, value);
        return this;
    }

    public MovieContentValues putSynopsisNull() {
        mContentValues.putNull(MovieColumns.SYNOPSIS);
        return this;
    }

    public MovieContentValues putRating(@Nullable String value) {
        mContentValues.put(MovieColumns.RATING, value);
        return this;
    }

    public MovieContentValues putRatingNull() {
        mContentValues.putNull(MovieColumns.RATING);
        return this;
    }
}
