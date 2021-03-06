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
import com.droid.ray.droidvoicemessage.common.DroidPreferences;
import com.droid.ray.droidvoicemessage.tts.DroidTTS;

import static com.droid.ray.droidvoicemessage.common.DroidCommon.TAG;

/**
 * Created by Robson on 03/02/2016.
 */


public class DroidNotification extends DroidBaseNotification {
    private String msg;


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            msg = getNotificationKitKat(sbn);
            if (!msg.isEmpty()) {
                Log.d(TAG, "onNotificationPosted: " + msg);
                DroidCommon.AddNotification(msg);
                DroidCommon.AddAllNotification(msg);
                DroidCommon.WaitingEndCallEndService(getBaseContext());
            }
        } catch (Exception ex) {
            Log.d(TAG, "onNotificationPosted: " + ex.getMessage());
        }
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
            tit = (String) extras.getCharSequence(Notification.EXTRA_TITLE).toString().trim().replace(":", ""); // Title

            if (DroidCommon.FilterTitleNotification(tit)) {
                CharSequence desc = extras.getCharSequence(Notification.EXTRA_TEXT); // / Description

                try {
                    Bundle bigExtras = mStatusBarNotification.getNotification().extras;
                    CharSequence[] descArray = bigExtras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
                    notif = descArray[descArray.length - 1].toString();

                } catch (Exception ex) {
                    notif = desc.toString();
                }

                notif = DroidCommon.SetNotification(tit, notif);

            }
        }
        return notif;
    }
}


