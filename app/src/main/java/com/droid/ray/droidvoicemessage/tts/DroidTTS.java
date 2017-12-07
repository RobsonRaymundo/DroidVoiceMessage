package com.droid.ray.droidvoicemessage.tts;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.droid.ray.droidvoicemessage.common.DroidCommon;

import java.util.Locale;

/**
 * Created by Robson on 22/09/2017.
 */

public class DroidTTS extends Service implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private Context context;
    private String tit;
    private String msg;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onInit(int i) {

        Fala(tit);
        DroidCommon.TimeSleep(1000);
        Fala(msg);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        tit = intent.getStringExtra("tit");
        msg = intent.getStringExtra("msg");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();


        tts = new TextToSpeech(context, this);
        tts.setLanguage(Locale.getDefault());
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


    private void Fala(String texto) {
        Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
        AguardandoFalar();
        stopSelf();
    }

    private void AguardandoFalar() {
        while (tts.isSpeaking()) {
            DroidCommon.TimeSleep(500);
        }
        stopSelf();
    }
}
