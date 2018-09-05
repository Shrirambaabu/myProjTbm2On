package com.conext.conext;

import android.app.Application;
import android.content.Context;

import com.conext.conext.utils.TypefaceUtil;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class AppManager extends Application {

    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


    private static AppManager mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "customFont/Roboto-Medium.ttf");

    }

    public AppManager() {
        mAppContext = this;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

}