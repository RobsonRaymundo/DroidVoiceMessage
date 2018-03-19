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
        InicializarObjetos();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart ");
    }

    @Override
    public void onInit(int i) {
        Log.d(TAG, "onInit ");
        ExibirTodasMensagensOriginal();
        if (!tts.isSpeaking()) {
            ArrayList<String> mensagens = new ArrayList<>();
            mensagens.addAll(DroidCommon.Mensagens);
            for (String str : mensagens) {
                Fala(str);
                DroidCommon.RemoverMsg(str);
            }

        }
        ExibirTodasMensagens();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.d(TAG, "onDestroy ");
        ZerarObjeto();
        super.onDestroy();
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


    private void Fala(final String texto) {
        try {
            //   Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
            Log.d(TAG, "Fala " + texto);

            while (tts.isSpeaking()) {
                Log.d(TAG, "AguardandoFalar: " + texto + " " + tts.isSpeaking());
                DroidCommon.TimeSleep(500);
            }
            DroidCommon.ultimaFrase = texto;
            DroidCommon.TimeSleep(1000);
            Log.d(TAG, "AguardandoFalar: " + texto + " " + tts.isSpeaking());


        } catch (Exception ex) {
            Log.d(TAG, "Fala: " + ex.getMessage());
        }
    }


    private static void ExibirTodasMensagensOriginal() {
        Log.d(TAG, "--------------------------------------------");
        for (String str : DroidCommon.Mensagens) {
            Log.d(TAG, "ExibirTodasMensagensOriginal: " + str);
        }
    }

    private static void ExibirTodasMensagens() {
        Log.d(TAG, "--------------------------------------------");
        for (String str : DroidCommon.Mensagens) {
            Log.d(TAG, "ExibirTodasMensagens: " + str);
        }
    }

}
