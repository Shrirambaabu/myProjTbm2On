package com.conext.conext.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.conext.conext.Adapter.ViewPagerAdapter;
import com.conext.conext.AppManager;
import com.conext.conext.HomeActivity;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.AllSkill;
import com.conext.conext.model.ProfileAlumni;
import com.conext.conext.ui.Fragments.AppTagsFragment;
import com.conext.conext.ui.Fragments.InfoFragment;
import com.conext.conext.ui.Fragments.MenteeFragment;
import com.conext.conext.ui.Fragments.MentorFragment;
import com.conext.conext.utils.IFragmentToActivity;
import com.conext.conext.utils.Prefs;
import com.conext.conext.utils.Utility;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.conext.conext.WebServiceMethods.WebService.addUserSKill;
import static com.conext.conext.WebServiceMethods.WebService.getAluminiDetails;
import static com.conext.conext.WebServiceMethods.WebService.getCompanyName;
import static com.conext.conext.WebServiceMethods.WebService.getName;
import static com.conext.conext.WebServiceMethods.WebService.getTagKey;
import static com.conext.conext.WebServiceMethods.WebService.settLocation;
import static com.conext.conext.WebServiceMethods.WebService.updateProfilePicture;
import static com.conext.conext.db.DbConstants.DEGREE;
import static com.conext.conext.db.DbConstants.FIELD_OF_STUDY;
import static com.conext.conext.db.DbConstants.SCHOOL_NAME;
import static com.conext.conext.db.DbConstants.YEAR_FINISH;
import static com.conext.conext.db.DbConstants.YEAR_START;
import static com.conext.conext.utils.Utility.animateFab;
import static com.conext.conext.utils.Utility.hideProgressDialog;
import static com.conext.conext.utils.Utility.isColorDark;
import static com.conext.conext.utils.Utility.menuIconColor;
import static com.conext.conext.utils.Utility.showProgressDialog;

