package com.conext.conext.model.db;

/**
 * Created by Ashith VL on 6/27/2017.
 */

public class USER_PROFILE {
    String UserKey = "";
    String EmailID = "";
    String Password = "";
    String FirstName = "";
    String LastName = "";
    String TitleDisplay = "";
    String CompanyDisplay = "";
    String ProfilePic = "";
    String DeleteFlg = "";
    String CreatedBy = "";
    String CreatedTs = "";
    String ModifiedBy = "";
    String ModifiedTs = "";
    String LocationKey = "";

    public USER_PROFILE() {
    }

    public USER_PROFILE(String emailID, String password, String modifiedTs) {
        EmailID = emailID;
        Password = password;
        ModifiedTs = modifiedTs;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getTitleDisplay() {
        return TitleDisplay;
    }

    public void setTitleDisplay(String titleDisplay) {
        TitleDisplay = titleDisplay;
    }

    public String getCompanyDisplay() {
        return CompanyDisplay;
    }

    public void setCompanyDisplay(String companyDisplay) {
        CompanyDisplay = companyDisplay;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public String getDeleteFlg() {
        return DeleteFlg;
    }

    public void setDeleteFlg(String deleteFlg) {
        DeleteFlg = deleteFlg;
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

    public String getLocationKey() {
        return LocationKey;
    }

    public void setLocationKey(String locationKey) {
        LocationKey = locationKey;
    }
}
