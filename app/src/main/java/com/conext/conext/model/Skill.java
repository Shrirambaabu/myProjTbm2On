package com.conext.conext.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ashith VL on 6/5/2017.
 */

public class Skill implements Serializable, Parcelable {

    private int icon;
    private String title;
    private boolean selected;
    private long skillId;

    public Skill() {

    }

    public Skill(String title) {
        this.title = title;
    }

    public Skill(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public Skill(String title, boolean selected) {
        this.title = title;
        this.selected = selected;
    }

    public Skill(int icon, String title, boolean selected) {
        this.icon = icon;
        this.title = title;
        this.selected = selected;
    }

    public Skill(int icon, String title, boolean selected, long skillId) {
        this.icon = icon;
        this.title = title;
        this.selected = selected;
        this.skillId = skillId;
    }

    protected Skill(Parcel in) {
        icon = in.readInt();
        title = in.readString();
        selected = in.readByte() != 0;
        skillId = in.readLong();
    }

    public static final Creator<Skill> CREATOR = new Creator<Skill>() {
        @Override
        public Skill createFromParcel(Parcel in) {
            return new Skill(in);
        }

        @Override
        public Skill[] newArray(int size) {
            return new Skill[size];
        }
    };

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }

    public long getSkillId() {
        return skillId;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(title);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeLong(skillId);
    }
}
