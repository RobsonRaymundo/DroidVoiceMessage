package com.droid.ray.droidvoicemessage.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.droid.ray.droidvoicemessage.common.DroidCommon;
import com.droid.ray.droidvoicemessage.tts.DroidTTS;

import static com.droid.ray.droidvoicemessage.common.DroidCommon.TAG;

/**
 * Created by Robson on 03/02/2016.
 */

public class DroidNotification extends DroidBaseNotification {
    private String msg;

    private void EnviaMsg(Context context) {
        try {
            Intent intentTTS = new Intent(context, DroidTTS.class);
            WaitingEndService(intentTTS);
            if (DroidCommon.Notification.size() > 0) {
                Log.d(TAG, "EnviaMsg - startService");
                context.startService(intentTTS);
            }
        } catch (Exception ex) {
            Log.d(TAG, "Erro EnviaMsg " + ex.getMessage());
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        msg = getNotificationKitKat(sbn);
        Log.d(TAG, "onNotificationPosted: " + msg);
        DroidCommon.AddNotification(msg);
        DroidCommon.AddAllNotification(msg);
        EnviaMsg(getBaseContext());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getNotificationKitKat(StatusBarNotification mStatusBarNotification) {
        String tit;
        String notif = "";
        String pack = mStatusBarNotification.getPackageName();// Package Name
        if (pack.contains("com.whatsapp") ||
                pack.contains("com.android.mms") ||
                pack.contains("com.facebook.orca")) {
            Bundle extras = mStatusBarNotification.getNotification().extras;
            tit = (String) extras.getCharSequence(Notification.EXTRA_TITLE); // Title

            if (DroidCommon.FilterTitleNotification(tit)) {
                CharSequence desc = extras.getCharSequence(Notification.EXTRA_TEXT); // / Description

                try {
                    Bundle bigExtras = mStatusBarNotification.getNotification().extras;
                    CharSequence[] descArray = bigExtras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
                    notif = descArray[descArray.length - 1].toString();

                } catch (Exception ex) {
                    notif = desc.toString();
                }

                if (!tit.toLowerCase().equals("whatsapp")) {
                    notif = tit + " " + notif;
                }

                notif = DroidCommon.SetNotification(notif);

            }
        }
        return notif;
    }

    private void WaitingEndService( final Intent intentTTS) {

        if (DroidTTS.tts != null) {
            new Thread(new Runnable() {
                public void run() {
                    while (DroidTTS.tts.isSpeaking()) {
                        Log.d(TAG, "WaitingEndService");
                        DroidCommon.TimeSleep(500);
                    }
                    try {
                        Log.d(TAG, "StopService");
                        stopService(intentTTS);
                    } catch (Exception ex) {
                        Log.d(TAG, "stopService: " + ex.getMessage());
                    }
                }
            }).start();
        }
    }


}
