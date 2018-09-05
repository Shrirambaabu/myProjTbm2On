package com.conext.conext.model;

/**
 * Created by Ashith VL on 7/8/2017.
 */

public class NetworkTag {
    public int tagKey;
    public int mentee;
    public int mentor;

    public NetworkTag(int tagKey, int mentee, int mentor) {
        this.tagKey = tagKey;
        this.mentee = mentee;
        this.mentor = mentor;
    }

    public int getTagKey() {

        return tagKey;
    }

    public void setTagKey(int tagKey) {
        this.tagKey = tagKey;
    }

    public int getMentee() {
        return mentee;
    }

    public void setMentee(int mentee) {
        this.mentee = mentee;
    }

    public int getMentor() {
        return mentor;
    }

    public void setMentor(int mentor) {
        this.mentor = mentor;
    }

    public NetworkTag() {

    }
}
