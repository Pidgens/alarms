package com.example.derekchiu.alarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

/**
 * Created by derekchiu on 12/29/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private TextView val1, val2, operation;
    private Spinner operationSpinner;
    private EditText inputVal;
    private boolean isRinging = false;
    private ToggleButton toggleButton;
    private int myValue;

    @Override
    public void onReceive(Context context, Intent intent) {

        MainActivity inst = MainActivity.instance();

        AudioManager manager = (AudioManager) inst.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 100, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);

        toggleButton = (ToggleButton) inst.findViewById(R.id.toggleButton);
        val1 = (TextView) inst.findViewById(R.id.val1);
        val2 = (TextView) inst.findViewById(R.id.val2);
        inputVal = (EditText) inst.findViewById(R.id.inputVal);
        operation = (TextView) inst.findViewById(R.id.operation);
        operationSpinner = (Spinner) inst.findViewById(R.id.operationSpinner);

        isRinging = true;
        myValue = 0;
        val1.setVisibility(View.VISIBLE);
        val2.setVisibility(View.VISIBLE);
        inputVal.setVisibility(View.VISIBLE);

        Random r1 = new Random();
        int i1 = r1.nextInt(10000-1000) + 1000;
        val1.setText(Integer.toString(i1));

        Random r2 = new Random();
        int i2 = r2.nextInt(10000-1000) + 1000;
        val2.setText(Integer.toString(i2));

        String operationString = operation.getText().toString();
        int endValIntermediate = 0;
        switch (operationString)
        {
            case "+":
                endValIntermediate = i1 + i2;
                break;
            case "-":
                endValIntermediate = i1 - i2;
                break;
            case "×":
                endValIntermediate = i1 * i2;
                break;
            case "÷":
                endValIntermediate = i1 / i2;
                break;

        }
        final int endVal = endValIntermediate;


        inputVal.setText("");

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final MediaPlayer player = MediaPlayer.create(inst, notification);
        player.setLooping(true);
        player.start();

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
                            toggleButton.setText("ALARM: OFF");
                            toggleButton.setTextColor(Color.RED);
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
