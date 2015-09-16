package com.nadajp.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by nadajp on 7/14/15.
 * Movie object, used to pass movie data between fragments
 */
public class Movie implements Parcelable {

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String mId;
    private String mTitle;
    private String mPosterPath;
    private String mReleaseDate;
    private String mSynopsis;
    private String mRating;
    private ArrayList<Trailer> mTrailers = new ArrayList<Trailer>();
    private ArrayList<Review> mReviews = new ArrayList<Review>();

    public Movie(String id, String title, String posterPath, String releaseDate, String synopsis, String rating) {
        mId = id;
        mTitle = title;
        mPosterPath = posterPath;
        mReleaseDate = releaseDate;
        mSynopsis = synopsis;
        mRating = rating;
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.mId = in.readString();
        this.mTitle = in.readString();
        this.mPosterPath = in.readString();
        this.mReleaseDate = in.readString();
        this.mSynopsis = in.readString();
        this.mRating = in.readString();
        this.mTrailers = in.createTypedArrayList(Trailer.CREATOR);
        this.mReviews = in.createTypedArrayList(Review.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mPosterPath);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mSynopsis);
        dest.writeString(this.mRating);
        dest.writeTypedList(mTrailers);
        dest.writeTypedList(mReviews);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String path) {
        mPosterPath = path;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String date) {
        mReleaseDate = date;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String synopsis) {
        mSynopsis = synopsis;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        mRating = rating;
    }

    public ArrayList<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        mTrailers = trailers;
    }

    public ArrayList<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        mReviews = reviews;
    }
}
