package com.conext.conext.model;

/**
 * Created by Shriram on 6/23/2017.
 */

public class UpcomingEvents {


    private String eventUpcomingName;
    private String eventUpcomingType;
    private String eventUpcomingLanguage;
    private String eventUpcomingDateStart;
    private String eventUpcomingMonthStart;
    private String eventUpcomingDateEnd;
    private String eventUpcomingMonthEnd;
    private String eventUpcomingAlarmNotify;
    private String eventUpcomingPlace;

    public UpcomingEvents() {
    }

    public UpcomingEvents(String eventUpcomingName, String eventUpcomingType, String eventUpcomingLanguage, String eventUpcomingDateStart, String eventUpcomingMonthStart, String eventUpcomingDateEnd, String eventUpcomingMonthEnd, String eventUpcomingAlarmNotify, String eventUpcomingPlace) {
        this.eventUpcomingName = eventUpcomingName;
        this.eventUpcomingType = eventUpcomingType;
        this.eventUpcomingLanguage = eventUpcomingLanguage;
        this.eventUpcomingDateStart = eventUpcomingDateStart;
        this.eventUpcomingMonthStart = eventUpcomingMonthStart;
        this.eventUpcomingDateEnd = eventUpcomingDateEnd;
        this.eventUpcomingMonthEnd = eventUpcomingMonthEnd;
        this.eventUpcomingAlarmNotify = eventUpcomingAlarmNotify;
        this.eventUpcomingPlace = eventUpcomingPlace;
    }

    public String getEventUpcomingName() {
        return eventUpcomingName;
    }

    public String getEventUpcomingType() {
        return eventUpcomingType;
    }

    public String getEventUpcomingLanguage() {
        return eventUpcomingLanguage;
    }

    public String getEventUpcomingDateStart() {
        return eventUpcomingDateStart;
    }

    public String getEventUpcomingMonthStart() {
        return eventUpcomingMonthStart;
    }

    public String getEventUpcomingDateEnd() {
        return eventUpcomingDateEnd;
    }

    public String getEventUpcomingMonthEnd() {
        return eventUpcomingMonthEnd;
    }

    public String getEventUpcomingAlarmNotify() {
        return eventUpcomingAlarmNotify;
    }

    public String getEventUpcomingPlace() {
        return eventUpcomingPlace;
    }
}
