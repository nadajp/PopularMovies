package com.nadajp.popularmovies.provider.trailer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.nadajp.popularmovies.provider.base.AbstractSelection;

/**
 * Selection for the {@code trailer} table.
 */
public class TrailerSelection extends AbstractSelection<TrailerSelection> {
    @Override
    protected Uri baseUri() {
        return TrailerColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection      A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TrailerCursor} object, which is positioned before the first entry, or null.
     */
    public TrailerCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TrailerCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public TrailerCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context    The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TrailerCursor} object, which is positioned before the first entry, or null.
     */
    public TrailerCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TrailerCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public TrailerCursor query(Context context) {
        return query(context, null);
    }


    public TrailerSelection id(long... value) {
        addEquals("trailer." + TrailerColumns._ID, toObjectArray(value));
        return this;
    }

    public TrailerSelection idNot(long... value) {
        addNotEquals("trailer." + TrailerColumns._ID, toObjectArray(value));
        return this;
    }

    public TrailerSelection orderById(boolean desc) {
        orderBy("trailer." + TrailerColumns._ID, desc);
        return this;
    }

    public TrailerSelection orderById() {
        return orderById(false);
    }

    public TrailerSelection moviedbId(String... value) {
        addEquals(TrailerColumns.MOVIEDB_ID, value);
        return this;
    }

    public TrailerSelection moviedbIdNot(String... value) {
        addNotEquals(TrailerColumns.MOVIEDB_ID, value);
        return this;
    }

    public TrailerSelection moviedbIdLike(String... value) {
        addLike(TrailerColumns.MOVIEDB_ID, value);
        return this;
    }

    public TrailerSelection moviedbIdContains(String... value) {
        addContains(TrailerColumns.MOVIEDB_ID, value);
        return this;
    }

    public TrailerSelection moviedbIdStartsWith(String... value) {
        addStartsWith(TrailerColumns.MOVIEDB_ID, value);
        return this;
    }

    public TrailerSelection moviedbIdEndsWith(String... value) {
        addEndsWith(TrailerColumns.MOVIEDB_ID, value);
        return this;
    }

    public TrailerSelection orderByMoviedbId(boolean desc) {
        orderBy(TrailerColumns.MOVIEDB_ID, desc);
        return this;
    }

    public TrailerSelection orderByMoviedbId() {
        orderBy(TrailerColumns.MOVIEDB_ID, false);
        return this;
    }

    public TrailerSelection name(String... value) {
        addEquals(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameNot(String... value) {
        addNotEquals(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameLike(String... value) {
        addLike(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameContains(String... value) {
        addContains(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameStartsWith(String... value) {
        addStartsWith(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameEndsWith(String... value) {
        addEndsWith(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection orderByName(boolean desc) {
        orderBy(TrailerColumns.NAME, desc);
        return this;
    }

    public TrailerSelection orderByName() {
        orderBy(TrailerColumns.NAME, false);
        return this;
    }

    public TrailerSelection source(String... value) {
        addEquals(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceNot(String... value) {
        addNotEquals(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceLike(String... value) {
        addLike(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceContains(String... value) {
        addContains(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceStartsWith(String... value) {
        addStartsWith(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceEndsWith(String... value) {
        addEndsWith(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection orderBySource(boolean desc) {
        orderBy(TrailerColumns.SOURCE, desc);
        return this;
    }

    public TrailerSelection orderBySource() {
        orderBy(TrailerColumns.SOURCE, false);
        return this;
    }
}
