package com.droid.ray.droidvoicemessage.common;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.droid.ray.droidvoicemessage.service.DroidPhoneService;

/**
 * Created by Robson on 04/08/2017.
 */

public class DroidCommon {
    public static final String TAG = "VoiceMessage";

    public static void ShowListener(Context context)
    {
        Intent mIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }

    public static void StartPhoneService(Context context)
    {
        Intent intent = new Intent(context, DroidPhoneService.class);
        context.startService(intent);
    }

    public static void TimeSleep(Integer seg) {
        try {
            Thread.sleep(seg);
        } catch (Exception ex) {
        }
    }

}
