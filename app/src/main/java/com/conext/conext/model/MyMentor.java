package com.conext.conext.model;

import android.graphics.Bitmap;

/**
 * Created by Shriram on 6/23/2017.
 */

public class MyMentor {
    private String mentorName;
    private String mentorID;
    private String mentorDescription;
    private String mentorIn;
    private String otherMentoring;
    private String mentorImage;

    public MyMentor() {
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public void setMentorDescription(String mentorDescription) {
        this.mentorDescription = mentorDescription;
    }

    public void setMentorIn(String mentorIn) {
        this.mentorIn = mentorIn;
    }

    public void setOtherMentoring(String otherMentoring) {
        this.otherMentoring = otherMentoring;
    }

    public void setMentorImage(String mentorImage) {
        this.mentorImage = mentorImage;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getMentorDescription() {
        return mentorDescription;
    }

    public String getMentorIn() {
        return mentorIn;
    }

    public String getOtherMentoring() {
        return otherMentoring;
    }

    public String getMentorImage() {
        return mentorImage;
    }

    public MyMentor(String mentorName, String mentorDescription, String mentorIn, String otherMentoring, String mentorImage) {
        this.mentorName = mentorName;
        this.mentorDescription = mentorDescription;
        this.mentorIn = mentorIn;
        this.otherMentoring = otherMentoring;
        this.mentorImage=mentorImage;
    }

    public String getMentorID() {
        return mentorID;
    }

    public void setMentorID(String mentorID) {
        this.mentorID = mentorID;
    }
}
