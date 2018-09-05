package com.conext.conext.model.db;

/**
 * Created by Shriram on 6/28/2017.
 */

public class USER_EDUCATION {
    private String EducationKey;
    private String UserKey;
    private String YearStart;
    private String YearFinsih;
    private String Degree;
    private String FieldOfStudy;
    private String Grade;
    private String Activities;
    private String Description;
    private String DeleteFlag;
    private String CreatedBy;
    private String CreatedTs;
    private String ModifiedBy;
    private String ModifiedTs;

    public String getEducationKey() {
        return EducationKey;
    }

    public void setEducationKey(String educationKey) {
        EducationKey = educationKey;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    public String getYearStart() {
        return YearStart;
    }

    public void setYearStart(String yearStart) {
        YearStart = yearStart;
    }

    public String getYearFinsih() {
        return YearFinsih;
    }

    public void setYearFinsih(String yearFinsih) {
        YearFinsih = yearFinsih;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getFieldOfStudy() {
        return FieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        FieldOfStudy = fieldOfStudy;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getActivities() {
        return Activities;
    }

    public void setActivities(String activities) {
        Activities = activities;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDeleteFlag() {
        return DeleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        DeleteFlag = deleteFlag;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedTs() {
        return CreatedTs;
    }

    public void setCreatedTs(String createdTs) {
        CreatedTs = createdTs;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getModifiedTs() {
        return ModifiedTs;
    }

    public void setModifiedTs(String modifiedTs) {
        ModifiedTs = modifiedTs;
    }
}
