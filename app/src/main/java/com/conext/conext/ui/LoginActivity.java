package com.conext.conext.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.conext.conext.AppManager;
import com.conext.conext.HomeActivity;
import com.conext.conext.R;
import com.conext.conext.WebServiceMethods.VolleyCallback;
import com.conext.conext.model.AllSkill;
import com.conext.conext.model.db.USER_PROFILE;
import com.conext.conext.utils.Prefs;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.conext.conext.WebServiceMethods.WebService.checkRegisteration;
import static com.conext.conext.WebServiceMethods.WebService.checkSchool;
import static com.conext.conext.WebServiceMethods.WebService.checkUser;
import static com.conext.conext.WebServiceMethods.WebService.getAluminiDetails;
import static com.conext.conext.WebServiceMethods.WebService.getUserSkills;
import static com.conext.conext.WebServiceMethods.WebService.loginUsingWs;
import static com.conext.conext.WebServiceMethods.WebService.registerUsingWs;
import static com.conext.conext.utils.Constants.FACEBOOK_LIKES;
import static com.conext.conext.utils.Utility.getDateTime;
import static com.conext.conext.utils.Utility.hideProgressDialog;
import static com.conext.conext.utils.Utility.showDialogue;
import static com.conext.conext.utils.Utility.showProgressDialog;

