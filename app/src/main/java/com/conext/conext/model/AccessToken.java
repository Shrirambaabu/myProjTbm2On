package com.conext.conext.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 26-03-2016.
 */
public class AccessToken {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private String expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}