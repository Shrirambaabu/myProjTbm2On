package com.conext.conext.model;

import android.graphics.Bitmap;

/**
 * Created by Ashith VL on 7/7/2017.
 */

public class Network {

    public String drawable;
    public int mentee;
    public int mentor;
    public int id;
    public NetworkTag networkTags;
    public int alumini;

    public NetworkTag getNetworkTags() {
        return networkTags;
    }

    public int getAlumini() {
        return alumini;
    }

    public void setAlumini(int alumini) {
        this.alumini = alumini;
    }

    public void setNetworkTags(NetworkTag networkTags) {
        this.networkTags = networkTags;
    }

    public Network(String drawable, int mentee, int mentor, int id, NetworkTag networkTags) {

        this.drawable = drawable;
        this.mentee = mentee;
        this.mentor = mentor;
        this.id = id;
        this.networkTags = networkTags;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Network() {

    }

    public Network(String drawable, int mentee, int mentor, int id) {

        this.drawable = drawable;
        this.mentee = mentee;
        this.mentor = mentor;
        this.id = id;
    }

    public Network(String d) {
        drawable = d;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }
}
