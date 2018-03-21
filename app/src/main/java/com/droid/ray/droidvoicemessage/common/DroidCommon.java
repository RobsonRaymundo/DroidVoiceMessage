package com.droid.ray.droidvoicemessage.common;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.droid.ray.droidvoicemessage.R;
import com.droid.ray.droidvoicemessage.service.DroidPhoneService;
import com.droid.ray.droidvoicemessage.tts.DroidTTS;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Robson on 04/08/2017.
 */

public class DroidCommon {
    private static Context contextCommon;
    public static final String TAG = "VoiceMessage";
    public static ArrayList<String> Notification = new ArrayList<>();
    public static ArrayList<String> AllNotification = new ArrayList<>();
    public static Boolean InCall = false;
    public static Boolean InThread = false;

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

    public static void ShowLayout(Context context, ViewGroup layout) {
        contextCommon = context;
        ScrollView sv = new ScrollView(context);
        final LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        TextView tvs = new TextView(context);
        tvs.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        tvs.setTextColor(context.getResources().getColor(R.color.colorBlack));
        tvs.setPadding(0,0,0,60);
        tvs.setText("Permita que o Voice Message tenha acesso a notificações");
        ll.addView(tvs);

        Button btn = new Button(context);
        btn.setText("Voice Message");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowListener(contextCommon);
            }
        });

        ll.addView(btn);

        TextView tv = new TextView(context);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        tv.setTextColor(context.getResources().getColor(R.color.colorBlack));
        tv.setPadding(0,100,0,60);
        tv.setText("Selecione abaixo os contatos que serão bloqueados na síntese de voz");
        ll.addView(tv);

        for (Map.Entry<String, ?> key : DroidPreferences.GetAllString(context).entrySet()) {
            CheckBox ch = new CheckBox(context);
            contextCommon = context;
            ch.setText(key.getKey());
            ch.setOnClickListener(getOnClickCheckBox(ch));
            ch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            if (key.getValue().equals("S"))
            {
                ch.setChecked(true);
            }
            ll.addView(ch);
        }

        //this.setContentView(sv);  // this causes the fab to fail
        layout.addView(sv);

    }


    static View.OnClickListener getOnClickCheckBox(final CheckBox checkBox) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valor = "N";

                if (checkBox.isChecked()) {
                    valor = "S";
                }

                DroidPreferences.SetString(contextCommon, checkBox.getText().toString(), valor);

            }
        };
    }
}