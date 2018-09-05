package com.conext.conext.rest;

import com.conext.conext.model.AccessToken;
import com.conext.conext.model.Profile;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by sunil on 26-03-2016.
 */
public interface ConextAPIRequest {

    @FormUrlEncoded
    @POST("/uas/oauth2/accessToken")
    Call<AccessToken> getAccessToken(@Field("grant_type") String grantType,
                                     @Field("code") String code,
                                     @Field("redirect_uri") String redirectUri,
                                     @Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecretKey);

    @GET("/v1/people/~")
    Call<Profile> getProfileInfo(@Header("Authorization") String accessToken,
                                 @Query("format") String format);
}