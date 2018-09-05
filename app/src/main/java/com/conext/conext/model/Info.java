package com.conext.conext.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashith VL on 6/6/2017.
 */

public class Info implements Parcelable {

    private String major,eduQualification,univName,year;

    public Info() {
    }

    public Info(String major, String eduQualification, String univName, String year) {
        this.major = major;
        this.eduQualification = eduQualification;
        this.univName = univName;
        this.year = year;
    }

    protected Info(Parcel in) {
        major = in.readString();
        eduQualification = in.readString();
        univName = in.readString();
        year = in.readString();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    public String getMajor() {
        return major;
    }

    public String getEduQualification() {
        return eduQualification;
    }

    public String getUnivName() {
        return univName;
    }

    public String getYear() {
        return year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(major);
        dest.writeString(eduQualification);
        dest.writeString(univName);
        dest.writeString(year);
    }
}
