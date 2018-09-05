package com.conext.conext.ui.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.Contact;
import com.conext.conext.model.db.USER_EVENT;
import com.conext.conext.ui.ContactsActivity;
import com.conext.conext.ui.OtherProfileActivity;
import com.conext.conext.ui.custom.ConatactDecorator;
import com.conext.conext.ui.custom.HexagonMaskView;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.conext.conext.WebServiceMethods.WebService.getAllContactsOfEventss;
import static com.conext.conext.WebServiceMethods.WebService.removeUser;
import static com.conext.conext.WebServiceMethods.WebService.updateEventDetailsParticipant;
import static com.conext.conext.WebServiceMethods.WebService.updateEventParticipantsStatus;
import static com.conext.conext.WebServiceMethods.WebService.updateUpcomingEventSelected;
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

public class EventDetailsFragment extends Fragment {

    private static final int CONTACT_CODE = 9;

    private static final int YES = 0;
    private static final int INTERESTED = 1;
    private static final int NO = 2;

    private static String createdUserKey;
    private ImageView bac;
    private TextView eventName, eventAddressMore, eventType, eventDate, eventMonth, eventDescription,
            eventYes, eventInterested, eventNo, eventTimeNotify, eventAddress;
    private HexagonMaskView addContact;
    private String id = null;
    private LinearLayout optionLinearLayout;
    private ArrayList<Contact> contactsLists = new ArrayList<>();
    private RecyclerView contactRecycler;
    private ContactAdapter contactAdapter = null;

