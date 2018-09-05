package com.conext.conext.ui.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.conext.conext.Adapter.RecyclerAdapterProfileInfo;
import com.conext.conext.Adapter.SkillAdapter;
import com.conext.conext.R;
import com.conext.conext.model.MyMentees;
import com.conext.conext.model.Skill;
import com.conext.conext.ui.OtherProfileActivity;
import com.conext.conext.ui.custom.HexagonMaskView;
import com.conext.conext.utils.ItemClickListener;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.conext.conext.db.DbConstants.COMPANY_DISPLAY;
import static com.conext.conext.db.DbConstants.IMAGE_KEY;
import static com.conext.conext.db.DbConstants.TITLE_DISPLAY;
import static com.conext.conext.db.DbConstants.USER_KEY;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.GET_MY_MENTEES_URL;
import static com.conext.conext.utils.Utility.menuIconColor;
import static com.conext.conext.utils.Utility.showDialogue;

public class MyMenteesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<MyMentees> myMenteeArrayList = new ArrayList<>();
    private ArrayList<Long> skillFilter = new ArrayList<>();
    private RecyclerAdapterMyMentee menteeAdapter = null;
    private RecyclerView skillRecyclerView, myMenteeRecyclerView;
    private String userKey;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int totalItemCount;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean loading = false;

    public MyMenteesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_mentees, container, false);
        // Inflate the layout for this fragment
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("My Mentees");

        ImageView skillDots = (ImageView) view.findViewById(R.id.skill_dots);
        SearchView searchView = (SearchView) view.findViewById(R.id.mSearch);

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ImageView searchButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        ImageView searchMagButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ImageView searchButtonClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        searchEditText.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        searchEditText.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        searchButton.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        searchMagButton.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        searchButtonClose.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        searchView.onActionViewExpanded();
        searchView.setIconified(true);
        searchView.setIconifiedByDefault(false);

        myMenteeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_mentees);
        skillRecyclerView = (RecyclerView) view.findViewById(R.id.skill_container_scroll);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        menuIconColor(skillDots.getDrawable(), R.color.colorPrimary, getActivity());

        userKey = Prefs.getUserKey();
        ArrayList<Skill> skillArrayList = Prefs.getUserSkill();

        for (int i = 0; i < skillArrayList.size(); i++)
            skillFilter.add(skillArrayList.get(i).getSkillId());

        settingAdapters(skillArrayList);

        //SEARCH
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                menteeAdapter.getFilter().filter(query);
                return false;
            }
        });
        return view;
    }

    private void filtering(SkillAdapter skillAdapter) {

        skillAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int id) {
                myMenteeArrayList.clear();
                if (skillFilter.size() > 0) {
                    boolean sameFlag = false;
                    for (int i = 0; i < skillFilter.size(); i++) {
                        if (skillFilter.get(i) == id)
                            sameFlag = true;
                    }
                    if (!sameFlag)
                        skillFilter.add((long) id);
                    else
                        skillFilter.remove((long) id);
                } else if (skillFilter.size() == 0) {
                    skillFilter.add((long) id);
                }

                if (skillFilter.size() == 0) {
                    myMenteeArrayList.clear();
                }
                menteeAdapter.notifyDataSetChanged();
                loadData();
            }
        });
    }

    private void settingAdapters(ArrayList<Skill> skillArrayList) {

        RecyclerView.LayoutManager mSkillLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        skillRecyclerView.setLayoutManager(mSkillLayoutManager);

        //create an ArrayAdaptar from the String Array
        SkillAdapter skillAdapter = new SkillAdapter(getActivity(), R.layout.card_skill, skillArrayList);

        //For performance, tell OS RecyclerView won't change size
        skillRecyclerView.setHasFixedSize(true);

        // Assign adapter to myMenteeRecyclerView
        skillRecyclerView.setAdapter(skillAdapter);

        menteeAdapter = new RecyclerAdapterMyMentee(getActivity(), myMenteeArrayList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myMenteeRecyclerView.setLayoutManager(mLayoutManager);
        myMenteeRecyclerView.setHasFixedSize(true);
        myMenteeRecyclerView.setAdapter(menteeAdapter);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });

        myMenteeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        filtering(skillAdapter);
    }

    private void myMenteeRequest(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (!skillFilter.isEmpty()) {

            Log.e("error", "loaded " + GET_MY_MENTEES_URL + "/" + android.text.TextUtils.join(",", skillFilter) +
                    "/" + userKey + "?start=" + start + "&size=" + size);

            StringRequest request = new StringRequest(Request.Method.GET, GET_MY_MENTEES_URL + "/"
                    + android.text.TextUtils.join(",", skillFilter) + "/" + userKey + "?start=" + start + "&size=" + size, new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    JSONArray response;
                    try {
                        response = new JSONArray(result);
                        for (int i = 0; i < response.length(); i++) {

                            final MyMentees myMentees = new MyMentees();

                            JSONObject obj = response.getJSONObject(i);

                            myMentees.setMentorID(String.valueOf(obj.getString(USER_KEY)));

                            myMentees.setMenteeName(obj.getString(TITLE_DISPLAY));

                            myMentees.setMenteeDescription(obj.getString(COMPANY_DISPLAY));

                            myMentees.setOtherMentoring(obj.getString("other"));

                            myMentees.setMenteeIn(obj.getString("mentee"));

                            myMentees.setMenteeImage(obj.getString(IMAGE_KEY));

                            myMenteeArrayList.add(myMentees);
                        }
                        menteeAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
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
            RequestQueue rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(request);
        } else {
            ArrayList<Skill> skillArrayList = Prefs.getUserSkill();
            ArrayList<Long> skillIdList =  new ArrayList<>();

            for (int i = 0; i < skillArrayList.size(); i++)
                skillIdList.add(skillArrayList.get(i).getSkillId());

            Log.e("error", "loaded " + GET_MY_MENTEES_URL + "/" + android.text.TextUtils.join(",", skillIdList) +
                    "/" + userKey + "/1" + "?start=" + start + "&size=" + size);

            StringRequest request = new StringRequest(Request.Method.GET, GET_MY_MENTEES_URL + "/"
                    + android.text.TextUtils.join(",", skillIdList) + "/" + userKey + "/1" + "?start=" + start + "&size=" + size, new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    JSONArray response;
                    try {
                        response = new JSONArray(result);
                        for (int i = 0; i < response.length(); i++) {

                            final MyMentees myMentees = new MyMentees();

                            JSONObject obj = response.getJSONObject(i);

                            myMentees.setMentorID(String.valueOf(obj.getString(USER_KEY)));

                            myMentees.setMenteeName(obj.getString(TITLE_DISPLAY));

                            myMentees.setMenteeDescription(obj.getString(COMPANY_DISPLAY));

                            myMentees.setOtherMentoring(obj.getString("other"));

                            myMentees.setMenteeIn(obj.getString("mentee"));

                            myMentees.setMenteeImage(obj.getString(IMAGE_KEY));

                            myMenteeArrayList.add(myMentees);
                        }
                        menteeAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
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
            RequestQueue rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(request);

        }
    }

    // adding 10 object creating dymically to arraylist and updating recyclerview when ever we reached last item
    private void loadMoreData(int totalItemCount) {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        myMenteeRequest(totalItemCount, totalItemCount + 5);
        menteeAdapter.notifyDataSetChanged();
    }

    // By default, we add 25 objects for first time.
    private void loadData() {
        int loadLimit = 5;
        myMenteeRequest(0, loadLimit);
    }

    @Override
    public void onRefresh() {
        myMenteeArrayList.clear();
        loadData();
    }

    class RecyclerAdapterMyMentee extends RecyclerView.Adapter<RecyclerAdapterMyMentee.MyViewHolder> implements Filterable {
        private List<MyMentees> myMenteesList;
        private Context context;
        private LayoutInflater inflater;
        private MenteeCustomFilter filter;

        RecyclerAdapterMyMentee(Context context, List<MyMentees> myMenteesList) {

            this.context = context;
            this.myMenteesList = myMenteesList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View rootView = inflater.inflate(R.layout.card_view_my_mentee, parent, false);
            return new RecyclerAdapterMyMentee.MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final MyMentees myMentees = myMenteesList.get(position);

            holder.menteeName.setText(myMentees.getMenteeName());
            holder.menteeDescription.setText(myMentees.getMenteeDescription());

            if (myMentees.getMenteeImage() != null) {
                Glide.with(context)
                        .load(BaseIP + "/" + myMentees.getMenteeImage())
                        .into(holder.contactImage);
            }

            holder.contactImage.setBorderColor(ContextCompat.getColor(context, R.color.bg_card));

            if (!myMentees.getMenteeIn().equals("")) {
                List<String> menteeInSkillArrayList = Arrays.asList(myMentees.getMenteeIn().split("\\s*,\\s*"));
                RecyclerAdapterProfileInfo skillInfoAdapter = new RecyclerAdapterProfileInfo(getActivity(), menteeInSkillArrayList, R.layout.my_mentee_row);
                holder.menteeInRecycler.setLayoutManager(new LinearLayoutManager(context));
                holder.menteeInRecycler.setHasFixedSize(true);
                holder.menteeInRecycler.setAdapter(skillInfoAdapter);
            } else {
                holder.menteeIn.setVisibility(View.GONE);
                holder.menteeInRecycler.setVisibility(View.INVISIBLE);
                holder.otherMentee.setVisibility(View.GONE);
                holder.otherSkill.setVisibility(View.GONE);
            }

            if (myMentees.getOtherMentoring().equals("")) {
                holder.otherMentee.setVisibility(View.GONE);
                holder.view.setVisibility(View.GONE);
                holder.otherSkill.setVisibility(View.GONE);
            } else {
                holder.otherMentee.setVisibility(View.VISIBLE);
                holder.otherSkill.setVisibility(View.VISIBLE);
                holder.otherSkill.setText(myMentees.getOtherMentoring());
            }

            holder.otherSkill.setText(myMentees.getOtherMentoring());

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
                    intent.putExtra("id", myMenteesList.get(position).getMentorID());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return myMenteesList.size();
        }


        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new MenteeCustomFilter(myMenteesList, this);
            }
            return filter;
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView menteeName, menteeDescription, menteeIn, otherMentee, otherSkill;
            // private PorterShapeImageView contactImage;
            private HexagonMaskView contactImage;
            private RecyclerView menteeInRecycler;
            private ItemClickListener itemClickListener;
            private View view;

            public MyViewHolder(View itemView) {
                super(itemView);
                menteeName = (TextView) itemView.findViewById(R.id.mentor_name);
                menteeDescription = (TextView) itemView.findViewById(R.id.mentor_description);
                menteeIn = (TextView) itemView.findViewById(R.id.mentee_in);
                menteeInRecycler = (RecyclerView) itemView.findViewById(R.id.mentee_in_recycler);
                otherMentee = (TextView) itemView.findViewById(R.id.other_interest);
                otherSkill = (TextView) itemView.findViewById(R.id.other_skill);
                contactImage = (HexagonMaskView) itemView.findViewById(R.id.contact_image);
                view = itemView.findViewById(R.id.view);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                this.itemClickListener.onItemClick(v, getLayoutPosition());
            }

            void setItemClickListener(ItemClickListener ic) {
                this.itemClickListener = ic;
            }
        }

    }


    private class MenteeCustomFilter extends Filter {
        private RecyclerAdapterMyMentee contactAdapter;
        List<MyMentees> menteeArrayList;

        MenteeCustomFilter(List<MyMentees> menteeArrayList, RecyclerAdapterMyMentee contactAdapter) {
            this.contactAdapter = contactAdapter;
            this.menteeArrayList = menteeArrayList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            //CHECK CONSTRAINT VALIDITY
            if (constraint != null && constraint.length() > 0) {
                //CHANGE TO UPPER
                constraint = constraint.toString().toUpperCase();
                //STORE OUR FILTERED PLAYERS
                ArrayList<MyMentees> filteredContact = new ArrayList<>();
                for (int i = 0; i < menteeArrayList.size(); i++) {
                    //CHECK
                    if (menteeArrayList.get(i).getMenteeName().toUpperCase().contains(constraint)) {
                        //ADD PLAYER TO FILTERED PLAYERS
                        filteredContact.add(menteeArrayList.get(i));
                    }
                }
                results.count = filteredContact.size();
                results.values = filteredContact;
            } else {
                results.count = menteeArrayList.size();
                results.values = menteeArrayList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactAdapter.myMenteesList = (ArrayList<MyMentees>) results.values;
            contactAdapter.notifyDataSetChanged();
        }
    }
}
