package com.conext.conext.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.conext.conext.model.db.USER_EVENT;
import com.conext.conext.ui.custom.ConatactDecorator;
import com.conext.conext.ui.custom.HexagonMaskView;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.conext.conext.AppManager.getAppContext;
import static com.conext.conext.WebServiceMethods.WebService.getAllContactsOfEventss;
import static com.conext.conext.db.DbConstants.ADDRESS;
import static com.conext.conext.db.DbConstants.DESCRIPTION;
import static com.conext.conext.db.DbConstants.EVENT_KEY;
import static com.conext.conext.db.DbConstants.EVENT_TITLE;
import static com.conext.conext.db.DbConstants.IMAGE_KEY;
import static com.conext.conext.db.DbConstants.IS_OPEN;
import static com.conext.conext.db.DbConstants.NAME;
import static com.conext.conext.db.DbConstants.PARTICIPANT_STATUS;
import static com.conext.conext.db.DbConstants.PROFILE_PIC;
import static com.conext.conext.db.DbConstants.TAG_NAME;
import static com.conext.conext.db.DbConstants.USER_KEY;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.GET_EVENT_DETAIL_URL;
import static com.conext.conext.utils.Utility.showDialogue;

public class EventDetailsActivity extends AppCompatActivity {


    private ImageView bac;
    private TextView eventName, eventAddressMore, eventType, eventDate, eventMonth, eventDescription, eventTimeNotify, eventAddress;
    private RecyclerView contactRecycler;
    private String id = null;
    private ArrayList<Contact> contactsLists = new ArrayList<>();
    private ContactAdapter contactAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
        }

        setupToolbar();

        addressingView();

        getEventDetails();

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


    private void settingContactAdapter() {

        contactAdapter = new ContactAdapter(getAppContext(), R.layout.card__contact_image, contactsLists);
        contactRecycler.setLayoutManager(new LinearLayoutManager(EventDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        //For performance, tell OS RecyclerView won't change size
        contactRecycler.setHasFixedSize(true);
        // Assign adapter to recyclerView
        contactRecycler.setAdapter(contactAdapter);

        contactRecycler.addItemDecoration(new ConatactDecorator(12));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Event Details");
        }
    }

    private void getEventDetails() {

        Log.e("EventDetails", "" + GET_EVENT_DETAIL_URL + "/" + Prefs.getUserKey() + "/" + id);
        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_EVENT_DETAIL_URL + "/" + Prefs.getUserKey() + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                    USER_EVENT userEvent = new USER_EVENT();
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);
                        userEvent.setUserKey(obj.getString(USER_KEY));
                        userEvent.setEventKey(obj.getString(EVENT_KEY));
                        userEvent.setEventStartDate(obj.getString("dateStartEventDetails"));
                        userEvent.setEventMonthStartDate(obj.getString("monthStartEventDetails"));
                        userEvent.setNotifyTime(obj.getString("notiftime"));
                        userEvent.setEventTitle(obj.getString(EVENT_TITLE));
                        userEvent.setAddress(obj.getString(ADDRESS));
                        userEvent.setEventTypeKey(obj.getString(NAME));
                        userEvent.setTagKey(obj.getString(TAG_NAME));
                        userEvent.setImageKey(obj.getString(IMAGE_KEY));
                        userEvent.setOpen(obj.getString(IS_OPEN).equals("1"));
                        userEvent.setDescription(obj.getString(DESCRIPTION));
                        userEvent.setContact(obj.getString("contacts"));
                        // userEvent.setEventEndDate(obj.getString("eventEndDate"));
                    }
                    handleUserEvent(userEvent);
                } catch (JSONException e) {
                    Log.e("tag", e.getMessage());
                    e.printStackTrace();
                }

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
                showDialogue(EventDetailsActivity.this, "Sorry! Server Error", "Oops!!!");
            }
        });

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(EventDetailsActivity.this);
        rQueue.add(request);
    }

    private void handleUserEvent(final USER_EVENT userEvent) {
        eventName.setText(userEvent.getEventTitle());
        eventType.setText(userEvent.getEventTypeKey() + " | " + userEvent.getTagKey());

        eventDescription.setText(userEvent.getDescription());

        eventDate.setText(userEvent.getEventStartDate());
        eventMonth.setText(userEvent.getEventMonthStartDate());
        eventTimeNotify.setText(userEvent.getNotifyTime());

        if (userEvent.getImageKey() != null) {
            Glide.with(EventDetailsActivity.this)
                    .load(BaseIP + "/" + userEvent.getImageKey().trim())
                    .asBitmap()
                    .into(bac);
        }

        String str = userEvent.getAddress();
        if (str.contains(",")) {
            eventAddress.setText(str.substring(0, str.indexOf(",")));
            eventAddressMore.setText(str.substring(str.indexOf(",") + 1, str.length()));
            eventAddressMore.setVisibility(View.VISIBLE);
        } else {
            eventAddress.setText(str);
        }

        getAllContactsOfEvents(userEvent);

    }

    private void getAllContactsOfEvents(USER_EVENT myEvents) {

        settingContactAdapter();

        getAllContactsOfEventss(myEvents.getEventKey(), EventDetailsActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        Contact contact1 = new Contact();
                        JSONObject obj = response.getJSONObject(i);
                        contact1.setId(obj.getString(USER_KEY));
                        contact1.setImage(obj.getString(PROFILE_PIC));
                        contact1.setStatus(obj.getString(PARTICIPANT_STATUS));
                        contactsLists.add(contact1);
                    }
                    contactAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addressingView() {

        bac = (ImageView) findViewById(R.id.bac);
        eventName = (TextView) findViewById(R.id.event_name);
        eventType = (TextView) findViewById(R.id.event_type);
        eventDate = (TextView) findViewById(R.id.event_date);
        eventMonth = (TextView) findViewById(R.id.event_month);
        eventDescription = (TextView) findViewById(R.id.event_other_des);

        eventTimeNotify = (TextView) findViewById(R.id.event_more_time_notify);
        eventAddress = (TextView) findViewById(R.id.event_more_place);
        eventAddressMore = (TextView) findViewById(R.id.event_more_place2);
        contactRecycler = (RecyclerView) findViewById(R.id.contact_recycler);

    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

        private List<Contact> contactList;
        private Context context;
        private int itemResource;

        ContactAdapter(Context context, int itemResource, List<Contact> contactList) {
            this.contactList = contactList;
            this.context = context;
            this.itemResource = itemResource;
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(itemResource, parent, false);
            return new ContactHolder(v);
        }

        @Override
        public void onBindViewHolder(final ContactHolder holder, final int position) {
            Contact contact = contactList.get(position);
            holder.img.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));

            if (contact.getImage() != null) {
                String imageUrl = contact.getImage();
                Glide.with(context)
                        .load(BaseIP + "/" + imageUrl)
                        .thumbnail(0.5f)
                        .into(holder.img);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Objects.equals(contactsLists.get(position).getId(), Prefs.getUserKey())) {
                        Intent intent = new Intent(EventDetailsActivity.this, OtherProfileActivity.class);
                        intent.putExtra("id", contactsLists.get(position).getId());
                        startActivity(intent);
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