    private Activity mActivity;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
        }

        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (bar != null) {
            bar.setTitle("Event Details");
        }

        addressingView(view);

        //  getEventDetails();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getEventDetails();

        if (contactAdapter != null) {
            contactsLists.clear();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CONTACT_CODE && resultCode == RESULT_OK) {

                ArrayList<Contact> contactsList = (ArrayList<Contact>) data.getSerializableExtra("contacts");

                ArrayList<String> contactId = new ArrayList<>();

                for (int i = 0; i < contactsLists.size(); i++) {
                    contactId.add(contactsLists.get(i).getId());
                }

                for (int i = 0; i < contactsList.size(); i++) {
                    if (!contactId.contains(contactsList.get(i).getId())) {
                        final String name = contactsList.get(i).getName();
                        updateEventDetailsParticipant(getActivity(), contactsList.get(i).getId(), id, contactsList.get(i).getStatus(), new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                if (result.equals("exits")) {
                                    Toast.makeText(getActivity(), name + " is already added to the Event!!! " + name + " has not Conformed ", Toast.LENGTH_LONG).show();
                                } else if (!result.equals(id)) {
                                    // contactsLists.remove(contact);
                                    //contactAdapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Something Went Wrong!!! Please try adding again", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "Your request has been Send", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), contactsList.get(i).getName() + " already exits", Toast.LENGTH_LONG).show();
                    }
                }
                contactAdapter.notifyDataSetChanged();
            }
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
                        createdUserKey = obj.getString(USER_KEY);
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
                        userEvent.setEventEndDate(obj.getString("eventEndDate"));
                        userEvent.setChoice(obj.getString("Choice"));
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
                showDialogue(getActivity(), "Sorry! Server Error", "Oops!!!");
            }
        });

        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
    }

    private void handleUserEvent(final USER_EVENT userEvent) {

        eventName.setText(userEvent.getEventTitle());

        eventType.setText(userEvent.getEventTypeKey() + " | " + userEvent.getTagKey());

        eventDescription.setText(userEvent.getDescription());
        eventDate.setText(userEvent.getEventStartDate());
        eventMonth.setText(userEvent.getEventMonthStartDate());
        eventTimeNotify.setText(userEvent.getNotifyTime());
        if (mActivity == null) {
            return;
        }
        if (userEvent.getImageKey() != null) {
            Glide.with(this)
                    .load(BaseIP + "/" + userEvent.getImageKey().trim())
                    .asBitmap()
                    .into(bac);
        }

        if (userEvent.isOpen())
            addContact.setVisibility(View.VISIBLE);
        else if (Prefs.getUserKey().equals(userEvent.getUserKey()))
            addContact.setVisibility(View.VISIBLE);
        else if (userEvent.getEventEndDate().equals("true"))
            addContact.setVisibility(View.VISIBLE);
        else
            addContact.setVisibility(View.GONE);

        String str = userEvent.getAddress();
        if (str.contains(",")) {
            eventAddress.setText(str.substring(0, str.indexOf(",")));
            eventAddressMore.setText(str.substring(str.indexOf(",") + 1, str.length()));
            eventAddressMore.setVisibility(View.VISIBLE);
        } else {
            eventAddress.setText(str);
        }

        if (Objects.equals(userEvent.getUserKey(), Prefs.getUserKey())) {
            optionLinearLayout.setVisibility(View.GONE);
            Log.e("Visibility", "One");
        } else {
            optionLinearLayout.setVisibility(View.VISIBLE);
            Log.e("Visibility", "two");
        }

        if (userEvent.getChoice().equals("1")) {
            optionLinearLayout.setVisibility(View.GONE);
            Log.e("Visiblity", "three");
        } else {
            optionLinearLayout.setVisibility(View.VISIBLE);
            Log.e("Visiblity", "four");
        }

        eventYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUpcomingEventSelected(userEvent.getEventKey(), Prefs.getUserKey(), YES, getActivity(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        handleVisibility(eventYes);
                    }
                });
            }
        });

        eventInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUpcomingEventSelected(userEvent.getEventKey(), Prefs.getUserKey(), INTERESTED, getActivity(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        handleVisibility(eventInterested);
                    }
                });
            }
        });

        eventNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUpcomingEventSelected(userEvent.getEventKey(), Prefs.getUserKey(), NO, getActivity(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        handleVisibility(eventNo);
                    }
                });
            }
        });

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(getActivity(), ContactsActivity.class);
                startActivityForResult(contactIntent, CONTACT_CODE);
            }
        });

        getAllContactsOfEvents(userEvent);

    }

    private void getAllContactsOfEvents(USER_EVENT myEvents) {

        settingContactAdapter();

        getAllContactsOfEventss(myEvents.getEventKey(), getActivity(), new VolleyCallback() {
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

    private void handleVisibility(TextView textView) {
        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        optionLinearLayout.setVisibility(View.GONE);
    }


    private void settingContactAdapter() {

        contactAdapter = new ContactAdapter(getActivity(), R.layout.card__contact_image, contactsLists);

        contactRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //For performance, tell OS RecyclerView won't change size
        contactRecycler.setHasFixedSize(true);
        // Assign adapter to recyclerView
        contactRecycler.setAdapter(contactAdapter);

        contactRecycler.addItemDecoration(new ConatactDecorator(12));
    }

    private void updateContactList(List<Contact> contactList) {
        contactsLists = (ArrayList<Contact>) contactList;
        contactAdapter.notifyDataSetChanged();
    }

    private void removeDelete(final Context context, final int position) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.Delete_Dialog);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        String status1 = null;
        String status2 = null;
        //mentee
        switch (contactsLists.get(position).getStatus()) {
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
                        contactsLists.remove(position);
                        contactAdapter.notifyItemRemoved(position);
                        contactAdapter.notifyItemRangeChanged(position, contactsLists.size());
                        contactAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        removeUser(contactsLists.get(position).getId(), id, getActivity());
                        updateContactList(contactsLists);
                    }
                })
                .setNegativeButton(status1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (contactsLists.get(position).getStatus()) {
                            case "0":
                                contactsLists.get(position).setStatus("1");
                                contactAdapter.notifyDataSetChanged();
                                updateContactList(contactsLists);
                                updateEventParticipantsStatus(contactsLists.get(position).getId(), id, 1, getActivity());
                                break;
                            case "1":
                                contactsLists.get(position).setStatus("0");
                                contactAdapter.notifyDataSetChanged();
                                updateEventParticipantsStatus(contactsLists.get(position).getId(), id, 0, getActivity());
                                updateContactList(contactsLists);
                                break;
                            case "2":
                                contactsLists.get(position).setStatus("1");
                                contactAdapter.notifyDataSetChanged();
                                updateEventParticipantsStatus(contactsLists.get(position).getId(), id, 1, getActivity());
                                updateContactList(contactsLists);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(status2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (contactsLists.get(position).getStatus()) {
                            case "0":
                                contactsLists.get(position).setStatus("2");
                                contactAdapter.notifyDataSetChanged();
                                updateEventParticipantsStatus(contactsLists.get(position).getId(), id, 2, getActivity());
                                updateContactList(contactsLists);
                                break;
                            case "1":
                                contactsLists.get(position).setStatus("2");
                                contactAdapter.notifyDataSetChanged();
                                updateEventParticipantsStatus(contactsLists.get(position).getId(), id, 2, getActivity());
                                updateContactList(contactsLists);
                                break;
                            case "2":
                                contactsLists.get(position).setStatus("0");
                                contactAdapter.notifyDataSetChanged();
                                updateEventParticipantsStatus(contactsLists.get(position).getId(), id, 0, getActivity());
                                updateContactList(contactsLists);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .show();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(context, R.style.DialogStyle);
//        } else {
//            builder = new AlertDialog.Builder(context);
//        }
//        builder.setMessage("Are you sure you want to delete this contact?")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, int which) {
//                        removeUser(Prefs.getUserKey(), id, getActivity(), new VolleyCallback() {
//                            @Override
//                            public void onSuccess(String result) {
//                                if (Boolean.parseBoolean(result)) {
//                                    contactsLists.remove(position);
//                                    contactAdapter.notifyItemRemoved(position);
//                                    contactAdapter.notifyItemRangeChanged(position, contactsLists.size());
//                                    contactAdapter.notifyDataSetChanged();
//                                    dialog.dismiss();
//                                }
//                            }
//                        });
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
    }


    private void addressingView(View view) {
        contactRecycler = (RecyclerView) view.findViewById(R.id.contact_recycler);
        optionLinearLayout = (LinearLayout) view.findViewById(R.id.optionLinearLayout);
        bac = (ImageView) view.findViewById(R.id.bac);
        eventName = (TextView) view.findViewById(R.id.event_name);
        eventType = (TextView) view.findViewById(R.id.event_type);
        eventDate = (TextView) view.findViewById(R.id.event_date);
        eventMonth = (TextView) view.findViewById(R.id.event_month);
        eventDescription = (TextView) view.findViewById(R.id.event_other_des);
        eventYes = (TextView) view.findViewById(R.id.interest_event_more_yes);
        eventInterested = (TextView) view.findViewById(R.id.interest_more_interest);
        eventNo = (TextView) view.findViewById(R.id.interest_event_more_no);
        eventTimeNotify = (TextView) view.findViewById(R.id.event_more_time_notify);
        eventAddress = (TextView) view.findViewById(R.id.event_more_place);
        eventAddressMore = (TextView) view.findViewById(R.id.event_more_place2);
        addContact = (HexagonMaskView) view.findViewById(R.id.add_contact);
        addContact.setBorderColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
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
                String imageUrl = contact.getImage().trim();
                Glide.with(context)
                        .load(BaseIP + "/" + imageUrl)
                        .thumbnail(0.5f)
                        .into(holder.img);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("tag", "clicked " + contactsLists.get(position).getId());
                    if (!Objects.equals(contactsLists.get(position).getId(), Prefs.getUserKey())) {
                        Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
                        intent.putExtra("id", contactsLists.get(position).getId());
                        startActivity(intent);
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!Objects.equals(contactsLists.get(position).getId(), Prefs.getUserKey())
                            && Objects.equals(Prefs.getUserKey(), createdUserKey)) {
                        removeDelete(context, position);
                    }
                    return false;
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
