package com.conext.conext;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.conext.conext.Adapter.DrawerListAdapter;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.ui.CreateEventActivity;
import com.conext.conext.ui.Fragments.EventsFragment;
import com.conext.conext.ui.Fragments.MyMenteesFragment;
import com.conext.conext.ui.Fragments.MyMentorsFragment;
import com.conext.conext.ui.Fragments.NetworkFragment;
import com.conext.conext.ui.Fragments.NotificationFragment;
import com.conext.conext.ui.LoginActivity;
import com.conext.conext.ui.ProfileActivity;
import com.conext.conext.ui.custom.HexagonMaskView;
import com.conext.conext.ui.custom.NavigationViewItemDecorator;
import com.conext.conext.utils.ItemClickListener;
import com.conext.conext.utils.NetworkController;
import com.conext.conext.utils.Prefs;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.conext.conext.WebServiceMethods.WebService.getAluminiDetails;
import static com.conext.conext.db.DbConstants.DEGREE;
import static com.conext.conext.db.DbConstants.FIELD_OF_STUDY;
import static com.conext.conext.db.DbConstants.SCHOOL_NAME;
import static com.conext.conext.db.DbConstants.YEAR_FINISH;
import static com.conext.conext.db.DbConstants.YEAR_START;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.USER_EMAIL_URL;
import static com.conext.conext.utils.Constants.USER_IMAGE_URL;
import static com.conext.conext.utils.Utility.menuIconColor;
import static com.conext.conext.utils.Utility.showDialogue;

