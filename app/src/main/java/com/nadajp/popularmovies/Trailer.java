package com.nadajp.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nadajp on 9/11/15.
 */
public class Trailer implements Parcelable {
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    private String mName;
    private String mSource;

    public Trailer() {
    }

    public Trailer(String name, String source) {
        mName = name;
        mSource = source;
    }

    protected Trailer(Parcel in) {
        this.mName = in.readString();
        this.mSource = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mSource);
    }

    public String getName() {
        return mName;
    }

    public String getSource() {
        return mSource;
    }
}
