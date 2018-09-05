package com.conext.conext.ui.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.conext.conext.Adapter.ContactAdapter;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.Contact;
import com.conext.conext.model.Skill;
import com.conext.conext.model.db.USER_EVENT;
import com.conext.conext.ui.CreateEventActivity;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.conext.conext.WebServiceMethods.WebService.updateUpcomingEventSelected;
import static com.conext.conext.db.DbConstants.ADDRESS;
import static com.conext.conext.db.DbConstants.EVENT_END_TIMESTAMP;
import static com.conext.conext.db.DbConstants.EVENT_KEY;
import static com.conext.conext.db.DbConstants.EVENT_START_TIMESTAMP;
import static com.conext.conext.db.DbConstants.EVENT_TITLE;
import static com.conext.conext.db.DbConstants.IMAGE_KEY;
import static com.conext.conext.db.DbConstants.NAME;
import static com.conext.conext.db.DbConstants.TAG_NAME;
import static com.conext.conext.db.DbConstants.USER_KEY;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.GET_UP_COMING_EVENT_LIST;
import static com.conext.conext.utils.Utility.showDialogue;

//import com.conext.conext.utils.DataReceivedListener;

public class UpcomingEventFragment extends Fragment implements /*DataReceivedListener,*/ View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<USER_EVENT> upcomingEventsArrayList = new ArrayList<>();
    private ArrayList<Long> skillFilter = new ArrayList<>();
    private RecyclerAdapterUpcomingEvents upcomingEventsAdapter;
    private RecyclerView recyclerView;
    private boolean flag = true, fabStatus = false;
    private FrameLayout fraToolFloat;
    private FloatingActionButton fabSetting;
    private String userKey;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private int totalItemCount;

    private LinearLayout linFab1, linFab2, linFab3, linFab4;
    private boolean loading = false;
    Context mContext;

    public UpcomingEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upcoming_event, container, false);
        mContext = getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_upcoming_event);
        userKey = Prefs.getUserKey();
        ArrayList<Skill> skillArrayList = Prefs.getUserSkill();

        for (int i = 0; i < skillArrayList.size(); i++)
            skillFilter.add(skillArrayList.get(i).getSkillId());

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        fab(view);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fabSetting.setImageResource(R.drawable.ic_add_white_24dp);
        hideFab();
        settingAdapter();
    }

    private void fab(View view) {

        fraToolFloat = (FrameLayout) view.findViewById(R.id.fraToolFloat_upcome_event);
        fabSetting = (FloatingActionButton) view.findViewById(R.id.fabSetting_upcome_events);
        FloatingActionButton fabSub1 = (FloatingActionButton) view.findViewById(R.id.fabSub1_upcome_event);
        FloatingActionButton fabSub2 = (FloatingActionButton) view.findViewById(R.id.fabSub2_upcome_events);
        FloatingActionButton fabSub3 = (FloatingActionButton) view.findViewById(R.id.fabSub3_upcome_events);
        FloatingActionButton fabSub4 = (FloatingActionButton) view.findViewById(R.id.fabSub4_upcome_events);

        linFab1 = (LinearLayout) view.findViewById(R.id.linFab1_upcome_events);
        linFab2 = (LinearLayout) view.findViewById(R.id.linFab2_upcome_events);
        linFab3 = (LinearLayout) view.findViewById(R.id.linFab3_upcome_events);
        linFab4 = (LinearLayout) view.findViewById(R.id.linFab4_upcome_events);

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
                        fabSetting.setImageResource(R.drawable.ic_add_white_24dp);
                        flag = false;
                        hideFab();
                    }
                } else {
                    fabSetting.setImageResource(R.drawable.ic_clear_white_24dp);
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
        linFab3.setVisibility(View.VISIBLE);
        linFab4.setVisibility(View.VISIBLE);
        fraToolFloat.setBackgroundColor(Color.argb(200, 0, 0, 0));
        fabStatus = true;
    }


    private void myEventsList(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (!skillFilter.isEmpty()) {

            Log.e("error", "loaded " + GET_UP_COMING_EVENT_LIST + "/"
                    + android.text.TextUtils.join(",", skillFilter) + "/" + userKey + "?start=" + start + "&size=" + size);

            //Requests the data from webservice using volley
            StringRequest request = new StringRequest(Request.Method.GET, GET_UP_COMING_EVENT_LIST + "/"
                    + android.text.TextUtils.join(",", skillFilter) + "/" + userKey + "?start=" + start + "&size=" + size, new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    JSONArray response;
                    try {
                        response = new JSONArray(result);
                        for (int i = 0; i < response.length(); i++) {
                            USER_EVENT userEvent = new USER_EVENT();
                            JSONObject obj = response.getJSONObject(i);

                            userEvent.setUserKey(obj.getString(USER_KEY));
                            userEvent.setEventKey(obj.getString(EVENT_KEY));
                            userEvent.setEventStartTs(obj.getString(EVENT_START_TIMESTAMP));
                            userEvent.setEventEndTs(obj.getString(EVENT_END_TIMESTAMP));
                            userEvent.setEventTitle(obj.getString(EVENT_TITLE));
                            userEvent.setAddress(obj.getString(ADDRESS));
                            userEvent.setImageKey(obj.getString(IMAGE_KEY));
                            userEvent.setEventTypeKey(obj.getString(NAME));
                            userEvent.setTagKey(obj.getString(TAG_NAME));
                            userEvent.setContact(obj.getString("contacts"));
                            userEvent.setEventDate(obj.getString("eventUpcomingEventDate"));
                            userEvent.setEventStartDate(obj.getString("eventUpcomingDate"));
                            userEvent.setEventEndDate(obj.getString("eventUpcomingMonth"));

                            upcomingEventsArrayList.add(userEvent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("err", e.getMessage());
                    } finally {
                        loading = false;
                        upcomingEventsAdapter.notifyDataSetChanged();
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
            RequestQueue rQueue = Volley.newRequestQueue(mContext);
            rQueue.add(request);
        }

    }

    private void settingAdapter() {
        upcomingEventsArrayList.clear();
        upcomingEventsAdapter = new RecyclerAdapterUpcomingEvents(getContext(), upcomingEventsArrayList);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(upcomingEventsAdapter);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    //  visibleItemCount = lLayout.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    //  lastVisibleItems = lLayout.findFirstVisibleItemPosition();
                    if (!loading) {
                        loadMoreData(totalItemCount + 1);
                        loading = true;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();
        switch (v.getId()) {

            case R.id.fabSub1_upcome_event:
                Intent intent1 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "3");
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.fabSub2_upcome_events:
                Intent intent2 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "2");
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.fabSub3_upcome_events:
                Intent intent3 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "1");
                intent3.putExtras(bundle);
                startActivity(intent3);
                break;
            case R.id.fabSub4_upcome_events:
                Intent intent4 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "0");
                intent4.putExtras(bundle);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void onRefresh() {
        upcomingEventsArrayList.clear();
        loadData();
    }

    // By default, we add 25 objects for first time.
    private void loadData() {
        int loadLimit = 5;
        myEventsList(0, loadLimit);
    }

    // adding 10 object creating dymically to arraylist and updating recyclerview when ever we reached last item
    private void loadMoreData(int totalItemCount) {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        myEventsList(totalItemCount, totalItemCount + 5);
        upcomingEventsAdapter.notifyDataSetChanged();
    }

    class RecyclerAdapterUpcomingEvents extends RecyclerView.Adapter<RecyclerAdapterUpcomingEvents.MyViewHolder> {

        private List<USER_EVENT> upcomingEventsList;
        private Context context;
        private LayoutInflater inflater;
        private String str;
        private ContactAdapter contactAdapter = null;


        RecyclerAdapterUpcomingEvents(Context context, List<USER_EVENT> upcomingEventsList) {
            this.context = context;
            this.upcomingEventsList = upcomingEventsList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View rootView = inflater.inflate(R.layout.card_view_upcoming_event, parent, false);
            return new RecyclerAdapterUpcomingEvents.MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final USER_EVENT upcomingEvents = upcomingEventsList.get(position);
            holder.eventUpcomingName.setText(upcomingEvents.getEventTitle());
            holder.eventUpcomingType.setText(upcomingEvents.getEventTypeKey() + " | " + upcomingEvents.getTagKey());

            if (upcomingEvents.getImageKey() != null) {
                Glide.with(context)
                        .load(BaseIP + "/" + upcomingEvents.getImageKey())
                        .into(holder.bac);
            }
            holder.eventUpcomingDate.setText(upcomingEvents.getEventStartDate());
            holder.eventUpcomingMonth.setText(upcomingEvents.getEventEndDate());
            holder.eventUpcomingAlarmNotify.setText(upcomingEvents.getEventDate());

            str = upcomingEvents.getAddress();
            if (str.contains(",")) {
                holder.eventUpcomingPlace.setText(str.substring(0, str.indexOf(",")));
                holder.eventUpcomingPlace2.setText(str.substring(str.indexOf(",") + 1, str.length()));
                holder.eventUpcomingPlace2.setVisibility(View.VISIBLE);
            } else {
                holder.eventUpcomingPlace.setText(str);
            }


            ArrayList<Contact> contactsList = new ArrayList<>();
            holder.contactRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.contactRecyclerView.setHasFixedSize(true);
            //  holder.contactRecyclerView.addItemDecoration(new ConatactDecorator(12));
            contactAdapter = new ContactAdapter(getActivity(), R.layout.card__contact_image, contactsList);
            holder.contactRecyclerView.setAdapter(contactAdapter);

            String contacts = upcomingEvents.getContact().replaceAll("\\[", "").replaceAll("\\]", "");
            ArrayList<String> profileContactsList = new ArrayList<>(Arrays.asList(contacts.split(",")));


            for (int i = 0; i < profileContactsList.size(); i++) {
                Contact contact = new Contact();
                contact.setImage(profileContactsList.get(i).trim());
                contactsList.add(contact);
                contactAdapter.notifyDataSetChanged();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", upcomingEventsList.get(position).getEventKey());
                    eventDetailsFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.child_fragment_container, eventDetailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            holder.eventUpcomingYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    updateUpcomingEventSelected(upcomingEvents.getEventKey(), Prefs.getUserKey(), 0, getActivity(), new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            holder.interest.setVisibility(View.GONE);
                            upcomingEventsList.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }
            });

            holder.eventUpcomingInterested.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUpcomingEventSelected(upcomingEvents.getEventKey(), Prefs.getUserKey(), 1, getActivity(), new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            holder.interest.setVisibility(View.GONE);
                            upcomingEventsList.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }
            });

            holder.eventUpcomingNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUpcomingEventSelected(upcomingEvents.getEventKey(), Prefs.getUserKey(), 2, getActivity(), new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            holder.interest.setVisibility(View.GONE);
                            upcomingEventsList.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }
            });

        }

        @Override
        public int getItemCount() {
            return upcomingEventsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {

            private TextView eventUpcomingName, eventUpcomingType, eventUpcomingDate, eventUpcomingMonth,
                    eventUpcomingAlarmNotify, eventUpcomingPlace, eventUpcomingPlace2, eventUpcomingYes,
                    eventUpcomingInterested, eventUpcomingNo;
            private ImageView bac;
            private RecyclerView contactRecyclerView;
            private LinearLayout interest;

            MyViewHolder(View itemView) {
                super(itemView);
                eventUpcomingName = (TextView) itemView.findViewById(R.id.event_name);
                eventUpcomingType = (TextView) itemView.findViewById(R.id.event_type);
                eventUpcomingDate = (TextView) itemView.findViewById(R.id.event_date);
                eventUpcomingMonth = (TextView) itemView.findViewById(R.id.event_month);

                eventUpcomingAlarmNotify = (TextView) itemView.findViewById(R.id.event_time_notify);
                eventUpcomingPlace = (TextView) itemView.findViewById(R.id.event_place);
                eventUpcomingPlace2 = (TextView) itemView.findViewById(R.id.event_place2);

                eventUpcomingYes = (TextView) itemView.findViewById(R.id.event_yes);
                eventUpcomingInterested = (TextView) itemView.findViewById(R.id.event_interested);
                eventUpcomingNo = (TextView) itemView.findViewById(R.id.event_no);

                bac = (ImageView) itemView.findViewById(R.id.bac);
                interest = (LinearLayout) itemView.findViewById(R.id.intrest);

                contactRecyclerView = (RecyclerView) itemView.findViewById(R.id.upcome_events_recycler);
            }

        }
    }
}
