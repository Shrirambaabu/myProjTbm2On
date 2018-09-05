package com.conext.conext.model.db;

/**
 * Created by Shriram on 6/28/2017.
 */

public class USER_TAG {
    private String UserKey;
    private String TagKey;
    private String MentorFlag;
    private String MenteeFlag;
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

    public String getTagKey() {
        return TagKey;
    }

    public void setTagKey(String tagKey) {
        TagKey = tagKey;
    }

    public String getMentorFlag() {
        return MentorFlag;
    }

    public void setMentorFlag(String mentorFlag) {
        MentorFlag = mentorFlag;
    }

    public String getMenteeFlag() {
        return MenteeFlag;
    }

    public void setMenteeFlag(String menteeFlag) {
        MenteeFlag = menteeFlag;
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
