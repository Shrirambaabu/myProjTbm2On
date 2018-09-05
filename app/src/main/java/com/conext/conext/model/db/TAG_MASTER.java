package com.conext.conext.model.db;

/**
 * Created by Shriram on 6/28/2017.
 */

public class TAG_MASTER {

    private String TagKey;
    private String TagName;
    private String TagDescription;
    private String CreatedBy;
    private String CreatedTs;
    private String ModifiedBy;
    private String ModifiedTs;

    public String getTagKey() {
        return TagKey;
    }

    public void setTagKey(String tagKey) {
        TagKey = tagKey;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String tagName) {
        TagName = tagName;
    }

    public String getTagDescription() {
        return TagDescription;
    }

    public void setTagDescription(String tagDescription) {
        TagDescription = tagDescription;
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
