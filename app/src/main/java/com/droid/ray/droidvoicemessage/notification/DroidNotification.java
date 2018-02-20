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

import static com.droid.ray.droidvoicemessage.common.DroidCommon.TAG;


/**
 * Created by Robson on 03/02/2016.
 */

public class DroidNotification extends DroidBaseNotification {
    private String msg = "";
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Context context = getBaseContext();
        getNotificationKitKat(sbn);


        if (!msg.toString().isEmpty()) {
            Intent intentTTS = new Intent(context, DroidTTS.class);
            try {
                context.stopService(intentTTS);
                DroidCommon.TimeSleep(1000);
                intentTTS.putExtra("msg", msg);
                context.startService(intentTTS);
            } catch (Exception ex) {
                Log.d(TAG, "onNotificationPosted ");
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void getNotificationKitKat(StatusBarNotification mStatusBarNotification) {
        String tit;
        msg = "";
        String pack = mStatusBarNotification.getPackageName();// Package Name
        if (pack.contains("com.whatsapp") ||
                pack.contains("com.android.mms") ||
                pack.contains("com.facebook.orca")) {
            Bundle extras = mStatusBarNotification.getNotification().extras;
            tit = (String) extras.getCharSequence(Notification.EXTRA_TITLE); // Title
            if (!tit.toLowerCase().equals("ícones de bate-papo ativos"))
            {
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

                if (!tit.toLowerCase().equals("whatsapp")) {
                    msg = tit + " " + msg;
                }

                if (msg.toLowerCase().equals("procurando novas mensagens")) {
                    msg = "";
                }

                Log.d(TAG, "tit: " + tit);
                Log.d(TAG, "msg: " + msg);
                Log.d(TAG, "--------------------------------------------");
            }


        }
    }

}
