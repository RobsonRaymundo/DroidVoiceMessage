package com.droid.ray.droidvoicemessage.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.droid.ray.droidvoicemessage.activity.MainActivity;
import com.droid.ray.droidvoicemessage.common.DroidCommon;

/**
 * Created by Robson on 29/01/2018.
 */

public class DroidPhoneService extends Service {
    private static final String TAG = "DroidVoiceMessage";
    private DroidPhoneReceiver phoneReceiver;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        phoneReceiver = new DroidPhoneReceiver();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(phoneReceiver, filter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(phoneReceiver);
        super.onDestroy();
    }

    private class DroidPhoneReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        Log.d(TAG, "Headset is unplugged");

                        break;
                    case 1:
                        Log.d(TAG, "Headset is plugged");
                        DroidCommon.ShowListener(context);
                        break;
                    default:
                        Log.d(TAG, "I have no idea what the headset state is");
                }
            }
        }
    }
}
