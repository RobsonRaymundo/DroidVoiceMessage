package com.droid.ray.droidvoicemessage.tts;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.droid.ray.droidvoicemessage.common.DroidCommon;

import java.util.Locale;

import static com.droid.ray.droidvoicemessage.common.DroidCommon.TAG;

/**
 * Created by Robson on 22/09/2017.
 */

public class DroidTTS extends Service implements TextToSpeech.OnInitListener {
    public static TextToSpeech tts;
    private Context context;
    private String msg;


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
        InicializarObjetos();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart ");
        ObtemMsg(intent);
    }

    @Override
    public void onInit(int i) {
        Log.d(TAG, "onInit ");
        if (!msg.isEmpty()) {
            Fala(msg);
            AguardandoFalar(msg);
        }


    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.d(TAG, "onDestroy ");
        ZerarObjeto();
        super.onDestroy();
    }

    private void ObtemMsg(Intent intent) {
        try {
            msg = intent.getStringExtra("msg");
            Log.d(TAG, "ObtemMsg " + msg);
        } catch (Exception ex) {
            Log.d(TAG, "ObtemMsg: " + ex.getMessage());
        }
    }


    private void InicializarObjetos() {
        try {
            context = getBaseContext();
            tts = new TextToSpeech(context, this);
            tts.setLanguage(Locale.getDefault());
            Log.d(TAG, "InicializarObjetos ");
        } catch (Exception ex) {
            Log.d(TAG, "InicializarObjetos: " + ex.getMessage());
        }
    }

    private void ZerarObjeto() {
        if (tts != null) {
            try {
                tts.stop();
                tts.shutdown();
            } catch (Exception ex) {
                Log.d(TAG, "ZerarObjeto: " + ex.getMessage());
            }
        }
    }


    private void Fala(String texto) {
        try {
            Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
            Log.d(TAG, "Fala");
        } catch (Exception ex) {
            Log.d(TAG, "Fala: " + ex.getMessage());
        }
    }

    private void AguardandoFalar(String texto) {
        new Thread(new Runnable() {
            public void run() {

                try {

                    while (tts.isSpeaking()) {
                        Log.d(TAG, "AguardandoFalar: " + msg);
                        DroidCommon.TimeSleep(500);
                    }
                }
                finally {
                    PararServico();
                }

            }
        }).start();
    }

    private void PararServico() {
        try {
            Log.d(TAG, "PararServico");
            //   stopSelf();
         //   DroidCommon.emServico = false;
            DroidCommon.TimeSleep(1000);
        } catch (Exception ex) {
            Log.d(TAG, "Erro PararServico: " + ex.getMessage());

        }
    }
    public static boolean isSpeaking()
    {
        boolean falando = false;

        try {

            if (tts != null) {
                falando = tts.isSpeaking();
            }
        }
        catch (Exception ex)
        {
            Log.d(TAG, "Erro isSpeaking: " + ex.getMessage());
        }

        return falando;
    }
}
