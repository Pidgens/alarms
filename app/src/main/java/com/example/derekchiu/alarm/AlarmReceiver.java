package com.example.derekchiu.alarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by derekchiu on 12/29/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    final int timer = 15;

    @Override
    public void onReceive(Context context, Intent intent) {

        MainActivity inst = MainActivity.instance();
        Log.i("ALARM", "SHOULD RING!!");
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final boolean toggled = intent.getExtras().getBoolean("toggled");
        final Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        if (!ringtone.isPlaying()) {
            Log.i("RINGTONE IS PLAYING", "START");
            Intent patternIntent = new Intent(MainActivity.instance(), PatternActivity.class);
            ringtone.play();
            
        }
        final Thread thread = new Thread() {
            @Override
            public void run() {
                    if (toggled) {
                        try {
                            sleep(10000);
                            ringtone.stop();
                            Log.i("RINGTONE IS NOT PLAYING", "STOP");

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        };
        thread.start();
        ComponentName componentName = new ComponentName(context.getPackageName(), AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(componentName)));
        setResultCode(Activity.RESULT_OK);
    }
}
