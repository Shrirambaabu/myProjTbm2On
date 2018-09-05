package com.conext.conext.model;

import com.google.gson.JsonObject;

/**
 * Created by sunil on 27-03-2016.
 */
public class Profile {

    private String firstName;
    private String headline;
    private String id;
    private String lastName;
    private JsonObject siteStandardProfileRequest;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public JsonObject getSiteStandardProfileRequest() {
        return siteStandardProfileRequest;
    }

    public void setSiteStandardProfileRequest(JsonObject siteStandardProfileRequest) {
        this.siteStandardProfileRequest = siteStandardProfileRequest;
    }
}