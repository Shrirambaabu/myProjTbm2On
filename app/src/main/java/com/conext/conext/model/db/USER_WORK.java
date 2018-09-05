package com.conext.conext.model.db;

/**
 * Created by Shriram on 6/28/2017.
 */

public class USER_WORK {
    private String UserKey;
    private String WorkKey;
    private String WorkTitle;
    private String LocationKey;
    private String YearStart;
    private String YearFinish;
    private String Description;
    private String DeleteFlag;
    private String CreatedBy;
    private String CreatedTs;
    private String ModifiedBy;
    private String ModifiedTs;

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    public String getWorkKey() {
        return WorkKey;
    }

    public void setWorkKey(String workKey) {
        WorkKey = workKey;
    }

    public String getWorkTitle() {
        return WorkTitle;
    }

    public void setWorkTitle(String workTitle) {
        WorkTitle = workTitle;
    }

    public String getLocationKey() {
        return LocationKey;
    }

    public void setLocationKey(String locationKey) {
        LocationKey = locationKey;
    }

    public String getYearStart() {
        return YearStart;
    }

    public void setYearStart(String yearStart) {
        YearStart = yearStart;
    }

    public String getYearFinish() {
        return YearFinish;
    }

    public void setYearFinish(String yearFinish) {
        YearFinish = yearFinish;
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
