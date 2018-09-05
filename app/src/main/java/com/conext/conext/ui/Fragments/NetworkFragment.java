package com.conext.conext.ui.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.conext.conext.Adapter.RecyclerViewAdapter;
import com.conext.conext.Adapter.SkillAdapter;
import com.conext.conext.R;
import com.conext.conext.model.Network;
import com.conext.conext.model.Skill;
import com.conext.conext.ui.custom.CustomRecyclerView;
import com.conext.conext.ui.custom.NetworkItemDecorator;
import com.conext.conext.utils.ItemClickListener;
import com.conext.conext.utils.NetworkController;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.conext.conext.db.DbConstants.MENTEE_FLAG;
import static com.conext.conext.db.DbConstants.MENTOR_FLAG;
import static com.conext.conext.db.DbConstants.PROFILE_PIC;
import static com.conext.conext.db.DbConstants.TAG_KEY;
import static com.conext.conext.db.DbConstants.TAG_NAME;
import static com.conext.conext.db.DbConstants.USER_KEY;
import static com.conext.conext.utils.Constants.GET_NETWORK_URL;
import static com.conext.conext.utils.Constants.GET_USER_TAGS_URL;
import static com.conext.conext.utils.Utility.menuIconColor;
import static com.conext.conext.utils.Utility.showDialogue;


public class NetworkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Network> arrayList = new ArrayList<>();
    private ArrayList<Long> skillFilter = new ArrayList<>();
    private ArrayList<Skill> skillArrayList = new ArrayList<>();

    private RequestQueue requestQueue;

    private RecyclerView  skillRecyclerView;
    private CustomRecyclerView networkRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridLayoutManager lLayout;

    private int totalItemCount;

    private RecyclerViewAdapter adapter;

    private boolean loading = false;
    private String userKey;

    public NetworkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Network");

        requestQueue = NetworkController.getInstance(getActivity()).getRequestQueue();

        skillRecyclerView = (RecyclerView) view.findViewById(R.id.skill_container_scroll);
        networkRecyclerView = (CustomRecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        ImageView skillDots = (ImageView) view.findViewById(R.id.skill_dots);
        Drawable skillDotsDrawable = skillDots.getDrawable();
        menuIconColor(skillDotsDrawable, R.color.colorPrimary, getActivity());

        userKey = Prefs.getUserKey();

        skillRequest();

        settingNetworkAdapter();

        return view;
    }

    private void settingSkillAdapter() {

        RecyclerView.LayoutManager mSkillLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        skillRecyclerView.setLayoutManager(mSkillLayoutManager);
        //create an ArrayAdapter from the String Array
        SkillAdapter skillAdapter = new SkillAdapter(getActivity(), R.layout.card_skill, skillArrayList);
        //For performance, tell OS RecyclerView won't change size
        skillRecyclerView.setHasFixedSize(true);
        // Assign adapter to networkRecyclerView
        skillRecyclerView.setAdapter(skillAdapter);

        filtering(skillAdapter);

    }

    private void settingNetworkAdapter() {
        // Create a grid layout with 12 columns
        // (least common multiple of 3 and 4
//        lLayout = new GridLayoutManager(getActivity(), 12, LinearLayoutManager.VERTICAL, false);
//        lLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                // 7 is the sum of items in one repeated section
//                switch (position % 7) {
//                    // first three items span 3 columns each
//                    case 0:
//                    case 1:
//                    case 2:
//                    case 3:
//                        return 3;
//                    case 4:
//                    case 5:
//                    case 6:
//                        return 4;
//                }
//                throw new IllegalStateException("internal error");
//            }
//        });

        adapter = new RecyclerViewAdapter(getActivity(), arrayList);
       // networkRecyclerView.setLayoutManager(lLayout);
        networkRecyclerView.setAdapter(adapter);
        //networkRecyclerView.addItemDecoration(new NetworkItemDecorator());

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                arrayList.clear();
                loadData();
            }
        });

        networkRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    //  visibleItemCount = lLayout.getChildCount();
                    totalItemCount = lLayout.getItemCount();
                    //  lastVisibleItems = lLayout.findFirstVisibleItemPosition();
                    if (!loading) {
                        loadMoreData(totalItemCount + 1);
                        loading = true;
                    }
                }
            }
        });

    }

    private void skillRequest() {

        Log.e("error", "loaded " + GET_USER_TAGS_URL + "/" + userKey);

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_USER_TAGS_URL + "/" + userKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        Skill skill = new Skill();
                        JSONObject obj = response.getJSONObject(i);
                        skill.setSkillId(Long.parseLong(obj.getString(TAG_KEY)));
                        skill.setTitle(obj.getString(TAG_NAME));
                        skillArrayList.add(skill);
                        skillFilter.add(Long.parseLong(obj.getString(TAG_KEY)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    Prefs.setUserSkill(skillArrayList);

                    settingSkillAdapter();

                    loadData();

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
        requestQueue.add(request);
    }

    private void networkViewRequest(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (!skillFilter.isEmpty()) {

            Log.e("error", "loaded " + GET_NETWORK_URL + "/" + android.text.TextUtils.join(",", skillFilter) +
                    "/" + userKey + "?start=" + start + "&size=" + size);

            StringRequest request = new StringRequest(Request.Method.GET, GET_NETWORK_URL + "/"
                    + android.text.TextUtils.join(",", skillFilter) + "/" + userKey + "?start=" + start + "&size=" + size, new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    JSONArray response;
                    try {
                        response = new JSONArray(result);
                        for (int i = 0; i < response.length(); i++) {
                            final Network network = new Network();
                            JSONObject obj = response.getJSONObject(i);

                            network.setId(Integer.parseInt(obj.getString(USER_KEY)));
                            network.setMentor(Integer.parseInt(obj.getString(MENTOR_FLAG)));
                            network.setMentee(Integer.parseInt(obj.getString(MENTEE_FLAG)));
                            network.setAlumini(Integer.parseInt(obj.getString("Alumini")));
                            network.setDrawable(obj.getString(PROFILE_PIC));

                            arrayList.add(network);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        //Notify adapter about data changes
                        adapter.notifyDataSetChanged();
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
            requestQueue.add(request);
        }
    }


    private void filtering(final SkillAdapter skillAdapter) {

        skillAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int id) {
                arrayList.clear();
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
                    arrayList.clear();
                }
                adapter.notifyDataSetChanged();
                loadData();
            }
        });
    }

    // By default, we add 25 objects for first time.
    private void loadData() {
        int loadLimit = 20;
        networkViewRequest(0, loadLimit);
    }

    // adding 10 object creating dymically to arraylist and updating recyclerview when ever we reached last item
    private void loadMoreData(int totalItemCount) {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        networkViewRequest(totalItemCount, totalItemCount + 20);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        arrayList.clear();
        loadData();
    }
}
