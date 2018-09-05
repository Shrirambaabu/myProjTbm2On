package com.conext.conext.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.conext.conext.AppManager;
import com.conext.conext.model.AllSkill;
import com.conext.conext.model.Skill;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by sunil on 26-03-2016.
 */
public class Prefs {

    private static SharedPreferences mSharedPreferences = null;
    private static final String PREFS_NAME = "CONEXT_APP_INFO";

    //Keys to access info from SharedPreferences
    private static final String ACCESS_TOKEN_PREFS_KEY = "access_token";
    private static final String ACCESS_TOKEN_FB_PREFS_KEY = "access_token_fb";
    private static final String USER_KEY_PREFS_KEY = "user_key";
    private static final String USER_IMAGE_PREFS_KEY = "user_image";
    private static final String LOCATION = "location";
    private static final String EXPIRES_IN_PREFS_KEY = "expires_in";
    private static final String USER_SKILL = "user_skill";
    private static final String USER_SKILL_MENTEE = "user_skill_mentee";
    private static final String USER_SKILL_MENTOR = "user_skill_mentor";
    private static final String LONG_PRESS_EDIT_MODE = "edit_mode";
    private static final String FIRST_TIME_PREFS_KEY = "first_time";


    public static void setEditMode(boolean editMode) {
        setDataInPrefsBoolean(LONG_PRESS_EDIT_MODE, editMode);
    }

    public static boolean getEditMode() {
        return getSharedPreference().getBoolean(LONG_PRESS_EDIT_MODE, true);
    }


    public static ArrayList<Skill> getUserSkill() {
        return getSharedPreferenceArrayList(USER_SKILL);
    }

    public static void setUserSkill(ArrayList<Skill> skillArrayList) {
        setArrayListInPrefs(USER_SKILL, skillArrayList);
    }


    public static ArrayList<AllSkill> getAllUserSkill() {
        return getSharedPreferenceSkillArrayList(USER_SKILL);
    }

    public static void setAllUserSkill(ArrayList<AllSkill> skillArrayList) {
        setSkillArrayListInPrefs(USER_SKILL, skillArrayList);
    }

    public static void setUserMenteeSkill(ArrayList<Skill> skillArrayList) {
        setArrayListInPrefs(USER_SKILL_MENTEE, skillArrayList);
    }

    public static ArrayList<Skill> getUserMenteeSkill() {
        return getSharedPreferenceArrayList(USER_SKILL_MENTEE);
    }

    public static ArrayList<AllSkill> getAllUserMenteeSkill() {
        return getSharedPreferenceSkillArrayList(USER_SKILL_MENTOR);
    }

    public static void setAllUserMenteeSkill(ArrayList<AllSkill> skillArrayList) {
        setSkillArrayListInPrefs(USER_SKILL_MENTEE, skillArrayList);
    }

    public static ArrayList<Skill> getUserMentorSkill() {
        return getSharedPreferenceArrayList(USER_SKILL_MENTOR);
    }

    public static void setUserMentorSkill(ArrayList<Skill> skillArrayList) {
        setArrayListInPrefs(USER_SKILL_MENTOR, skillArrayList);
    }

    public static ArrayList<AllSkill> getAllUserMentorSkill() {
        return getSharedPreferenceSkillArrayList(USER_SKILL_MENTOR);
    }

    public static void setAllUserMentorSkill(ArrayList<AllSkill> skillArrayList) {
        setSkillArrayListInPrefs(USER_SKILL_MENTOR, skillArrayList);
    }

    public static void setAccessTokenInPrefs(String accessToken) {
        setDataInPrefs(ACCESS_TOKEN_PREFS_KEY, accessToken);
    }

    public static String getAccessTokenFromPrefs() {
        return getSharedPreference().getString(ACCESS_TOKEN_PREFS_KEY, null);
    }

    public static void setAccessFbTokenInPrefs(String accessToken) {
        setDataInPrefs(ACCESS_TOKEN_FB_PREFS_KEY, accessToken);
    }

    public static String getAccessFbTokenFromPrefs() {
        return getSharedPreference().getString(ACCESS_TOKEN_FB_PREFS_KEY, null);
    }


    public static void setUserKey(String accessToken) {
        setDataInPrefs(USER_KEY_PREFS_KEY, accessToken);
    }

    public static String getUserKey() {
        return getSharedPreference().getString(USER_KEY_PREFS_KEY, null);
    }


    public static void setUserImage(String image) {
        setDataInPrefs(USER_IMAGE_PREFS_KEY, image);
    }

    public static String getUserImage() {
        return getSharedPreference().getString(USER_IMAGE_PREFS_KEY, null);
    }

    public static void setLoc(String loc) {
        setDataInPrefs(LOCATION, loc);
    }

    public static String getLoc() {
        return getSharedPreference().getString(LOCATION, null);
    }

    public static void setExpiresInPrefs(String expiresIn) {
        setDataInPrefs(EXPIRES_IN_PREFS_KEY, expiresIn);
    }

    public static String getExpiresInFromPrefs() {
        return getSharedPreference().getString(EXPIRES_IN_PREFS_KEY, null);
    }


    private static void setArrayListInPrefs(String key, ArrayList<Skill> arrayList) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        Gson gson = new Gson();

        String json = gson.toJson(arrayList);

        editor.putString(key, json);
        editor.apply();
    }

    private static ArrayList<Skill> getSharedPreferenceArrayList(String PREFS_NAME) {
        if (mSharedPreferences == null) {
            mSharedPreferences = AppManager.getAppContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        Gson gson = new Gson();
        String json = mSharedPreferences.getString(PREFS_NAME, null);
        Type type = new TypeToken<ArrayList<Skill>>() {
        }.getType();

        return gson.fromJson(json, type);

    }


    private static void setSkillArrayListInPrefs(String key, ArrayList<AllSkill> arrayList) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        Gson gson = new Gson();

        String json = gson.toJson(arrayList);

        editor.putString(key, json);
        editor.apply();
    }

    private static ArrayList<AllSkill> getSharedPreferenceSkillArrayList(String PREFS_NAME) {
        if (mSharedPreferences == null) {
            mSharedPreferences = AppManager.getAppContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        Gson gson = new Gson();
        String json = mSharedPreferences.getString(PREFS_NAME, null);
        Type type = new TypeToken<ArrayList<AllSkill>>() {
        }.getType();

        return gson.fromJson(json, type);

    }


    private static void setDataInPrefs(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static void setDataInPrefsBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static SharedPreferences getSharedPreference() {
        if (mSharedPreferences == null) {
            mSharedPreferences = AppManager.getAppContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        return mSharedPreferences;
    }

    public static void setFirstTime(String value) {
        setDataInPrefs(FIRST_TIME_PREFS_KEY, value);
    }

    public static String getFirstTime() {
        return getSharedPreference().getString(FIRST_TIME_PREFS_KEY, null);
    }

}