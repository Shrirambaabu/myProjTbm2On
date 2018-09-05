package com.conext.conext.model;

import android.graphics.Bitmap;

/**
 * Created by Shriram on 6/24/2017.
 */

public class OtherNotification {

    private String notifyName;
    private String notifyTime;
    private String mentorMentee;
    private String eventId;
    private String eventType;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getMentorMentee() {
        return mentorMentee;
    }

    public void setMentorMentee(String mentorMentee) {
        this.mentorMentee = mentorMentee;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getNotifyName() {
        return notifyName;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public String getWantsMentor() {
        return mentorMentee;
    }

    public OtherNotification() {
    }
}
