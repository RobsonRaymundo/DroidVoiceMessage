package com.droid.ray.droidvoicemessage.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.droid.ray.droidvoicemessage.common.DroidCommon;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DroidVoiceMessage";
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        context = getBaseContext();

        DroidCommon.ShowListener(context);
      //  DroidCommon.StartPhoneService(context);
     //   finish();
    }



}
