package com.conext.conext.model.db;

import com.conext.conext.model.Contact;

/**
 * Created by Shriram on 6/28/2017.
 */

public class USER_EVENT {
    private String EventKey;
    private String UserKey;
    private String EventTypeKey;
    private String TagKey;
    private String LocationKey;
    private String Description;
    private String eventDate;
    private String noOfPeople;
    private String EventTitle;
    private String EventStartDate;

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    private String choice;

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    private String notifyTime;

    public String getEventMonthStartDate() {
        return EventMonthStartDate;
    }

    public void setEventMonthStartDate(String eventMonthStartDate) {
        EventMonthStartDate = eventMonthStartDate;
    }

    private String EventMonthStartDate;
    private String EventEndDate;

    private String EventStartTs;
    private String EventEndTs;

    private String Address;
    private boolean isOpen;
    private String ImageKey;
    private String DeleteFlag;
    private String CreatedBy;
    private String CreatedTs;
    private String ModifiedBy;
    private String ModifiedTs;
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventStartDate() {
        return EventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        EventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return EventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        EventEndDate = eventEndDate;
    }
    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public USER_EVENT() {

    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }
    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    public String getEventTypeKey() {
        return EventTypeKey;
    }

    public void setEventTypeKey(String eventTypeKey) {
        EventTypeKey = eventTypeKey;
    }

    public String getTagKey() {
        return TagKey;
    }

    public void setTagKey(String tagKey) {
        TagKey = tagKey;
    }

    public String getLocationKey() {
        return LocationKey;
    }

    public void setLocationKey(String locationKey) {
        LocationKey = locationKey;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEventStartTs() {
        return EventStartTs;
    }

    public void setEventStartTs(String eventStartTs) {
        EventStartTs = eventStartTs;
    }

    public String getEventEndTs() {
        return EventEndTs;
    }

    public void setEventEndTs(String eventEndTs) {
        EventEndTs = eventEndTs;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getImageKey() {
        return ImageKey;
    }

    public void setImageKey(String imageKey) {
        ImageKey = imageKey;
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
