package com.conext.conext.model.db;

/**
 * Created by Shriram on 6/28/2017.
 */

public class EVENT_TYPE_MASTER {
    private String EventTypeKey;
    private String Name;
    private String DefaultOC;
    private String DefaultImage;
    private String DeleteFlag;
    private String CreatedBy;
    private String CreatedTs;
    private String ModifiedBy;
    private String ModifiedTs;

    public String getEventTypeKey() {
        return EventTypeKey;
    }

    public void setEventTypeKey(String eventTypeKey) {
        EventTypeKey = eventTypeKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDefaultOC() {
        return DefaultOC;
    }

    public void setDefaultOC(String defaultOC) {
        DefaultOC = defaultOC;
    }

    public String getDefaultImage() {
        return DefaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        DefaultImage = defaultImage;
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
