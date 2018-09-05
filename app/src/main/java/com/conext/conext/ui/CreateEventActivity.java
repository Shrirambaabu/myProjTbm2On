package com.conext.conext.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.conext.conext.AppManager;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.Contact;
import com.conext.conext.model.Skill;
import com.conext.conext.model.db.EVENT_TYPE_MASTER;
import com.conext.conext.ui.Fragments.DatePickerFragment;
import com.conext.conext.ui.Fragments.TimePickerFragment;
import com.conext.conext.ui.custom.HexagonMaskView;
import com.conext.conext.utils.Prefs;
import com.conext.conext.utils.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.conext.conext.WebServiceMethods.WebService.getEventType;
import static com.conext.conext.WebServiceMethods.WebService.getUserContact;
import static com.conext.conext.WebServiceMethods.WebService.setEventLocation;
import static com.conext.conext.db.DbConstants.EVENT_TYPE_KEY;
import static com.conext.conext.db.DbConstants.NAME;
import static com.conext.conext.db.DbConstants.PROFILE_PIC;
import static com.conext.conext.db.DbConstants.USER_KEY;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.CREATE_EVENT_WEIGHT_URL;
import static com.conext.conext.utils.Utility.hideProgressDialog;
import static com.conext.conext.utils.Utility.showDialogue;
import static com.conext.conext.utils.Utility.showProgressDialog;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, CompoundButton.OnCheckedChangeListener {

    private static final int CONTACT_CODE = 1;
    private static final int PLACE_PICKER_REQUEST = 111;

    private Uri uriPath;
    private TextView cancelText, saveText, startSessionText, endSessionText;
    private ImageView cameraImage, timePickStart, timePickEnd;
    private AppCompatEditText calledEdit, descriptionEdit, dateStartEdit, dateEndEdit, startHourEdit,
            startMinEdit, endHourEdit, end_min_edit, locateEdit;

    private LinearLayout timeStartHoursLinear, timeStartHoursLinear2, timeStartHoursLinear3, timeStartHoursLinear4;

    private RelativeLayout relativeLayoutBackground;
    private AppCompatSpinner eventName, eventLanguage;
    private AppCompatCheckBox checkDay, mentee, mentor, participant;
    private RadioGroup openClose;
    private boolean checkBoxMenteeBoolean = false;
    private boolean checkBoxMentorBoolean = false;
    private boolean checkBoxParticipantBoolean = false;

    private RecyclerView contactsRecyclerView;
    // private PorterShapeImageView imageViewHexBtn;
    private HexagonMaskView imageViewHexBtn;
    private static String eventKey;
    private static String tagKey;
    private String eventTypeEvent;
    private String descriptionText;
    private String locationData;
    private String timeHourStart;
    private String timeHourEnd;
    private String timeStart;
    private static String locationKey;
    private String dbStart;
    private String formatedDbEnd;
    private String formatedDb;
    private String dbEnd;
    private String dateFinalStart = null;
    private String dateFinalEnd = null;

    private Date d1;
    private GoogleApiClient mGoogleApiClient;
    private ContactAdapter contactAdapter = null;
    private Calendar datetime2;
    private int defaultValue = 0;
    private String dateCurFinalStart;
    private List<Skill> categories = new ArrayList<>();
    private List<String> eventSkill = new ArrayList<>(), eventType = new ArrayList<>();
    private static ArrayList<Contact> contactsList = new ArrayList<>();
    private static ArrayList<Contact> contactsCurrentList = new ArrayList<>();
    private ArrayList<String> otherUserKey = new ArrayList<>();
    private ArrayList<String> contacts = new ArrayList<>();
    private ArrayList<String> contactStatus = new ArrayList<>();
    private ProgressDialog pDialog;

    public CreateEventActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_new_event);

        pDialog = new ProgressDialog(CreateEventActivity.this, R.style.MyThemeProgress);

        //mappingView
        mappingView();
        //eventNameSpinner
        eventNameSpinner();
        //eventLanguageSpinner
        eventLanguageSpinner();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("value") != null)
                defaultValue = Integer.parseInt(bundle.getString("value"));
        }

        settingToolbar();

        settingGoogleClient();

        //attachingListener
        listener();
        //settingContactRecyclerView
        settingContactRecyclerView();

        if (bundle != null) {
            if (bundle.getInt("id") != 0) {
                getContactInfo(bundle.getInt("id"));
                Log.e("tag", "Integer.parseInt(bundle.getString(\"id\"))" + bundle.getInt("id"));
                contacts.add(String.valueOf(bundle.getInt("id")));
                contactStatus.add("2");
            }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        contactsList.clear();
        otherUserKey.clear();
        contactStatus.clear();
        contacts.clear();
        finish();
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        EasyImage.clearConfiguration(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7460 && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                        compressedImageFile = new Compressor(CreateEventActivity.this).compressToFile(imageFile);
                        // Get length of file in bytes
                        long fileSizeInBytes = compressedImageFile.length();
                        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                        long fileSizeInMB = fileSizeInKB / 1024;
                        // Checking Less than 1mb
                        if (fileSizeInMB > 1) {
                            Toast.makeText(CreateEventActivity.this, "Sorry !!! File Size Too Large", Toast.LENGTH_LONG).show();
                        } else {
                            uriPath = Uri.fromFile(compressedImageFile);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPath);
                                bitmap = Bitmap.createScaledBitmap(bitmap, 1080, 512, false);
                                relativeLayoutBackground.setBackground(new BitmapDrawable(getResources(), bitmap));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA) {
                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(CreateEventActivity.this);
                        if (photoFile != null) photoFile.delete();
                    }
                }
            });

        } else if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK && data != null) {
            Place place = PlacePicker.getPlace(this, data);
            locateEdit.setText(place.getAddress());
            String locateLatitude = String.valueOf(place.getLatLng().latitude);
            String locateLongitude = String.valueOf(place.getLatLng().longitude);

            // showProgressDialog(pDialog);
            setEventLocation(Prefs.getUserKey(), locateLatitude, locateLongitude, CreateEventActivity.this, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    locationKey = result;
                }
            });

        } else if (requestCode == CONTACT_CODE && resultCode == RESULT_OK) {

            final ArrayList<Contact> contactsLists = (ArrayList<Contact>) data.getSerializableExtra("contacts");

            contactsList.clear();
            otherUserKey.clear();
            otherUserKey.add(Prefs.getUserKey());
            contactsList.addAll(contactsCurrentList);
            contacts.clear();
            contactStatus.clear();
            showProgressDialog(pDialog);

            for (int i = 0; i < contactsLists.size(); i++) {
                if (!otherUserKey.contains(contactsLists.get(i).getId())) {
                    otherUserKey.add(contactsLists.get(i).getId());
                    Contact contact = new Contact();
                    contact.setId(contactsLists.get(i).getId());
                    contact.setName(contactsLists.get(i).getName());
                    contact.setImage(contactsLists.get(i).getImage());
                    contact.setStatus(contactsLists.get(i).getStatus());
                    contact.setColg(contactsLists.get(i).getColg());
                    contact.setJob(contactsLists.get(i).getJob());
                    contactsList.add(contact);
                    contacts.add(contactsLists.get(i).getId());
                    contactStatus.add(contactsLists.get(i).getStatus());
                } else {
                    Toast.makeText(CreateEventActivity.this, contactsLists.get(i).getName() + " Already exits", Toast.LENGTH_LONG).show();
                }
            }
            contactAdapter.notifyDataSetChanged();
            hideProgressDialog(pDialog);
        }
    }

    private void updateContactList(List<Contact> contactList) {
        contactsList = (ArrayList<Contact>) contactList;
        otherUserKey.clear();
        contacts.clear();
        contactStatus.clear();
        otherUserKey.add(Prefs.getUserKey());
        for (int i = 0; i < contactsList.size(); i++) {
            otherUserKey.add(contactsList.get(i).getId());
            contacts.add(contactsList.get(i).getId());
            contactStatus.add(contactsList.get(i).getStatus());
        }
    }

    private void getContactInfo(int userKey) {
        contactsCurrentList.clear();
        getUserContact(String.valueOf(userKey), CreateEventActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        Contact contact = new Contact();
                        contact.setId(obj.getString(USER_KEY));
                        contact.setImage(obj.getString(PROFILE_PIC));
                        otherUserKey.add(obj.getString(USER_KEY));
                        contactsList.add(contact);
                        contactsCurrentList.add(contact);
                    }
                    contactAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        contactsList.clear();
        otherUserKey.clear();
        contactStatus.clear();
        contacts.clear();
        finish();
        return true;
    }

    private void settingContactRecyclerView() {
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(CreateEventActivity.this, LinearLayoutManager.HORIZONTAL, false));

        //create an ArrayAdapter from the String Array
        contactAdapter = new ContactAdapter(CreateEventActivity.this, R.layout.card__contact_image, contactsList);
        //For performance, tell OS RecyclerView won't change size
        contactsRecyclerView.setHasFixedSize(true);
        // Assign adapter to recyclerView
        contactsRecyclerView.setAdapter(contactAdapter);

        otherUserKey.clear();
        //   otherUserKey.add(Prefs.getUserKey());

        getContactInfo(Integer.parseInt(Prefs.getUserKey()));

    }

    DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            dbEnd = myFormat(year, monthOfYear, dayOfMonth);
            dateFinalEnd = formatDate(year, monthOfYear, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE,MMM d yyyy");
            try {
                if (dateFinalStart != null) {
                    dateEndEdit.setEnabled(true);
                    Date d2 = sdf.parse(dateFinalEnd);
                    if (d2.compareTo(d1) < 0) {
                        Toast.makeText(CreateEventActivity.this, "Enter valid date", Toast.LENGTH_LONG).show();
                        dateEndEdit.setText("");
                    } else {
                        dateEndEdit.setText(formatDate(year, monthOfYear, dayOfMonth));
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            dbStart = myFormat(year, monthOfYear, dayOfMonth);
            dateFinalStart = formatDate(year, monthOfYear, dayOfMonth);
            // todayDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE,MMM d yyyy");
            try {
                String d3 = sdf.format(new Date());
                Date d4 = sdf.parse(d3);
                d1 = sdf.parse(dateFinalStart);
                if (d1.compareTo(d4) < 0) {
                    Toast.makeText(CreateEventActivity.this, "Enter valid date", Toast.LENGTH_LONG).show();
                    dateStartEdit.setText("");
                } else {
                    dateStartEdit.setText(formatDate(year, monthOfYear, dayOfMonth));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (checkDay.isChecked()) {
                if (dateFinalStart == null) {
                    dateEndEdit.setText("");
                    startHourEdit.setText("");
                    startMinEdit.setText("");
                    startSessionText.setText("AM");
                    checkDay.setChecked(false);
                    endHourEdit.setText("");
                    end_min_edit.setText("");
                    endSessionText.setText("AM");
                    timePickStart.setEnabled(true);
                    startHourEdit.setEnabled(true);
                    end_min_edit.setEnabled(true);
                    timePickEnd.setEnabled(true);
                    endHourEdit.setEnabled(true);
                    end_min_edit.setEnabled(true);
                    dateEndEdit.setEnabled(true);

                } else {
                    dateEndEdit.setText(dateFinalStart);
                    startHourEdit.setText("12");
                    startMinEdit.setText("00");
                    startSessionText.setText("AM");
                    timePickStart.setEnabled(false);
                    startHourEdit.setEnabled(false);
                    end_min_edit.setEnabled(false);
                    timePickEnd.setEnabled(false);
                    dateEndEdit.setEnabled(false);
                    endHourEdit.setEnabled(false);
                    end_min_edit.setEnabled(false);
                    endHourEdit.setText("11");
                    end_min_edit.setText("59");
                    endSessionText.setText("PM");
                }
            }
        }
    };

    private String myFormat(int year, int monthOfYear, int dayOfMonth) {

        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DATE, dayOfMonth);
        Date date = calendar.getTime();
        return parseFormat.format(date);
    }

    private Bundle dateDialog() {

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        return args;
    }


    private String formatDate(int year, int monthOfYear, int dayOfMonth) {

        SimpleDateFormat parseFormat = new SimpleDateFormat("EEE,MMM d yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DATE, dayOfMonth);
        Date date = calendar.getTime();
        //Date date = new Date(newYear, monthOfYear, dayOfMonth);
        return parseFormat.format(date);
    }

    private void listener() {

        cameraImage.setOnClickListener(this);
        dateEndEdit.setOnClickListener(this);
        dateStartEdit.setOnClickListener(this);
        cancelText.setOnClickListener(this);
        saveText.setOnClickListener(this);
        timePickStart.setOnClickListener(this);
        checkDay.setOnCheckedChangeListener(this);
        mentor.setOnCheckedChangeListener(this);
        participant.setOnCheckedChangeListener(this);
        mentee.setOnCheckedChangeListener(this);
        timePickEnd.setOnClickListener(this);
        startHourEdit.setOnClickListener(this);
        startMinEdit.setOnClickListener(this);
        locateEdit.setOnClickListener(this);
        calledEdit.setOnClickListener(this);
        endHourEdit.setOnClickListener(this);
        end_min_edit.setOnClickListener(this);
        imageViewHexBtn.setOnClickListener(this);
        timeStartHoursLinear.setOnClickListener(this);
        timeStartHoursLinear2.setOnClickListener(this);
        timeStartHoursLinear3.setOnClickListener(this);
        timeStartHoursLinear4.setOnClickListener(this);

    }

    private void mappingView() {

        relativeLayoutBackground = (RelativeLayout) findViewById(R.id.head_event);
        //imageViewHexBtn = (PorterShapeImageView) findViewById(R.id.add_contact);
        imageViewHexBtn = (HexagonMaskView) findViewById(R.id.add_contact);
        imageViewHexBtn.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        contactsRecyclerView = (RecyclerView) findViewById(R.id.contact_recycler);
        cancelText = (TextView) findViewById(R.id.cancel);
        saveText = (TextView) findViewById(R.id.save);
        cameraImage = (ImageView) findViewById(R.id.take_pic);
        startSessionText = (TextView) findViewById(R.id.start_session);
        endSessionText = (TextView) findViewById(R.id.end_session);

        calledEdit = (AppCompatEditText) findViewById(R.id.what_called);
        descriptionEdit = (AppCompatEditText) findViewById(R.id.descripText);
        dateStartEdit = (AppCompatEditText) findViewById(R.id.date_picked_start);
        dateEndEdit = (AppCompatEditText) findViewById(R.id.date_picked_end);
        startHourEdit = (AppCompatEditText) findViewById(R.id.time_start_hours);
        startMinEdit = (AppCompatEditText) findViewById(R.id.time_start_min);
        endHourEdit = (AppCompatEditText) findViewById(R.id.time_end_hours);
        end_min_edit = (AppCompatEditText) findViewById(R.id.time_end_min);
        timePickStart = (ImageView) findViewById(R.id.time_pick_start);
        timePickEnd = (ImageView) findViewById(R.id.time_pick_end);
        cameraImage = (ImageView) findViewById(R.id.take_pic);
        eventName = (AppCompatSpinner) findViewById(R.id.what_event);
        eventLanguage = (AppCompatSpinner) findViewById(R.id.tag_event);
        checkDay = (AppCompatCheckBox) findViewById(R.id.day_check);
        // limitEvent = (AppCompatCheckBox) findViewById(R.id.limit_event);
        openClose = (RadioGroup) findViewById(R.id.open_close);
        locateEdit = (AppCompatEditText) findViewById(R.id.locate_edit);
        mentee = (AppCompatCheckBox) findViewById(R.id.mentee);
        mentor = (AppCompatCheckBox) findViewById(R.id.mentor);
        participant = (AppCompatCheckBox) findViewById(R.id.participant);

        timeStartHoursLinear = (LinearLayout) findViewById(R.id.time_start_hours_linear);
        timeStartHoursLinear2 = (LinearLayout) findViewById(R.id.time_start_hours_linear2);
        timeStartHoursLinear3 = (LinearLayout) findViewById(R.id.time_start_hours_linear3);
        timeStartHoursLinear4 = (LinearLayout) findViewById(R.id.time_start_hours_linear4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.date_picked_start:

                DatePickerFragment dateStart = new DatePickerFragment();
                // Set Up Current Date Into dialog
                dateStart.setArguments(dateDialog());
                //Set Call back to capture selected date
                dateStart.setCallBack(onStartDateSetListener);
                dateStart.show(getSupportFragmentManager(), "Date Picker");

                break;
            case R.id.date_picked_end:

                DatePickerFragment dateEnd = new DatePickerFragment();
                //Set Up Current Date Into dialog
                dateEnd.setArguments(dateDialog());
                //Set Call back to capture selected date
                dateEnd.setCallBack(onEndDateSetListener);
                dateEnd.show(getSupportFragmentManager(), "Date Picker");

                break;
            case R.id.time_pick_start:

                TimePickerFragment timeStart = new TimePickerFragment();
                timeStart.setArguments(timePickerDialog());
                timeStart.setCallBack(ontime);
                timeStart.show(getSupportFragmentManager(), "Time Picker");

                break;

            case R.id.time_start_hours:

                TimePickerFragment timeStarth = new TimePickerFragment();
                timeStarth.setArguments(timePickerDialog());
                timeStarth.setCallBack(ontime);
                timeStarth.show(getSupportFragmentManager(), "Time Picker");

                break;
            case R.id.time_start_min:

                TimePickerFragment timeStartmin = new TimePickerFragment();
                timeStartmin.setArguments(timePickerDialog());
                timeStartmin.setCallBack(ontime);
                timeStartmin.show(getSupportFragmentManager(), "Time Picker");

                break;


            case R.id.time_pick_end:

                TimePickerFragment timeEnd = new TimePickerFragment();
                timeEnd.setArguments(timePickerDialog());
                timeEnd.setCallBack(onTime);
                timeEnd.show(getSupportFragmentManager(), "Time Picker");

                break;


            case R.id.time_end_hours:

                TimePickerFragment timeEndHr = new TimePickerFragment();
                timeEndHr.setArguments(timePickerDialog());
                timeEndHr.setCallBack(onTime);
                timeEndHr.show(getSupportFragmentManager(), "Time Picker");

                break;
            case R.id.time_end_min:

                TimePickerFragment timeEndSec = new TimePickerFragment();
                timeEndSec.setArguments(timePickerDialog());
                timeEndSec.setCallBack(onTime);
                timeEndSec.show(getSupportFragmentManager(), "Time Picker");

                break;

            case R.id.time_start_hours_linear:

                TimePickerFragment timeStart1 = new TimePickerFragment();
                timeStart1.setArguments(timePickerDialog());
                timeStart1.setCallBack(ontime);
                timeStart1.show(getSupportFragmentManager(), "Time Picker");

                break;
            case R.id.time_start_hours_linear2:

                TimePickerFragment timeStart2 = new TimePickerFragment();
                timeStart2.setArguments(timePickerDialog());
                timeStart2.setCallBack(ontime);
                timeStart2.show(getSupportFragmentManager(), "Time Picker");

                break;

            case R.id.time_start_hours_linear4:

                TimePickerFragment timeEnd2 = new TimePickerFragment();
                timeEnd2.setArguments(timePickerDialog());
                timeEnd2.setCallBack(onTime);
                timeEnd2.show(getSupportFragmentManager(), "Time Picker");

                break;
            case R.id.time_start_hours_linear3:

                TimePickerFragment timeEnd1 = new TimePickerFragment();
                timeEnd1.setArguments(timePickerDialog());
                timeEnd1.setCallBack(onTime);
                timeEnd1.show(getSupportFragmentManager(), "Time Picker");

                break;
            case R.id.take_pic:
                EasyImage.configuration(this)
                        .setImagesFolderName("Conext")
                        .saveInRootPicturesDirectory()
                        .setCopyExistingPicturesToPublicLocation(true);
                EasyImage.openChooserWithGallery(CreateEventActivity.this, "Choose Image from ...", 0);

                break;
           /* case R.id.limit_event:

                if (limitEvent.isChecked()) {
                    checkBoxEventBoolean = limitEvent.isChecked();
                }
                break;
*/
            case R.id.locate_edit:

                if (mGoogleApiClient == null || !mGoogleApiClient.isConnected())
                    return;

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(CreateEventActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.d("PlacesAPI Demo", "GooglePlayServicesRepairableException thrown");
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.d("PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown");
                }

                break;
            case R.id.add_contact:

                Intent contactIntent = new Intent(CreateEventActivity.this, ContactsActivity.class);
                if (!contactsList.isEmpty()) {
                    for (Contact u1 : contactsList) {
                        if (u1.getId().equals(Prefs.getUserKey())) {
                            contactsList.remove(u1);
                            break;
                        }
                    }
                }
                contactIntent.putExtra("contactsList", contactsList);
                startActivityForResult(contactIntent, CONTACT_CODE);

                break;
            case R.id.save:
                //   showProgressDialog(pDialog);
                eventTypeEvent = calledEdit.getText().toString();
                descriptionText = descriptionEdit.getText().toString();
                locationData = locateEdit.getText().toString();
                dateFinalEnd = dateEndEdit.getText().toString();
                timeHourStart = startHourEdit.getText().toString();
                timeHourEnd = endHourEdit.getText().toString();
                if (uriPath == null) {
                    Toast.makeText(CreateEventActivity.this, "Select the Image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (eventTypeEvent == null || eventTypeEvent.isEmpty()) {
                    Toast.makeText(CreateEventActivity.this, "Enter the Title", Toast.LENGTH_SHORT).show();
                    return;
                } else if (descriptionText == null || descriptionText.isEmpty()) {
                    Toast.makeText(CreateEventActivity.this, "Enter the description", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dateFinalStart == null) {
                    Toast.makeText(CreateEventActivity.this, "Select the Start Date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (timeHourStart == null || timeHourStart.isEmpty()) {
                    Toast.makeText(CreateEventActivity.this, "Select the Start Time", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dateFinalEnd == null || dateFinalEnd.isEmpty()) {
                    Toast.makeText(CreateEventActivity.this, "Select the End Date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (timeHourEnd == null || timeHourEnd.isEmpty()) {
                    Toast.makeText(CreateEventActivity.this, "Select the End time", Toast.LENGTH_SHORT).show();
                    return;
                } else if (locationData == null || locationData.isEmpty()) {
                    Toast.makeText(CreateEventActivity.this, "Select the Location", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!checkBoxMenteeBoolean && !checkBoxMentorBoolean && !checkBoxParticipantBoolean) {
                    Toast.makeText(CreateEventActivity.this, "Select Mentee/Mentor/Participant", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkBoxMenteeBoolean && checkBoxMentorBoolean && checkBoxParticipantBoolean) {
                    Toast.makeText(CreateEventActivity.this, "Select any one from Mentee/Mentor/Participant", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!checkBoxMenteeBoolean && checkBoxMentorBoolean && checkBoxParticipantBoolean) {
                    Toast.makeText(CreateEventActivity.this, "Select any one Mentee/Mentor/Participant", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkBoxMenteeBoolean && !checkBoxMentorBoolean && checkBoxParticipantBoolean) {
                    Toast.makeText(CreateEventActivity.this, "Select any one Mentee/Mentor/Participant", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkBoxMenteeBoolean && checkBoxMentorBoolean && !checkBoxParticipantBoolean) {
                    Toast.makeText(CreateEventActivity.this, "Select any one Mentee/Mentor/Participant", Toast.LENGTH_SHORT).show();
                    return;
                } else if (otherUserKey.size() <= 1) {
                    Toast.makeText(CreateEventActivity.this, "Add People to the event", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveData();
                break;
            case R.id.cancel:
                otherUserKey.clear();
                contactStatus.clear();
                contacts.clear();
                contactsList.clear();
                finish();
                break;
        }

    }

    private void saveData() {

        String timeMinStart = startMinEdit.getText().toString();
        String timeSessionStart = startSessionText.getText().toString();
        String timeMinEnd = end_min_edit.getText().toString();
        String timeSessionEnd = endSessionText.getText().toString();

        byte[] inputImageData = null;
        if (uriPath != null) {

            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(uriPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                inputImageData = Utility.getBytes(iStream);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dbEnd == null) {
            dbEnd = dbStart;
        }

        final boolean checkOpenCloseBoolean = openClose.getCheckedRadioButtonId() == R.id.open;

        final int checkMenteeMentorParticipant;
        if (checkBoxMenteeBoolean && !checkBoxMentorBoolean && !checkBoxParticipantBoolean) {
            checkMenteeMentorParticipant = 1;
        } else if (!checkBoxMenteeBoolean && checkBoxMentorBoolean && !checkBoxParticipantBoolean) {
            checkMenteeMentorParticipant = 2;
        } else {
            checkMenteeMentorParticipant = 3;
        }

        if (!eventTypeEvent.isEmpty() && !descriptionText.isEmpty() && !timeHourStart.isEmpty() && !timeHourStart.isEmpty() && !timeSessionStart.isEmpty()
                && !timeHourEnd.isEmpty() && !timeMinEnd.isEmpty() && !timeSessionEnd.isEmpty() && !locationData.isEmpty() && inputImageData != null) {

            String dbSaveTime = (dbStart + " " + timeHourStart + ":" + timeMinStart + " " + timeSessionStart);
            SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            SimpleDateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date dateDb = readFormat.parse(dbSaveTime);
                formatedDb = writeFormat.format(dateDb);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String dbSaveEndTime = (dbEnd + " " + timeHourEnd + ":" + timeMinEnd + " " + timeSessionEnd);
            try {
                Date dateDb = readFormat.parse(dbSaveEndTime);
                formatedDbEnd = writeFormat.format(dateDb);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final String imageString = Base64.encodeToString(inputImageData, Base64.DEFAULT);
            showProgressDialog(pDialog);
            //Requests the data from webservice using volley
            StringRequest request = new StringRequest(Request.Method.POST, CREATE_EVENT_WEIGHT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    otherUserKey.clear();
                    contactStatus.clear();
                    contacts.clear();
                    contactsList.clear();
                    Toast.makeText(CreateEventActivity.this, "Event Created", Toast.LENGTH_LONG).show();
                    finish();
                    hideProgressDialog(pDialog);
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
                    hideProgressDialog(pDialog);
                    showDialogue(CreateEventActivity.this, "Sorry! Server Error", "Oops!!!");
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("UserKey", Prefs.getUserKey());
                    parameters.put("EventTypeKey", eventKey);
                    parameters.put("TagKey", tagKey);
                    parameters.put("LocationKey", String.valueOf(locationKey));
                    parameters.put("Description", descriptionText);
                    parameters.put("EventTitle", eventTypeEvent);
                    parameters.put("EventStartTs", formatedDb);
                    parameters.put("EventEndTs", formatedDbEnd);
                    parameters.put("Address", locationData);
                    parameters.put("isOpen", String.valueOf(checkOpenCloseBoolean));
                    parameters.put("ImageKey", imageString);
                    parameters.put("CreatedBy", Prefs.getUserKey());
                    parameters.put("ModifiedBy", Prefs.getUserKey());
                    parameters.put("otherUserKey", android.text.TextUtils.join(",", otherUserKey));
                    parameters.put("partitcipantStatus", String.valueOf(checkMenteeMentorParticipant));
                    parameters.put("contacts", android.text.TextUtils.join(",", contacts));
                    parameters.put("contactStatus", android.text.TextUtils.join(",", contactStatus));
                    return parameters;
                }
            };
            int MY_SOCKET_TIMEOUT_MS = 10000;//10 seconds - change to what you want
            request.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            RequestQueue rQueue = Volley.newRequestQueue(CreateEventActivity.this);
            rQueue.add(request);
        }
    }


    private void eventLanguageSpinner() {

        categories = Prefs.getUserSkill();

        if (categories.size() > 0) {
            for (int i = 0; i < categories.size(); i++) {
                eventSkill.add(categories.get(i).getTitle());
            }
        }

        setSpinnerValue(eventSkill, eventLanguage);

        eventLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String tagSelected = eventSkill.get(position);

                for (Skill item : categories) {
                    if (item.getTitle().equals(tagSelected)) {
                        tagKey = String.valueOf(item.getSkillId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void eventNameSpinner() {

        getEventType(CreateEventActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray response;
                List<EVENT_TYPE_MASTER> categories = new ArrayList<>();
                try {
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        EVENT_TYPE_MASTER event_type_master = new EVENT_TYPE_MASTER();

                        JSONObject obj = response.getJSONObject(i);
                        event_type_master.setEventTypeKey(obj.getString(EVENT_TYPE_KEY));
                        event_type_master.setName(obj.getString(NAME));

                        categories.add(event_type_master);
                    }
                    handle(categories);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handle(final List<EVENT_TYPE_MASTER> categories) {

        if (categories.size() > 0) {
            for (int i = 0; i < categories.size(); i++) {
                eventType.add(categories.get(i).getName());
            }
        }

        setSpinnerValue(eventType, eventName);

        eventName.setSelection(defaultValue);
        eventName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String eventTypeSelected = eventType.get(position);

                for (EVENT_TYPE_MASTER item : categories) {
                    if (item.getName().equals(eventTypeSelected)) {
                        eventKey = String.valueOf(item.getEventTypeKey());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setSpinnerValue(final List<String> list, AppCompatSpinner spinner) {

        // Creating adapter for spinner
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(CreateEventActivity.this, R.layout.spin_list_custom, list);

        // Drop down layout style - list view with radio button
        languageAdapter.setDropDownViewResource(R.layout.spin_list_custom);
        // attaching data adapter to spinner
        spinner.setAdapter(languageAdapter);

    }

    private Bundle timePickerDialog() {

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("hour", Calendar.HOUR_OF_DAY);
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("minute", calender.get(Calendar.MINUTE));
        return args;
    }


    private void settingGoogleClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(CreateEventActivity.this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    private void settingToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Events");
        }
    }

    TimePickerDialog.OnTimeSetListener onTime = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.e("Check: ", "TimeEnd Called");
            ArrayList<String> timeArrayList = convertToHourMinuteTimeSet(hourOfDay, minute);
            String timeEndFinal = String.valueOf(timeArrayList.get(0) + timeArrayList.get(1) + timeArrayList.get(2));
            SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat dateFormat = new SimpleDateFormat("hhmmaa");
            Calendar c = Calendar.getInstance();
            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);
            try {
                Date dateTimePickedStart = dateFormat.parse(timeEndFinal);
                String dateTimeFinalStart = df.format(dateTimePickedStart);

                if (dateFinalEnd == null || timeStart == null) {
                    Toast.makeText(CreateEventActivity.this, "Select the Start Time/End Date", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (dbEnd.equals(dateCurFinalStart)) {
                    if (datetime.getTimeInMillis() < datetime2.getTimeInMillis()) {

                        Toast.makeText(CreateEventActivity.this, "Enter Valid time", Toast.LENGTH_SHORT).show();
                        endHourEdit.setText("");
                        end_min_edit.setText("");
                        endSessionText.setText("AM");

                    } else {
                        endHourEdit.setText(String.valueOf(timeArrayList.get(0)));
                        end_min_edit.setText(String.valueOf(timeArrayList.get(1)));
                        endSessionText.setText(String.valueOf(timeArrayList.get(2)));
                    }
                } else {
                    endHourEdit.setText(String.valueOf(timeArrayList.get(0)));
                    end_min_edit.setText(String.valueOf(timeArrayList.get(1)));
                    endSessionText.setText(String.valueOf(timeArrayList.get(2)));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    };

    TimePickerDialog.OnTimeSetListener ontime = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            ArrayList<String> timeArrayList = convertToHourMinuteTimeSet(hourOfDay, minute);
            timeStart = String.valueOf(timeArrayList.get(0) + timeArrayList.get(1) + timeArrayList.get(2));

            Calendar c = Calendar.getInstance();
            datetime2 = Calendar.getInstance();
            datetime2.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime2.set(Calendar.MINUTE, minute);
            Date cc = c.getTime();
            SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");

            SimpleDateFormat dateFormat = new SimpleDateFormat("hhmmaa");
            SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
            dateCurFinalStart = dateFormat3.format(cc);


            try {
                Date dateTimePickedStart = dateFormat.parse(timeStart);

                if (dateFinalStart == null) {
                    Toast.makeText(CreateEventActivity.this, "Select the Start Date", Toast.LENGTH_SHORT).show();
                    dateEndEdit.setText("");
                    startHourEdit.setText("");
                    startMinEdit.setText("");
                    startSessionText.setText("AM");
                    endHourEdit.setText("");
                    end_min_edit.setText("");
                    endSessionText.setText("AM");
                    return;
                } else {
                    dateStartEdit.setText(dateFinalStart);
                    startSessionText.setText("AM");
                    endHourEdit.setText("");
                    end_min_edit.setText("");
                    endSessionText.setText("AM");
                }
                if (dbStart.equals(dateCurFinalStart)) {
                    if (datetime2.getTimeInMillis() < c.getTimeInMillis()) {
                        Toast.makeText(CreateEventActivity.this, "Enter Valid time", Toast.LENGTH_SHORT).show();
                        startHourEdit.setText("");
                        startMinEdit.setText("");
                        startSessionText.setText("AM");
                    } else {
                        startHourEdit.setText(timeArrayList.get(0));
                        startMinEdit.setText(timeArrayList.get(1));
                        startSessionText.setText(timeArrayList.get(2));
                    }
                } else {
                    startHourEdit.setText(timeArrayList.get(0));
                    startMinEdit.setText(timeArrayList.get(1));
                    startSessionText.setText(timeArrayList.get(2));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    };

    private ArrayList<String> convertToHourMinuteTimeSet(int hourOfDay, int minute) {

        ArrayList<String> integerArrayList = new ArrayList<>();

        int hour = hourOfDay;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (minute < 10)
            min = "0" + minute;
        else
            min = String.valueOf(minute);

        integerArrayList.add(String.valueOf(hour));
        integerArrayList.add(min);
        integerArrayList.add(timeSet);

        return integerArrayList;

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.day_check:
                if (isChecked) {
                    if (dateFinalStart == null) {
                        Toast.makeText(CreateEventActivity.this, "Select the Start Date Before Checking", Toast.LENGTH_SHORT).show();
                        checkDay.setChecked(false);
                        dateEndEdit.setText("");
                        startHourEdit.setText("");
                        startMinEdit.setText("");
                        startSessionText.setText("AM");
                        endHourEdit.setText("");
                        end_min_edit.setText("");
                        endSessionText.setText("AM");
                        return;
                    } else {
                        dateStartEdit.setText(dateFinalStart);
                        dateEndEdit.setText(dateFinalStart);
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE,MMM d yyyy");
                        String currentDateSet = sdf.format(new Date());

                        if (currentDateSet.equals(dateFinalStart)) {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
                            String formattedDate = df.format(c.getTime());
                            String[] parts = formattedDate.split(" ");
                            String part1 = parts[0];
                            String part2 = parts[1];
                            String[] time = part1.split(":");
                            String hourText = time[0];
                            String minText = time[1];
                            startHourEdit.setText(hourText);
                            startMinEdit.setText(minText);
                            startSessionText.setText(part2);
                            timePickStart.setEnabled(false);
                            startHourEdit.setEnabled(false);
                            end_min_edit.setEnabled(false);
                            timePickEnd.setEnabled(false);
                            dateEndEdit.setEnabled(false);
                            endHourEdit.setEnabled(false);
                            end_min_edit.setEnabled(false);
                            endHourEdit.setText("11");
                            end_min_edit.setText("59");
                            endSessionText.setText("PM");
                        } else {
                            startHourEdit.setText("12");
                            startMinEdit.setText("00");
                            startSessionText.setText("AM");
                            timePickStart.setEnabled(false);
                            startHourEdit.setEnabled(false);
                            end_min_edit.setEnabled(false);
                            timePickEnd.setEnabled(false);
                            dateEndEdit.setEnabled(false);
                            endHourEdit.setEnabled(false);
                            end_min_edit.setEnabled(false);
                            endHourEdit.setText("11");
                            end_min_edit.setText("59");
                            endSessionText.setText("PM");
                        }
                    }
                } else {
                    dateEndEdit.setText("");
                    dateFinalStart = null;
                    timePickStart.setEnabled(true);
                    startHourEdit.setEnabled(true);
                    end_min_edit.setEnabled(true);
                    timePickEnd.setEnabled(true);
                    dateEndEdit.setEnabled(true);
                    endHourEdit.setEnabled(true);
                    end_min_edit.setEnabled(true);
                    startHourEdit.setText("");
                    startMinEdit.setText("");
                    startSessionText.setText("AM");
                    endHourEdit.setText("");
                    end_min_edit.setText("");
                    endSessionText.setText("AM");
                    dateEndEdit.setText("");
                    dateStartEdit.setText("");
                    dateEndEdit.setText("");
                }

                break;
            case R.id.mentor:
                if (isChecked) {
                    checkBoxMentorBoolean = mentor.isChecked();
                } else {
                    checkBoxMentorBoolean = false;
                }
                break;
            case R.id.mentee:
                if (isChecked) {
                    checkBoxMenteeBoolean = mentee.isChecked();
                } else {
                    checkBoxMenteeBoolean = false;
                }
                break;
            case R.id.participant:
                if (isChecked) {
                    checkBoxParticipantBoolean = participant.isChecked();
                } else {
                    checkBoxParticipantBoolean = false;
                }
                break;
        }
    }


    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

        private List<Contact> contactList;
        private Context context;
        private int itemResource;

        ContactAdapter(Context context, int itemResource, List<Contact> contactList) {
            this.contactList = contactList;
            this.context = context;
            this.itemResource = itemResource;
        }


        @Override
        public ContactAdapter.ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(itemResource, parent, false);
            return new ContactAdapter.ContactHolder(v);
        }

        @Override
        public void onBindViewHolder(final ContactAdapter.ContactHolder holder, final int position) {
            final Contact contact = contactList.get(position);

            Log.e("tag", "" + position + " --> " + contactList.get(position).getName() + " --> " + contactList.get(position).getId());

            if (contact.getImage() != null) {
                Glide.with(context)
                        .load(BaseIP + "/" + contact.getImage())
                        .into(holder.img);
            }

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Context context = v.getContext();

                    if (!Objects.equals(contact.getId(), Prefs.getUserKey())) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            builder = new AlertDialog.Builder(context, R.style.Delete_Dialog);
//                        } else {
//                            builder = new AlertDialog.Builder(context);
//                        }
                        String status1 = null;
                        String status2 = null;
                        //mentee
                        switch (contact.getStatus()) {
                            case "0": //mentee
                                status1 = "Add as Mentor";
                                status2 = "Add as Participant";
                                break;
                            case "1": //mentor
                                status1 = "Add as Mentee";
                                status2 = "Add as Participant";
                                break;
                            case "2": //participant
                                status1 = "Add as Mentor";
                                status2 = "Add as Mentee";
                                break;
                        }
                        builder.setMessage("Are you sure you want to update this User?")
                                .setPositiveButton("Remove User", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        contactList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, contactList.size());
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                        updateContactList(contactList);
                                    }
                                })
                                .setNegativeButton(status1, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (contact.getStatus()) {
                                            case "0":
                                                contact.setStatus("1");
                                                notifyDataSetChanged();
                                                updateContactList(contactList);
                                                break;
                                            case "1":
                                                contact.setStatus("0");
                                                notifyDataSetChanged();
                                                updateContactList(contactList);
                                                break;
                                            case "2":
                                                contact.setStatus("1");
                                                notifyDataSetChanged();
                                                updateContactList(contactList);
                                                break;
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .setNeutralButton(status2, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (contact.getStatus()) {
                                            case "0":
                                                contact.setStatus("2");
                                                notifyDataSetChanged();
                                                updateContactList(contactList);
                                                break;
                                            case "1":
                                                contact.setStatus("2");
                                                notifyDataSetChanged();
                                                updateContactList(contactList);
                                                break;
                                            case "2":
                                                contact.setStatus("0");
                                                notifyDataSetChanged();
                                                updateContactList(contactList);
                                                break;
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                    return true;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Objects.equals(contactList.get(position).getId(), Prefs.getUserKey())) {
                        Intent intent = new Intent(context, OtherProfileActivity.class);
                        intent.putExtra("id", contactList.get(position).getId());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.contactList.size();
        }

        class ContactHolder extends RecyclerView.ViewHolder {

            //  private ImageView img;
            private HexagonMaskView img;

            ContactHolder(View itemView) {
                super(itemView);
                // Set up the UI widgets of the holder
                this.img = (HexagonMaskView) itemView.findViewById(R.id.add_contact_image);
            }
        }
    }
}

