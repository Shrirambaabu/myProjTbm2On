package com.conext.conext.ui.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conext.conext.Adapter.Pager;
import com.conext.conext.R;

public class EventsFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    int index;


    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null)
            index = bundle.getInt("index", 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_events, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null)
            actionBar.setTitle("Events");

        setupViewPagerEvents(view);

        return view;
    }


    private void setupViewPagerEvents(View view) {

        viewPager = (ViewPager) view.findViewById(R.id.pagerEvents);
        setupViewPager(viewPager);
        //Initializing the tabLayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayoutEvents);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        Pager adapter = new Pager(getChildFragmentManager());

        adapter.addFrag(new UpcomingEventFragment(), "Upcoming Events");

        adapter.addFrag(new MyEventsFragment(), "My Events");

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(index);

    }

}
