package com.example.mdjahirulislam.bssmu_demo.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.view.TaskListActivity;

import java.util.Locale;

public class AlarmService extends IntentService {

    private NotificationManager alarmNotificationManager;
    private TextToSpeech myTTS;
    private String msg= "Wake Up! Wake Up!";


    public AlarmService() {
        super("AlarmService");

    }

    @Override
    public void onHandleIntent(Intent intent) {
        myTTS = new TextToSpeech( getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage( Locale.UK );
                }
            }
        } );


        sendNotification(msg);

    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService( Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TaskListActivity.class), 0);


        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
                this)
                .setContentTitle("Alarm")
                .setSmallIcon( R.drawable.ic_add_white_24dp)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel( true )
                .setVibrate( new long[] { 1000, 1000, 1000, 1000, 1000 } )
                .setLights( Color.RED, 3000, 3000);


        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(Integer.valueOf( (int) System.currentTimeMillis() ), alarmNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
        myTTS.speak( msg, TextToSpeech.QUEUE_FLUSH, null );


    }
}
