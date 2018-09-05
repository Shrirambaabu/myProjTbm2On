package com.conext.conext.model;

import java.io.Serializable;

/**
 * Created by Ashith VL on 6/20/2017.
 */

public class Contact implements Serializable {

    private String name;
    private String image;
    private String colg;
    private String job;
    private String id;
    private String status = "2";

    public Contact() {
    }

    public Contact(String name, String image, String colg, String job, String id) {
        this.name = name;
        this.image = image;
        this.colg = colg;
        this.job = job;
        this.id = id;
    }

    /* 0 --> mentee;
       1 --> mentor; */


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColg() {
        return colg;
    }

    public void setColg(String colg) {
        this.colg = colg;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
