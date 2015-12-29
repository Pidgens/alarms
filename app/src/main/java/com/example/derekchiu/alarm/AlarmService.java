package com.example.derekchiu.alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by derekchiu on 12/29/15.
 */
public class AlarmService extends IntentService {

    private NotificationManager notificationManager;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification("Alarm RING");
    }

    private void sendNotification(String msg) {
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent newIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, newIntent, 0);
        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(this).setContentTitle("ALARM").setSmallIcon(R.drawable.alarm)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);
        alarmNotificationBuilder.setContentIntent(contentIntent);
        notificationManager.notify(1, alarmNotificationBuilder.build());
    }
}
