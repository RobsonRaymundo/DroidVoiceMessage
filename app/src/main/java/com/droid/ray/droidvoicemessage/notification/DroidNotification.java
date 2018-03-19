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
            AguardandoTerminoServico(intentTTS);
            if (DroidCommon.Mensagens.size() > 0) {
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
        DroidCommon.AdicionaMsg(msg);
        EnviaMsg(getBaseContext());
    }

    private boolean FilterTitleNotification(String msg) {
        String titleMsg = msg.toLowerCase();
        return !titleMsg.toLowerCase().equals("ícones de bate-papo ativos") &&
                !titleMsg.toLowerCase().contains("mensagens):") &&
                !titleMsg.toLowerCase().contains("whatsapp web") &&
                !titleMsg.toLowerCase().equals("mensagens está em execução");

    }

    private String SetNotif(String notif) {
        String notifLower = notif.toLowerCase();
        if (notif.equals(DroidCommon.ultimaFrase) ||  notifLower.equals("procurando novas mensagens") || notifLower.equals("mensagens está em execução")) {
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

            }
        }
        return notif;
    }

    private void AguardandoTerminoServico( final Intent intentTTS) {

        if (DroidTTS.tts != null) {
            new Thread(new Runnable() {
                public void run() {
                    while (DroidTTS.tts.isSpeaking()) {
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
    }


}
