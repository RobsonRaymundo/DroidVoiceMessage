package com.droid.ray.droidvoicemessage.common;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
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
import java.util.Map;

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

    public static void EnviaMsg(Context context) {
        if (DroidCommon.InThread == false) {
            try {
                Intent intentTTS = new Intent(context, DroidTTS.class);
                WaitingEndService(intentTTS, context);
                if (DroidCommon.Notification.size() > 0) {
                    Log.d(TAG, "EnviaMsg - startService");
                    context.startService(intentTTS);
                }
            } catch (Exception ex) {
                Log.d(TAG, "Erro EnviaMsg " + ex.getMessage());
            }
        }
    }

    public static void WaitingEndService(final Intent intentTTS, final Context context) {

        if (DroidTTS.tts != null) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        DroidCommon.InThread = true;
                        while (DroidTTS.tts.isSpeaking()) {
                            Log.d(TAG, "WaitingEndService");
                            DroidCommon.TimeSleep(500);
                        }
                        try {
                            Log.d(TAG, "StopService");
                            context.stopService(intentTTS);
                        } catch (Exception ex) {
                            Log.d(TAG, "stopService: " + ex.getMessage());
                        }
                    } finally {
                        DroidCommon.InThread = false;
                    }
                }
            }).start();
        }
    }


    public static void InCall() {
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
        }
    }

    public static void WaitingEndCall(Context context) {
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
                    //EnviaMsg(getBaseContext());
                }
            }).start();
        } else EnviaMsg(context);
    }


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
        tv.setTextColor(context.getResources().getColor(R.color.colorBlack));
        tv.setPadding(0, 100, 0, 60);
        tv.setText("Selecione os contatos para usar a síntese de voz");
        ll.addView(tv);

        //for (Map.Entry<String, ?> key : DroidPreferences.GetAllString(context).entrySet()) {
        for (Map.Entry<String, ?> key : DroidPreferences.GetAllString(context).entrySet()) {
            CheckBox ch = new CheckBox(context);
            contextCommon = context;
            ch.setText(key.getKey());
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