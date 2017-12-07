package com.droid.ray.droidvoicemessage.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.droid.ray.droidvoicemessage.common.DroidCommon;
import com.droid.ray.droidvoicemessage.tts.DroidTTS;


/**
 * Created by Robson on 03/02/2016.
 */

public class DroidNotification extends DroidBaseNotification {
    CharSequence tit;
    String msg;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Context context = getBaseContext();
        getNotificationKitKat(sbn);


        if (!tit.toString().isEmpty())
        {
            Intent intentTTS = new Intent(context, DroidTTS.class);
            try {
                context.stopService(intentTTS);
                DroidCommon.TimeSleep(1000);
                intentTTS.putExtra("tit", "Mensagem de " + tit);
                intentTTS.putExtra("msg", msg);
                context.startService(intentTTS);
            } catch (Exception ex) {
                Log.d("DroidVoiceMessage", "onNotificationPosted ");
            }
        }


    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void getNotificationKitKat(StatusBarNotification mStatusBarNotification) {
        String pack = mStatusBarNotification.getPackageName();// Package Name
        msg = "";
        tit = "";
        if (pack.contains("com.whatsapp")) {
            Bundle extras = mStatusBarNotification.getNotification().extras;
            tit = extras.getCharSequence(Notification.EXTRA_TITLE); // Title
            CharSequence desc = extras.getCharSequence(Notification.EXTRA_TEXT); // / Description
            try {
                Bundle bigExtras = mStatusBarNotification.getNotification().extras;
                CharSequence[] descArray = bigExtras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
                msg = descArray[descArray.length - 1].toString();

            } catch (Exception ex) {

            }
            if (msg.isEmpty()) {
                msg = desc.toString();
            }

        }
    }

}
