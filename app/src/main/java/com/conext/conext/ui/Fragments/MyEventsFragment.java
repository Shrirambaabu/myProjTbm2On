package com.conext.conext.ui.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.conext.conext.model.Contact;
import com.conext.conext.model.db.USER_EVENT;
import com.conext.conext.ui.CreateEventActivity;
import com.conext.conext.ui.custom.HexagonMaskView;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.conext.conext.db.DbConstants.ADDRESS;
import static com.conext.conext.db.DbConstants.EVENT_KEY;
import static com.conext.conext.db.DbConstants.EVENT_TITLE;
import static com.conext.conext.db.DbConstants.IMAGE_KEY;
import static com.conext.conext.db.DbConstants.NAME;
import static com.conext.conext.db.DbConstants.TAG_NAME;
import static com.conext.conext.db.DbConstants.USER_KEY;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.GET_MY_EVENTS_LIST_URL;
import static com.conext.conext.utils.Utility.showDialogue;


public class MyEventsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private boolean fabStatus = false;
    private FrameLayout fraToolFloat;
    private FloatingActionButton fabSetting;

    private LinearLayout linFab1, linFab2, linFab3, linFab4;
    private boolean flag = true;
    private RecyclerAdapterMyEvents myEventsAdapter = null;
    private RecyclerView recyclerView;
    private ArrayList<USER_EVENT> myEventsArrayList = new ArrayList<>();
    private String userKey;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int totalItemCount;
    private boolean loading = false;
    private RecyclerView.LayoutManager mLayoutManager;
    Context mContext;

    public MyEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        mContext = getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_my_event);

        userKey = Prefs.getUserKey();

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
        fraToolFloat = (FrameLayout) view.findViewById(R.id.fraToolFloat_my_events);
        fabSetting = (FloatingActionButton) view.findViewById(R.id.fabSetting_my_events);
        FloatingActionButton fabSub1 = (FloatingActionButton) view.findViewById(R.id.fabSub1_my_events);
        FloatingActionButton fabSub2 = (FloatingActionButton) view.findViewById(R.id.fabSub2_my_events);
        FloatingActionButton fabSub3 = (FloatingActionButton) view.findViewById(R.id.fabSub3_my_events);
        FloatingActionButton fabSub4 = (FloatingActionButton) view.findViewById(R.id.fabSub4_my_events);

        linFab1 = (LinearLayout) view.findViewById(R.id.linFab1_my_events);
        linFab2 = (LinearLayout) view.findViewById(R.id.linFab2_my_events);
        linFab3 = (LinearLayout) view.findViewById(R.id.linFab3_my_events);
        linFab4 = (LinearLayout) view.findViewById(R.id.linFab4_my_events);

        fabSub1.setOnClickListener(this);
        fabSub2.setOnClickListener(this);
        fabSub3.setOnClickListener(this);
        fabSub4.setOnClickListener(this);
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

    private void showFab() {
        linFab1.setVisibility(View.VISIBLE);
        linFab2.setVisibility(View.VISIBLE);
        linFab3.setVisibility(View.VISIBLE);
        linFab3.setVisibility(View.VISIBLE);
        linFab4.setVisibility(View.VISIBLE);
        fraToolFloat.setBackgroundColor(Color.argb(200, 0, 0, 0));
        fabStatus = true;
    }

    private void hideFab() {
        linFab1.setVisibility(View.INVISIBLE);
        linFab2.setVisibility(View.INVISIBLE);
        linFab3.setVisibility(View.INVISIBLE);
        linFab4.setVisibility(View.INVISIBLE);
        fraToolFloat.setBackgroundColor(Color.argb(0, 255, 255, 255));
        fabStatus = false;

    }

    private void settingAdapter() {
        myEventsArrayList.clear();
        myEventsAdapter = new RecyclerAdapterMyEvents(getContext(), myEventsArrayList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myEventsAdapter);


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                myEventsArrayList.clear();
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

            case R.id.fabSub1_my_events:
                Intent intent1 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "3");
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.fabSub2_my_events:
                Intent intent2 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "2");
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.fabSub3_my_events:
                Intent intent3 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "1");
                intent3.putExtras(bundle);
                startActivity(intent3);
                break;
            case R.id.fabSub4_my_events:
                Intent intent4 = new Intent(getActivity(), CreateEventActivity.class);
                bundle.putString("value", "0");
                intent4.putExtras(bundle);
                startActivity(intent4);
                break;
        }

    }

    private void myEventsList(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        Log.e("error", "loaded " + GET_MY_EVENTS_LIST_URL + "/" + userKey + "?start=" + start + "&size=" + size);

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_MY_EVENTS_LIST_URL
                + "/" + userKey + "?start=" + start + "&size=" + size, new Response.Listener<String>() {
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
                        userEvent.setEventTitle(obj.getString(EVENT_TITLE));
                        userEvent.setAddress(obj.getString(ADDRESS));
                        userEvent.setEventTypeKey(obj.getString(NAME));
                        userEvent.setTagKey(obj.getString(TAG_NAME));
                        userEvent.setEventTypeKey(obj.getString(NAME));
                        userEvent.setImageKey(obj.getString(IMAGE_KEY));
                        userEvent.setContact(obj.getString("contacts"));
                        userEvent.setEventDate(obj.getString("myEventDate"));
                        userEvent.setEventStartDate(obj.getString("myDate"));
                        userEvent.setEventEndDate(obj.getString("myMonth"));

                        myEventsArrayList.add(userEvent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    myEventsAdapter.notifyDataSetChanged();
                    loading = false;
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

    @Override
    public void onRefresh() {
        myEventsArrayList.clear();
        loadData();
    }

    // By default, we add 25 objects for first time.
    private void loadData() {
        int loadLimit = 5;
        myEventsList(0, loadLimit);
    }

    // adding 10 object creating dyamically to arrayList and updating recyclerview when ever we reached last item
    private void loadMoreData(int totalItemCount) {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        myEventsList(totalItemCount, totalItemCount + 5);
        myEventsAdapter.notifyDataSetChanged();
    }

    class RecyclerAdapterMyEvents extends RecyclerView.Adapter<RecyclerAdapterMyEvents.MyViewHolder> {

        private List<USER_EVENT> myEventsList = new ArrayList<>();
        private Context context;
        private LayoutInflater inflater;
        private String str;

        RecyclerAdapterMyEvents(Context context, List<USER_EVENT> myEventsList) {
            this.context = context;
            this.myEventsList = myEventsList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_my_events, parent, false);
            return new RecyclerAdapterMyEvents.MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            USER_EVENT myEvents = myEventsList.get(holder.getAdapterPosition());

            holder.eventMyType.setText(myEvents.getEventTypeKey() + " | " + myEvents.getTagKey());
            holder.eventMyName.setText(myEvents.getEventTitle());
            holder.eventMyAddPeople.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            holder.eventMyDate.setText(myEvents.getEventStartDate());
            holder.eventMyMonth.setText(myEvents.getEventEndDate());

            holder.eventMyAlarmNotify.setText(myEvents.getEventDate());

            str = myEvents.getAddress();
            if (str.contains(",")) {
                holder.eventMyPlace.setText(str.substring(0, str.indexOf(",")));
                holder.eventMyPlace2.setText(str.substring(str.indexOf(",") + 1, str.length()));
                holder.eventMyPlace2.setVisibility(View.VISIBLE);
            } else {
                holder.eventMyPlace.setText(str);
            }

            Glide.with(context)
                    .load(BaseIP + "/" + myEvents.getImageKey())
                    .into(holder.bac);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", myEventsList.get(holder.getAdapterPosition()).getEventKey());
                    eventDetailsFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.child_fragment_container, eventDetailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            ArrayList<Contact> newContactArrayList = new ArrayList<>();
            holder.contactMyRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.contactMyRecyclerView.setHasFixedSize(true);
            // holder.contactMyRecyclerView.addItemDecoration(new ConatactDecorator(12));
            ContactAdapter contactAdapterMyEvents = new ContactAdapter(getActivity(), R.layout.card__contact_image, newContactArrayList);
            holder.contactMyRecyclerView.setAdapter(contactAdapterMyEvents);

            String contacts = myEvents.getContact().replaceAll("\\[", "").replaceAll("\\]", "");
            ArrayList<String> profileContactsList = new ArrayList<>(Arrays.asList(contacts.split(",")));


            for (int i = 0; i < profileContactsList.size(); i++) {
                Contact contact = new Contact();
                contact.setImage(profileContactsList.get(i).trim());
                newContactArrayList.add(contact);
                contactAdapterMyEvents.notifyDataSetChanged();
            }

        }

        @Override
        public int getItemCount() {
            return myEventsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView eventMyName, eventMyType, eventMyDate, eventMyMonth, eventMyAlarmNotify, eventMyPlace, eventMyPlace2;
            private ImageView bac;
            private RecyclerView contactMyRecyclerView;
            private HexagonMaskView eventMyAddPeople;

            MyViewHolder(View itemView) {
                super(itemView);
                eventMyName = (TextView) itemView.findViewById(R.id.event_my_name);
                eventMyType = (TextView) itemView.findViewById(R.id.event_my_type);
                eventMyDate = (TextView) itemView.findViewById(R.id.event_my_date);
                eventMyMonth = (TextView) itemView.findViewById(R.id.event_my_month);
                eventMyAlarmNotify = (TextView) itemView.findViewById(R.id.event_my_time_notify);
                eventMyPlace = (TextView) itemView.findViewById(R.id.event_my_place);
                eventMyPlace2 = (TextView) itemView.findViewById(R.id.event_my_place2);
                bac = (ImageView) itemView.findViewById(R.id.bac);
                eventMyAddPeople = (HexagonMaskView) itemView.findViewById(R.id.hex_one_event_my);
                contactMyRecyclerView = (RecyclerView) itemView.findViewById(R.id.my_events_recycler);
            }

        }
    }

}