package com.conext.conext.ui.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conext.conext.Adapter.RecyclerAdapterProfileAlumni;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.ProfileAlumni;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.conext.conext.AppManager.getAppContext;
import static com.conext.conext.WebServiceMethods.WebService.getAluminiDetails;
import static com.conext.conext.db.DbConstants.DEGREE;
import static com.conext.conext.db.DbConstants.FIELD_OF_STUDY;
import static com.conext.conext.db.DbConstants.SCHOOL_NAME;
import static com.conext.conext.db.DbConstants.YEAR_FINISH;
import static com.conext.conext.db.DbConstants.YEAR_START;


public class Alumni extends Fragment {

    private ArrayList<ProfileAlumni> profileAlumniArrayList = new ArrayList<>();

    private RecyclerView recyclerView;

    public Alumni() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alumini_profile, container, false);

        displayAlumni(view);

        return view;
    }

    private void displayAlumni(final View view) {

        getAluminiDetails(Prefs.getUserKey(), getActivity(), new VolleyCallback() {
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

                        profileAlumniArrayList.add(profileAlumni);
                    }
                    settingAdapter(view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void settingAdapter(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_alumni);
        RecyclerAdapterProfileAlumni profileAlumniAdapter = new RecyclerAdapterProfileAlumni(getActivity(), profileAlumniArrayList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getAppContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(profileAlumniAdapter);
    }
}
