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
import static com.droid.ray.droidvoicemessage.common.DroidCommon.msgs;


/**
 * Created by Robson on 03/02/2016.
 */

public class DroidNotification extends DroidBaseNotification {
    private String msg;


    private void EnviaMsg(Context context, String msg, String msgs) {
        try {
            Intent intentTTS = new Intent(context, DroidTTS.class);
            DroidCommon.emServico = true;
            AguardandoTerminoServico(context, intentTTS);
            //  DroidCommon.TimeSleep(1000);
            intentTTS.putExtra("msg",  msgs + " " + msg);
            DroidCommon.msgs = "";
            Log.d(TAG, "onNotificationPosted - startService");
            context.startService(intentTTS);
        } catch (Exception ex) {
            Log.d(TAG, "onNotificationPosted " + ex.getMessage());
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        final Context context = getBaseContext();
        Log.d(TAG, "onNotificationPosted inicial");
        msg = getNotificationKitKat(sbn);

        Log.d(TAG, "msg: " + msg);
        Log.d(TAG, "msgs:" + DroidCommon.msgs);
        Log.d(TAG, "--------------------------------------------");

        if (!msg.toString().isEmpty()) {

            if (DroidCommon.emServico) {
                DroidCommon.msgs =  DroidCommon.msgs + " " + msg;
            } else {
                EnviaMsg(context,  msg, DroidCommon.msgs);

            }
        }
    }

    private void AguardandoTerminoServico(final Context context, final Intent intentTTS) {
        Log.d(TAG, "AguardandoTerminoServico");

        new Thread(new Runnable() {
            public void run() {
                while (DroidCommon.emServico) {
                    Log.d(TAG, "AguardandoTerminoServico");
                    DroidCommon.TimeSleep(500);
                }
                try {
                    Log.d(TAG, "ParandoServico");
                    stopService(intentTTS);
                } catch (Exception ex) {
                    Log.d(TAG, "stopService: " + ex.getMessage());
                }
            }
        }).start();
    }

    private boolean FilterTitleNotification(String msg) {
        String titleMsg = msg.toLowerCase();
        return !titleMsg.toLowerCase().equals("ícones de bate-papo ativos") &&
                !titleMsg.toLowerCase().contains("mensagens):") &&
                !titleMsg.toLowerCase().equals("mensagens está em execução");

    }

    private String SetNotif(String notif) {
        String notifMsg = notif.toLowerCase();
        if (notifMsg.equals("procurando novas mensagens") || notifMsg.equals("mensagens está em execução")) {
            notif = "";
        }

        return notif;
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

            if (FilterTitleNotification(tit)) {
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

                notif = SetNotif(notif);

                Log.d(TAG, "tit: " + tit);
                Log.d(TAG, notif + notif);
                Log.d(TAG, "--------------------------------------------");
            }
        }
        return notif;
    }

}
