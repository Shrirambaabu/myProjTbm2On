package com.conext.conext.WebServiceMethods;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.conext.conext.model.db.USER_EVENT;
import com.conext.conext.model.db.USER_PROFILE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.conext.conext.utils.Constants.ADD_USER_INFO_URL;
import static com.conext.conext.utils.Constants.ADD_USER_SKILL_URL;
import static com.conext.conext.utils.Constants.ALUMNI_URL;
import static com.conext.conext.utils.Constants.CHECK_REGISTRATION_URL;
import static com.conext.conext.utils.Constants.CHECK_SCHOOL_URL;
import static com.conext.conext.utils.Constants.CHECK_USER_URL;
import static com.conext.conext.utils.Constants.COMPANY_NAME_URL;
import static com.conext.conext.utils.Constants.CREATE_EVENT_URL;
import static com.conext.conext.utils.Constants.EVENT_DETAILS_URL;
import static com.conext.conext.utils.Constants.GET_ALL_CONTACTS_OF_EVENTS_URL;
import static com.conext.conext.utils.Constants.GET_EDUCATION_URL;
import static com.conext.conext.utils.Constants.GET_EVENT_END_DATE_URL;
import static com.conext.conext.utils.Constants.GET_EVENT_SELETCTED_URL;
import static com.conext.conext.utils.Constants.GET_EVENT_TYPE_KEY_URL;
import static com.conext.conext.utils.Constants.GET_FACEBOOK_SKILLS;
import static com.conext.conext.utils.Constants.GET_MENTEE_MENTOR_REQUEST_URL;
import static com.conext.conext.utils.Constants.GET_MY_EVENTS_ATTENDED_URL;
import static com.conext.conext.utils.Constants.GET_MY_EVENTS_URL;
import static com.conext.conext.utils.Constants.GET_MY_MENTEES_URL;
import static com.conext.conext.utils.Constants.GET_MY_PARTICULAR_EVENTS_ATTENDED_URL;
import static com.conext.conext.utils.Constants.GET_PROFILE_SKILL_URL;
import static com.conext.conext.utils.Constants.GET_TAG_URL;
import static com.conext.conext.utils.Constants.GET_TAG_USER_KEY_URL;
import static com.conext.conext.utils.Constants.GET_USER_CHOICE_EVENT_DETAILS_URL;
import static com.conext.conext.utils.Constants.GET_USER_CONTACT_URL;
import static com.conext.conext.utils.Constants.GET_USER_MENTEE_SKILL_URL;
import static com.conext.conext.utils.Constants.GET_USER_OTHER_SKILL_URL;
import static com.conext.conext.utils.Constants.LOCATION_URL;
import static com.conext.conext.utils.Constants.LOGIN_URL;
import static com.conext.conext.utils.Constants.NAME_URL;
import static com.conext.conext.utils.Constants.REGISTER_URL;
import static com.conext.conext.utils.Constants.REMOVE_USER_URL;
import static com.conext.conext.utils.Constants.SET_LOCATION_URL;
import static com.conext.conext.utils.Constants.TAG_KEY_URL;
import static com.conext.conext.utils.Constants.UPDATE_EVENT_PARTICIPANT;
import static com.conext.conext.utils.Constants.UPDATE_EVENT_PARTICIPANTS_URL;
import static com.conext.conext.utils.Constants.UPDATE_EVENT_PARTICIPANT_STATUS_URL;
import static com.conext.conext.utils.Constants.UPDATE_PARTICIPANT_STATUS_URL;
import static com.conext.conext.utils.Constants.UPDATE_PROFILE_PIC;
import static com.conext.conext.utils.Constants.UPDATE_UPCOMMING_EVENTS_SELECTED_URL;
import static com.conext.conext.utils.Constants.USER_EMAIL_URL;
import static com.conext.conext.utils.Constants.USER_IMAGE_URL;
import static com.conext.conext.utils.Utility.showDialogue;

/**
 * Created by Ashith VL on 8/8/2017.
 */

public class WebService {

