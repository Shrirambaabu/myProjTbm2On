package com.conext.conext.model;

/**
 * Created by Ashith VL on 6/22/2017.
 */

public class User {

    String uName, password, uId;

    public User(String uName, String password) {
        this.uName = uName;
        this.password = password;
    }

    public User(String uName, String password, String uId) {
        this.uName = uName;
        this.password = password;
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
