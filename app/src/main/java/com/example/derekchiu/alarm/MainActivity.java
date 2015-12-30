package com.example.derekchiu.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private int androidAPI = Build.VERSION.SDK_INT;


    public static MainActivity instance() {
        return inst;
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        nextAlarm = (TextView) findViewById(R.id.nextAlarm);
        iterate = (EditText) findViewById(R.id.iterated);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onToggle(View view) {
        // if alarm on
        togggled = false;
        if (((ToggleButton) view).isChecked()) {
            togggled = true;
            ((ToggleButton) view).setText("ALARM: ON");
            ((ToggleButton) view).setTextColor(Color.GREEN);
            Calendar calendar = Calendar.getInstance();
            if (androidAPI > Build.VERSION_CODES.LOLLIPOP_MR1) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
            } else {
                Log.i("API", "API ERROR");
            }
            // Check calendar time
            Intent newIntent = new Intent(MainActivity.this, AlarmReceiver.class);
            newIntent.putExtra("toggled", togggled);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0, newIntent,0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } else {
            ((ToggleButton) view).setText("ALARM: OFF");
            ((ToggleButton) view).setTextColor(Color.RED);
            Intent newIntent = new Intent(MainActivity.this, AlarmReceiver.class);
            newIntent.putExtra("toggled", togggled);
            pendingIntent = PendingIntent.getBroadcast(this, 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.set(AlarmManager.RTC, 0, pendingIntent);
            alarmManager.cancel(pendingIntent);
        }
    }
}
