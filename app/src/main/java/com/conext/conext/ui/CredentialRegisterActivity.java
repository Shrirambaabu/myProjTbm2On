package com.conext.conext.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.conext.conext.AppManager;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.db.USER_PROFILE;
import com.conext.conext.utils.Prefs;

import static com.conext.conext.WebServiceMethods.WebService.checkUser;
import static com.conext.conext.WebServiceMethods.WebService.registerUsingWs;
import static com.conext.conext.utils.Utility.getDateTime;
import static com.conext.conext.utils.Utility.hideProgressDialog;
import static com.conext.conext.utils.Utility.showProgressDialog;

public class CredentialRegisterActivity extends AppCompatActivity {

    private AppCompatEditText edit_username, edit_password, edit_conform_password, edit_first, edit_last, edit_company;
    private Button btn_login_sign_in;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hides the Action bar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_credential_register);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getInt("num") != 0)
                edit_username.setText(bundle.getInt("num"));
            else if (bundle.getString("num")!=null)
                edit_username.setText(bundle.getString("num"));
        }

        pDialog = new ProgressDialog(CredentialRegisterActivity.this, R.style.MyThemeProgress);

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

    private void addingListener() {

        btn_login_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        edit_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                checkUser(s.toString(), CredentialRegisterActivity.this, new VolleyCallback() {
                    @Override
                    public void onSuccess(String userKey) {
                        if (!userKey.equals("") || !userKey.equals("0"))
                            userExits();
                    }
                });
            }
        });
    }

    private void userExits() {

        //Alert dialog after clicking the Register Account
        final AlertDialog.Builder builder = new AlertDialog.Builder(CredentialRegisterActivity.this);
        builder.setTitle("Information");
        builder.setMessage("Email Id Already Exits");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Finishing the dialog and removing Activity from stack.
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void login() {

        checkUser(edit_username.getText().toString(), CredentialRegisterActivity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("") || result.equals("0")) {
                    if ((edit_first.getText().length() < 2) && (edit_last.getText().length() < 2)) {
                        Toast.makeText(getApplicationContext(), "First Name and Last Name Should be more than 2 Characters", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (edit_first.getText().length() < 2) {
                        Toast.makeText(getApplicationContext(), "First Name Should be more than 2 Characters", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (edit_last.getText().length() < 2) {
                        Toast.makeText(getApplicationContext(), "Last Name Should be more than 2 Characters", Toast.LENGTH_LONG).show();
                        return;
                    }

                    credentialRegister();
                } else {
                    userExits();
                }
            }
        });

    }

    private void credentialRegister() {

        if (edit_password.getText().toString().equals(edit_conform_password.getText().toString())
                && !edit_password.getText().toString().trim().isEmpty() &&
                !edit_conform_password.getText().toString().trim().isEmpty()) {

            USER_PROFILE user = new USER_PROFILE();

            user.setEmailID(edit_username.getText().toString());
            user.setPassword(edit_conform_password.getText().toString());
            user.setFirstName(edit_first.getText().toString());
            user.setLastName(edit_last.getText().toString());
            user.setTitleDisplay(edit_first.getText().toString() + " " + edit_last.getText().toString());
            user.setCompanyDisplay(edit_company.getText().toString());
            user.setProfilePic("");
            user.setDeleteFlg("0");
            user.setCreatedBy("");
            user.setCreatedTs(getDateTime());
            user.setModifiedBy("");
            user.setModifiedTs(getDateTime());
            user.setLocationKey("");

            showProgressDialog(pDialog);
            registerUsingWs(user, CredentialRegisterActivity.this, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    handleRegister(Long.parseLong(result));
                }
            });
        } else {

            //I am showing Alert Dialog Box here for alerting user about wrong credentials
            final AlertDialog.Builder builder = new AlertDialog.Builder(CredentialRegisterActivity.this);
            builder.setTitle("Alert");
            builder.setMessage("Password and Confirm Password doesn't match.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void handleRegister(final long uId) {

        if (uId != 0) {
            final Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    hideProgressDialog(pDialog);
                    Intent intent = new Intent(CredentialRegisterActivity.this, SuccessPopUp.class);
                    intent.putExtra("key", uId);
                    Prefs.setUserKey(String.valueOf(uId));
                    startActivity(intent);
                    overridePendingTransition(0, R.anim.success_pop);
                }
            };
            handler.postDelayed(r, 500);
        } else {
            hideProgressDialog(pDialog);
            //I am showing Alert Dialog Box here for alerting user about wrong credentials
            final AlertDialog.Builder builder = new AlertDialog.Builder(CredentialRegisterActivity.this);
            builder.setTitle("Alert");
            builder.setMessage("Something Went Wrong!!!.Please Try Again Later");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void addressingView() {

        edit_username = (AppCompatEditText) findViewById(R.id.edt_username);
        edit_password = (AppCompatEditText) findViewById(R.id.edt_password);
        edit_conform_password = (AppCompatEditText) findViewById(R.id.edt_conform_password);
        edit_first = (AppCompatEditText) findViewById(R.id.edt_first);
        edit_last = (AppCompatEditText) findViewById(R.id.edt_last_name);
        edit_company = (AppCompatEditText) findViewById(R.id.edt_Company_name);

        btn_login_sign_in = (Button) findViewById(R.id.btn_login_sign_in);

    }
}