/**
 * Created by Ashith VL on 10/14/2017.
 */

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private TextView headerText;
    private HexagonMaskView porterShapeImageView;
    private RequestQueue requestQueue;
    private DrawerListAdapter drawerListAdapter;

    private static int selectedPosition = 0;

    public static int[] drawer_icons = {R.drawable.net_menu,
            R.drawable.event_menu, R.mipmap.ic_launcher_event_try, R.drawable.mentees_menu,
            R.drawable.mentors_menu, R.drawable.book, R.drawable.book,
            R.drawable.study, R.drawable.study, R.drawable.study,
            R.drawable.study, R.drawable.study};

    private ArrayList<String> navigation_items;

    private RecyclerView lv_drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv_drawer = (RecyclerView) findViewById(R.id.lv_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        requestQueue = NetworkController.getInstance(HomeActivity.this).getRequestQueue();

        navigationDrawer();

        SetDrawer();

        navHeader();

        getAluminiDetails(Prefs.getUserKey(), HomeActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            navigation_items.set(6, obj.getString(DEGREE) + " , " + obj.getString(FIELD_OF_STUDY));
                            navigation_items.set(7, obj.getString(YEAR_START) + " - " + obj.getString(YEAR_FINISH));
                            navigation_items.set(8, obj.getString(SCHOOL_NAME));
                        } catch (Exception e) {
                            Log.d("error", e.getMessage());
                        }
                    }
                    drawerListAdapter.notifyItemRangeChanged(6,8);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        displaySelectedScreen(R.id.network_view);
    }

    private void navHeader() {

        RelativeLayout linearLayoutUserProfile = (RelativeLayout) findViewById(R.id.header);
        headerText = (TextView) findViewById(R.id.name);
        porterShapeImageView = (HexagonMaskView) findViewById(R.id.image_view_profile_pic);
        porterShapeImageView.setRadius(1000f);
        porterShapeImageView.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

        StringRequest emailRequest = new StringRequest(Request.Method.GET, USER_EMAIL_URL + "/" + Prefs.getUserKey(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                headerText.setText(s);
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
                showDialogue(HomeActivity.this, "Sorry! Server Error", "Oops!!!");
            }
        });

        requestQueue.add(emailRequest);

        //Requests the data from webservice using volley
        StringRequest imageRequest = new StringRequest(Request.Method.GET, USER_IMAGE_URL + "/" + Prefs.getUserKey(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Glide.with(HomeActivity.this)
                        .load(BaseIP + "/" + s)
                        .thumbnail(0.5f)
                        .into(porterShapeImageView);
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
                showDialogue(HomeActivity.this, "Sorry! Server Error", "Oops!!!");
            }
        });

        requestQueue.add(imageRequest);

        linearLayoutUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
    }

    private void navigationDrawer() {
        // Initializing Drawer Layout and ActionBarToggle

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }

    private void SetDrawer() {

        init();

        Drawable drawable = toolbar.getNavigationIcon();
        menuIconColor(drawable, R.color.black, HomeActivity.this);

        drawerListAdapter = new DrawerListAdapter(HomeActivity.this, navigation_items, drawer_icons);
        lv_drawer.setHasFixedSize(true);
        lv_drawer.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        lv_drawer.addItemDecoration(new NavigationViewItemDecorator());
        lv_drawer.setAdapter(drawerListAdapter);

        drawerListAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                TextView tv_title = (TextView) v.findViewById(R.id.tv_title);

                if (pos == 0) {
                    displaySelectedScreen(R.id.network_view);
                //    tv_title.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                    drawerLayout.closeDrawers();
                } else if (pos == 1) {
                    displaySelectedScreen(R.id.event);
               //     tv_title.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                    drawerLayout.closeDrawers();
                } else if (pos == 2) {
                    displaySelectedScreen(R.id.my_event);
              //      tv_title.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                    drawerLayout.closeDrawers();
                } else if (pos == 3) {
                    displaySelectedScreen(R.id.my_mentees);
              //      tv_title.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                    drawerLayout.closeDrawers();
                } else if (pos == 4) {
                    displaySelectedScreen(R.id.my_mentors);
                //    tv_title.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                    drawerLayout.closeDrawers();
                } else if (pos == 5 || pos == 6 || pos == 7 || pos == 8) {
                    v.setClickable(false);
                    v.setEnabled(false);
                } else if (pos == 9) {
                    displaySelectedScreen(R.id.create_event);
           //         tv_title.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                    drawerLayout.closeDrawers();
                } else if (pos == 10) {
                    displaySelectedScreen(R.id.create_mentorship);
           //         tv_title.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                    drawerLayout.closeDrawers();
                } else if (pos == 11) {
                    displaySelectedScreen(R.id.sign_out);
           //         tv_title.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                    drawerLayout.closeDrawers();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    tv_title.setTextColor(Color.parseColor("#80000000"));
                }
            }

        });


    }

    private void init() {

        navigation_items = new ArrayList<>();

        //adding menu items for navigationList
        navigation_items.add("Network View");
        navigation_items.add("Events");
        navigation_items.add("My Events");
        navigation_items.add("My Mentees");
        navigation_items.add("My Mentors");
        navigation_items.add("My Alumni");
        navigation_items.add("colg");
        navigation_items.add("degree");
        navigation_items.add("year");
        navigation_items.add("Create an Event");
        navigation_items.add("Create a Mentorship Program");
        navigation_items.add("Sign Out");

    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.network_view:
                fragment = networkFragment();
                selectedPosition = 0;
                MenuItem nMenuItem1 = toolbar.getMenu().findItem(R.id.net_img);
                MenuItem eMenuItem1 = toolbar.getMenu().findItem(R.id.event_switch);
                MenuItem notiMenuItem1 = toolbar.getMenu().findItem(R.id.notification_tool);
                Drawable nDrawable1;
                if (nMenuItem1 != null && eMenuItem1 != null && notiMenuItem1 != null) {
                    nDrawable1 = nMenuItem1.getIcon();
                    eMenuItem1.setIcon(R.drawable.ic_event_black_24dp);
                    notiMenuItem1.setIcon(R.drawable.notification_image);
                    menuIconColor(nDrawable1, R.color.colorAccent, HomeActivity.this);
                }
                break;
            case R.id.event:
                eventFragment(0);
                selectedPosition = 1;
                MenuItem nMenuItem2 = toolbar.getMenu().findItem(R.id.net_img);
                MenuItem eMenuItem2 = toolbar.getMenu().findItem(R.id.event_switch);
                MenuItem notiMenuItem2 = toolbar.getMenu().findItem(R.id.notification_tool);
                Drawable eDrawable2;
                if (nMenuItem2 != null && eMenuItem2 != null && notiMenuItem2 != null) {
                    eDrawable2 = eMenuItem2.getIcon();
                    nMenuItem2.setIcon(R.drawable.net_img);
                    notiMenuItem2.setIcon(R.drawable.notification_image);
                    menuIconColor(eDrawable2, R.color.colorAccent, HomeActivity.this);
                }
                break;
            case R.id.my_event:
                selectedPosition = 6;
                eventFragment(1);
                MenuItem nMenuItem21 = toolbar.getMenu().findItem(R.id.net_img);
                MenuItem eMenuItem21 = toolbar.getMenu().findItem(R.id.event_switch);
                MenuItem notiMenuItem21 = toolbar.getMenu().findItem(R.id.notification_tool);
                Drawable eDrawable21;
                if (nMenuItem21 != null && eMenuItem21 != null && notiMenuItem21 != null) {
                    eDrawable21 = eMenuItem21.getIcon();
                    nMenuItem21.setIcon(R.drawable.net_img);
                    notiMenuItem21.setIcon(R.drawable.notification_image);
                    menuIconColor(eDrawable21, R.color.colorAccent, HomeActivity.this);
                }

                break;
            case R.id.my_mentees:
                fragment = new MyMenteesFragment();

                selectedPosition = 2;
                MenuItem nMenuItem3 = toolbar.getMenu().findItem(R.id.net_img);
                MenuItem eMenuItem3 = toolbar.getMenu().findItem(R.id.event_switch);
                MenuItem notiMenuItem3 = toolbar.getMenu().findItem(R.id.notification_tool);
                if (nMenuItem3 != null && eMenuItem3 != null && notiMenuItem3 != null) {
                    nMenuItem3.setIcon(R.drawable.net_img);
                    eMenuItem3.setIcon(R.drawable.ic_event_black_24dp);
                    notiMenuItem3.setIcon(R.drawable.notification_image);
                }
                break;
            case R.id.my_mentors:
                fragment = new MyMentorsFragment();
                selectedPosition = 3;
                MenuItem nMenuItem4 = toolbar.getMenu().findItem(R.id.net_img);
                MenuItem eMenuItem4 = toolbar.getMenu().findItem(R.id.event_switch);
                MenuItem notiMenuItem4 = toolbar.getMenu().findItem(R.id.notification_tool);
                if (nMenuItem4 != null && eMenuItem4 != null && notiMenuItem4 != null) {
                    nMenuItem4.setIcon(R.drawable.net_img);
                    eMenuItem4.setIcon(R.drawable.ic_event_black_24dp);
                    notiMenuItem4.setIcon(R.drawable.notification_image);
                }
                break;
            case R.id.create_event:
                selectedPosition = 4;
                Intent createEventIntent = new Intent(new Intent(HomeActivity.this, CreateEventActivity.class));
                createEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(createEventIntent);
                break;
            case R.id.create_mentorship:
                //navigationView.getMenu().findItem(R.id.create_mentorship).setChecked(false);
                selectedPosition = 5;
                 /* fragment=new CreateEventActivity();*/
                break;

            case R.id.sign_out:
                LoginManager.getInstance().logOut();
                break;
        }

        if (fragment != null) {
            if ((fragment instanceof EventsFragment)) {
                return;
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else if (selectedPosition != 0) {
            displaySelectedScreen(R.id.network_view);
        }
        AppManager.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppManager.activityPaused();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else if (selectedPosition != 0) {
            displaySelectedScreen(R.id.network_view);
        } else

        {
            /*new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            HomeActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();*/
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        Drawable defaultDrawable = toolbar.getMenu().findItem(R.id.net_img).getIcon();
        menuIconColor(defaultDrawable, R.color.colorAccent, HomeActivity.this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;
        Drawable networkDrawable;
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.net_img:
                fragment = networkFragment();
                item.setChecked(true);
                selectedPosition = 0;
                //  navigationView.getMenu().getItem(0).setChecked(true);
                networkDrawable = item.getIcon();
                menuIconColor(networkDrawable, R.color.colorAccent, HomeActivity.this);
                toolbar.getMenu().findItem(R.id.event_switch).setIcon(R.drawable.ic_event_black_24dp);
                toolbar.getMenu().findItem(R.id.notification_tool).setIcon(R.drawable.notification_image);
                break;
            // action with ID action_settings was selected
            case R.id.event_switch:
                eventFragment(0);
                item.setChecked(true);
                selectedPosition = 1;
                //  navigationView.getMenu().getItem(1).setChecked(true);
                Drawable eventDrawable = item.getIcon();
                menuIconColor(eventDrawable, R.color.colorAccent, HomeActivity.this);
                toolbar.getMenu().findItem(R.id.net_img).setIcon(R.drawable.net_img);
                toolbar.getMenu().findItem(R.id.notification_tool).setIcon(R.drawable.notification_image);
                break;
            case R.id.notification_tool:
                fragment = new NotificationFragment();
                item.setChecked(true);
                selectedPosition = 7;
                Drawable notificationDrawable = item.getIcon();
                menuIconColor(notificationDrawable, R.color.colorAccent, HomeActivity.this);
                toolbar.getMenu().findItem(R.id.net_img).setIcon(R.drawable.net_img);
                toolbar.getMenu().findItem(R.id.event_switch).setIcon(R.drawable.ic_event_black_24dp);
                //  navigationView.getMenu().findItem(R.id.network_view).setChecked(true);
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result", "got result 0 ,Don't Remove");
    }


    private void eventFragment(int index) {

        Fragment fragment = new EventsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment, "event");
        ft.commit();
    }

    private Fragment networkFragment() {
        return new NetworkFragment();
    }

}
