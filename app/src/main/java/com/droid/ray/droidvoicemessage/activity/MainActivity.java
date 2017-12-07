package com.droid.ray.droidvoicemessage.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        Intent mIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }
}
