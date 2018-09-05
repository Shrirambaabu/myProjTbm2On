package com.conext.conext.ui.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.conext.conext.Adapter.RecyclerAdapterProfileInfo;
import com.conext.conext.R;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.conext.conext.utils.Constants.GET_PROFILE_SKILL_URL;
import static com.conext.conext.utils.Utility.showDialogue;


public class Info extends Fragment {


    private CardView mentorCardView, menteeCardView;
    private TextView otherInterestSkill;
    private RecyclerView recyclerView, menteeRecyclerView, otherInterestRecyclerView;

    public Info() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_profile, container, false);

        addressingView(view);

        displayInfo();

        return view;
    }

    private void addressingView(View view) {
        mentorCardView = (CardView) view.findViewById(R.id.mentorCard);
        menteeCardView = (CardView) view.findViewById(R.id.menteeCard);
        otherInterestSkill = (TextView) view.findViewById(R.id.other_interest_text);

        recyclerView = (RecyclerView) view.findViewById(R.id.list_mentor_recycler);
        menteeRecyclerView = (RecyclerView) view.findViewById(R.id.list_mentee_recycler);
        otherInterestRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_other_info);
    }

    private void displayInfo() {

        Log.e("tag", "loaded  " + GET_PROFILE_SKILL_URL + "/" + Prefs.getUserKey());

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.GET, GET_PROFILE_SKILL_URL + "/" + Prefs.getUserKey(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray response;
                List<String> infoMentorArrayList = new ArrayList<>();
                List<String> infoMenteeArrayList = new ArrayList<>();
                List<String> skillOtherArrayList = new ArrayList<>();
                try {
                    response = new JSONArray(s);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        if (obj.has("other")) {
                            skillOtherArrayList.addAll(Arrays.asList(obj.getString("other").split("\\s*,\\s*")));
                        }
                        if (obj.has("mentee")) {
                            infoMenteeArrayList.addAll(Arrays.asList(obj.getString("mentee").split("\\s*,\\s*")));
                        }
                        if (obj.has("mentor")) {
                            infoMentorArrayList.addAll(Arrays.asList(obj.getString("mentor").split("\\s*,\\s*")));
                        }
                    }
                    settingMentorAdapter(infoMentorArrayList);
                    settingMenteeAdapter(infoMenteeArrayList);
                    settingUserOtherAdapter(skillOtherArrayList);
                } catch (JSONException e) {
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

    private void settingMentorAdapter(List<String> skillArrayList) {

        RecyclerAdapterProfileInfo skillInfoAdapter;
        if (!skillArrayList.isEmpty() && !skillArrayList.contains("")) {
            mentorCardView.setVisibility(View.VISIBLE);
        }
        skillInfoAdapter = new RecyclerAdapterProfileInfo(getActivity(), skillArrayList, R.layout.profile_other_interest);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(skillInfoAdapter);
    }

    private void settingUserOtherAdapter(List<String> skillOtherArrayList) {

        if (!skillOtherArrayList.isEmpty() && !skillOtherArrayList.contains("")) {
            otherInterestSkill.setVisibility(View.VISIBLE);
            otherInterestRecyclerView.setVisibility(View.VISIBLE);
        }
        RecyclerAdapterProfileInfo skillInfoOtherAdapter = new RecyclerAdapterProfileInfo(getActivity(), skillOtherArrayList, R.layout.other_profile_other_interest);

        otherInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        otherInterestRecyclerView.setHasFixedSize(true);
        otherInterestRecyclerView.setAdapter(skillInfoOtherAdapter);

    }

    private void settingMenteeAdapter(List<String> infoMenteeArrayList) {

        if (!infoMenteeArrayList.isEmpty() && !infoMenteeArrayList.contains("")) {
            menteeCardView.setVisibility(View.VISIBLE);
        }

        RecyclerAdapterProfileInfo infoMenteeAdapter = new RecyclerAdapterProfileInfo(getActivity(), infoMenteeArrayList, R.layout.profile_mentee);
        menteeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        menteeRecyclerView.setHasFixedSize(true);
        menteeRecyclerView.setAdapter(infoMenteeAdapter);

    }

}
