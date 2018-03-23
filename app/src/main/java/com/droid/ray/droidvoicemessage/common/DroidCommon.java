package com.droid.ray.droidvoicemessage.common;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CompoundButtonCompat;
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
import com.droid.ray.droidvoicemessage.activity.MainActivity;
import com.droid.ray.droidvoicemessage.service.DroidPhoneService;
import com.droid.ray.droidvoicemessage.tts.DroidTTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Robson on 04/08/2017.
 */

public class DroidCommon {
    private static Context contextCommon;
    public static final String TAG = "VoiceMessage";
    public static ArrayList<String> Notification = new ArrayList<>();
    public static ArrayList<String> AllNotification = new ArrayList<>();
    public static Boolean inCall = false;
    public static Boolean InThread = false;
    public static Boolean LoopingNotification = false;

    public static final int PERMISSION_ALL = 2;
    public static final String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS};
    public static boolean forceBreak = false;

    public static void EnviaMsg(Context context) {
        try {
            if (DroidCommon.Notification.size() > 0) {
                Log.d(TAG, "EnviaMsg - startService");
                StopService();
                StartService();
            }
        } catch (Exception ex) {
            Log.d(TAG, "Erro EnviaMsg " + ex.getMessage());
        }
    }

    private static void StopService() {
        try {
            Log.d(TAG, "StopService");
            contextCommon.stopService(new Intent(contextCommon, DroidTTS.class));
            TimeSleep(500);
        } catch (Exception ex) {
            Log.d(TAG, "stopService: " + ex.getMessage());
        }
    }

    public static void StartService() {

        try {
            Log.d(TAG, "StartService");
            contextCommon.startService(new Intent(contextCommon, DroidTTS.class));
            TimeSleep(500);
        } catch (Exception ex) {
            Log.d(TAG, "startService: " + ex.getMessage());
        }
    }

    public static void WaitingEndCallEndService(final Context context) {
        if (DroidCommon.inCall) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (DroidCommon.inCall) {
                            Log.d(TAG, "WaitingEndCall");
                            DroidCommon.TimeSleep(2000);
                        }
                    } catch (Exception ex) {
                        Log.d(TAG, "WaitingEndCall: " + ex.getMessage());
                    }
                }
            }).start();
        } else if (DroidTTS.tts != null && DroidTTS.tts.isSpeaking()) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        DroidCommon.InThread = true;
                        if (DroidTTS.tts != null) {
                            while (DroidTTS.tts.isSpeaking()) {
                                Log.d(TAG, "WaitingEndService");
                                DroidCommon.TimeSleep(500);
                            }
                        }
                      //  StopService();
                    } finally {
                        DroidCommon.InThread = false;
                    }
                }
            }).start();
        } else if (DroidCommon.InThread == false) {
            EnviaMsg(context);
        }
    }

    public static String SetNotification(String tit, String notif) {

        if (!tit.toLowerCase().equals("whatsapp")) {
            notif = tit + " " + notif;
            try {
                String PrefNotif = DroidPreferences.GetString(contextCommon, tit);
                if (PrefNotif.equals("") && notif.trim().isEmpty() == false) {
                    if ((tit.contains("(") || tit.contains("(")) == false) {
                        DroidPreferences.SetString(contextCommon, tit, "N");
                    }
                }
                if (PrefNotif.equals("") || PrefNotif.equals("N")) {
                    notif = "";
                }

            } catch (Exception ex) {
                Log.d(TAG, "DroidPreferences: " + ex.getMessage());
            }
        } else notif = "";

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
        tvs.setTextColor(context.getResources().getColor(R.color.colorWhite));
        tvs.setPadding(0, 0, 0, 60);
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
        tv.setTextColor(context.getResources().getColor(R.color.colorWhite));
        tv.setPadding(0, 100, 0, 60);
        tv.setText("Selecione os contatos para usar a síntese de voz");
        ll.addView(tv);

        Map<String, String> map = new TreeMap<String, String>(DroidPreferences.GetAllString(context));
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            Map.Entry key = (Map.Entry) iterator2.next();
            CheckBox ch = new CheckBox(context);
            if (Build.VERSION.SDK_INT < 21) {
                CompoundButtonCompat.setButtonTintList(ch, ColorStateList.valueOf(Color.WHITE));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
            } else {
                ch.setButtonTintList(ColorStateList.valueOf(Color.WHITE));//setButtonTintList is accessible directly on API>19
            }
            ch.setTextColor(context.getResources().getColor(R.color.colorWhite));
            //ch.setBackgroundColor(context.getResources().getColor(R.color.colorBlack));
            contextCommon = context;
            ch.setText(key.getKey().toString());
            ch.setOnClickListener(getOnClickCheckBox(ch));
            ch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            if (key.getValue().equals("S")) {
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

    public static void getAllContact(Context context) {

        try {
            //This class provides applications access to the content model.
            ContentResolver cr = context.getContentResolver();

//RowContacts for filter Account Types
            Cursor contactCursor = cr.query(
                    ContactsContract.RawContacts.CONTENT_URI,
                    new String[]{ContactsContract.RawContacts._ID,
                            ContactsContract.RawContacts.CONTACT_ID},
                    ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                    new String[]{"com.whatsapp"},
                    null);

//ArrayList for Store Whatsapp Contact
            ArrayList<String> myWhatsappContacts = new ArrayList<>();

            if (contactCursor != null) {
                if (contactCursor.getCount() > 0) {
                    if (contactCursor.moveToFirst()) {
                        do {
                            //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                            String whatsappContactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                            if (whatsappContactId != null) {
                                //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                                Cursor whatsAppContactCursor = cr.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{whatsappContactId}, null);

                                if (whatsAppContactCursor != null) {
                                    whatsAppContactCursor.moveToFirst();
                                    String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                    String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                    String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                    whatsAppContactCursor.close();

                                    //Add Number to ArrayList
                                    myWhatsappContacts.add(number);

                                    if (DroidPreferences.GetString(context, name).equals("")) {
                                        DroidPreferences.SetString(context, name, "N");
                                    }

                                    Log.d(TAG, "WhatsApp contact name " + name);
                                    Log.d(TAG, " WhatsApp contact number :  " + number);
                                }
                            }
                        } while (contactCursor.moveToNext());
                        contactCursor.close();
                    }
                }
            }

            Log.d(TAG, " WhatsApp contact size :  " + myWhatsappContacts.size());

        } catch (Exception ex) {
            Log.d(TAG, " erro getContact:  " + ex.getMessage());
        }
    }

    public static boolean AskPermissionGrand(Activity activity, Context appContext) {
        boolean retorno = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : PERMISSIONS) {
                if (appContext.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
                    retorno = false;
                }
            }
        }
        return retorno;
    }
}