package com.conext.conext.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.conext.conext.Adapter.SkillAdapter;
import com.conext.conext.AppManager;
import com.conext.conext.R;
import com.conext.conext.model.Contact;
import com.conext.conext.model.Skill;
import com.conext.conext.ui.custom.CustomFilter;
import com.conext.conext.ui.custom.HexagonMaskView;
import com.conext.conext.utils.AlphabetItem;
import com.conext.conext.utils.ItemClickListener;
import com.conext.conext.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

import static com.conext.conext.db.DbConstants.COMPANY_DISPLAY;
import static com.conext.conext.db.DbConstants.PROFILE_PIC;
import static com.conext.conext.db.DbConstants.SCHOOL_NAME;
import static com.conext.conext.db.DbConstants.TITLE_DISPLAY;
import static com.conext.conext.db.DbConstants.USER_KEY;
import static com.conext.conext.utils.Constants.BaseIP;
import static com.conext.conext.utils.Constants.GET_CONTACTS_EVENT_LIST_URL;
import static com.conext.conext.utils.Utility.menuIconColor;
import static com.conext.conext.utils.Utility.showDialogue;

public class ContactsActivity extends AppCompatActivity {

    private IndexFastScrollRecyclerView mRecyclerView, contactsRecyclerViewT;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView searchView;

    private ContactAdapter contactAdapter = null;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private ArrayList<Contact> multiSelect_list = new ArrayList<>();
    private ArrayList<Long> skillFilter = new ArrayList<>();
    private ArrayList<Contact> multiContactList = new ArrayList<>();

