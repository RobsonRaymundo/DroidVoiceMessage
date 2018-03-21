package com.droid.ray.droidvoicemessage.tts;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import com.droid.ray.droidvoicemessage.common.DroidCommon;

import java.util.ArrayList;
import java.util.Locale;

import static com.droid.ray.droidvoicemessage.common.DroidCommon.TAG;

/**
 * Created by Robson on 22/09/2017.
 */

public class DroidTTS extends Service implements TextToSpeech.OnInitListener {
    public static TextToSpeech tts;
    private Context context;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind ");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate ");
        InitializeObjects();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart ");
    }

    @Override
    public void onInit(int i) {
        Log.d(TAG, "onInit ");
        LoopingNotification();
    }

    private void LoopingNotification()
    {
        if (!tts.isSpeaking()) {
            ShowNotification();
            ArrayList<String> mensagens = new ArrayList<>();
            mensagens.addAll(DroidCommon.Notification);
            for (String str : mensagens) {
                Speak(str);
                DroidCommon.RemoveNotification(str);
                //DroidCommon.InCall();
                if (DroidCommon.inCall) break;
            }
            if (DroidCommon.AllNotification.size() > 200) {
                DroidCommon.AllNotification.clear();
                if (mensagens != null) {
                    DroidCommon.AllNotification.addAll(mensagens);
                }
            }
            ShowNotification();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.d(TAG, "onDestroy ");
        ResetObject();
        super.onDestroy();
    }


    private void InitializeObjects() {
        try {
            context = getBaseContext();
            tts = new TextToSpeech(context, this);
            tts.setLanguage(Locale.getDefault());
            Log.d(TAG, "InitializeObjects ");
        } catch (Exception ex) {
            Log.d(TAG, "InitializeObjects: " + ex.getMessage());
        }
    }

    private void ResetObject() {
        if (tts != null) {
            try {
                tts.stop();
                tts.shutdown();
            } catch (Exception ex) {
                Log.d(TAG, "ZerarObjeto: " + ex.getMessage());
            }
        }
    }


    private void Speak(final String texto) {
        try {
            //   Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
            Log.d(TAG, "Speak " + texto);

            while (tts.isSpeaking()) {
                Log.d(TAG, "isSpeaking?: " + texto + " " + tts.isSpeaking());
                DroidCommon.TimeSleep(500);
            }
            DroidCommon.TimeSleep(1000);
            Log.d(TAG, "isSpeaking?: " + texto + " " + tts.isSpeaking());


        } catch (Exception ex) {
            Log.d(TAG, "Speak: " + ex.getMessage());
        }
    }

    private static void ShowNotification() {
        Log.d(TAG, "--------------------------------------------");
        for (String str : DroidCommon.Notification) {
            Log.d(TAG, "ShowNotification: " + str);
        }
    }

}
