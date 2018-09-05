package com.conext.conext.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.OtherNotification;
import com.conext.conext.model.Skill;
import com.conext.conext.model.db.USER_EVENT;
import com.conext.conext.ui.custom.HexagonMaskView;
import com.conext.conext.utils.NetworkController;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.conext.conext.WebServiceMethods.WebService.updateParticipantStatus;
import static com.conext.conext.db.DbConstants.EVENT_KEY;
import static com.conext.conext.db.DbConstants.EVENT_TITLE;
import static com.conext.conext.db.DbConstants.IMAGE_KEY;
import static com.conext.conext.db.DbConstants.MENTEE;
import static com.conext.conext.db.DbConstants.MENTEE_FLAG;
import static com.conext.conext.db.DbConstants.MENTOR;
import static com.conext.conext.db.DbConstants.MENTOR_FLAG;
import static com.conext.conext.db.DbConstants.PARTICIPANT;
import static com.conext.conext.db.DbConstants.TAG_NAME;
import static com.conext.conext.db.DbConstants.TITLE_DISPLAY;
import static com.conext.conext.db.DbConstants.USER_KEY;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.GET_UP_COMING_EVENT_NOTIFICATION_LIST;
import static com.conext.conext.utils.Constants.GET_UP_COMING_EVENT_OTHER_NOTIFICATION_LIST;
import static com.conext.conext.utils.Utility.showDialogue;


public class NotificationFragment extends Fragment {


    public static final int START_SIZE = 0;
    public static final int SIZE_UP_TO = 5;
    private RecyclerAdapterOtherNotification otherNotificationAdapter;
    private RecyclerAdapterUpcomingNotification upcomingAdapter;
    private RecyclerView.LayoutManager mLayoutManager, mLayoutManagerOther;
    private int totalItemCountUp, totalItemCountOther;
    private boolean loading = false, loadingOther = false;

    private ArrayList<OtherNotification> otherNotificationArrayList = new ArrayList<>();
    private ArrayList<USER_EVENT> myEventsArrayList = new ArrayList<>();
    private TextView up_coming, otherNotification, nothingText;

    private RequestQueue rQueue;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Notification");

        up_coming = (TextView) view.findViewById(R.id.up_comming);
        otherNotification = (TextView) view.findViewById(R.id.notifi);
        nothingText = (TextView) view.findViewById(R.id.nothing_text);

        rQueue = NetworkController.getInstance(getActivity()).getRequestQueue();

        displayUpcomingNotification(view);

        displayOtherNotification(view);

