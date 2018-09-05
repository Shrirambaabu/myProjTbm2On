package com.conext.conext.ui.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conext.conext.Adapter.RecyclerAdapterProfileEvents;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.db.USER_EVENT;
import com.conext.conext.ui.CreateEventActivity;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.conext.conext.WebServiceMethods.WebService.getParticularMyAttendedEvents;
import static com.conext.conext.db.DbConstants.EVENT_KEY;
import static com.conext.conext.db.DbConstants.EVENT_TITLE;
import static com.conext.conext.db.DbConstants.IMAGE_KEY;
import static com.conext.conext.db.DbConstants.NAME;
import static com.conext.conext.db.DbConstants.TAG_NAME;
import static com.conext.conext.db.DbConstants.USER_KEY;

public class Events extends Fragment implements View.OnClickListener {

    private static final String COFFEE_CONNECT_EVENT_TYPE_KEY = "4";
    private static final String LUNCH_MEET_EVENT_TYPE_KEY = "3";
    private static final String ALUMNI_MEET_EVENT_TYPE_KEY = "2";
    private static final String MENTOR_SHIP_MEET_EVENT_TYPE_KEY = "1";
    private static final String DEFAULT = "1,2,3,4";
    private ArrayList<USER_EVENT> profileEventsArrayList = new ArrayList<>();
    private RecyclerAdapterProfileEvents profileEventsAdapter = null;

    private ImageView eventsAttendedImageView, coffeeConnectImageView, lunchMeetImageView, alumniImageView, mentorshipImageView;
    private TextView eventsAttendedTextView, coffeeConnectTextView, lunchMeetTextView, alumniTextView,
            mentorshipTextView, eventsAttend;
    private RecyclerView eventsRecyclerView;

    private boolean fabStatus = false;
    private FrameLayout fraToolFloat;
    private boolean flag = true;
    private FloatingActionButton fabSetting;
    private LinearLayout linFab1, linFab2, linFab3, linFab4;

