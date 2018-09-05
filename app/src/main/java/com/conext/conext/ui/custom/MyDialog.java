package com.conext.conext.ui.custom;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.conext.conext.R;

import static com.conext.conext.utils.Utility.isNetworkAvailable;

/**
 * Created by Ashith VL on 10/12/2017.
 */

public class MyDialog extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.no_internet);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        this.setFinishOnTouchOutside(false);

        Button button = (Button) findViewById(R.id.okay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable(MyDialog.this)){
                    finish();
                }else {
                    Toast.makeText(MyDialog.this, "No Internet !!! Please make sure you have an active internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //disabling backPress
    }
}
