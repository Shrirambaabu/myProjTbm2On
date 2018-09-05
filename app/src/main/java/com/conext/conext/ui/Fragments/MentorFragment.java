package com.conext.conext.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.conext.conext.R;
import com.conext.conext.model.AllSkill;
import com.conext.conext.utils.IFragmentToActivity;
import com.conext.conext.utils.Prefs;

import java.util.ArrayList;

public class MentorFragment extends Fragment {

    private SkillListAdapter dataAdapter = null;
    private ArrayList<AllSkill> skillArrayList = new ArrayList<>();
    private ArrayList<AllSkill> selectedMentorSkillList = new ArrayList<>();

    private TextView emptyTextView;
    private ListView listView;
    private View view;

    private IFragmentToActivity mCallback;

    public MentorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (IFragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mentor", selectedMentorSkillList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mentor, container, false);
        //addressing
        addressing();

        setHasOptionsMenu(true);

        //Generate list View from ArrayList
        displayListView();
        //recreating listView
        listView.invalidate();
        //values changes in arrayList is being updated in adapter
        dataAdapter.notifyDataSetChanged();

        return view;
    }

    private void addressing() {

        emptyTextView = (TextView) view.findViewById(R.id.empty);
        listView = (ListView) view.findViewById(R.id.mentor_skill_list_view);

    }

    private void displayListView() {

        skillArrayList = Prefs.getAllUserSkill();

        if (skillArrayList == null || skillArrayList.size() <= 0) {
            skillArrayList = new ArrayList<>();
            listView.setEmptyView(emptyTextView);
            emptyTextView.setVisibility(View.VISIBLE);
        }

        //create an ArrayAdaptar from the String Array
        dataAdapter = new SkillListAdapter(getContext(), R.layout.mentor_skill_row_layout, skillArrayList);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
                AllSkill skill = (AllSkill) parent.getItemAtPosition(position);
                if (!skill.isMentor()) {
                    skill.setMentor(true);
                    checkBox.setChecked(true);
                } else {
                    skill.setMentor(false);
                    checkBox.setChecked(false);
                }

                selectedMentorSkillList = dataAdapter.skillList;

                Prefs.setAllUserMentorSkill(selectedMentorSkillList);
                mCallback.passSkill(selectedMentorSkillList);
            }
        });

        //recreating listView
        listView.invalidate();
        //values changes in arrayList is being updated in adapter
        dataAdapter.notifyDataSetChanged();


    }

    public void fragmentCommunicationMentor(ArrayList<AllSkill> skillarraylist) {

        dataAdapter = null;
        //Array list of skillArrayList
        if (skillarraylist.size() <= 0 || skillArrayList.size() <= 0) {
            listView.setEmptyView(emptyTextView);
            emptyTextView.setVisibility(View.VISIBLE);
        }
        //create an ArrayAdaptar from the String Array
        dataAdapter = new SkillListAdapter(getContext(), R.layout.mentor_skill_row_layout, skillarraylist);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
                AllSkill skill = (AllSkill) parent.getItemAtPosition(position);
                if (!skill.isMentor()) {
                    skill.setMentor(true);
                    checkBox.setChecked(true);
                } else {
                    skill.setMentor(false);
                    checkBox.setChecked(false);
                }

                selectedMentorSkillList = dataAdapter.skillList;

                Prefs.setAllUserMentorSkill(selectedMentorSkillList);
                mCallback.passSkill(selectedMentorSkillList);
                mCallback.passAllUserSkill(selectedMentorSkillList);
            }
        });


        //recreating listView
        listView.invalidate();
        //values changes in arrayList is being updated in adapter
        dataAdapter.notifyDataSetChanged();

    }

    private class SkillListAdapter extends ArrayAdapter<AllSkill> {
        private ArrayList<AllSkill> skillList;

        SkillListAdapter(Context context, int textViewResourceId, ArrayList<AllSkill> skillList) {
            super(context, textViewResourceId, skillList);
            this.skillList = new ArrayList<AllSkill>();
            this.skillList.addAll(skillList);
        }

        private class ViewHolder {
            TextView title;
            CheckBox skillNameCheck;
            RelativeLayout relativeLayout;
        }

        @Override
        public boolean isEnabled(int position) {

            AllSkill skill = skillList.get(position);

            if (skill.isMentee())
                return false;
            return true;

        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.mentor_skill_row_layout, null);

                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.id);
                holder.skillNameCheck = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                AllSkill skill = skillList.get(position);

                boolean checkState = skill.isMentor();
                boolean menCheckState = skill.isMentee();

                if (menCheckState) {
                    holder.skillNameCheck.setEnabled(false);
                    // holder.relativeLayout.setClickable(false);
                    // holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.skillNameCheck.setEnabled(true);
                    // holder.relativeLayout.setClickable(true);
                    // holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

                if (checkState) {
                    holder.skillNameCheck.setChecked(true);
                } else {
                    holder.skillNameCheck.setChecked(false);
                }


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            AllSkill skill = skillList.get(position);
            holder.title.setText(skill.getTitle());
            holder.skillNameCheck.setChecked(skill.isMentor());
            holder.skillNameCheck.setTag(skill);

            return convertView;
        }
    }
}
