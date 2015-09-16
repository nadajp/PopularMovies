package com.nadajp.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nadajp on 9/11/15.
 */
public class Review implements Parcelable {
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    private String mAuthor;
    private String mContent;

    public Review() {
    }

    public Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    protected Review(Parcel in) {
        this.mAuthor = in.readString();
        this.mContent = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAuthor);
        dest.writeString(this.mContent);
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }
}