    public Events() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_profile, container, false);

        fraToolFloat = (FrameLayout) view.findViewById(R.id.fraToolFloat);
        fabSetting = (FloatingActionButton) view.findViewById(R.id.fabSetting);

        linFab1 = (LinearLayout) view.findViewById(R.id.linFab1);
        linFab2 = (LinearLayout) view.findViewById(R.id.linFab2);
        linFab3 = (LinearLayout) view.findViewById(R.id.linFab3);
        linFab4 = (LinearLayout) view.findViewById(R.id.linFab4);

        //addressing view
        addressingView(view);
        //attaching listeners
        listeners();

        fab(view);

        displayEventsProfile();

        return view;

    }

    private void fab(View view) {

        FloatingActionButton fabSub1 = (FloatingActionButton) view.findViewById(R.id.fabSub1);
        FloatingActionButton fabSub2 = (FloatingActionButton) view.findViewById(R.id.fabSub2);
        FloatingActionButton fabSub3 = (FloatingActionButton) view.findViewById(R.id.fabSub3);
        FloatingActionButton fabSub4 = (FloatingActionButton) view.findViewById(R.id.fabSub4);

        fabSub1.setOnClickListener(this);
        fabSub2.setOnClickListener(this);
        fabSub3.setOnClickListener(this);
        fabSub4.setOnClickListener(this);

        /* when fab Setting (fab main) clicked */
        fabSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabStatus) {
                    if (flag) {
                        fabSetting.setImageResource(R.drawable.prof_plus);
                        fabSetting.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                        flag = false;
                        hideFab();
                    }
                } else {
                    fabSetting.setImageResource(R.drawable.ic_clear_skill);
                    fabSetting.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.bg_card));
                    flag = true;
                    showFab();
                }
            }
        });

        hideFab();
    }

    private void hideFab() {
        linFab1.setVisibility(View.INVISIBLE);
        linFab2.setVisibility(View.INVISIBLE);
        linFab3.setVisibility(View.INVISIBLE);
        linFab4.setVisibility(View.INVISIBLE);
        fraToolFloat.setBackgroundColor(Color.argb(0, 255, 255, 255));
        fabStatus = false;
    }

    private void showFab() {
        linFab1.setVisibility(View.VISIBLE);
        linFab2.setVisibility(View.VISIBLE);
        linFab3.setVisibility(View.VISIBLE);
        linFab4.setVisibility(View.VISIBLE);
        fraToolFloat.setBackgroundColor(Color.argb(200, 0, 0, 0));
        fabStatus = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        fabSetting.setImageResource(R.drawable.prof_plus);
        fabSetting.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
        hideFab();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result", "got result 1");
    }

    private void displayEventsProfile() {

        getParticularMyAttendedEvents(Prefs.getUserKey(), DEFAULT, getActivity(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                JSONArray response;
                try {
                    profileEventsArrayList.clear();
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        USER_EVENT userEvent = new USER_EVENT();
                        JSONObject obj = response.getJSONObject(i);
                        userEvent.setUserKey(obj.getString(USER_KEY));
                        userEvent.setEventKey(obj.getString(EVENT_KEY));
                        userEvent.setEventTypeKey(obj.getString(NAME));
                        userEvent.setEventTitle(obj.getString(EVENT_TITLE));
                        userEvent.setEventStartDate(obj.getString("dateStartEventDetails"));
                        userEvent.setEventMonthStartDate(obj.getString("monthStartEventDetails"));
                        userEvent.setImageKey(obj.getString(IMAGE_KEY));
                        userEvent.setTagKey(obj.getString(TAG_NAME));
                        profileEventsArrayList.add(userEvent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", e.getMessage());
                }

                settingProfileAdapterCount(profileEventsArrayList);
            }
        });
    }

    private static ArrayList<USER_EVENT> settingResponse(String result, ArrayList<USER_EVENT> profileEventsArrayList) {
        JSONArray response;
        profileEventsArrayList.clear();
        try {
            response = new JSONArray(result);
            for (int i = 0; i < response.length(); i++) {
                USER_EVENT userEvent = new USER_EVENT();

                JSONObject obj = response.getJSONObject(i);
                userEvent.setUserKey(obj.getString(USER_KEY));
                userEvent.setEventKey(obj.getString(EVENT_KEY));
                userEvent.setEventTypeKey(obj.getString(NAME));
                userEvent.setEventTitle(obj.getString(EVENT_TITLE));
                userEvent.setEventStartDate(obj.getString("dateStartEventDetails"));
                userEvent.setEventMonthStartDate(obj.getString("monthStartEventDetails"));
                userEvent.setImageKey(obj.getString(IMAGE_KEY));
                userEvent.setTagKey(obj.getString(TAG_NAME));

                profileEventsArrayList.add(userEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileEventsArrayList;
    }

    private void settingProfileAdapterCount(ArrayList<USER_EVENT> profileEventsArrayList) {

        profileEventsAdapter = new RecyclerAdapterProfileEvents(getActivity(), profileEventsArrayList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        eventsRecyclerView.setLayoutManager(mLayoutManager);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setAdapter(profileEventsAdapter);

        settingRecyclerAdapterProfileEvents(profileEventsArrayList);

        int attendEvent = profileEventsArrayList.size();

        String eventFinalCount;
        if (attendEvent < 10) {
            eventFinalCount = "" + 0 + attendEvent;
        } else {
            eventFinalCount = "" + attendEvent;
        }
        eventsAttendedTextView.setText(eventFinalCount);

        getParticularMyAttendedEvents(Prefs.getUserKey(), COFFEE_CONNECT_EVENT_TYPE_KEY, getActivity(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, new ArrayList<USER_EVENT>());
                int coffeeConnectCount = finalProfileEventsArrayList.size();
                coffeeConnectCount(coffeeConnectCount);
            }
        });

        getParticularMyAttendedEvents(Prefs.getUserKey(), LUNCH_MEET_EVENT_TYPE_KEY, getActivity(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, new ArrayList<USER_EVENT>());
                int lunchMeetCount = finalProfileEventsArrayList.size();
                lunchMeetCount(lunchMeetCount);
            }
        });

        getParticularMyAttendedEvents(Prefs.getUserKey(), ALUMNI_MEET_EVENT_TYPE_KEY, getActivity(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, new ArrayList<USER_EVENT>());
                int alumniCount = finalProfileEventsArrayList.size();
                alumniCount(alumniCount);
            }
        });

        getParticularMyAttendedEvents(Prefs.getUserKey(), MENTOR_SHIP_MEET_EVENT_TYPE_KEY, getActivity(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, new ArrayList<USER_EVENT>());
                int mentorShipCount = finalProfileEventsArrayList.size();
                mentorShipCount(mentorShipCount);
            }
        });
    }

    private void mentorShipCount(int mentorShipCount) {
        String mentorShip;
        if (mentorShipCount < 10) {
            mentorShip = "" + 0 + mentorShipCount;
        } else {
            mentorShip = "" + mentorShipCount;
        }
        mentorshipTextView.setText(mentorShip);
    }

    private void alumniCount(int alumniCount) {
        String alumniConnect;
        if (alumniCount < 10) {
            alumniConnect = "" + 0 + alumniCount;
        } else {
            alumniConnect = "" + alumniCount;
        }
        alumniTextView.setText(alumniConnect);
    }

    private void lunchMeetCount(int lunchMeetCount) {
        String lunchMeet;
        if (lunchMeetCount < 10) {
            lunchMeet = "" + 0 + lunchMeetCount;
        } else {
            lunchMeet = "" + lunchMeetCount;
        }
        lunchMeetTextView.setText(lunchMeet);
    }

    private void coffeeConnectCount(int coffeeConnectCount) {
        String coffeeConnect;
        if (coffeeConnectCount < 10) {
            coffeeConnect = "" + 0 + coffeeConnectCount;
        } else {
            coffeeConnect = "" + coffeeConnectCount;
        }
        coffeeConnectTextView.setText(coffeeConnect);
    }

    private void listeners() {

        eventsAttendedImageView.setOnClickListener(this);
        coffeeConnectImageView.setOnClickListener(this);
        lunchMeetImageView.setOnClickListener(this);
        alumniImageView.setOnClickListener(this);
        mentorshipImageView.setOnClickListener(this);

    }

    private void addressingView(View view) {

        eventsAttendedImageView = (ImageView) view.findViewById(R.id.event_attend_prof);
        coffeeConnectImageView = (ImageView) view.findViewById(R.id.event_coffee_prof);
        lunchMeetImageView = (ImageView) view.findViewById(R.id.event_lunch_prof);
        alumniImageView = (ImageView) view.findViewById(R.id.event_alumini_prof);
        mentorshipImageView = (ImageView) view.findViewById(R.id.event_mentor_prof);

        eventsAttendedTextView = (TextView) view.findViewById(R.id.event_prof_attended);
        coffeeConnectTextView = (TextView) view.findViewById(R.id.event_coffee_attended);
        lunchMeetTextView = (TextView) view.findViewById(R.id.event_lunch_attended);
        alumniTextView = (TextView) view.findViewById(R.id.event_alumni_meet);
        mentorshipTextView = (TextView) view.findViewById(R.id.event_prof_ship);
        eventsAttend = (TextView) view.findViewById(R.id.events_attended_text_view);
        eventsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_events);
    }


    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.fabSub1:
                Intent intent1 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "0");
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.fabSub2:
                Intent intent2 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "1");
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.fabSub3:
                Intent intent3 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "2");
                intent3.putExtras(bundle);
                startActivity(intent3);
                break;
            case R.id.fabSub4:
                Intent intent4 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "3");
                intent4.putExtras(bundle);
                startActivity(intent4);
                break;

            case R.id.event_attend_prof:

                eventsAttendedImageView.setImageResource(R.drawable.event_attended);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorshipImageView.setImageResource(R.drawable.cc_connect);

                setMyEventsAttended(profileEventsArrayList);

                break;
            case R.id.event_coffee_prof:

                eventsAttendedImageView.setImageResource(R.drawable.cc_connect);

                coffeeConnectImageView.setImageResource(R.drawable.event_attended);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorshipImageView.setImageResource(R.drawable.cc_connect);

                getParticularMyAttendedEvents(Prefs.getUserKey(), COFFEE_CONNECT_EVENT_TYPE_KEY, getActivity(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        profileEventsArrayList.clear();
                        ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, profileEventsArrayList);
                        settingRecyclerAdapterProfileEvents(finalProfileEventsArrayList);
                    }
                });

                break;
            case R.id.event_lunch_prof:

                eventsAttendedImageView.setImageResource(R.drawable.cc_connect);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.event_attended);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorshipImageView.setImageResource(R.drawable.cc_connect);

                getParticularMyAttendedEvents(Prefs.getUserKey(), LUNCH_MEET_EVENT_TYPE_KEY, getActivity(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        profileEventsArrayList.clear();
                        ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, profileEventsArrayList);
                        settingRecyclerAdapterProfileEvents(finalProfileEventsArrayList);
                    }
                });

                break;
            case R.id.event_alumini_prof:

                eventsAttendedImageView.setImageResource(R.drawable.cc_connect);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.event_attended);

                mentorshipImageView.setImageResource(R.drawable.cc_connect);

                getParticularMyAttendedEvents(Prefs.getUserKey(), ALUMNI_MEET_EVENT_TYPE_KEY, getActivity(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        profileEventsArrayList.clear();
                        ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, profileEventsArrayList);
                        settingRecyclerAdapterProfileEvents(finalProfileEventsArrayList);
                    }
                });

                break;
            case R.id.event_mentor_prof:

                eventsAttendedImageView.setImageResource(R.drawable.cc_connect);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorshipImageView.setImageResource(R.drawable.event_attended);

                getParticularMyAttendedEvents(Prefs.getUserKey(), MENTOR_SHIP_MEET_EVENT_TYPE_KEY, getActivity(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        profileEventsArrayList.clear();
                        ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, profileEventsArrayList);
                        settingRecyclerAdapterProfileEvents(finalProfileEventsArrayList);
                    }
                });

                break;
            default:

                eventsAttendedImageView.setImageResource(R.drawable.event_attended);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorshipImageView.setImageResource(R.drawable.cc_connect);

                setMyEventsAttended(profileEventsArrayList);
        }

    }

    private void setMyEventsAttended(ArrayList<USER_EVENT> profileEventsArrayList) {


        final ArrayList<USER_EVENT> finalProfileEventsArrayList = profileEventsArrayList;
        getParticularMyAttendedEvents(Prefs.getUserKey(), DEFAULT, getActivity(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ArrayList<USER_EVENT> profileEventsArrayList = settingResponse(result, finalProfileEventsArrayList);
                settingRecyclerAdapterProfileEvents(profileEventsArrayList);
            }
        });
    }

    private void settingRecyclerAdapterProfileEvents(ArrayList<USER_EVENT> profileEventsArrayList) {

        if (!profileEventsArrayList.isEmpty()) {
            eventsAttend.setVisibility(View.VISIBLE);
        } else {
            eventsAttend.setVisibility(View.GONE);
        }
        profileEventsAdapter.notifyDataSetChanged();

    }

}
