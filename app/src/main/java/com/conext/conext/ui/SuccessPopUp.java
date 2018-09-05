package com.conext.conext.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;

import com.conext.conext.AppManager;
import com.conext.conext.R;

/**
 * Created by Ashith VL on 6/4/2017.
 */

public class SuccessPopUp extends Activity {

    // public ImageView nextBtn;
    long key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.success);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Intent intent = getIntent();
        if (intent != null) {
            key = intent.getLongExtra("key", 0);
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent regPerIntent = new Intent(SuccessPopUp.this, RegistrationPersonaliseActivity.class);
                regPerIntent.putExtra("key", key);
                startActivity(regPerIntent);
                SuccessPopUp.this.overridePendingTransition( android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 2000);

        //  nextBtn = (ImageView) findViewById(R.id.nextBtn);
        //  nextBtn.setOnClickListener(this);

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


}