    public static void loginUsingWs(final Activity activtiy, final String uName, final String password, final VolleyCallback callback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("null"))
                    s = String.valueOf(0);
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", uName);
                parameters.put("password", password);
                return parameters;
            }
        };


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }

    public static void updateProfilePicture(final Activity activtiy, final byte[] bytes, final String userKey, final VolleyCallback callback) {

        final String imageString = Base64.encodeToString(bytes, Base64.DEFAULT);
        Log.e("tag", " key " + userKey + " " + imageString);
        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_PROFILE_PIC, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onSuccess("false" + volleyError.toString());
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("inputData", imageString);
                parameters.put("userKey", userKey);
                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }

    public static void updateEventDetailsParticipant(final Activity activtiy, final String userKey,
                                                     final String eventKey, final String status, final VolleyCallback callback) {

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_EVENT_PARTICIPANT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onSuccess("false");
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("UserKey", userKey);
                parameters.put("eventKey", eventKey);
                parameters.put("partitcipantStatus", status);
                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }


    public static void getName(final String userKey, final Activity activtiy, final VolleyCallback volleyCallback) {


        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, NAME_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }

    public static void getFacebookSkills(final String userKey, final Activity activtiy, final VolleyCallback volleyCallback) {

        Log.e("error", "loaded " + GET_FACEBOOK_SKILLS + "/" + userKey);
        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_FACEBOOK_SKILLS + "/" + userKey , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }


    public static void getUserChoiceOnEventDetails(final String eventKey, final Activity activtiy, final VolleyCallback volleyCallback) {
        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_USER_CHOICE_EVENT_DETAILS_URL + "/" + eventKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }

    public static void isOpen(final String eventKey, final Activity activtiy, final VolleyCallback volleyCallback) {
        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_USER_CHOICE_EVENT_DETAILS_URL + "/" + eventKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }

    public static void removeUser(final String userKey, final String id, final Activity activtiy) {
        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, REMOVE_USER_URL + "/" + userKey + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }
    public static void removeUser(final String userKey, final Activity activtiy) {
        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, REMOVE_USER_URL + "/" + userKey , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }

    public static void getCompanyName(final String userKey, final Activity activtiy, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, COMPANY_NAME_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }


    public static void getUserEmail(final String userKey, final Activity activtiy, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, USER_EMAIL_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    volleyCallback.onSuccess(s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }


    public static void getUserImage(final String userKey, final Activity activtiy, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, USER_IMAGE_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    volleyCallback.onSuccess(s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }


    public static void getEventEndDate(final String evenKey, final Activity activtiy, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_EVENT_END_DATE_URL + "/" + evenKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    volleyCallback.onSuccess(s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }

    public static void getEducation(final Activity activtiy, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_EDUCATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    volleyCallback.onSuccess(s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }

    public static void getTags(final Activity activtiy, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_TAG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    volleyCallback.onSuccess(s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
    }

    public static String getEventCreator(final String userKey, final String id, final Activity activtiy) {

        final String[] eventCreator = {null};

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, EVENT_DETAILS_URL + "/" + userKey + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    //tagKey = Long.parseLong(s);
                    eventCreator[0] = s;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                // showDialogue(activtiy, "Sorry! Server Error", "Oops!!!");
            }
        });


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activtiy);
        rQueue.add(request);
        return eventCreator[0];
    }

    public static void getTagKey(final String title, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, TAG_KEY_URL + "/" + title, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void getTagUserKey(final String title, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_TAG_USER_KEY_URL + "/" + title, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void getEventType(final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_EVENT_TYPE_KEY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }


    public static void getEventTypeKey(final String title, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_EVENT_SELETCTED_URL + "/" + title, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void addUserSKill(final String userKey, final String tagKey, final String s1, final String s2, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, ADD_USER_SKILL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                volleyCallback.onSuccess("true");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("UserKey", userKey);
                parameters.put("tagKey", tagKey);
                parameters.put("mentee", s1);
                parameters.put("mentor", s2);
                return parameters;
            }
        };


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void getAluminiDetails(final String userKey, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, ALUMNI_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }


    public static void getMyAttendedEvents(final String userKey, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_MY_EVENTS_ATTENDED_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e("tag", response);
                volleyCallback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }


    public static void getParticularMyAttendedEvents(final String userKey, final String eventTypeKey, final Activity activity, final VolleyCallback volleyCallback) {

        Log.e("tag", "loaded --> " + GET_MY_PARTICULAR_EVENTS_ATTENDED_URL + "/" + userKey + "/" + eventTypeKey);

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_MY_PARTICULAR_EVENTS_ATTENDED_URL + "/" + userKey + "/" + eventTypeKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("tag", response);
                volleyCallback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void getMyEvents(final String userKey, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_MY_EVENTS_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.e("tag", response);
                volleyCallback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void getAllContactsOfEventss(final String eventKey, final Activity activity, final VolleyCallback volleyCallback) {

        Log.e("EventDetails", "" + GET_ALL_CONTACTS_OF_EVENTS_URL + "/" + eventKey);

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_ALL_CONTACTS_OF_EVENTS_URL + "/" + eventKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void getUserContact(final String userKey, final Activity activity, final VolleyCallback volleyCallback) {

        Log.e("EventDetails", "" + GET_USER_CONTACT_URL + "/" + userKey);

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_USER_CONTACT_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Log.e("tag", response);
                volleyCallback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }


    public static void getUserMenteeSkill(final String userKey, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_USER_MENTEE_SKILL_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.e("tag", response);
                volleyCallback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }


    public static void getMenteeMentorRequest(final String userKey, final Activity activity, final VolleyCallback volleyCallback) {

        Log.e("OtherNotify", "" + GET_MENTEE_MENTOR_REQUEST_URL + "/" + userKey);
        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_MENTEE_MENTOR_REQUEST_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                volleyCallback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }


    public static void getUserSkills(final String userKey, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_PROFILE_SKILL_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }


    public static void settLocation(final String userKey, final String latitude, final String longitude, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, LOCATION_URL + "/" + userKey + "/" + latitude + "/" + longitude, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // Log.e("tag", "location");
                volleyCallback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        });


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);

    }


    public static void registerUsingWs(final USER_PROFILE user, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyCallback.onSuccess("error");
                Log.e("tag", volleyError.toString());
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("EmailID", user.getEmailID());
                parameters.put("Password", user.getPassword());
                parameters.put("FirstName", user.getFirstName());
                parameters.put("LastName", user.getLastName());
                parameters.put("TitleDisplay", user.getTitleDisplay());
                parameters.put("CompanyDisplay", user.getCompanyDisplay());
                parameters.put("ProfilePic", user.getProfilePic());
                parameters.put("DeleteFlag", user.getDeleteFlg());
                parameters.put("CreatedBy", user.getCreatedBy());
                parameters.put("ModifiedBy", user.getModifiedBy());
                parameters.put("LocationKey", "0");

                Log.e("EmailID", user.getEmailID());
                Log.e("Password", user.getPassword());
                Log.e("FirstName", user.getFirstName());
                Log.e("LastName", user.getLastName());
                Log.e("TitleDisplay", user.getTitleDisplay());
                Log.e("CompanyDisplay", user.getCompanyDisplay());
                Log.e("ProfilePic", user.getProfilePic());
                Log.e("DeleteFlag", user.getDeleteFlg());
                Log.e("CreatedBy", user.getCreatedBy());
                Log.e("ModifiedBy", user.getModifiedBy());
                Log.e("LocationKey", "0");

                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);

    }

    public static void setEventLocation(final String UserKey, final String Latitude, final String Longitude, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, SET_LOCATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("UserKey", UserKey);
                parameters.put("Latitude", Latitude);
                parameters.put("Longitude", Longitude);
                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);

    }

    public static void createEvent(final USER_EVENT user_event, final String UserKey, final Activity activity, final VolleyCallback volleyCallback) {

        final String imageString = Base64.encodeToString(user_event.getImage(), Base64.DEFAULT);

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, CREATE_EVENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("UserKey", UserKey);
                parameters.put("EventTypeKey", user_event.getEventTypeKey());
                parameters.put("TagKey", user_event.getTagKey());
                parameters.put("LocationKey", user_event.getLocationKey());
                parameters.put("Description", user_event.getDescription());
                parameters.put("EventTitle", user_event.getEventTitle());
                parameters.put("EventStartTs", user_event.getEventStartTs());
                parameters.put("EventEndTs", user_event.getEventEndTs());
                parameters.put("Address", user_event.getAddress());
                parameters.put("isOpen", String.valueOf(user_event.isOpen()));
                parameters.put("ImageKey", imageString);
                parameters.put("CreatedBy", user_event.getCreatedBy());
                parameters.put("ModifiedBy", user_event.getModifiedBy());
                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);

    }

    public static void updateUpcomingEventSelected(final String eventKey, final String userKey, final int participantStatus, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_UPCOMMING_EVENTS_SELECTED_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("userKey", userKey);
                parameters.put("eventId", eventKey);
                parameters.put("partitcipantStatus", String.valueOf(participantStatus));
                parameters.put("mentor", "0");
                parameters.put("mentee", "0");
                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void updateParticipantStatus(final String eventKey, final String userKey, final String status, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_PARTICIPANT_STATUS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("userKey", userKey);
                parameters.put("eventId", eventKey);
                parameters.put("status", status);
                return parameters;
            }
        };


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);

    }

    public static void updateEventParticipantsStatus(final String userKey, final String eventKey, final int status, final Activity activity) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_EVENT_PARTICIPANT_STATUS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("userKey", userKey);
                parameters.put("eventId", eventKey);
                parameters.put("status", String.valueOf(status));
                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);

    }

    public static void updateEventParticipants(final String eventKey, final String userKey, final List<String> otherUserKey, final int status, final Activity activity, final VolleyCallback volleyCallback) {

        if (!otherUserKey.isEmpty()) {

            //Requests the data from webservice using volley
            StringRequest request = new StringRequest(Request.Method.POST, UPDATE_EVENT_PARTICIPANTS_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (s != null)
                        volleyCallback.onSuccess(s);
                    else
                        volleyCallback.onSuccess(String.valueOf(0));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    /**
                     *  Returns error message when,
                     *  server is down,
                     *  incorrect IP
                     *  Server not deployed
                     */
                    showDialogue(activity, "Sorry! Server Error", "Oops!!!");
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("userKey", userKey);
                    parameters.put("eventId", eventKey);
                    parameters.put("otherUserKey", android.text.TextUtils.join(",", otherUserKey));
                    parameters.put("partitcipantStatus", String.valueOf(status));
                    return parameters;
                }
            };

            settingTime(request);

            // Adding request to request queue
            RequestQueue rQueue = Volley.newRequestQueue(activity);
            rQueue.add(request);

        }
    }


    public static void checkUser(final String email, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, CHECK_USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Log.e("tag", volleyError.toString());
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", email);
                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void checkRegisteration(final String userkey, final Activity activity, final VolleyCallback volleyCallback) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, CHECK_REGISTRATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null)
                    volleyCallback.onSuccess(s);
                else
                    volleyCallback.onSuccess(String.valueOf(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Log.e("tag", volleyError.toString());
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", userkey);
                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }


    public static void checkSchool(final String school, final String schoolType, final String userKey, final Activity activity) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, CHECK_SCHOOL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("response school", s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Log.e("tag", volleyError.toString());
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("school", school);
                parameters.put("schoolType", schoolType);
                parameters.put("userKey", userKey);
                return parameters;
            }
        };


        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    public static void addUserInfo(final String userKey, final String univ, final String startYear,
                                   final String endYear, final String major, final String edu, final Activity activity) {

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, ADD_USER_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //   Log.e("tag", volleyError.toString());
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                showDialogue(activity, "Sorry! Server Error", "Oops!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("UserKey", userKey);
                parameters.put("eduKey", univ);
                parameters.put("yearStart", startYear);
                parameters.put("yearEnd", endYear);
                parameters.put("major", major);
                parameters.put("degree", edu);
                return parameters;
            }
        };

        settingTime(request);

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);

    }

    private static void settingTime(StringRequest request) {
        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}