      /*  Log.e("UpcomeMain",""+myEventsArrayList.isEmpty());
        Log.e("OtherMain",""+otherNotificationArrayList.isEmpty());
        if (myEventsArrayList.isEmpty() && otherNotificationArrayList.isEmpty()) {
            nothingText.setVisibility(View.VISIBLE);
        } else {
            nothingText.setVisibility(View.GONE);
        }
*/
        return view;
    }


    private void displayUpcomingNotification(final View view) {

        upcomingAdapter = new RecyclerAdapterUpcomingNotification(getActivity(), myEventsArrayList);

        RecyclerView recyclerViewUp = (RecyclerView) view.findViewById(R.id.upcome_recycler);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewUp.setLayoutManager(mLayoutManager);
        recyclerViewUp.setHasFixedSize(true);
        recyclerViewUp.setAdapter(upcomingAdapter);

        getMyUpComingEventList(START_SIZE, SIZE_UP_TO);

        recyclerViewUp.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    //  visibleItemCount = lLayout.getChildCount();
                    totalItemCountUp = mLayoutManager.getItemCount();
                    //  lastVisibleItems = lLayout.findFirstVisibleItemPosition();
                    if (!loading) {
                        loadMoreData(totalItemCountUp + 1);
                        loading = true;
                    }
                }
            }
        });

    }

    private void loadMoreData(int totalItemCount) {
        getMyUpComingEventList(totalItemCount, totalItemCount + 5);
        upcomingAdapter.notifyDataSetChanged();
    }


    private void getMyUpComingEventList(int start, int size) {

        ArrayList<Skill> skillArrayList = Prefs.getUserSkill();

        ArrayList<Long> skillFilter = new ArrayList<>();
        for (int i = 0; i < skillArrayList.size(); i++)
            skillFilter.add(skillArrayList.get(i).getSkillId());

        if (!skillFilter.isEmpty()) {

            Log.e("error", "loaded " + GET_UP_COMING_EVENT_NOTIFICATION_LIST + "/"
                    + android.text.TextUtils.join(",", skillFilter) + "/" + Prefs.getUserKey() + "?start=" + start + "&size=" + size);

            StringRequest request = new StringRequest(Request.Method.GET, GET_UP_COMING_EVENT_NOTIFICATION_LIST + "/"
                    + android.text.TextUtils.join(",", skillFilter) + "/" + Prefs.getUserKey() + "?start=" + start + "&size=" + size, new Response.Listener<String>() {
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
                            userEvent.setEventStartTs(obj.getString("dateStarUpcomingEvent"));
                            userEvent.setEventEndTs(obj.getString("monthStartUpcomingEvent"));
                            userEvent.setEventTitle(obj.getString(EVENT_TITLE));
                            userEvent.setImageKey(obj.getString(IMAGE_KEY));
                            userEvent.setNoOfPeople(obj.getString("noOfPeople"));
                            userEvent.setEventDate(obj.getString("dateTimeMyEvent"));

                            myEventsArrayList.add(userEvent);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error", e.getMessage());
                    } finally {
                        loading = false;
                        upcomingAdapter.notifyDataSetChanged();
                        updateUpComingAdapter();
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

            rQueue.add(request);
        }
    }

    private void updateUpComingAdapter() {
        Log.e("UpcAdapter", "" + myEventsArrayList.isEmpty());
        Log.e("UpcOtherAdapter", "" + otherNotificationArrayList.isEmpty());
        if (!myEventsArrayList.isEmpty()) {
            up_coming.setVisibility(View.VISIBLE);
        } else {
            up_coming.setVisibility(View.GONE);
        }

        if (myEventsArrayList.isEmpty() && otherNotificationArrayList.isEmpty()) {
            nothingText.setVisibility(View.VISIBLE);
        } else {
            nothingText.setVisibility(View.GONE);
        }


    }

    private void displayOtherNotification(final View view) {

        otherNotificationAdapter = new RecyclerAdapterOtherNotification(getActivity(), otherNotificationArrayList);

        RecyclerView recyclerViewOther = (RecyclerView) view.findViewById(R.id.other_recycler);
        mLayoutManagerOther = new LinearLayoutManager(getActivity());
        recyclerViewOther.setLayoutManager(mLayoutManagerOther);
        recyclerViewOther.setHasFixedSize(true);
        recyclerViewOther.setAdapter(otherNotificationAdapter);

        getMenteeMentorRequestList(START_SIZE, SIZE_UP_TO);

        recyclerViewOther.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    //  visibleItemCount = lLayout.getChildCount();
                    totalItemCountOther = mLayoutManagerOther.getItemCount();
                    //  lastVisibleItems = lLayout.findFirstVisibleItemPosition();
                    if (!loadingOther) {
                        loadMoreDataOther(totalItemCountOther + 1);
                        loadingOther = true;
                    }
                }
            }
        });
    }

    private void getMenteeMentorRequestList(int start, int size) {

        Log.e("error", "loaded " + GET_UP_COMING_EVENT_OTHER_NOTIFICATION_LIST + "/" + Prefs.getUserKey() + "?start=" + start + "&size=" + size);

        StringRequest request = new StringRequest(Request.Method.GET, GET_UP_COMING_EVENT_OTHER_NOTIFICATION_LIST + "/" + Prefs.getUserKey() + "?start=" + start + "&size=" + size, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        OtherNotification otherNotification = new OtherNotification();

                        otherNotification.setEventId(obj.getString(EVENT_KEY));

                        if (obj.getString(MENTOR_FLAG).equals("0") && obj.getString(MENTEE_FLAG).equals("1"))
                            otherNotification.setMentorMentee(MENTEE);
                        else if (obj.getString(MENTOR_FLAG).equals("1") && obj.getString(MENTEE_FLAG).equals("0"))
                            otherNotification.setMentorMentee(MENTOR);
                        else
                            otherNotification.setMentorMentee(PARTICIPANT);

                        otherNotification.setNotifyName(obj.getString(TITLE_DISPLAY));
                        otherNotification.setImg(obj.getString(IMAGE_KEY));
                        otherNotification.setNotifyTime(obj.getString("notificationTime"));
                        otherNotification.setEventType(obj.getString(TAG_NAME));

                        otherNotificationArrayList.add(otherNotification);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("printStackTrace", e.getMessage());
                } finally {
                    loadingOther = false;
                    updateOtherNotificationAdapter();
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

        rQueue.add(request);
    }

    private void loadMoreDataOther(int totalItemCount) {
        getMenteeMentorRequestList(totalItemCount, totalItemCountOther + 5);
        otherNotificationAdapter.notifyDataSetChanged();
    }

    private void updateOtherNotificationAdapter() {
        Log.e("OtherUpcomAdapter", "" + myEventsArrayList.isEmpty());
        Log.e("OtherAdapter", "" + otherNotificationArrayList.isEmpty());
        if (!otherNotificationArrayList.isEmpty()) {
            otherNotification.setVisibility(View.VISIBLE);
        } else {
            otherNotification.setVisibility(View.GONE);
        }
        if (myEventsArrayList.isEmpty() && otherNotificationArrayList.isEmpty()) {
            nothingText.setVisibility(View.VISIBLE);
        } else {
            nothingText.setVisibility(View.GONE);
        }

        otherNotificationAdapter.notifyDataSetChanged();
    }

    class RecyclerAdapterOtherNotification extends RecyclerView.Adapter<RecyclerAdapterOtherNotification.MyViewHolder> {
        private List<OtherNotification> otherNotificationList;
        private Context context;
        private LayoutInflater inflater;

        RecyclerAdapterOtherNotification(Context context, List<OtherNotification> otherNotificationList) {

            this.context = context;
            this.otherNotificationList = otherNotificationList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View rootView = inflater.inflate(R.layout.card_view_other_notification, parent, false);
            return new RecyclerAdapterOtherNotification.MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final boolean[] visible = {false};
            final OtherNotification otherNotification = otherNotificationList.get(position);

            Log.e("OtherNotif", "" + otherNotification.getImg());
            holder.notifyName.setText(otherNotification.getNotifyName());
            holder.notifyTime.setText(otherNotification.getNotifyTime());

            Spanned result;

            if (otherNotification.getMentorMentee().equals("Mentee")) {
                String sourceString = "Wants you to be a Mentee in <b>" + otherNotification.getEventType() + "</b> Event";

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    result = Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY);
                } else {
                    result = Html.fromHtml(sourceString);
                }
                holder.wantsMentor.setText(result);

            } else if (otherNotification.getMentorMentee().equals("Mentor")) {
                String sourceString = "Wants you to Mentor him for <b>" + otherNotification.getEventType() + "</b> ";

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    result = Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY);
                } else {
                    result = Html.fromHtml(sourceString);
                }
                holder.wantsMentor.setText(result);

            } else {
                String sourceString = "Wants you to be a Participant for <b>" + otherNotification.getEventType() + "</b> Event";

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    result = Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY);
                } else {
                    result = Html.fromHtml(sourceString);
                }
                holder.wantsMentor.setText(result);
            }

            holder.contactNotify.setBorderColor(ContextCompat.getColor(context, R.color.black));

            if (otherNotification.getImg() != null) {
                Glide.with(context)
                        .load(BaseIP + "/" + otherNotification.getImg().trim())
                        .into(holder.contactNotify);
            }

            holder.defaultOption.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!visible[0]) {
                        holder.longPressOption.setVisibility(View.VISIBLE);
                        visible[0] = true;
                    } else {
                        holder.longPressOption.setVisibility(View.GONE);
                        visible[0] = false;
                    }
                    return true;
                }
            });

            holder.acceptNotify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateParticipantStatus(otherNotification.getEventId(), Prefs.getUserKey(), "1", getActivity(), new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {

                            otherNotificationList(position);

                        }
                    });
                }
            });

            holder.declineNotify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateParticipantStatus(otherNotification.getEventId(), Prefs.getUserKey(), "2", getActivity(), new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            otherNotificationList(position);
                        }
                    });
                }
            });

            holder.wantsMentor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", otherNotification.getEventId());
                    eventDetailsFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.child_fragment_container, eventDetailsFragment)
                            .addToBackStack(null).commit();
                }
            });
        }

        private void otherNotificationList(int position) {
            otherNotificationList.remove(position);

            otherNotificationAdapter.notifyDataSetChanged();

            if (!otherNotificationArrayList.isEmpty()) {
                otherNotification.setVisibility(View.VISIBLE);
            } else {
                otherNotification.setVisibility(View.GONE);
            }
            if (myEventsArrayList.isEmpty() && otherNotificationArrayList.isEmpty()) {
                nothingText.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return otherNotificationList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView notifyName, notifyTime, wantsMentor, declineNotify, acceptNotify;
            private RelativeLayout longPressOption, defaultOption;
            // private PorterShapeImageView contactNotify;
            private HexagonMaskView contactNotify;

            MyViewHolder(View itemView) {
                super(itemView);

                notifyName = (TextView) itemView.findViewById(R.id.notify_name);
                notifyTime = (TextView) itemView.findViewById(R.id.notify_time);
                wantsMentor = (TextView) itemView.findViewById(R.id.wants_mentor);
                declineNotify = (TextView) itemView.findViewById(R.id.decline_notify);
                acceptNotify = (TextView) itemView.findViewById(R.id.accept_notify);

                contactNotify = (HexagonMaskView) itemView.findViewById(R.id.contact_notify);
                longPressOption = (RelativeLayout) itemView.findViewById(R.id.longPress);
                defaultOption = (RelativeLayout) itemView.findViewById(R.id.notifyNormal);

            }
        }
    }

    class RecyclerAdapterUpcomingNotification extends RecyclerView.Adapter<RecyclerAdapterUpcomingNotification.MyViewHolder> {
        private List<USER_EVENT> notificationUpcomingList;
        private Context context;
        private LayoutInflater inflater;

        RecyclerAdapterUpcomingNotification(Context context, List<USER_EVENT> notificationUpcomingList) {

            this.context = context;
            this.notificationUpcomingList = notificationUpcomingList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_notification_upcoming, parent, false);
            return new RecyclerAdapterUpcomingNotification.MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final USER_EVENT notificationUpcoming = notificationUpcomingList.get(position);

            holder.meetNotify.setText(notificationUpcoming.getEventTitle());

            holder.dateNotify.setText(notificationUpcoming.getEventStartDate());
            holder.monthNotify.setText(notificationUpcoming.getEventStartDate());

            holder.timeNotify.setText(notificationUpcoming.getEventDate() + " onwards");

            Glide.with(context)
                    .load(BaseIP + "/" + notificationUpcoming.getImageKey())
                    .thumbnail(0.5f)
                    .into(holder.imageNotify);

            if (notificationUpcoming.getNoOfPeople().equals("0"))
                holder.friendsNotify.setText("1 friend are going");
            else
                holder.friendsNotify.setText(notificationUpcoming.getNoOfPeople() + " friends are going");


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("tag", "click" + notificationUpcomingList.get(position).getEventKey());

                    EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", notificationUpcomingList.get(position).getEventKey());
                    eventDetailsFragment.setArguments(bundle);
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.child_fragment_container, eventDetailsFragment)
                            .addToBackStack(null).commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return notificationUpcomingList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView dateNotify, monthNotify, meetNotify, timeNotify, friendsNotify;
            private ImageView imageNotify;

            MyViewHolder(View itemView) {
                super(itemView);
                dateNotify = (TextView) itemView.findViewById(R.id.notify_upcome_date_one);
                monthNotify = (TextView) itemView.findViewById(R.id.notify_upcome_month_one);
                meetNotify = (TextView) itemView.findViewById(R.id.notify_meet);
                timeNotify = (TextView) itemView.findViewById(R.id.onwards_notify);
                friendsNotify = (TextView) itemView.findViewById(R.id.friends_notify);

                imageNotify = (ImageView) itemView.findViewById(R.id.img_notify_upcome);
            }
        }
    }
}
