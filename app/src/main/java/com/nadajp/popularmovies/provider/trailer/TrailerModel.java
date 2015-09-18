package com.nadajp.popularmovies.provider.trailer;

import android.support.annotation.Nullable;

import com.nadajp.popularmovies.provider.base.BaseModel;

/**
 * Data model for the {@code trailer} table.
 */
public interface TrailerModel extends BaseModel {

    /**
     * Get the {@code moviedb_id} value.
     * Can be {@code null}.
     */
    @Nullable
    String getMoviedbId();

    /**
     * Get the {@code name} value.
     * Can be {@code null}.
     */
    @Nullable
    String getName();

    /**
     * Get the {@code source} value.
     * Can be {@code null}.
     */
    @Nullable
    String getSource();
}
