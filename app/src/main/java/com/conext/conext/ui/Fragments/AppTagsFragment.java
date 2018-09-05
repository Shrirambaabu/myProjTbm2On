package com.conext.conext.ui.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.AllSkill;
import com.conext.conext.ui.RegistrationPersonaliseActivity;
import com.conext.conext.ui.custom.AddSkillDialog;
import com.conext.conext.ui.custom.EmptyRecyclerView;
import com.conext.conext.utils.IFragmentToActivity;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.conext.conext.WebServiceMethods.WebService.getFacebookSkills;
import static com.conext.conext.db.DbConstants.TAG_NAME;

public class AppTagsFragment extends Fragment {

    private static final int REQUEST_CODE = 111;
    private SkillAdapter dataAdapter = null;
    private TextView emptyTextView;
    private Paint p = new Paint();
    private EmptyRecyclerView recyclerView;
    private ArrayList<AllSkill> skillArrayList = new ArrayList<>();


    private IFragmentToActivity mCallback;

    public AppTagsFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apptags, container, false);

        emptyTextView = (TextView) view.findViewById(R.id.empty);
        recyclerView = (EmptyRecyclerView) view.findViewById(R.id.user_sel_skills_list);

        //Generate list View from ArrayList
        displayListView();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        RegistrationPersonaliseActivity RegistrationPersonaliseActivity = (RegistrationPersonaliseActivity) getActivity();
        RegistrationPersonaliseActivity.floatingActionButtonAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAppTag();
            }
        });
    }

    private void displayListView() {

        //Array list of skillArrayList
        if (Prefs.getAllUserSkill() == null) {
            skillArrayList = new ArrayList<>();
        } else {
            skillArrayList = Prefs.getAllUserSkill();
        }

        //setting recyclerView
        settingRecyclerView();

        initSwipe();
    }

    private void settingRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setting empty view if recycler is empty
        recyclerView.setEmptyView(emptyTextView);

        //create an ArrayAdapter from the String Array
        dataAdapter = new SkillAdapter(getActivity(), R.layout.skill_list_row_layout, skillArrayList);

        //For performance, tell OS RecyclerView won't change size
        recyclerView.setHasFixedSize(true);

        // Assign adapter to recyclerView
        recyclerView.setAdapter(dataAdapter);



        getFacebookSkill();

    }

    private void initSwipe() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    skillArrayList.remove(position);
                    dataAdapter.notifyItemRemoved(position);
                    dataAdapter.notifyDataSetChanged();

                    if (skillArrayList == null) {
                        skillArrayList.clear();

                    }
                    Prefs.setAllUserSkill(skillArrayList);

                    mCallback.passSkill(skillArrayList);
                    mCallback.passSkillMentor(skillArrayList);

                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {

                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.decline);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    private void addAppTag() {

        Intent skillAddIntent = new Intent(getActivity(), AddSkillDialog.class);
        startActivityForResult(skillAddIntent, REQUEST_CODE);
    }


    private void getFacebookSkill() {
        getFacebookSkills(Prefs.getUserKey(), getActivity(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray response;
                try {
                    skillArrayList.clear();
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObjectNames = response.getJSONObject(i);

                        AllSkill allSkill = new AllSkill();

                        allSkill.setIcon(R.drawable.app_logo);
                        allSkill.setTitle(jsonObjectNames.getString(TAG_NAME));
                        allSkill.setMentee(false);
                        allSkill.setMentor(false);

                        skillArrayList.add(allSkill);
                    }
                    dataAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Prefs.setAllUserSkill(skillArrayList);

                mCallback.passSkill(skillArrayList);
                mCallback.passSkillMentor(skillArrayList);
                mCallback.passAllUserSkill(skillArrayList);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String title = data.getStringExtra("title");
                int icon = data.getIntExtra("icon", R.drawable.app_logo);

                int flag = 0;
                for (AllSkill yp : skillArrayList) {
                    if (yp.getTitle().equals(title)) {
                        flag = 1;
                    }
                }
                if (skillArrayList.size() >= 10) {
                    flag = 2;
                }

                if (flag == 1) {
                    Toast.makeText(getActivity(), title + " already exists", Toast.LENGTH_LONG).show();
                } else if (flag == 2) {
                    Toast.makeText(getActivity(), "Only 10 skill allowed", Toast.LENGTH_LONG).show();
                } else {
                    AllSkill skill = new AllSkill(icon, title, false, false);
                    skillArrayList.add(skill);
                    dataAdapter.notifyDataSetChanged();

                    Prefs.setAllUserSkill(skillArrayList);

                    mCallback.passSkill(skillArrayList);
                    mCallback.passSkillMentor(skillArrayList);
                    mCallback.passAllUserSkill(skillArrayList);
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "User Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }


    class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillHolder> {

        private List<AllSkill> skillList;
        private Context context;
        private int itemResource;

        SkillAdapter(Context context, int itemResource, List<AllSkill> skillList) {
            this.skillList = skillList;
            this.context = context;
            this.itemResource = itemResource;
        }

        @Override
        public SkillHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(itemResource, parent, false);
            return new SkillAdapter.SkillHolder(v);
        }

        @Override
        public void onBindViewHolder(SkillHolder holder, int position) {
            AllSkill skill = skillList.get(holder.getAdapterPosition());
            // 4. Bind the data to the ViewHolder
            holder.title.setText(skill.getTitle());
            holder.img.setImageResource(skill.getIcon());
        }

        @Override
        public int getItemCount() {
            return this.skillList.size();
        }

        public class SkillHolder extends RecyclerView.ViewHolder {

            TextView title;
            ImageView img;

            SkillHolder(View itemView) {
                super(itemView);
                // Set up the UI widgets of the holder
                this.title = (TextView) itemView.findViewById(R.id.txt_skill_name);
                this.img = (ImageView) itemView.findViewById(R.id.img);
            }
        }

    }
}
