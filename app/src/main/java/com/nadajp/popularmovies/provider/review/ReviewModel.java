package com.nadajp.popularmovies.provider.review;

import android.support.annotation.Nullable;

import com.nadajp.popularmovies.provider.base.BaseModel;

/**
 * Data model for the {@code review} table.
 */
public interface ReviewModel extends BaseModel {

    /**
     * Get the {@code moviedb_id} value.
     * Can be {@code null}.
     */
    @Nullable
    String getMoviedbId();

    /**
     * Get the {@code author} value.
     * Can be {@code null}.
     */
    @Nullable
    String getAuthor();

    /**
     * Get the {@code content} value.
     * Can be {@code null}.
     */
    @Nullable
    String getContent();
}