public class RegistrationPersonaliseActivity extends AppCompatActivity implements IFragmentToActivity, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Toolbar toolbar;
    private TextView subtitleTextView;
    private CollapsingToolbarLayout collapsingToolbar;
    private ViewPagerAdapter adapter;
    private ProgressDialog pDialog;
    private ArrayList<AllSkill> userTagArrayList = new ArrayList<>();
    private ArrayList<AllSkill> menteeTagArrayList = new ArrayList<>();
    private ArrayList<AllSkill> mentorTagArrayList = new ArrayList<>();
    private Uri uriPath = null;
    private long tagKey;
    private LocationManager manager;

    //  private int GALLERY_KITKAT_INTENT_CALLED = 11;

    //  private int menuOption = 0;

    private ImageView imageView;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;

    private String address = null;

    private Drawable editDrawable, doneDrawable, overflowIcon;
    public FloatingActionButton floatingActionButtonAddInfo;

    private String companyName;
    private ArrayList<ProfileAlumni> alumni = new ArrayList<>();

    private boolean pic = false;
    // private boolean skill = false;
    private boolean info = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_personalise);

        pDialog = new ProgressDialog(RegistrationPersonaliseActivity.this, R.style.MyThemeProgress);

        floatingActionButtonAddInfo = (FloatingActionButton) findViewById(R.id.fab_add);
        imageView = (ImageView) findViewById(R.id.header_image);
        subtitleTextView = (TextView) findViewById(R.id.subtitle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("");

        //editMode = Prefs.getEditMode();

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Utility.showGPSSettingsAlert(RegistrationPersonaliseActivity.this);
        }

        buildGoogleApiClient();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000);
        locationRequest.setFastestInterval(15 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//      accessToken = Prefs.getAccessTokenFromPrefs();

        setupToolbar();

        setupViewPager();

        setupCollapsingToolbar();
/*
        LoginManager.getInstance().logOut();*/

        imageViewClickListener(imageView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        AppManager.activityPaused();
    }

    private void imageViewClickListener(ImageView imageView) {

        EasyImage.configuration(this)
                .setImagesFolderName("Conext")
                .saveInRootPicturesDirectory()
                .setCopyExistingPicturesToPublicLocation(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(RegistrationPersonaliseActivity.this, "Choose Image from ...", 0);
            }
        });

    }

    public synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        MenuItem doneMenuItem = menu.findItem(R.id.done);

        doneDrawable = doneMenuItem.getIcon();
        changeBasedOnImage(imageView);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                byte[] inputData = new byte[0];
                if (uriPath != null) {
                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(uriPath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputData = Utility.getBytes(iStream);
                        pic = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    inputData = byteArrayOutputStream.toByteArray();

//                    Drawable defaultImage = ContextCompat.getDrawable(RegistrationPersonaliseActivity.this, R.drawable.profile);
//                    Bitmap bitmap = ((BitmapDrawable) defaultImage).getBitmap();
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    inputData = stream.toByteArray();
                    pic = true;
                }

                showProgressDialog(pDialog);
                Log.e("tag", "updateProfilePicture called");
                updateProfilePicture(RegistrationPersonaliseActivity.this, inputData, Prefs.getUserKey(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("true")) {
                            getAluminiDetails(Prefs.getUserKey(), RegistrationPersonaliseActivity.this, new VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    JSONArray response;
                                    try {
                                        response = new JSONArray(result);
                                        if (response.length() > 0) {
                                            Log.e("tag", "updateProfilePicture closed");
                                            if (userTagArrayList.size() > 1) {
                                                Handler handler = new Handler();
                                                Runnable r1 = new Runnable() {
                                                    public void run() {
                                                        handleOtherSkills();
                                                    }
                                                };
                                                handler.postDelayed(r1, userTagArrayList.size() * 1000 + 3000);
                                                Runnable r2 = new Runnable() {
                                                    public void run() {
                                                        handleMentorSkill();
                                                    }
                                                };
                                                handler.postDelayed(r2, mentorTagArrayList.size() * 1000 + 6000);
                                                Runnable r3 = new Runnable() {
                                                    public void run() {
                                                        handleMenteeSkills();
                                                    }
                                                };
                                                handler.postDelayed(r3, menteeTagArrayList.size() * 1000 + 9000);
                                                Runnable r4 = new Runnable() {
                                                    public void run() {
                                                        handleThree(pic, true, info);
                                                    }
                                                };
                                                handler.postDelayed(r4, 12000);
                                            } else {
                                                // removeUser(Prefs.getUserKey(), RegistrationPersonaliseActivity.this);
                                                Toast.makeText(RegistrationPersonaliseActivity.this, "Atleast two Skill must be Added", Toast.LENGTH_LONG).show();
                                                hideProgressDialog(pDialog);
                                            }
                                        } else {
                                            // removeUser(Prefs.getUserKey(), RegistrationPersonaliseActivity.this);
                                            Toast.makeText(RegistrationPersonaliseActivity.this, "Atleast one Info must be Added", Toast.LENGTH_LONG).show();
                                            hideProgressDialog(pDialog);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            hideProgressDialog(pDialog);
                            Toast.makeText(RegistrationPersonaliseActivity.this, "Some error occurred while updating !", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleOtherSkills() {
        Log.e("tag", "handleOtherSkills called");
        if (userTagArrayList.size() < 2) {
            Toast.makeText(RegistrationPersonaliseActivity.this, "Atleast two Skill must be Added", Toast.LENGTH_LONG).show();
            hideProgressDialog(pDialog);
        } else {
            final ArrayList<AllSkill> otherAllSkills = new ArrayList<>();
            for (AllSkill allUserSkill : userTagArrayList) {
                if (!allUserSkill.isMentor() && !allUserSkill.isMentee()) {
                    otherAllSkills.add(allUserSkill);
                }
            }

            if (otherAllSkills.size() > 0) {
                for (int i = 0; i < otherAllSkills.size(); i++) {
                    getTagKey(otherAllSkills.get(i).getTitle(), RegistrationPersonaliseActivity.this, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            tagKey = Long.parseLong(result);
                            addUserSKill(Prefs.getUserKey(), String.valueOf(tagKey), "0", "0", RegistrationPersonaliseActivity.this, new VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    Log.e("tag", "handleOtherSkills closed");

                                }
                            });
                        }
                    });
                }
            }
        }
    }

    private void handleMentorSkill() {
        Log.e("tag", "handleMentorSkill called");
        //Mentor Skills
        ArrayList<AllSkill> mentorAllSkills = new ArrayList<>();

        for (AllSkill allUserSkill : mentorTagArrayList) {
            if (allUserSkill.isMentor() && !allUserSkill.isMentee()) {
                mentorAllSkills.add(allUserSkill);
            }
        }

        if (mentorAllSkills.size() > 0) {
            for (int i = 0; i < mentorAllSkills.size(); i++) {

                getTagKey(mentorAllSkills.get(i).getTitle(), RegistrationPersonaliseActivity.this, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        tagKey = Long.parseLong(result);
                        addUserSKill(Prefs.getUserKey(), String.valueOf(tagKey), "0", "1", RegistrationPersonaliseActivity.this, new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e("tag", "handleMentorSkill closed");
                            }
                        });
                    }
                });
            }
        }
    }

    private void handleMenteeSkills() {
        Log.e("tag", "handleMenteeSkill called");
        //Mentee skills
        ArrayList<AllSkill> menteeAllSkills = new ArrayList<>();

        for (AllSkill allUserSkill : menteeTagArrayList) {
            if (!allUserSkill.isMentor() && allUserSkill.isMentee()) {
                menteeAllSkills.add(allUserSkill);
            }
        }

        if (menteeAllSkills.size() > 0) {
            for (int i = 0; i < menteeAllSkills.size(); i++) {

                getTagKey(menteeAllSkills.get(i).getTitle(), RegistrationPersonaliseActivity.this, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        tagKey = Long.parseLong(result);
                        addUserSKill(Prefs.getUserKey(), String.valueOf(tagKey), "1", "0", RegistrationPersonaliseActivity.this, new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e("tag", "handleMentorSkill closed");
                            }
                        });
                    }
                });

            }
        }

    }

    private void handleThree(final boolean pic, final boolean skill, final boolean info) {
        Log.e("tag", "handleThree called");
        getAluminiDetails(Prefs.getUserKey(), RegistrationPersonaliseActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        ProfileAlumni profileAlumni = new ProfileAlumni();

                        JSONObject obj = response.getJSONObject(i);
                        profileAlumni.setAlumniStudy(obj.getString(DEGREE) + " , " + obj.getString(FIELD_OF_STUDY));
                        profileAlumni.setAlumniYear(obj.getString(YEAR_START) + " - " + obj.getString(YEAR_FINISH));
                        profileAlumni.setAlumniUniversity(obj.getString(SCHOOL_NAME));
                        alumni.add(profileAlumni);
                    }
                    handleTwo(pic, skill, info, alumni);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Log.e("tag", "handleThree closed");
    }

    private void handleTwo(boolean pic, boolean skill, boolean info, ArrayList<ProfileAlumni> alumni) {

        Log.e("tag", "handleTwo called");
        if (alumni.isEmpty()) {
            Toast.makeText(RegistrationPersonaliseActivity.this, "Atleast one Info must be Added", Toast.LENGTH_LONG).show();
            hideProgressDialog(pDialog);
        } else
            info = true;

        if (pic && skill && info) {
            Prefs.setAllUserSkill(new ArrayList<AllSkill>());
            Prefs.setAllUserMenteeSkill(new ArrayList<AllSkill>());
            Prefs.setAllUserMentorSkill(new ArrayList<AllSkill>());
            Prefs.setUserKey("");
            Prefs.setUserImage(null);
            Prefs.setLoc("");
            //  Prefs.setEditMode(true);
            hideProgressDialog(pDialog);
            startActivity(new Intent(RegistrationPersonaliseActivity.this, HomeActivity.class));
            overridePendingTransition(0, android.R.anim.accelerate_decelerate_interpolator);
            finish();
            Toast.makeText(RegistrationPersonaliseActivity.this, "Registration Successful !!!", Toast.LENGTH_LONG).show();
        } else {
            //  removeUser(Prefs.getUserKey(), RegistrationPersonaliseActivity.this);
            hideProgressDialog(pDialog);
        }

        Log.e("tag", "handleTwo closed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                //compression done here
                File compressedImageFile = null;
                try {
                    compressedImageFile = new Compressor(RegistrationPersonaliseActivity.this).compressToFile(imageFile);
                    // Get length of file in bytes
                    long fileSizeInBytes = compressedImageFile.length();
                    // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;
                    // Checking Less than 1mb
                    if (fileSizeInMB > 1) {
                        Toast.makeText(RegistrationPersonaliseActivity.this, "Sorry !!! File Size Too Large", Toast.LENGTH_LONG).show();
                    } else {
                        uriPath = Uri.fromFile(compressedImageFile);
                        imageView.setImageURI(uriPath);
                        changeBasedOnImage(imageView);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(RegistrationPersonaliseActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });

    }

    private void changeBasedOnImage(ImageView imageView) {
        Palette.from(((BitmapDrawable) imageView.getDrawable()).getBitmap()).maximumColorCount(16).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getDominantSwatch();

                if (toolbar.getOverflowIcon() != null || toolbar.getMenu().findItem(R.id.done) != null
                        || toolbar.getMenu().findItem(R.id.edit) != null) {
                    overflowIcon = toolbar.getOverflowIcon();
                    doneDrawable = toolbar.getMenu().findItem(R.id.done).getIcon();
                    editDrawable = toolbar.getMenu().findItem(R.id.edit).getIcon();

                    if (vibrant != null) {
                        if (isColorDark(vibrant.getRgb())) {
                            subtitleTextView.setTextColor(Color.WHITE);
                            collapsingToolbar.setExpandedTitleColor(Color.WHITE);

                            menuIconColor(overflowIcon, R.color.white, RegistrationPersonaliseActivity.this);
                            menuIconColor(doneDrawable, R.color.white, RegistrationPersonaliseActivity.this);
                            menuIconColor(editDrawable, R.color.white, RegistrationPersonaliseActivity.this);

                        } else {
                            subtitleTextView.setTextColor(Color.BLACK);
                            collapsingToolbar.setExpandedTitleColor(Color.BLACK);

                            menuIconColor(overflowIcon, R.color.black, RegistrationPersonaliseActivity.this);
                            menuIconColor(doneDrawable, R.color.black, RegistrationPersonaliseActivity.this);
                            menuIconColor(editDrawable, R.color.black, RegistrationPersonaliseActivity.this);

                        }
                    }
                }
            }
        });
    }

    private void setupCollapsingToolbar() {

        getName(Prefs.getUserKey(), RegistrationPersonaliseActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                setTitle(result, collapsingToolbar);
            }
        });

        overflowIcon = toolbar.getOverflowIcon();

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(RegistrationPersonaliseActivity.this, R.color.colorPrimary));
                    subtitleTextView.setVisibility(View.GONE);
                    toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Dark_ActionBar);

                    changeBasedOnImage(imageView);

                } else {

                    getCompanyName(Prefs.getUserKey(), RegistrationPersonaliseActivity.this, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            companyName = result;
                            handleSubTitle(companyName);
                        }
                    });
                }
            }
        });

    }

    private void handleSubTitle(String companyName) {
        toolbar.setBackgroundColor(ContextCompat.getColor(RegistrationPersonaliseActivity.this, android.R.color.transparent));
        if (!companyName.equals("0")) {
            subtitleTextView.setText(companyName);
            subtitleTextView.setVisibility(View.VISIBLE);
            changeBasedOnImage(imageView);
        }
    }

    private static void setTitle(String name, CollapsingToolbarLayout collapsingToolbar) {
        collapsingToolbar.setTitleEnabled(true);
        collapsingToolbar.setTitle(name);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    private void setupViewPager() {

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        floatingActionButtonAddInfo.show();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0 || tab.getPosition() == 1)
                    animateFab(tab.getPosition(), floatingActionButtonAddInfo);
                else
                    floatingActionButtonAddInfo.hide();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        imageView.setImageDrawable(ContextCompat.getDrawable(RegistrationPersonaliseActivity.this, R.drawable.profile));

        if (Prefs.getUserImage() != null) {
            Log.e("tag", "loading image " + Prefs.getUserImage());
            Glide.with(RegistrationPersonaliseActivity.this)
                    .load(Prefs.getUserImage())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Log.e("tag", "loading image");
                            imageView.setImageBitmap(resource);

                            changeBasedOnImage(imageView);
                        }
                    });
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("location", address);
        InfoFragment infoFragment = new InfoFragment();
        infoFragment.setArguments(bundle);

        adapter.addFrag(infoFragment, "INFO");
        adapter.addFrag(new AppTagsFragment(), "SKILL");
        adapter.addFrag(new MenteeFragment(), "MENTEE");
        adapter.addFrag(new MentorFragment(), "MENTOR");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeBasedOnImage(imageView);
                /*if (position == 3) {
                    invalidateOptionsMenu();
                    menuOption = 1;
                } else {
                    invalidateOptionsMenu();
                    menuOption = 0;
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void passSkill(ArrayList<AllSkill> msg) {
        MenteeFragment menteeFragment = (MenteeFragment) adapter.getFragment(2);
        if (menteeFragment != null) {
            menteeFragment.fragmentCommunication(msg);
        } else {
            Log.e("tag", "MenteeFragment is not initialized");
        }
    }

    @Override
    public void passSkillMentor(ArrayList<AllSkill> msg) {
        MentorFragment mentorFragment = (MentorFragment) adapter.getFragment(3);
        if (mentorFragment != null) {
            mentorFragment.fragmentCommunicationMentor(msg);
        } else {
            Log.e("tag", "MentorFragment is not initialized");
        }
    }

    @Override
    public void passAllUserSkill(ArrayList<AllSkill> allSkills) {
        userTagArrayList = allSkills;
        menteeTagArrayList = allSkills;
        mentorTagArrayList = allSkills;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("tag", " connectionResult --> " + connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        address = Utility.getAddress(RegistrationPersonaliseActivity.this, location);

        Log.e("tag", " Lat--> " + latitude + " long--> " + longitude + " Address--> " + address);

        Prefs.setLoc(address);

        settLocation(Prefs.getUserKey(), String.valueOf(latitude), String.valueOf(longitude), RegistrationPersonaliseActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
        if (this.googleApiClient != null) {
            this.googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
        AppManager.activityResumed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.googleApiClient != null) {
            this.googleApiClient.disconnect();
        }
        EasyImage.clearConfiguration(this);
    }

    @Override
    public void onBackPressed() {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Utility.showGPSSettingsAlert(RegistrationPersonaliseActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST_FINE_LOCATION) {
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i("tag", "Location permission has now been granted. Showing preview.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}


