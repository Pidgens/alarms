package com.example.derekchiu.alarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

/**
 * Created by derekchiu on 12/29/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private TextView val1;
    private TextView val2;
    private EditText inputVal;
    private boolean isRinging = false;
    private ToggleButton toggleButton;
    private int myValue;

    @Override
    public void onReceive(Context context, Intent intent) {

        MainActivity inst = MainActivity.instance();
        toggleButton = (ToggleButton) inst.findViewById(R.id.toggleButton);
        val1 = (TextView) inst.findViewById(R.id.val1);
        val2 = (TextView) inst.findViewById(R.id.val2);
        inputVal = (EditText) inst.findViewById(R.id.inputVal);
        isRinging = true;
        myValue = 0;
        val1.setVisibility(View.VISIBLE);
        val2.setVisibility(View.VISIBLE);
        inputVal.setVisibility(View.VISIBLE);

        Random r1 = new Random();
        int i1 = r1.nextInt(500-100) + 100;
        val1.setText(Integer.toString(i1));

        Random r2 = new Random();
        int i2 = r2.nextInt(500-100) + 100;
        val2.setText(Integer.toString(i2));

        final int endVal = i1 + i2;

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final boolean toggled = intent.getExtras().getBoolean("toggled");
//        final Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        inputVal.setText(String.valueOf(0));

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final MediaPlayer player = MediaPlayer.create(inst, notification);
        player.setLooping(true);
        player.start();
//        ringtone.play();
        if (isRinging) {
            inputVal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {



                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!inputVal.getText().toString().equals("")) {
                        myValue = Integer.valueOf(inputVal.getText().toString());
                        Log.i("ENDVALUE", String.valueOf(endVal));
                        Log.i("MYVALUE", String.valueOf(myValue));
                        if (myValue == endVal) {
                            isRinging = false;
                            toggleButton.setChecked(false);
                            player.stop();
                        }
                    }
                }
            });
        }

        ComponentName componentName = new ComponentName(context.getPackageName(), AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(componentName)));
        setResultCode(Activity.RESULT_OK);
    }
}
