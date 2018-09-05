package com.conext.conext.model.db;

/**
 * Created by Shriram on 6/28/2017.
 */

public class LOCATION {
    private String LocationKey;
    private String Type;
    private String Lattitude;
    private String Longitude;
    private String DeleteFlag;
    private String CreatedBy;
    private String CreatedTs;
    private String ModifiedBy;
    private String ModifiedTs;

    public String getLocationKey() {
        return LocationKey;
    }

    public void setLocationKey(String locationKey) {
        LocationKey = locationKey;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
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
