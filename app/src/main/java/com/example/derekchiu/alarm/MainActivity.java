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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private Spinner operationSpinner;
    private PendingIntent pendingIntent;
    private ToggleButton toggleButton;
    private static MainActivity inst;
    private TextView val1, val2, inputVal, operation;
    private boolean togggled;
    private int alarm_minute;
    private int hour;
    private Calendar nextCalendar;

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
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        operationSpinner = (Spinner) findViewById(R.id.operationSpinner);
        operation = (TextView) findViewById(R.id.operation);
        val1 = (TextView) findViewById(R.id.val1);
        val2 = (TextView) findViewById(R.id.val2);
        inputVal = (TextView) findViewById(R.id.inputVal);

//        val1.setVisibility(View.INVISIBLE);
//        val2.setVisibility(View.INVISIBLE);
//        inputVal.setVisibility(View.INVISIBLE);
//        operation.setVisibility(View.INVISIBLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.operations, R.layout.my_spinner_textview);
        adapter.setDropDownViewResource(R.layout.my_spinner_textview);
        operationSpinner.setAdapter(adapter);

        operationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operation.setText(operationSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                alarm_minute = minute;
                hour = hourOfDay;
            }
        });

    }

    // TIME IS NOT SET CORRECTLY.
    @TargetApi(Build.VERSION_CODES.M)
    public void onToggle(View view) {
        // if alarm on
        timePicker = (TimePicker) findViewById(R.id.timePicker);

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
