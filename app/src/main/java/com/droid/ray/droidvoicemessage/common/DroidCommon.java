package com.droid.ray.droidvoicemessage.common;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.droid.ray.droidvoicemessage.service.DroidPhoneService;
import com.droid.ray.droidvoicemessage.tts.DroidTTS;

import java.util.ArrayList;

/**
 * Created by Robson on 04/08/2017.
 */

public class DroidCommon {
    public static final String TAG = "VoiceMessage";
    public static ArrayList<String> Notification = new ArrayList<>();
    public static ArrayList<String> AllNotification = new ArrayList<>();

    public static String SetNotification(String notif) {
        String notifLower = notif.toLowerCase();
        if (DroidCommon.AllNotification.contains(notif) || notifLower.equals("procurando novas mensagens") || notifLower.equals("mensagens está em execução")) {
            notif = "";
        }
        return notif;
    }

    public static boolean FilterTitleNotification(String msg) {
        String titleMsg = msg.toLowerCase();
        return !titleMsg.toLowerCase().equals("ícones de bate-papo ativos") &&
                !titleMsg.toLowerCase().contains("mensagens):") &&
                !titleMsg.toLowerCase().contains("whatsapp web") &&
                !titleMsg.toLowerCase().equals("mensagens está em execução");

    }

    public static void ShowListener(Context context) {
        Intent mIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }

    public static void StartPhoneService(Context context) {
        Intent intent = new Intent(context, DroidPhoneService.class);
        context.startService(intent);
    }

    public static void TimeSleep(Integer seg) {
        try {
            Thread.sleep(seg);
        } catch (Exception ex) {
        }
    }

    public static void AddNotification(String notif) {
        try {
            if (!notif.toString().isEmpty() && !DroidCommon.Notification.contains(notif)) {
                Notification.add(notif);
                Log.d(TAG, "AddNotification: " + notif);
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error AddNotification: " + ex.getMessage());
        }
    }

    public static void AddAllNotification(String notif) {
        try {
            if (!notif.toString().isEmpty() && !DroidCommon.AllNotification.contains(notif)) {
                AllNotification.add(notif);
                Log.d(TAG, "AddAllNotification: " + notif);
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error AddAllNotification: " + ex.getMessage());
        }
    }

    public static void RemoveNotification(String msg) {
        try {
            int pos = Notification.indexOf(msg);
            Notification.remove(pos);
            Log.d(TAG, "RemoveNotification: " + msg);
        } catch (Exception ex) {
            Log.d(TAG, "Error RemoveNotification: " + ex.getMessage());
        }

    }


}