    private Toolbar toolbar;
    private ActionMode mActionMode;
    private int totalItemCount;
    private boolean loading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contactsRecyclerViewT = (IndexFastScrollRecyclerView) findViewById(R.id.recyclerViewContact);
        RecyclerView skillRecyclerView = (RecyclerView) findViewById(R.id.skill_container_scroll);
        mRecyclerView = (IndexFastScrollRecyclerView) findViewById(R.id.fast_scroller);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("All Contacts");
        }

        ImageView skillDots = (ImageView) findViewById(R.id.skill_dots);
        Drawable skillDotsDrawable = skillDots.getDrawable();
        menuIconColor(skillDotsDrawable, R.color.colorPrimary, ContactsActivity.this);

        searchViewImplementation();

        ArrayList<Skill> skillArrayList = new ArrayList<>();

        if (Prefs.getUserSkill() != null)
            skillArrayList = Prefs.getUserSkill();

        for (int i = 0; i < skillArrayList.size(); i++) {
            skillFilter.add(skillArrayList.get(i).getSkillId());
        }

        RecyclerView.LayoutManager mSkillLayoutManager = new LinearLayoutManager(ContactsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        skillRecyclerView.setLayoutManager(mSkillLayoutManager);
        SkillAdapter skillAdapter = new SkillAdapter(ContactsActivity.this, R.layout.card_skill, skillArrayList);
        skillRecyclerView.setHasFixedSize(true);
        skillRecyclerView.setAdapter(skillAdapter);

        settingContactAdapter();

        skillAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int id) {

                ArrayList<Contact> selectedContactList = contactAdapter.selected_usersList;

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

                contactArrayList.clear();
                loadData(selectedContactList);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppManager.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppManager.activityPaused();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void multi_select(ArrayList<Contact> multiselect_list) {
//
//        if (mActionMode == null)
//            mActionMode = startActionMode(mActionModeCallback);
//
//        if (multiselect_list.size() > 0) {
//            mActionMode.setTitle("" + multiselect_list.size() + " Selected");
//        } else {
//            mActionMode.setTitle("All Contacts");
//        }
//        refreshAdapter();
//    }


    public void refreshAdapter() {
        contactAdapter.selected_usersList = multiSelect_list;
        contactAdapter.contactList = contactArrayList;
        contactAdapter.notifyDataSetChanged();
    }

    private void settingContactAdapter() {

        mLayoutManager = new LinearLayoutManager(ContactsActivity.this);
        contactsRecyclerViewT.setLayoutManager(mLayoutManager);

        ArrayList<Contact> bundleContactArrayList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundleContactArrayList = (ArrayList<Contact>) getIntent().getSerializableExtra("contactsList");
        }
        if (!bundleContactArrayList.isEmpty()) {
            multiSelect_list.addAll(bundleContactArrayList);

            for (Contact u1 : multiSelect_list) {
                for (Contact u2 : contactArrayList) {
                    if (u1.getId().equals(u2.getId())) {
                        contactArrayList.remove(u2);
                        contactArrayList.add(u1);
                        break;
                    }
                }
            }
        }
        loadData(multiSelect_list);

        //create an ArrayAdapter from the String Array
        contactAdapter = new ContactAdapter(ContactsActivity.this, R.layout.card_contact, contactArrayList, multiSelect_list);

        sortingContacts(contactArrayList);

        //For performance, tell OS RecyclerView won't change size
        contactsRecyclerViewT.setHasFixedSize(true);
        contactsRecyclerViewT.setItemViewCacheSize(20);
        contactsRecyclerViewT.setDrawingCacheEnabled(true);
        contactsRecyclerViewT.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        contactsRecyclerViewT.addItemDecoration(new DividerItemDecoration(ContactsActivity.this, DividerItemDecoration.VERTICAL));

        // Assign adapter to recyclerView
        contactsRecyclerViewT.setAdapter(contactAdapter);

        //SEARCH
        queryTextListener();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));
        contactsRecyclerViewT.setIndexTextSize(12);
        contactsRecyclerViewT.setIndexBarColor("#33334c");
        contactsRecyclerViewT.setIndexBarCornerRadius(0);
        contactsRecyclerViewT.setIndexBarTransparentValue((float) 0.4);
        contactsRecyclerViewT.setIndexbarMargin(0);
        contactsRecyclerViewT.setIndexbarWidth(40);
        contactsRecyclerViewT.setPreviewPadding(0);
        contactsRecyclerViewT.setIndexBarTextColor("#FFFFFF");
        contactsRecyclerViewT.setIndexBarVisibility(true);

        ArrayList<AlphabetItem> mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < contactArrayList.size(); i++) {
            Contact contact = contactArrayList.get(i);
            String name = contact.getName();
            if (name == null || name.trim().isEmpty())
                continue;
            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }

        contactAdapter.setCallback(new InterfaceCallback() {
            @Override
            public void multiSelect(ArrayList<Contact> multiselect_list) {

                if (mActionMode == null)
                    mActionMode = startActionMode(mActionModeCallback);

                if (multiselect_list.size() > 0) {
                    mActionMode.setTitle("" + multiselect_list.size() + " Selected");
                } else {
                    mActionMode.setTitle("All Contacts");
                }
                refreshAdapter();
            }
        });


        contactsRecyclerViewT.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    ArrayList<Contact> selectedContactList = contactAdapter.selected_usersList;
                    totalItemCount = mLayoutManager.getItemCount();
                    if (!loading) {
                        loadMoreData(totalItemCount + 1, selectedContactList);
                        loading = true;
                    }
                }
            }
        });

    }

    private void sortingContacts(ArrayList<Contact> contactList) {
        ArrayList<Contact> contactArrayList = contactList;
        if (!contactArrayList.isEmpty()) {
            for (int i = 0; i < contactArrayList.size(); i++) {
                Contact contact = contactArrayList.get(i);
                if (contact.getId().equals(Prefs.getUserKey())) {
                    contactArrayList.remove(i);
                }
            }

            Collections.sort(contactArrayList, new Comparator<Contact>() {
                @Override
                public int compare(Contact lhs, Contact rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        }
        contactAdapter.notifyDataSetChanged();
    }


    private void loadData(ArrayList<Contact> selectedContactList) {
        int loadLimit = 10;
        getContactList(0, loadLimit, selectedContactList);
    }

    private void loadMoreData(int totalItemCount, ArrayList<Contact> selectedContactList) {
        getContactList(totalItemCount, totalItemCount + 10, selectedContactList);
        contactAdapter.notifyDataSetChanged();
    }

    private void getContactList(int start, int size, final ArrayList<Contact> selectedContactList) {
        if (!skillFilter.isEmpty()) {

            Log.e("error", "loaded " + GET_CONTACTS_EVENT_LIST_URL + "/" + android.text.TextUtils.join(",", skillFilter) + "?start=" + start + "&size=" + size);

            StringRequest request = new StringRequest(Request.Method.GET, GET_CONTACTS_EVENT_LIST_URL + "/" +
                    android.text.TextUtils.join(",", skillFilter) + "?start=" + start + "&size=" + size, new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    JSONArray response;
                    try {
                        response = new JSONArray(result);
                        for (int i = 0; i < response.length(); i++) {
                            Contact userKey = new Contact();
                            JSONObject obj = response.getJSONObject(i);

                            userKey.setId(obj.getString(USER_KEY));
                            String name = obj.getString(TITLE_DISPLAY);
                            userKey.setName(name.substring(0, 1).toUpperCase() + name.substring(1));
                            userKey.setJob(obj.getString(COMPANY_DISPLAY));
                            userKey.setColg(obj.getString(SCHOOL_NAME));
                            userKey.setImage(obj.getString(PROFILE_PIC));

                            contactArrayList.add(userKey);
                        }

                        for (Contact u1 : selectedContactList) {
                            boolean unique = true;
                            for (Contact u2 : multiContactList) {
                                if (u1.getId().equals(u2.getId())) {
                                    unique = false;
                                    break;
                                }
                            }
                            if (unique) {
                                multiContactList.add(u1);
                            }
                        }

                        for (Contact u1 : multiContactList) {
                            for (Contact u2 : contactArrayList) {
                                if (u1.getId().equals(u2.getId())) {
                                    contactArrayList.remove(u2);
                                    contactArrayList.add(u1);
                                    break;
                                }
                            }
                        }

                        sortingContacts(contactArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("tag", e.getMessage());
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
                    showDialogue(ContactsActivity.this, "Sorry! Server Error", "Oops!!!");
                }
            });

            // Adding request to request queue
            RequestQueue rQueue = Volley.newRequestQueue(ContactsActivity.this);
            rQueue.add(request);
        }
    }

    private void queryTextListener() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                contactAdapter.getFilter().filter(query);
                return false;
            }
        });

    }

    private void searchViewImplementation() {

        searchView = (SearchView) findViewById(R.id.mSearch);

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ImageView searchButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        ImageView searchMagButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ImageView searchButtonClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        searchEditText.setTextColor(ContextCompat.getColor(ContactsActivity.this, R.color.white));
        searchEditText.setHintTextColor(ContextCompat.getColor(ContactsActivity.this, R.color.white));
        searchButton.setColorFilter(ContextCompat.getColor(ContactsActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        searchMagButton.setColorFilter(ContextCompat.getColor(ContactsActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        searchButtonClose.setColorFilter(ContextCompat.getColor(ContactsActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        searchView.onActionViewExpanded();
        searchView.setIconified(true);
        searchView.setIconifiedByDefault(false);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            toolbar.setVisibility(View.GONE);
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contact, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.done:
                    if (multiSelect_list != null) {
                        int param = getIntent().getIntExtra("selectedPosition", -1);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedPosition", param);
                        resultIntent.putExtra("contacts", multiSelect_list);
                        setResult(Activity.RESULT_OK, resultIntent);
                        onBackPressed();
                    }
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            multiSelect_list = new ArrayList<>();
            refreshAdapter();
            onBackPressed();
        }
    };

    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> implements SectionIndexer, Filterable {

        public List<Contact> contactList;
        private List<Contact> filteredUsersList;
        private CustomFilter filter;
        private Context mContext;
        private int itemResource;
        private ArrayList<Contact> selected_usersList = new ArrayList<>();

        private InterfaceCallback callback;
        private ArrayList<Integer> mSectionPositions = new ArrayList<>();

        ContactAdapter(Context mContext, int itemResource, List<Contact> contactList, ArrayList<Contact> selectedList) {
            this.contactList = contactList;
            this.mContext = mContext;
            this.itemResource = itemResource;
            this.selected_usersList = selectedList;
            this.filteredUsersList = contactList;
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(itemResource, parent, false);
            return new ContactHolder(v);
        }

        @Override
        public void onBindViewHolder(final ContactHolder holder, int position) {
            final Contact contact = contactList.get(position);

            holder.college.setText(contact.getColg());
            holder.name.setText(contact.getName());
            holder.job.setText(contact.getJob());
            holder.img.setBorderColor(R.color.bg_card);
            if (contact.getImage() != null) {
                Glide.with(mContext)
                        .load(BaseIP + "/" + contact.getImage())
                        .into(holder.img);
                holder.img.setVisibility(View.VISIBLE);
            }

            if (selected_usersList.contains(contactList.get(position))) {
                holder.rr_layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bg_card_selected));
                holder.selected.setVisibility(View.VISIBLE);

                holder.mentee.setVisibility(View.VISIBLE);
                holder.mentor.setVisibility(View.VISIBLE);
                holder.participant.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(contact.getStatus()) || "2".equalsIgnoreCase(contact.getStatus())) {
                    holder.mentor.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, R.color.colorPrimary));
                    holder.mentee.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, R.color.colorPrimary));
                    holder.participant.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, R.color.colorAccent));
                } else if ("0".equalsIgnoreCase(contact.getStatus())) {
                    holder.mentor.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, R.color.colorPrimary));
                    holder.participant.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, R.color.colorPrimary));
                    holder.mentee.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, R.color.colorAccent));
                } else if ("1".equalsIgnoreCase(contact.getStatus())) {
                    holder.mentor.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, R.color.colorAccent));
                    holder.participant.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, R.color.colorPrimary));
                    holder.mentee.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, R.color.colorPrimary));
                }

                holder.mentor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected_usersList.remove(contact);
                        contact.setStatus("1");
                        selected_usersList.add(contact);
                        notifyDataSetChanged();
                        callback.multiSelect(selected_usersList);
                    }
                });


                holder.mentee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected_usersList.remove(contact);
                        contact.setStatus("0");
                        selected_usersList.add(contact);
                        notifyDataSetChanged();
                        callback.multiSelect(selected_usersList);
                    }
                });


                holder.participant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected_usersList.remove(contact);
                        contact.setStatus("2");
                        selected_usersList.add(contact);
                        notifyDataSetChanged();
                        callback.multiSelect(selected_usersList);
                    }
                });

            } else {
                holder.rr_layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cement));
                holder.selected.setVisibility(View.GONE);
                holder.mentee.setVisibility(View.GONE);
                holder.mentor.setVisibility(View.GONE);
                holder.participant.setVisibility(View.GONE);
                holder.mentor.setOnClickListener(null);
                holder.mentee.setOnClickListener(null);
                holder.participant.setOnClickListener(null);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // default participant
                    contact.setStatus("2");
                    addOrRemoveInMultiSelect(contact);
                    notifyDataSetChanged();
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(ContactsActivity.this, OtherProfileActivity.class);
                    intent.putExtra("id", Integer.parseInt(contact.getId()));
                    startActivity(intent);
                    return true;
                }
            });
        }

        private void addOrRemoveInMultiSelect(Contact contact) {
            if (selected_usersList.contains(contact))
                selected_usersList.remove(contact);
            else
                selected_usersList.add(contact);

            // multi_select(selected_usersList);
            callback.multiSelect(selected_usersList);
        }

        @Override
        public int getItemCount() {
            return this.contactList.size();
        }

        @Override
        public Object[] getSections() {
            List<String> sections = new ArrayList<>(26);
            mSectionPositions = new ArrayList<>(26);
            for (int i = 0, size = contactList.size(); i < size; i++) {
                Contact contact = contactList.get(i);
                String section = String.valueOf(contact.getName().charAt(0)).toUpperCase();
                if (!sections.contains(section)) {
                    sections.add(section);
                    mSectionPositions.add(i);
                }
            }
            return sections.toArray(new String[0]);
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            return mSectionPositions.get(sectionIndex);
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new CustomFilter(filteredUsersList, this);
            }
            return filter;
        }

        void setCallback(InterfaceCallback callback) {
            this.callback = callback;
        }

        class ContactHolder extends RecyclerView.ViewHolder {

            private TextView name, college, job, mentee, mentor, participant;
            private ImageView selected;
            private HexagonMaskView img;
            private RelativeLayout rr_layout;

            ContactHolder(View itemView) {
                super(itemView);

                img = (HexagonMaskView) itemView.findViewById(R.id.contact_image);
                name = (TextView) itemView.findViewById(R.id.contact_name);
                college = (TextView) itemView.findViewById(R.id.contact_colg);
                job = (TextView) itemView.findViewById(R.id.contact_job);
                mentee = (TextView) itemView.findViewById(R.id.mentee);
                mentor = (TextView) itemView.findViewById(R.id.mentor);
                participant = (TextView) itemView.findViewById(R.id.participant);
                rr_layout = (RelativeLayout) itemView.findViewById(R.id.rr_layout);
                selected = (ImageView) itemView.findViewById(R.id.tic_contact_selected);

            }
        }
    }

    interface InterfaceCallback {

        void multiSelect(ArrayList<Contact> position);
    }

}