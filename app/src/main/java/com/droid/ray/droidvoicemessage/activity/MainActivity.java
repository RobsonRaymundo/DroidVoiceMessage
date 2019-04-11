package com.droid.ray.droidvoicemessage.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.droid.ray.droidvoicemessage.R;
import com.droid.ray.droidvoicemessage.common.DroidCommon;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DroidVoiceMessage";
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getBaseContext();
            DroidCommon.forceBreak = true;
            DroidCommon.forceBreak = false;
            setContentView(R.layout.activity_main);
            //DroidCommon.StartPhoneService(context);

            if (DroidCommon.AskPermissionGrand(this, getApplicationContext())) {
                DroidCommon.getAllContact(context);
                DroidCommon.ShowLayout(context, (ViewGroup) findViewById(R.id.layout_id));
            }
        } catch (Exception ex) {
            Log.d(TAG, "onCreate: " + ex.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Do your work.
                    DroidCommon.getAllContact(context);
                    DroidCommon.ShowLayout(context, (ViewGroup) findViewById(R.id.layout_id));
                }
                return;
            }
        }

    }
}

