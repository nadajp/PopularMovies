package com.nadajp.popularmovies.provider.movie;

import android.support.annotation.Nullable;

import com.nadajp.popularmovies.provider.base.BaseModel;

/**
 * Data model for the {@code movie} table.
 */
public interface MovieModel extends BaseModel {

    /**
     * Get the {@code movie_id} value.
     * Can be {@code null}.
     */
    @Nullable
    String getMovieId();

    /**
     * Get the {@code title} value.
     * Can be {@code null}.
     */
    @Nullable
    String getTitle();

    /**
     * Get the {@code poster_path} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPosterPath();

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    String getReleaseDate();

    /**
     * Get the {@code synopsis} value.
     * Can be {@code null}.
     */
    @Nullable
    String getSynopsis();

    /**
     * Get the {@code rating} value.
     * Can be {@code null}.
     */
    @Nullable
    String getRating();
}
