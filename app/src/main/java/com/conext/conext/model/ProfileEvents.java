package com.conext.conext.model;

/**
 * Created by Shriram on 6/26/2017.
 */

public class ProfileEvents {
    private String profileEventName;
    private String profileEventType;
    private String profileEventLanguage;
    private String profileEventDateStart;
    private String profileEventMonthStart;
    private String profileEventHiphen;
    private String profileEventDateEnd;
    private String profileEventMonthEnd;
    private String profileEventImage;

    public String getProfileEventName() {
        return profileEventName;
    }

    public String getProfileEventType() {
        return profileEventType;
    }

    public String getProfileEventLanguage() {
        return profileEventLanguage;
    }

    public String getProfileEventDateStart() {
        return profileEventDateStart;
    }

    public String getProfileEventMonthStart() {
        return profileEventMonthStart;
    }

    public String getProfileEventHiphen() {
        return profileEventHiphen;
    }

    public String getProfileEventDateEnd() {
        return profileEventDateEnd;
    }

    public String getProfileEventMonthEnd() {
        return profileEventMonthEnd;
    }

    public String getProfileEventImage() {
        return profileEventImage;
    }

    public ProfileEvents(String profileEventName, String profileEventType, String profileEventLanguage, String profileEventDateStart, String profileEventMonthStart, String profileEventHiphen, String profileEventDateEnd, String profileEventMonthEnd, String profileEventImage) {
        this.profileEventName = profileEventName;
        this.profileEventType = profileEventType;
        this.profileEventLanguage = profileEventLanguage;
        this.profileEventDateStart = profileEventDateStart;
        this.profileEventMonthStart = profileEventMonthStart;
        this.profileEventHiphen = profileEventHiphen;
        this.profileEventDateEnd = profileEventDateEnd;
        this.profileEventMonthEnd = profileEventMonthEnd;
        this.profileEventImage = profileEventImage;
    }

    public ProfileEvents() {
    }
}
