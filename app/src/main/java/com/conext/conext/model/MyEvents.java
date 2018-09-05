package com.conext.conext.model;

/**
 * Created by Shriram on 6/23/2017.
 */

public class MyEvents {
    private String eventMyName;
    private String eventMyType;
    private String eventMyDateStart;
    private String eventMyMonthStart;
    private String eventMyAlarmNotify;
    private String eventMyPlace;
    private String eventMyLanguage;
    private String eventMyDateEnd;
    private String eventMyMonthEnd;

    public String getEventMyName() {
        return eventMyName;
    }

    public String getEventMyType() {
        return eventMyType;
    }

    public String getEventMyDateStart() {
        return eventMyDateStart;
    }

    public String getEventMyMonthStart() {
        return eventMyMonthStart;
    }

    public String getEventMyAlarmNotify() {
        return eventMyAlarmNotify;
    }

    public String getEventMyPlace() {
        return eventMyPlace;
    }

    public String getEventMyLanguage() {
        return eventMyLanguage;
    }

    public String getEventMyDateEnd() {
        return eventMyDateEnd;
    }

    public String getEventMyMonthEnd() {
        return eventMyMonthEnd;
    }

    public MyEvents(String eventMyName, String eventMyType, String eventMyLanguage,String eventMyDateStart,String eventMyMonthStart,String eventMyDateEnd, String eventMyMonthEnd,  String eventMyAlarmNotify, String eventMyPlace ) {
        this.eventMyName = eventMyName;
        this.eventMyType = eventMyType;
        this.eventMyDateStart = eventMyDateStart;
        this.eventMyMonthStart = eventMyMonthStart;
        this.eventMyAlarmNotify = eventMyAlarmNotify;
        this.eventMyPlace = eventMyPlace;
        this.eventMyLanguage = eventMyLanguage;
        this.eventMyDateEnd = eventMyDateEnd;
        this.eventMyMonthEnd = eventMyMonthEnd;
    }

    public MyEvents() {

    }
}
