package com.conext.conext.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.conext.conext.AppManager;
import com.conext.conext.ui.custom.MyDialog;

/**
 * Created by Ashith VL on 9/12/2017.
 */

public class InternetBroadcastReceiver extends BroadcastReceiver {


    private static boolean firstConnect = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppManager.isActivityVisible()) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(cm!=null) {
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if (activeNetwork == null) {
                        if (firstConnect) {
                            Log.e("tag UTIL", "no Internet available");
                            firstConnect = false;
                            Intent i = new Intent();
                            i.setClassName("com.conext.conext", "com.conext.conext.ui.custom.MyDialog");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);

                            //      context.startActivity(new Intent(context, MyDialog.class));
                        } else {
                            firstConnect = false;
                        }
                    } else if(activeNetwork.isConnected()){
                        firstConnect = true;
                    }
                }
            } catch (Exception e) {
                Log.e("tag", "no Internet available " + e);
            }
        }

    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // finish();
            }
        });

        return builder;
    }

}