/**
 * Created by Ashith VL on 6/4/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 123;
    private static final int APP_REQUEST_CODE = 99;
    private AppCompatEditText editName, editPassword;
    private Button login, register;
    private ProgressDialog pDialog;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        pDialog = new ProgressDialog(LoginActivity.this, R.style.MyThemeProgress);

        //Hides the Action bar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        Prefs.setAllUserSkill(new ArrayList<AllSkill>());
        Prefs.setAllUserMenteeSkill(new ArrayList<AllSkill>());
        Prefs.setAllUserMentorSkill(new ArrayList<AllSkill>());
        Prefs.setUserKey("");
        Prefs.setLoc("");

        checkAndRequestPermissions();

        //sqLiteDBHelper.deleteEvents();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(LoginActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(LoginActivity.this, "Permission denied to Camera Access", Toast.LENGTH_SHORT).show();
                }
                if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(LoginActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                if (grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(LoginActivity.this, "Permission denied to read Location Data", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions() {

        int storageReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storageWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int LocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storageReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (storageWritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (LocationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST);
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {

                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {

                            // Get phone number
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            String phoneNumberString = phoneNumber.toString();

                            Intent intent = new Intent(LoginActivity.this, CredentialRegisterActivity.class);
                            intent.putExtra("num", phoneNumberString);
                            startActivity(intent);

                        }

                        @Override
                        public void onError(final AccountKitError error) {
                            // Handle Error
                        }
                    });

                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

            }

            // Surface the result to your user in an appropriate way.
            Log.e("tag", toastMessage);
        }
    }

    private void addressingView() {

        editName = (AppCompatEditText) findViewById(R.id.edt_username);
        editPassword = (AppCompatEditText) findViewById(R.id.edt_password);
        AppCompatImageView img = (AppCompatImageView) findViewById(R.id.img);

        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_linkedin_reg);

        loginButton = (LoginButton) findViewById(R.id.login_button_fb);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_likes",
                "user_education_history,user_work_history"));

    }

    private void addingListener() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            Log.e("User is login", "YES " + AccessToken.getCurrentAccessToken());
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me?fields=email", null,
                    HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(final GraphResponse response) {

                    JSONObject graphResponse = response.getJSONObject();
                    Log.e("tag", "graphResponse ==> " + graphResponse);

                    if (graphResponse.has("email")) {
                        try {
                            Log.e("tag", "email ==> " + graphResponse.getString("email"));
                            checkUser(graphResponse.getString("email"), LoginActivity.this, new VolleyCallback() {
                                @Override
                                public void onSuccess(String userKey) {
                                    if (!userKey.equals("") || !userKey.equals("0")) {
                                        Prefs.setUserKey(userKey);
                                        checkRegisteration(userKey, LoginActivity.this, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(String result) {
                                                if (!result.equals("0")){
                                                    Intent login = new Intent(LoginActivity.this, HomeActivity.class);
                                                    startActivity(login);
                                                }else {
                                                    Intent reg = new Intent(LoginActivity.this, RegistrationPersonaliseActivity.class);
                                                    startActivity(reg);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).executeAsync();

        } else if (AccountKit.getCurrentAccessToken() != null) {

            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(Account account) {
                    checkUser(account.getPhoneNumber().toString(), LoginActivity.this, new VolleyCallback() {
                        @Override
                        public void onSuccess(String userKey) {
                            if (!userKey.equals("") || !userKey.equals("0")) {
                                Prefs.setUserKey(userKey);
                                Intent login = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(login);
                            }
                        }
                    });
                }

                @Override
                public void onError(AccountKitError accountKitError) {

                }
            });

        } else {
            Log.e("User is not login", "OK");
        }

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                showProgressDialog(pDialog);

                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "User Cancelled Facebook Sign In!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("tag", "error ==> " + error.getMessage());
            }
        });

    }

    private void graphRequest(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        JSONObject graphResponse = response.getJSONObject();

                        Log.e("tag", "graphResponse ==> " + graphResponse);

                        if (graphResponse != null)
                            getFacebookData(graphResponse);
                        else {
                            hideProgressDialog(pDialog);
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Oops !!!");
                            builder.setMessage("Facebook Login Failed !!!");
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,first_name,last_name,email,work,education,picture.width(1080).height(720),likes.limit(10000)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getFacebookData(final JSONObject object) {

        try {
            if (object.has("email")) {
                Log.e("tag", "email ==> " + object.getString("email"));
                checkUser(object.getString("email").trim(), LoginActivity.this, new VolleyCallback() {
                    @Override
                    public void onSuccess(final String userKey) {
                        if (userKey.equals("") || userKey.equals("0")) {
                            final USER_PROFILE user = new USER_PROFILE();

                            try {
                                user.setEmailID(object.getString("email").trim());
                                user.setPassword("");
                                user.setDeleteFlg("0");
                                user.setCreatedBy("");
                                user.setCreatedTs(getDateTime());
                                user.setModifiedBy("");
                                user.setModifiedTs(getDateTime());
                                user.setLocationKey("");

                                if (object.has("first_name")) {
                                    Log.e("tag", "first_name ==> " + object.getString("first_name"));
                                    user.setFirstName(object.getString("first_name").trim());
                                } else {
                                    user.setFirstName("");
                                }

                                if (object.has("last_name")) {
                                    Log.e("tag", "last_name ==> " + object.getString("last_name"));
                                    user.setLastName(object.getString("last_name").trim());
                                } else {
                                    user.setLastName("");
                                }

                                if (object.has("name")) {
                                    user.setTitleDisplay(object.getString("name").trim());
                                } else if (object.has("first_name") && object.has("last_name")) {
                                    user.setTitleDisplay(object.getString("first_name").trim() + " " + object.getString("last_name").trim());
                                } else {
                                    user.setTitleDisplay("");
                                }
                                if (object.has("work")) {
                                    JSONArray workJsonArray = object.getJSONArray("work");
                                    for (int n = 0; n < workJsonArray.length(); n++) {
                                        JSONObject workJsonObject = workJsonArray.getJSONObject(n);
                                        if (workJsonObject.has("employer")) {
                                            JSONObject employerJsonObject = workJsonObject.getJSONObject("employer");
                                            Log.e("tag", "employer Company name ==> " + employerJsonObject.getString("name"));
                                            user.setCompanyDisplay(employerJsonObject.getString("name").trim());
                                            if (workJsonObject.has("position")) {
                                                JSONObject positionJsonObject = workJsonObject.getJSONObject("position");
                                                Log.e("tag", "position name ==> " + positionJsonObject.getString("name"));
                                                user.setCompanyDisplay(positionJsonObject.getString("name").trim() + " at "
                                                        + employerJsonObject.getString("name").trim());
                                            }
                                        }
                                    }
                                } else {
                                    user.setCompanyDisplay("");
                                }
                                user.setProfilePic("");

                                registerUsingWs(user, LoginActivity.this, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(final String userKey) {
                                        if (object.has("picture")) {
                                            try {
                                                JSONObject pictureJsonObject = null;
                                                pictureJsonObject = object.getJSONObject("picture");
                                                if (pictureJsonObject.has("data")) {
                                                    JSONObject dataJsonObject = pictureJsonObject.getJSONObject("data");
                                                    Log.e("tag", "url ==> " + dataJsonObject.getString("url").trim());
                                                    Prefs.setUserImage(dataJsonObject.getString("url").trim());
                                                    Prefs.setUserKey(userKey);
                                                }
                                                if (object.has("education")) {
                                                    JSONArray educationJsonArray = object.getJSONArray("education");
                                                    for (int n = 0; n < educationJsonArray.length(); n++) {
                                                        JSONObject educationJsonObject = educationJsonArray.getJSONObject(n);
                                                        if (educationJsonObject.has("school")) {
                                                            JSONObject schoolJsonObject = educationJsonObject.getJSONObject("school");
                                                            Log.e("tag", "school name ==> " + schoolJsonObject.getString("name"));
                                                            String schoolType = null;
                                                            if (educationJsonObject.has("type")) {
                                                                Log.e("tag", "school type  ==> " + educationJsonObject.getString("type"));
                                                                schoolType = educationJsonObject.getString("type");
                                                            }
                                                            checkSchool(schoolJsonObject.getString("name").trim(), schoolType, userKey, LoginActivity.this);
                                                        }
                                                    }
                                                }
                                                if (object.has("likes")) {
                                                    JSONObject likesJsonObject = object.getJSONObject("likes");
                                                    if (likesJsonObject.has("data")) {
                                                        final JSONArray dataJsonArray = likesJsonObject.getJSONArray("data");

                                                        Log.e("tag", "dataJsonArray size ==> " + dataJsonArray.length());

                                                        StringRequest request = new StringRequest(Request.Method.POST, FACEBOOK_LIKES, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {

                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError volleyError) {
                                                                showDialogue(LoginActivity.this, "Sorry! Server Error", "Oops!!!");
                                                            }
                                                        }) {
                                                            //adding parameters to send
                                                            @Override
                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                Map<String, String> parameters = new HashMap<String, String>();
                                                                parameters.put("dataJsonArray", dataJsonArray.toString());
                                                                parameters.put("userKey", userKey);
                                                                return parameters;
                                                            }
                                                        };
                                                        // Adding request to request queue
                                                        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                                                        rQueue.add(request);

                                                        for (int i = 0; i < dataJsonArray.length(); i++) {
                                                            JSONObject likedJsonObject = dataJsonArray.getJSONObject(i);

                                                            Log.e("tag", "liked names ==> " + likedJsonObject.get("name"));

                                                        }
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Intent registerPersonalizeIntent = new Intent(LoginActivity.this, RegistrationPersonaliseActivity.class);
                                        startActivity(registerPersonalizeIntent);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            getAluminiDetails(userKey, LoginActivity.this, new VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    JSONArray response;
                                    try {
                                        response = new JSONArray(result);
                                        if (response.length() >= 1) {

                                            getUserSkills(userKey, LoginActivity.this, new VolleyCallback() {
                                                @Override
                                                public void onSuccess(String result) {
                                                    JSONArray response;
                                                    try {
                                                        response = new JSONArray(result);
                                                        JSONObject jsonObject = response.getJSONObject(0);
                                                        Log.e("tag", "onSuccess: " + jsonObject);
                                                        if (jsonObject.length() >= 2) {
                                                            Prefs.setUserKey(userKey);
                                                            hideProgressDialog(pDialog);
                                                            Intent login = new Intent(LoginActivity.this, HomeActivity.class);
                                                            startActivity(login);
                                                        } else {
                                                            Prefs.setUserKey(userKey);
                                                            hideProgressDialog(pDialog);
                                                            Intent login = new Intent(LoginActivity.this, RegistrationPersonaliseActivity.class);
                                                            startActivity(login);
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });


                                        } else {
                                            Prefs.setUserKey(userKey);
                                            hideProgressDialog(pDialog);
                                            Intent login = new Intent(LoginActivity.this, RegistrationPersonaliseActivity.class);
                                            startActivity(login);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                hideProgressDialog(pDialog);
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Information");
                builder.setMessage("Sorry!!! You don't have an Email attached to your Facebook account! Please confirm with Your Mobile number");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Finishing the dialog and removing Activity from stack.
                        dialogInterface.dismiss();
                        phoneLogin();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } catch (Exception e) {
            hideProgressDialog(pDialog);
            Log.e("tag", "BUNDLE Exception : " + e.toString());
        }
    }

    private void openRegister() {

        Intent register = new Intent(this, CredentialRegisterActivity.class);
        startActivity(register);
        /*
        Intent intent = new Intent(getApplicationContext(), LinkedInRegActivity.class);
        //startActivity(intent);
        startActivity(intent);*/

    }

    private void openLogin() {

        String uName = editName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        showProgressDialog(pDialog);
        loginUsingWs(LoginActivity.this, uName, password, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                handleLogin(Long.parseLong(result));
            }
        });

        hideProgressDialog(pDialog);
    }

    private void handleLogin(long userKey) {

        if (userKey != 0) {
            Prefs.setUserKey(String.valueOf(userKey));
            Intent login = new Intent(this, HomeActivity.class);
            startActivity(login);
        } else {
            //I am showing Alert Dialog Box here for alerting user about wrong credentials
            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Alert");
            builder.setMessage("Please enter valid credentials!!!");
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

    public void phoneLogin() {
        final Intent intent = new Intent(LoginActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE, AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }


}