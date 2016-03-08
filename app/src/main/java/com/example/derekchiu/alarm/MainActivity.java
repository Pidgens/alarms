package com.example.derekchiu.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private TextView nextAlarm;
    private EditText iterate;
    private PendingIntent pendingIntent;
    private ToggleButton toggleButton;
    private static MainActivity inst;
    private TextView alarmStatus;
    private boolean togggled;
    private int alarm_minute;
    private int hour;

    private int androidAPI = Build.VERSION.SDK_INT;


    public static MainActivity instance() {
        return inst;
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        nextAlarm = (TextView) findViewById(R.id.nextAlarm);
        iterate = (EditText) findViewById(R.id.iterated);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                alarm_minute = minute;
                hour = hourOfDay;
            }
        });

        iterate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    String next;
                    if (androidAPI > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        alarm_minute = Integer.valueOf(s.toString()) + timePicker.getMinute();
                        hour = timePicker.getHour();
//                        Log.i("I", String.valueOf(i));
                    }
                    else {
                        alarm_minute = Integer.valueOf(s.toString()) + timePicker.getCurrentMinute();
                        hour = timePicker.getCurrentHour();
//                        Log.i("I2", String.valueOf(i));
                    }
                    // more than an hour
                    if (alarm_minute > 59) {
                        alarm_minute = (alarm_minute % 60);
                        hour += 1;
                    }
                    hour = hour % 12;
                    next = String.valueOf(hour) + " : " + String.valueOf(alarm_minute);
                    nextAlarm.setText(next);
                }
            }
        });
    }

    // TIME IS NOT SET CORRECTLY.
    @TargetApi(Build.VERSION_CODES.M)
    public void onToggle(View view) {
        // if alarm on
        timePicker = (TimePicker) findViewById(R.id.timePicker);
//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                alarm_minute = minute;
//                hour = hourOfDay;
//            }
//        });

        togggled = false;
        if (((ToggleButton) view).isChecked()) {
            togggled = true;
            ((ToggleButton) view).setText("ALARM: ON");
            ((ToggleButton) view).setTextColor(Color.GREEN);
            Calendar calendar = Calendar.getInstance();

            Log.i("API SHIT 1", String.valueOf(androidAPI));
            Log.i("API SHIT 2", String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            if (androidAPI >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, alarm_minute);
                Log.i("TIME PICKER MINUTE", String.valueOf(alarm_minute));

            } else {
                Log.i("API", "API ERROR");
            }
            Log.i("CALENDAR MILLSECONDS", String.valueOf(calendar.getTimeInMillis()));
            Log.i("CALENDAR CURRENT HOUR", String.valueOf(calendar.getTime().getHours()));
            Log.i("CALENDAR CURRENT MINUTE", String.valueOf(calendar.getTime().getMinutes()));
            // Check calendar time
            Intent newIntent = new Intent(MainActivity.this, AlarmReceiver.class);
            newIntent.putExtra("toggled", togggled);
            Log.i("TOGGLED", String.valueOf(togggled));
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0, newIntent,0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } else {
            Log.i("STATUS", "OFF");
            ((ToggleButton) view).setText("ALARM: OFF");
            ((ToggleButton) view).setTextColor(Color.RED);
            alarmManager.cancel(pendingIntent);
        }
    }
}
