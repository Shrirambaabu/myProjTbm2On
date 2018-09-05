package com.conext.conext.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashith VL on 7/2/2017.
 */

public class AllSkill implements Parcelable {

    private int icon;
    private String title;
    private boolean mentee;
    private boolean mentor;
    private long skillId;

    public AllSkill() {
    }

    public AllSkill(int icon, String title, boolean mentee, boolean mentor) {
        this.icon = icon;
        this.title = title;
        this.mentee = mentee;
        this.mentor = mentor;
    }

    public AllSkill(String title, boolean mentee, boolean mentor) {
        this.title = title;
        this.mentee = mentee;
        this.mentor = mentor;
    }

    protected AllSkill(Parcel in) {
        icon = in.readInt();
        title = in.readString();
        mentee = in.readByte() != 0;
        mentor = in.readByte() != 0;
        skillId = in.readLong();
    }

    public static final Creator<AllSkill> CREATOR = new Creator<AllSkill>() {
        @Override
        public AllSkill createFromParcel(Parcel in) {
            return new AllSkill(in);
        }

        @Override
        public AllSkill[] newArray(int size) {
            return new AllSkill[size];
        }
    };

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMentee() {
        return mentee;
    }

    public void setMentee(boolean mentee) {
        this.mentee = mentee;
    }

    public boolean isMentor() {
        return mentor;
    }

    public void setMentor(boolean mentor) {
        this.mentor = mentor;
    }

    public long getSkillId() {
        return skillId;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(title);
        dest.writeByte((byte) (mentee ? 1 : 0));
        dest.writeByte((byte) (mentor ? 1 : 0));
        dest.writeLong(skillId);
    }
}
