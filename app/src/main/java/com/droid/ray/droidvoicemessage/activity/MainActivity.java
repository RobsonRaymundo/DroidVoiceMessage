package com.droid.ray.droidvoicemessage.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.droid.ray.droidvoicemessage.R;
import com.droid.ray.droidvoicemessage.common.DroidCommon;
import com.droid.ray.droidvoicemessage.common.DroidPreferences;

import java.util.Map;

import static com.droid.ray.droidvoicemessage.common.DroidCommon.TAG;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DroidVoiceMessage";
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getBaseContext();
            setContentView(R.layout.activity_main);
            final ViewGroup layout = (ViewGroup) findViewById(R.id.layout_id);
            DroidCommon.StartPhoneService(context);
            DroidCommon.ShowLayout(context, layout);
            Log.d(TAG, "onCreate ");
        } catch (Exception ex) {
            Log.d(TAG, "onCreate: " + ex.getMessage());
        }
    }
}

