package com.conext.conext.ui.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.conext.conext.AppManager;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.Skill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static com.conext.conext.WebServiceMethods.WebService.getTags;
import static com.conext.conext.db.DbConstants.TAG_KEY;
import static com.conext.conext.db.DbConstants.TAG_NAME;

public class AddSkillDialog extends Activity implements View.OnClickListener {

    private AutoCompleteTextView autoCompleteTextView;
    private Button addBtn, cancelBtn;

    private Skill skill = null;
    private ArrayList<Skill> mUserSkillsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_skill_dialog);
        setTheme(R.style.fade_in_fade_out);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.skill_autoCompleteTextView);
        addBtn = (Button) findViewById(R.id.add_skill);
        cancelBtn = (Button) findViewById(R.id.cancel_skill);

        //setting Listeners
        setting();

        getTags(AddSkillDialog.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        Skill skill = new Skill();

                        int[] resImg = {R.drawable.app_logo, R.drawable.ic_call_black_24dp, R.drawable.user_logo};
                        Random rand = new Random();

                        JSONObject obj = response.getJSONObject(i);
                        skill.setIcon(resImg[rand.nextInt(resImg.length)]);
                        skill.setTitle(obj.getString(TAG_NAME));
                        skill.setSkillId(obj.getInt(TAG_KEY));

                        mUserSkillsList.add(skill);

                    }
                    handleAutoComplete(mUserSkillsList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void handleAutoComplete(ArrayList<Skill> mUserSkillsList) {
        SkillAutoCompleteAdapter skillAutoCompleteAdapter = new SkillAutoCompleteAdapter(this, mUserSkillsList);
        autoCompleteTextView.setAdapter(skillAutoCompleteAdapter);

        //setting ItemClickListener
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder holder = (ViewHolder) view.getTag();
                skill = (Skill) holder.ivSkillImage.getTag();
            }
        });

        //setting TextWatcher
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                skill = null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setting() {
        addBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_skill) {

            if (skill != null) {
                Intent intent = new Intent();
                intent.putExtra("title", skill.getTitle());
                intent.putExtra("icon", skill.getIcon());
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                autoCompleteTextView.requestFocus();
            }

        } else if (v.getId() == R.id.cancel_skill) {
            finish();
        }
    }

    private class SkillAutoCompleteAdapter extends BaseAdapter implements Filterable {
        private Context context;
        private ArrayList<Skill> originalList= new ArrayList<>();
        private ArrayList<Skill> tempSkill= new ArrayList<>();
        private ArrayList<Skill> suggestions = new ArrayList<>();
        private Filter filter = new CustomFilter();

        public SkillAutoCompleteAdapter(Context context, ArrayList<Skill> originalList) {
            this.context = context;
            this.originalList = originalList;
            this.tempSkill = originalList;
        }

        @Override
        public int getCount() {
            return suggestions.size(); // Return the size of the suggestions list.
        }

        @Override
        public Object getItem(int position) {
            return suggestions.get(position);
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);

            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.skill_custom_auto_complete_row, parent, false);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Skill tempSkill = suggestions.get(position);

            holder.txtSkill.setText(tempSkill.getTitle());
            holder.ivSkillImage.setImageResource(tempSkill.getIcon());

            // holder.txtSkill.setTag(holder);
            holder.ivSkillImage.setTag(tempSkill);

            return convertView;
        }


        @Override
        public Filter getFilter() {
            return filter;
        }


        private class CustomFilter extends Filter {

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                if (resultValue instanceof Skill) {
                    Skill u = (Skill) resultValue;
                    return u.getTitle();
                } else {
                    return super.convertResultToString(resultValue);
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                suggestions.clear();
                // Check if the Original List and Constraint aren't null.
                if (originalList != null && constraint != null) {
                    for (Skill people : tempSkill) {
                        // Compare item in original list if it contains constraints.

                        if (people.getTitle().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(people); // If TRUE add item in Suggestions.
                        }
                    }
                }
                // Create new Filter Results and return this to publishResults;
                FilterResults results = new FilterResults();
                results.values = suggestions;
                results.count = suggestions.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (ArrayList<Skill>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }

    }

}

class ViewHolder {
    TextView txtSkill;
    ImageView ivSkillImage;

    ViewHolder(View view) {
        txtSkill = (TextView) view.findViewById(R.id.skill_title);
        ivSkillImage = (ImageView) view.findViewById(R.id.skill_image);
    }
}