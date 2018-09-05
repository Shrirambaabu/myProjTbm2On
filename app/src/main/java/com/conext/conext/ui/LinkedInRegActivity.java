package com.conext.conext.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.conext.conext.AppManager;
import com.conext.conext.MainActivity;
import com.conext.conext.R;
import com.conext.conext.model.AccessToken;
import com.conext.conext.rest.ConextAPIRequest;
import com.conext.conext.ui.custom.CustomWebView;
import com.conext.conext.utils.Constants;
import com.conext.conext.utils.Prefs;
import com.conext.conext.utils.Utility;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by sunil on 24-03-2016.
 */

public class LinkedInRegActivity extends Activity implements Callback<AccessToken> {

    private static final String TAG = LinkedInRegActivity.class.getSimpleName();

    private CustomWebView linkedInWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin_reg);

        linkedInWebView = (CustomWebView) findViewById(R.id.linkedin_webview);
        linkedInWebView.loadUrl(Utility.getAuthorizationUrl());

        linkedInWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //This method will be executed each time a page finished loading.
                //The only we do is dismiss the progressDialog, in case we are showing any.
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                //This method will be called when the Auth proccess redirect to our RedirectUri.
                //We will check the url looking for our RedirectUri.
                if (authorizationUrl.startsWith(Constants.REDIRECT_URI)) {
                    Uri uri = Uri.parse(authorizationUrl);

                    //We take from the url the authorizationToken and the state token.
                    //We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(Constants.STATE_PARAM);

                    if (stateToken == null || !stateToken.equals(Constants.STATE)) {
                        Log.d(TAG, "State token doesn't match");
                        Log.d(TAG, "State token doesn't match" + stateToken);
                        Log.d(TAG, "State token doesn't match" + Constants.STATE);
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(Constants.RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                        Log.d(TAG, "The user doesn't allow authorization.");
                        return true;
                    }

                    Log.d(TAG, "Auth token received ==> " + authorizationToken);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.ACCESS_TOKEN_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    // prepare call in Retrofit 2.0
                    ConextAPIRequest conextAPIRequest = retrofit.create(ConextAPIRequest.class);

                    Call<AccessToken> call = conextAPIRequest.getAccessToken(Constants.GRANT_TYPE, authorizationToken,
                            Constants.REDIRECT_URI, Constants.APP_CLIENT_ID, Constants.APP_CLIENT_SECRET_KEY);
                    //asynchronous call
                    call.enqueue(LinkedInRegActivity.this);
                } else {
                    //Default behaviour
                    Log.d(TAG, "Redirecting to: " + authorizationUrl);
                    linkedInWebView.loadUrl(authorizationUrl);
                }

                return true;
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

    @Override
    public void onResponse(Response<AccessToken> response, Retrofit retrofit) {
        AccessToken accessToken = response.body();
        Log.d(TAG, "Access Token ==> " + accessToken.getAccessToken());
        Log.d(TAG, "Expires In ==> " + accessToken.getExpiresIn());
        Log.d(TAG, "Expires In ==> " + response.body());

        Prefs.setAccessTokenInPrefs(accessToken.getAccessToken());
        Prefs.setExpiresInPrefs(accessToken.getExpiresIn());

        setResult(Constants.LINKEDIN_LOGIN_REQ_CODE);
        Intent intent = new Intent(getApplicationContext(), CredentialRegisterActivity.class);
        intent.putExtra("IS_LINKEDIN_LOGIN", true);
        //Intent intent = new Intent(getApplicationContext(), ConextProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG, "Error in retrieving access token");
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}