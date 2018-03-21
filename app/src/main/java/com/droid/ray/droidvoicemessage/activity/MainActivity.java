package com.droid.ray.droidvoicemessage.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.droid.ray.droidvoicemessage.R;
import com.droid.ray.droidvoicemessage.common.DroidCommon;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DroidVoiceMessage";
    private Context context;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private static final int MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getBaseContext();
            setContentView(R.layout.activity_main);
            final ViewGroup layout = (ViewGroup) findViewById(R.id.layout_id);
            DroidCommon.StartPhoneService(context);
            DroidCommon.ShowLayout(context, layout);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission has not been granted, therefore prompt the user to grant permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getApplicationContext().checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission has not been granted, therefore prompt the user to grant permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                            MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS);
                }
            }
            Log.d(TAG, "onCreate ");
        } catch (Exception ex) {
            Log.d(TAG, "onCreate: " + ex.getMessage());
        }
    }
}

