package com.conext.conext.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.conext.conext.Adapter.ViewPagerAdapter;
import com.conext.conext.AppManager;
import com.conext.conext.R;
import com.conext.conext.ui.Fragments.Alumni;
import com.conext.conext.ui.Fragments.Events;
import com.conext.conext.ui.Fragments.Info;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.conext.conext.db.DbConstants.COMPANY_DISPLAY;
import static com.conext.conext.db.DbConstants.PROFILE_PIC;
import static com.conext.conext.db.DbConstants.TITLE_DISPLAY;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.GET_PROFILE_BASIC_DETAILS;
import static com.conext.conext.utils.Utility.isColorDark;
import static com.conext.conext.utils.Utility.menuIconColor;
import static com.conext.conext.utils.Utility.showDialogue;

/**
 * Created by Ashith VL on 6/9/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView subtitleTextView;
    private CollapsingToolbarLayout collapsingToolbar;

    private Drawable navDrawable, overflowIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setupToolbar();
        setupViewPager();

        displayDetails();

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

    private void displayDetails() {

        Log.e("tag", "loaded  " + GET_PROFILE_BASIC_DETAILS + "/" + Prefs.getUserKey());

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_PROFILE_BASIC_DETAILS + "/" + Prefs.getUserKey(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray response;
                String userName = null, imageUrl = null, companyName = null;
                try {
                    response = new JSONArray(s);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        userName = obj.getString(TITLE_DISPLAY);
                        companyName = obj.getString(COMPANY_DISPLAY);
                        imageUrl = obj.getString(PROFILE_PIC);

                    }
                    setupCollapsingToolbar(userName, companyName, imageUrl);

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
                showDialogue(ProfileActivity.this, "Sorry! Server Error", "Oops!!!");
            }
        });

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(ProfileActivity.this);
        rQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {

            case R.id.profile_mentoship:
                Intent intent4 = new Intent(ProfileActivity.this, CreateEventActivity.class);
                bundle.putString("value", "0");
                intent4.putExtras(bundle);
                startActivity(intent4);
                break;
            case R.id.profile_alumni:
                Intent intent3 = new Intent(ProfileActivity.this, CreateEventActivity.class);
                bundle.putString("value", "1");
                intent3.putExtras(bundle);
                startActivity(intent3);
                break;
            case R.id.profile_lunch:
                Intent intent2 = new Intent(ProfileActivity.this, CreateEventActivity.class);
                bundle.putString("value", "2");
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.profile_coffee:
                Intent intent1 = new Intent(ProfileActivity.this, CreateEventActivity.class);
                bundle.putString("value", "3");
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void setupCollapsingToolbar(String name, final String companyName, String imageUrl) {

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView headerImageView = (ImageView) findViewById(R.id.header_image);

        Glide.with(ProfileActivity.this)
                .load(BaseIP + "/" + imageUrl)
                .asBitmap()
                .into(headerImageView);

        collapsingToolbar.setTitleEnabled(true);
        collapsingToolbar.setTitle(name);

        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);

        subtitleTextView = (TextView) findViewById(R.id.textViewJob);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, final int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
//                fraToolFloat.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        fraToolFloat.animate().translationY(verticalOffset).start();
//                    }
//                });

                Drawable drawableOverflow = toolbar.getOverflowIcon();
                Drawable drawable = toolbar.getNavigationIcon();

                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorPrimary));
                    subtitleTextView.setVisibility(View.GONE);
                    //fraToolFloat.setVisibility(View.GONE);

                    menuIconColor(drawableOverflow, R.color.black, ProfileActivity.this);
                    menuIconColor(drawable, R.color.black, ProfileActivity.this);
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this, android.R.color.transparent));

                    subtitleTextView.setText(companyName);

                    subtitleTextView.setVisibility(View.VISIBLE);
                    //  fraToolFloat.setVisibility(View.VISIBLE);
                    menuIconColor(drawableOverflow, R.color.white, ProfileActivity.this);
                    menuIconColor(drawable, R.color.white, ProfileActivity.this);
                }
            }
        });

        if (headerImageView.getDrawable() != null) {
            Palette.from(((BitmapDrawable) headerImageView.getDrawable()).getBitmap()).maximumColorCount(16).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrant = palette.getDominantSwatch();

                    if (vibrant != null) {
                        overflowIcon = toolbar.getOverflowIcon();
                        navDrawable = toolbar.getNavigationIcon();

                        if (isColorDark(vibrant.getRgb())) {
                            subtitleTextView.setTextColor(Color.WHITE);
                            collapsingToolbar.setExpandedTitleColor(Color.WHITE);

                            menuIconColor(overflowIcon, R.color.white, ProfileActivity.this);
                            menuIconColor(navDrawable, R.color.white, ProfileActivity.this);
                        } else {
                            collapsingToolbar.setExpandedTitleColor(Color.BLACK);
                            menuIconColor(overflowIcon, R.color.black, ProfileActivity.this);
                            menuIconColor(navDrawable, R.color.black, ProfileActivity.this);
                        }
                    }
                }
            });
        }
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

    private void setupViewPager() {

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //Initializing the tabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new Info(), "INFO");
        adapter.addFrag(new Alumni(), "ALUMNI");
        adapter.addFrag(new Events(), "EVENTS");

        viewPager.setAdapter(adapter);
    }


}
