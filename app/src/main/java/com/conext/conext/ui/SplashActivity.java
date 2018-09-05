package com.conext.conext.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.conext.conext.AppManager;
import com.conext.conext.HomeActivity;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.utils.Prefs;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.conext.conext.WebServiceMethods.WebService.checkUser;
import static com.conext.conext.WebServiceMethods.WebService.getAluminiDetails;
import static com.conext.conext.WebServiceMethods.WebService.getUserSkills;
import static com.conext.conext.utils.Utility.isNetworkAvailable;

/**
 * Created by Ashith VL on 10/15/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        if (AccessToken.getCurrentAccessToken() != null && isNetworkAvailable(SplashActivity.this)) {
            Log.e("User is login", "YES " + AccessToken.getCurrentAccessToken());
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me?fields=email",
                    null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(final GraphResponse response) {

                    JSONObject graphResponse = response.getJSONObject();

                    Log.e("tag", "graphResponse ==> " + graphResponse);

                    if (graphResponse.has("email")) {
                        try {
                            Log.e("tag", "email ==> " + graphResponse.getString("email"));
                            checkUser(graphResponse.getString("email"), SplashActivity.this, new VolleyCallback() {
                                @Override
                                public void onSuccess(final String userKey) {
                                    if (!userKey.equals("") || !userKey.equals("0")) {

                                        getAluminiDetails(userKey, SplashActivity.this, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(String result) {
                                                JSONArray response;
                                                Log.e("tag", "onSuccess: " + result);
                                                try {
                                                    response = new JSONArray(result);
                                                    if (response.length() >= 1) {
                                                        Log.e("tag", "response.length(): " + response.length());

                                                        getUserSkills(userKey, SplashActivity.this, new VolleyCallback() {
                                                            @Override
                                                            public void onSuccess(String result) {
                                                                JSONArray response;
                                                                try {
                                                                    response = new JSONArray(result);
                                                                    JSONObject jsonObject = response.getJSONObject(0);
                                                                    Log.e("tag", "onSuccess: " + jsonObject);
                                                                    if (jsonObject.length() >= 2) {
                                                                        Log.e("tag", "response.length(): " + response.length());
                                                                        Prefs.setUserKey(userKey);
                                                                        Intent login = new Intent(SplashActivity.this, HomeActivity.class);
                                                                        startActivity(login);
                                                                        finish();
                                                                    } else {
                                                                        Log.e("tag", "response.length(): skill " + response.length());
                                                                        Prefs.setUserKey(userKey);
                                                                        Intent login = new Intent(SplashActivity.this, RegistrationPersonaliseActivity.class);
                                                                        startActivity(login);
                                                                        finish();
                                                                    }

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Prefs.setUserKey(userKey);
                                                        Log.e("tag", "getAluminiDetails.length(): " + response.length());
                                                        Intent login = new Intent(SplashActivity.this, RegistrationPersonaliseActivity.class);
                                                        startActivity(login);
                                                        finish();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).executeAsync();
        } else {

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, SPLASH_TIME_OUT);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppManager.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppManager.activityPaused();
    }
}
