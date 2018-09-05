package com.conext.conext.model;

/**
 * Created by Shriram on 6/24/2017.
 */

public class NotificationUpcoming {
    private String imageNotify;
    private String dateNotify;
    private String monthNotify;
    private String meetNotify;
    private String timeNotify;
    private String friendsNotify;
    private String imageMoreNotify;

    public String getImageNotify() {
        return imageNotify;
    }

    public String getDateNotify() {
        return dateNotify;
    }

    public String getMonthNotify() {
        return monthNotify;
    }

    public String getMeetNotify() {
        return meetNotify;
    }

    public String getTimeNotify() {
        return timeNotify;
    }

    public String getFriendsNotify() {
        return friendsNotify;
    }

    public String getImageMoreNotify() {
        return imageMoreNotify;
    }

    public NotificationUpcoming(String imageNotify, String dateNotify, String monthNotify, String meetNotify, String timeNotify, String friendsNotify, String imageMoreNotify) {
        this.imageNotify = imageNotify;
        this.dateNotify = dateNotify;
        this.monthNotify = monthNotify;
        this.meetNotify = meetNotify;
        this.timeNotify = timeNotify;
        this.friendsNotify = friendsNotify;
        this.imageMoreNotify = imageMoreNotify;
    }

    public NotificationUpcoming() {
    }
}
