package com.conext.conext.ui;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.conext.conext.Adapter.RecyclerAdapterProfileEvents;
import com.conext.conext.Adapter.RecyclerAdapterProfileInfo;
import com.conext.conext.AppManager;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.db.USER_EVENT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.conext.conext.WebServiceMethods.WebService.getParticularMyAttendedEvents;
import static com.conext.conext.db.DbConstants.COMPANY_DISPLAY;
import static com.conext.conext.db.DbConstants.EVENT_KEY;
import static com.conext.conext.db.DbConstants.EVENT_TITLE;
import static com.conext.conext.db.DbConstants.IMAGE_KEY;
import static com.conext.conext.db.DbConstants.NAME;
import static com.conext.conext.db.DbConstants.PROFILE_PIC;
import static com.conext.conext.db.DbConstants.TAG_NAME;
import static com.conext.conext.db.DbConstants.TITLE_DISPLAY;
import static com.conext.conext.db.DbConstants.USER_KEY;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.GET_OTHER_PROFILE_DETAILS;
import static com.conext.conext.utils.Utility.isColorDark;
import static com.conext.conext.utils.Utility.menuIconColor;
import static com.conext.conext.utils.Utility.showDialogue;

public class OtherProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView eventsAttendedImageView, coffeeConnectImageView, lunchMeetImageView, alumniImageView,
            mentorShipImageView, headerImageView;
    private TextView eventsAttendedTextView, coffeeConnectTextView, lunchMeetTextView, alumniTextView,
            mentorShipTextView, subtitleTextView, otherInterestText, eventsAttend;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private FloatingActionButton fab_other;
    boolean flag = false;
    private LinearLayout mentorCardView, menteeCardView;
    private ArrayList<USER_EVENT> otherProfileEventsArrayList = new ArrayList<>();
    private Drawable navDrawable, overflowIcon;
    private RecyclerAdapterProfileEvents otherProfileEventsAdapter = null;

    private String id;
    private String userName, imageUrl, companyName;

    public static final String COFFEE_CONNECT_EVENT_TYPE_KEY = "4";
    public static final String LUNCH_MEET_EVENT_TYPE_KEY = "3";
    public static final String ALUMNI_MEET_EVENT_TYPE_KEY = "2";
    public static final String MENTOR_SHIP_MEET_EVENT_TYPE_KEY = "1";
    public static final String DEFAULT = "1,2,3,4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        id = String.valueOf(getIntent().getExtras().getInt("id"));
        if (id.equals("0")) {
            id = getIntent().getExtras().getString("id");
            Log.e("tag", "id --> " + id);
        }
        //addressing view
        addressingView();

        setupToolbar();

        gettingBasicProfileDetails();

        fab();

        displayDetails();
        //attaching listeners
        listeners();
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

    private void settingAdapters(List<String> mentorSkillArrayList, List<String> menteeSkillArrayList, List<String> skillOtherArrayList) {


        RecyclerView mentorOtherRecyclerView = (RecyclerView) findViewById(R.id.recycler_other_mentor);
        RecyclerView menteeOtherRecyclerView = (RecyclerView) findViewById(R.id.recycler_other_mentee);
        RecyclerView otherInterestRecyclerView = (RecyclerView) findViewById(R.id.recycler_other_other);

        if (!mentorSkillArrayList.isEmpty() && !mentorSkillArrayList.contains("")) {
            mentorCardView.setVisibility(View.VISIBLE);
        } else {
            mentorCardView.setVisibility(View.GONE);
        }
        if (!menteeSkillArrayList.isEmpty() && !menteeSkillArrayList.contains("")) {
            menteeCardView.setVisibility(View.VISIBLE);
        } else {
            menteeCardView.setVisibility(View.GONE);
        }
        if (skillOtherArrayList.isEmpty() || skillOtherArrayList.contains("")) {
            otherInterestText.setVisibility(View.GONE);
            View otherView = (View) findViewById(R.id.other_view);
            otherView.setVisibility(View.GONE);
            otherInterestRecyclerView.setVisibility(View.GONE);
        }


        RecyclerAdapterProfileInfo otherMenteeAdapter = new RecyclerAdapterProfileInfo(OtherProfileActivity.this, menteeSkillArrayList, R.layout.other_profile_mentee);

        menteeOtherRecyclerView.setLayoutManager(new LinearLayoutManager(OtherProfileActivity.this));
        menteeOtherRecyclerView.setHasFixedSize(true);
        menteeOtherRecyclerView.setAdapter(otherMenteeAdapter);

        RecyclerAdapterProfileInfo skillOtherAdapter = new RecyclerAdapterProfileInfo(OtherProfileActivity.this, skillOtherArrayList, R.layout.other_profile_other_interest);

        otherInterestRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        otherInterestRecyclerView.setHasFixedSize(true);
        otherInterestRecyclerView.setAdapter(skillOtherAdapter);

        RecyclerAdapterProfileInfo skillMentorOtherAdapter = new RecyclerAdapterProfileInfo(OtherProfileActivity.this, mentorSkillArrayList, R.layout.other_profile_mentor);

        mentorOtherRecyclerView.setLayoutManager(new LinearLayoutManager(OtherProfileActivity.this));
        mentorOtherRecyclerView.setHasFixedSize(true);
        mentorOtherRecyclerView.setAdapter(skillMentorOtherAdapter);

    }

    private void gettingBasicProfileDetails() {

        Log.e("tag", "loaded  " + GET_OTHER_PROFILE_DETAILS + "/" + id);

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_OTHER_PROFILE_DETAILS + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray response;
                List<String> mentorSkillArrayList = new ArrayList<>();
                List<String> menteeSkillArrayList = new ArrayList<>();
                List<String> skillOtherArrayList = new ArrayList<>();
                try {
                    response = new JSONArray(s);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        userName = obj.getString(TITLE_DISPLAY);
                        companyName = obj.getString(COMPANY_DISPLAY);
                        imageUrl = obj.getString(PROFILE_PIC);

                        if (obj.has("other")) {
                            skillOtherArrayList.addAll(Arrays.asList(obj.getString("other").split("\\s*,\\s*")));
                            Log.e("tag", "obj.getString(\"other\") --> " + obj.getString("other"));
                        }
                        if (obj.has("mentee")) {
                            menteeSkillArrayList.addAll(Arrays.asList(obj.getString("mentee").split("\\s*,\\s*")));
                            Log.e("tag", "obj.getString(\"mentee\") --> " + obj.getString("mentee"));
                        }
                        if (obj.has("mentor")) {
                            mentorSkillArrayList.addAll(Arrays.asList(obj.getString("mentor").split("\\s*,\\s*")));
                            Log.e("tag", "obj.getString(\"mentor\") --> " + obj.getString("mentor"));
                        }
                    }

                    Log.e("Mentor", "" + mentorSkillArrayList.contains(""));
                    Log.e("Mentee", "" + menteeSkillArrayList.contains(""));
                    Log.e("other", "" + skillOtherArrayList.contains(""));

                    setupCollapsingToolbar();

                    settingAdapters(mentorSkillArrayList, menteeSkillArrayList, skillOtherArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
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
                showDialogue(OtherProfileActivity.this, "Sorry! Server Error", "Oops!!!");
            }
        });

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(OtherProfileActivity.this);
        rQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
         /* case R.id.other_profile_favorite:
                Toast.makeText(getAppContext(), "You have clicked Favorites", Toast.LENGTH_LONG).show();
                break;*/
            case R.id.other_profile_share:
                Toast.makeText(OtherProfileActivity.this, "You have clicked Share", Toast.LENGTH_LONG).show();
                break;
            case R.id.other_profile_invite:
                Toast.makeText(OtherProfileActivity.this, "You have clicked Invite", Toast.LENGTH_LONG).show();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void fab() {
        fab_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    fab_other.setImageResource(R.drawable.ic_star_border_white_24dp);
                    flag = false;
                } else {
                    fab_other.setImageResource(R.drawable.ic_star_white_24dp);
                    flag = true;
                }
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setupCollapsingToolbar() {

        Glide.with(OtherProfileActivity.this)
                .load(BaseIP + "/" + imageUrl)
                .asBitmap()
                .into(headerImageView);

        collapsingToolbar.setTitleEnabled(true);
        collapsingToolbar.setTitle(userName);

        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);

        subtitleTextView = (TextView) findViewById(R.id.other_subtitle);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                Drawable drawableOverflow = toolbar.getOverflowIcon();
                Drawable drawable = toolbar.getNavigationIcon();
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(OtherProfileActivity.this, R.color.colorPrimary));
                    subtitleTextView.setVisibility(View.GONE);
                    menuIconColor(drawableOverflow, R.color.black, OtherProfileActivity.this);
                    menuIconColor(drawable, R.color.black, OtherProfileActivity.this);
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(OtherProfileActivity.this, android.R.color.transparent));
                    subtitleTextView.setText(companyName);
                    menuIconColor(drawableOverflow, R.color.white, OtherProfileActivity.this);
                    menuIconColor(drawable, R.color.white, OtherProfileActivity.this);
                    subtitleTextView.setVisibility(View.VISIBLE);
                }

            }
        });

        if (headerImageView.getDrawable() != null) {
            Palette.from(((BitmapDrawable) headerImageView.getDrawable()).getBitmap()).maximumColorCount(16).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrant = palette.getDominantSwatch();
                    overflowIcon = toolbar.getOverflowIcon();
                    navDrawable = toolbar.getNavigationIcon();
                    if (vibrant != null) {
                        if (isColorDark(vibrant.getRgb())) {
                            subtitleTextView.setTextColor(Color.WHITE);
                            collapsingToolbar.setExpandedTitleColor(Color.WHITE);
                            menuIconColor(overflowIcon, R.color.white, OtherProfileActivity.this);
                            menuIconColor(navDrawable, R.color.white, OtherProfileActivity.this);
                        } else {
                            subtitleTextView.setTextColor(Color.BLACK);
                            collapsingToolbar.setExpandedTitleColor(Color.BLACK);
                            menuIconColor(overflowIcon, R.color.black, OtherProfileActivity.this);
                            menuIconColor(navDrawable, R.color.black, OtherProfileActivity.this);
                        }
                    }
                }
            });
        }
    }

    private void displayDetails() {

        getParticularMyAttendedEvents(id, DEFAULT, OtherProfileActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray response;
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
                        otherProfileEventsArrayList.add(userEvent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", e.getMessage());
                }
                settingProfileAdapterCount();
            }
        });
    }

    private static ArrayList<USER_EVENT> settingResponse(String result, ArrayList<USER_EVENT> profileEventsArrayList) {
        JSONArray response;
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

    private void settingProfileAdapterCount() {
        otherProfileEventsAdapter = new RecyclerAdapterProfileEvents(OtherProfileActivity.this, otherProfileEventsArrayList);

        RecyclerView eventsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_profile_other);
        RecyclerView.LayoutManager eventManager = new LinearLayoutManager(OtherProfileActivity.this);
        eventsRecyclerView.setLayoutManager(eventManager);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setAdapter(otherProfileEventsAdapter);

        int attendEvent = otherProfileEventsArrayList.size();
        if (attendEvent == 0) {
            eventsAttend.setVisibility(View.GONE);
        }
        String eventFinalCount;
        if (attendEvent < 10) {
            eventFinalCount = "" + 0 + attendEvent;
        } else {
            eventFinalCount = "" + attendEvent;
        }
        eventsAttendedTextView.setText(eventFinalCount);

        getParticularMyAttendedEvents(id, COFFEE_CONNECT_EVENT_TYPE_KEY, OtherProfileActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, new ArrayList<USER_EVENT>());
                int coffeeConnectCount = finalProfileEventsArrayList.size();
                coffeeConnectCount(coffeeConnectCount);
            }
        });

        getParticularMyAttendedEvents(id, LUNCH_MEET_EVENT_TYPE_KEY, OtherProfileActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, new ArrayList<USER_EVENT>());
                int lunchMeetCount = finalProfileEventsArrayList.size();
                lunchMeetCount(lunchMeetCount);
            }
        });

        getParticularMyAttendedEvents(id, ALUMNI_MEET_EVENT_TYPE_KEY, OtherProfileActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, new ArrayList<USER_EVENT>());
                int alumniCount = finalProfileEventsArrayList.size();
                alumniCount(alumniCount);
            }
        });

        getParticularMyAttendedEvents(id, MENTOR_SHIP_MEET_EVENT_TYPE_KEY, OtherProfileActivity.this, new VolleyCallback() {
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
        mentorShipTextView.setText(mentorShip);
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
        mentorShipImageView.setOnClickListener(this);
    }

    private void addressingView() {

        eventsAttendedImageView = (ImageView) findViewById(R.id.event_attend_prof_other);
        coffeeConnectImageView = (ImageView) findViewById(R.id.event_coffee_prof_other);
        lunchMeetImageView = (ImageView) findViewById(R.id.event_lunch_prof_other);
        alumniImageView = (ImageView) findViewById(R.id.event_alumini_prof_other);
        mentorShipImageView = (ImageView) findViewById(R.id.event_mentor_prof_other);

        eventsAttendedTextView = (TextView) findViewById(R.id.event_prof_attended_other);

        coffeeConnectTextView = (TextView) findViewById(R.id.event_coffee_attended_other);
        lunchMeetTextView = (TextView) findViewById(R.id.event_lunch_attended_other);
        alumniTextView = (TextView) findViewById(R.id.event_alumni_meet_other);
        mentorShipTextView = (TextView) findViewById(R.id.event_prof_ship_other);


        eventsAttend = (TextView) findViewById(R.id.events_attended_text_view);

        mentorCardView = (LinearLayout) findViewById(R.id.mentor_card_other_profile);
        menteeCardView = (LinearLayout) findViewById(R.id.mentee_card_other_profile);
        otherInterestText = (TextView) findViewById(R.id.other_interest_text_other_profile);
        eventsAttend = (TextView) findViewById(R.id.events_attended_text_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.other_collapsing_toolbar);

        headerImageView = (ImageView) findViewById(R.id.header_image);
        fab_other = (FloatingActionButton) findViewById(R.id.fab_other);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.event_attend_prof_other:

                eventsAttendedImageView.setImageResource(R.drawable.event_attended);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorShipImageView.setImageResource(R.drawable.cc_connect);

                otherProfileEventsArrayList.clear();

                setMyEventsAttended(otherProfileEventsArrayList);

                break;

            case R.id.event_coffee_prof_other:

                eventsAttendedImageView.setImageResource(R.drawable.cc_connect);

                coffeeConnectImageView.setImageResource(R.drawable.event_attended);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorShipImageView.setImageResource(R.drawable.cc_connect);

                getParticularMyAttendedEvents(id, COFFEE_CONNECT_EVENT_TYPE_KEY, OtherProfileActivity.this, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        otherProfileEventsArrayList.clear();
                        ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, otherProfileEventsArrayList);
                        settingRecyclerAdapterProfileEvents(finalProfileEventsArrayList);
                    }
                });


                break;
            case R.id.event_lunch_prof_other:

                eventsAttendedImageView.setImageResource(R.drawable.cc_connect);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.event_attended);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorShipImageView.setImageResource(R.drawable.cc_connect);

                getParticularMyAttendedEvents(id, LUNCH_MEET_EVENT_TYPE_KEY, OtherProfileActivity.this, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        otherProfileEventsArrayList.clear();
                        ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, otherProfileEventsArrayList);
                        settingRecyclerAdapterProfileEvents(finalProfileEventsArrayList);
                    }
                });

                break;
            case R.id.event_alumini_prof_other:

                eventsAttendedImageView.setImageResource(R.drawable.cc_connect);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.event_attended);

                mentorShipImageView.setImageResource(R.drawable.cc_connect);


                getParticularMyAttendedEvents(id, ALUMNI_MEET_EVENT_TYPE_KEY, OtherProfileActivity.this, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        otherProfileEventsArrayList.clear();
                        ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, otherProfileEventsArrayList);
                        settingRecyclerAdapterProfileEvents(finalProfileEventsArrayList);
                    }
                });

                break;
            case R.id.event_mentor_prof_other:

                eventsAttendedImageView.setImageResource(R.drawable.cc_connect);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorShipImageView.setImageResource(R.drawable.event_attended);

                getParticularMyAttendedEvents(id, MENTOR_SHIP_MEET_EVENT_TYPE_KEY, OtherProfileActivity.this, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        otherProfileEventsArrayList.clear();
                        ArrayList<USER_EVENT> finalProfileEventsArrayList = settingResponse(result, otherProfileEventsArrayList);
                        settingRecyclerAdapterProfileEvents(finalProfileEventsArrayList);
                    }
                });

                break;
            default:

                eventsAttendedImageView.setImageResource(R.drawable.event_attended);

                coffeeConnectImageView.setImageResource(R.drawable.cc_connect);

                lunchMeetImageView.setImageResource(R.drawable.cc_connect);

                alumniImageView.setImageResource(R.drawable.cc_connect);

                mentorShipImageView.setImageResource(R.drawable.cc_connect);

                setMyEventsAttended(otherProfileEventsArrayList);
        }

    }

    private void setMyEventsAttended(ArrayList<USER_EVENT> profileEventsArrayList) {

        final ArrayList<USER_EVENT> finalProfileEventsArrayList = profileEventsArrayList;
        getParticularMyAttendedEvents(id, DEFAULT, OtherProfileActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ArrayList<USER_EVENT> profileEventsArrayList = settingResponse(result, finalProfileEventsArrayList);
                settingRecyclerAdapterProfileEvents(profileEventsArrayList);
            }
        });
    }

    private void settingRecyclerAdapterProfileEvents(ArrayList<USER_EVENT> profileEventsArrayList) {

        if (profileEventsArrayList.isEmpty()) {
            eventsAttend.setVisibility(View.GONE);
        } else {
            eventsAttend.setVisibility(View.VISIBLE);
        }

        otherProfileEventsAdapter.notifyDataSetChanged();
    }

}