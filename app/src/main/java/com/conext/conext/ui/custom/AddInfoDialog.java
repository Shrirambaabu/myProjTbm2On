package com.conext.conext.ui.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.conext.conext.AppManager;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.db.EDUCATION_MASTER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.conext.conext.WebServiceMethods.WebService.getEducation;
import static com.conext.conext.db.DbConstants.EDUCATION_KEY;
import static com.conext.conext.db.DbConstants.SCHOOL_NAME;

public class AddInfoDialog extends Activity implements View.OnClickListener {

    private EditText majorSplAppCompatEditText, eduAppCompatEditText, yearFromAppCompatEditText, yearTillAppCompatEditText;
    private Button addBtn, cancelBtn;
    private AutoCompleteTextView univAppCompatEditText;

    private ArrayList<String> university = new ArrayList<>();
    private ArrayList<EDUCATION_MASTER> education_masterArrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String selectedItemText = null;

    private String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_info_dialog);
        setTheme(R.style.fade_in_fade_out);

        //mapping to view object
        mapping();
        //setting Listeners
        settingListeners();

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

    private void settingListeners() {
        addBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void mapping() {

        majorSplAppCompatEditText = (EditText) findViewById(R.id.major_spl);
        eduAppCompatEditText = (EditText) findViewById(R.id.edu);
        univAppCompatEditText = (AutoCompleteTextView) findViewById(R.id.univ);
        yearFromAppCompatEditText = (EditText) findViewById(R.id.start_year);
        yearTillAppCompatEditText = (EditText) findViewById(R.id.end_year);
        addBtn = (Button) findViewById(R.id.add);
        cancelBtn = (Button) findViewById(R.id.cancel);

        getEducation(AddInfoDialog.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        EDUCATION_MASTER education_master = new EDUCATION_MASTER();
                        JSONObject obj = response.getJSONObject(i);
                        education_master.setEducationKey(obj.getString(EDUCATION_KEY));
                        education_master.setSchoolName(obj.getString(SCHOOL_NAME));
                        education_masterArrayList.add(education_master);
                    }
                    handleAutoComplete(education_masterArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleAutoComplete(ArrayList<EDUCATION_MASTER> education_masterArrayList) {

        if (education_masterArrayList.size() > 0) {
            for (int i = 0; i < education_masterArrayList.size(); i++) {
                EDUCATION_MASTER education_master = education_masterArrayList.get(i);
                university.add(education_master.getSchoolName());
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, university);
        univAppCompatEditText.setAdapter(adapter);
        univAppCompatEditText.setThreshold(1);

        univAppCompatEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = adapter.getItem(position);
                for (int j = 0; j < university.size(); j++) {
                    if (university.get(j).equals(selectedItemText)) {
                        index = String.valueOf(j + 1);
                    }
                }
            }
        });

        univAppCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedItemText = null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            if (!majorSplAppCompatEditText.getText().toString().equals("") && !eduAppCompatEditText.getText().toString().equals("") &&
                    !univAppCompatEditText.getText().toString().equals("") && selectedItemText != null && !yearFromAppCompatEditText.getText().toString().equals("") &&
                    !yearTillAppCompatEditText.getText().toString().equals("") && yearFromAppCompatEditText.getText().length() == 4
                    && yearTillAppCompatEditText.getText().length() == 4) {

                Intent intent = new Intent();
                intent.putExtra("major", majorSplAppCompatEditText.getText().toString());
                intent.putExtra("edu", eduAppCompatEditText.getText().toString());
                intent.putExtra("uni", selectedItemText);
                intent.putExtra("univ", index);
                intent.putExtra("year_start", yearFromAppCompatEditText.getText().toString());
                intent.putExtra("year_end", yearTillAppCompatEditText.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                onBackPressed();
            } else if (!eduAppCompatEditText.getText().toString().equals("")) {
                eduAppCompatEditText.requestFocus();
            } else if (!univAppCompatEditText.getText().toString().equals("")) {
                univAppCompatEditText.requestFocus();
            } else if (selectedItemText == null) {
                univAppCompatEditText.requestFocus();
            } else if (!yearFromAppCompatEditText.getText().toString().equals("") && yearFromAppCompatEditText.getText().length() == 4) {
                yearFromAppCompatEditText.requestFocus();
            } else if (!yearTillAppCompatEditText.getText().toString().equals("") && yearTillAppCompatEditText.getText().length() == 4) {
                yearTillAppCompatEditText.requestFocus();
            }
        } else if (v.getId() == R.id.cancel) {
            finish();
        }
    }
}