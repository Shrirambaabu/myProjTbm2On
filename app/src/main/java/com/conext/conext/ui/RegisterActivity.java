package com.conext.conext.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.conext.conext.AppManager;
import com.conext.conext.R;

public class RegisterActivity extends AppCompatActivity {

    private AppCompatEditText editUsername ,editPassword;
    private Button linkedInSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hides the Action bar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_register);

        addressingView();

        addingListener();
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

    private void addressingView() {

        editUsername=(AppCompatEditText) findViewById(R.id.edt_username);
        editPassword=(AppCompatEditText)findViewById(R.id.edt_password);

        linkedInSign=(Button)findViewById(R.id.btn_linkedin_sign_in);
    }

    private void addingListener() {

        linkedInSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkedIn();
            }
        });

    }

    private void linkedIn() {

        Intent successIntent = new Intent(RegisterActivity.this,SuccessPopUp.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(successIntent);

    }


}
