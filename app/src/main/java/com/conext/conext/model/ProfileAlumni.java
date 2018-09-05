package com.conext.conext.model;

/**
 * Created by Shriram on 6/26/2017.
 */

public class ProfileAlumni {
    private String alumniUniversity;
    private String alumniStudy;
    private String alumniYear;

    public String getAlumniUniversity() {
        return alumniUniversity;
    }

    public String getAlumniStudy() {
        return alumniStudy;
    }

    public String getAlumniYear() {
        return alumniYear;
    }

    public void setAlumniUniversity(String alumniUniversity) {
        this.alumniUniversity = alumniUniversity;
    }

    public void setAlumniStudy(String alumniStudy) {
        this.alumniStudy = alumniStudy;
    }

    public void setAlumniYear(String alumniYear) {
        this.alumniYear = alumniYear;
    }

    public ProfileAlumni(String alumniUniversity, String alumniStudy, String alumniYear) {
        this.alumniUniversity = alumniUniversity;
        this.alumniStudy = alumniStudy;
        this.alumniYear = alumniYear;
    }

    public ProfileAlumni() {
    }
}
