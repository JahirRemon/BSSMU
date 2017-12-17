package com.example.mdjahirulislam.bssmu_demo.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeechService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.database.DatabaseSource;
import com.example.mdjahirulislam.bssmu_demo.model.TaskModel;
import com.example.mdjahirulislam.bssmu_demo.view.TaskListActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AlarmService extends IntentService {

    private NotificationManager alarmNotificationManager;
    private TextToSpeech myTTS;
    private String msg;
    private DatabaseSource db;
    private TaskModel taskModel;
    private SimpleDateFormat simpleDateFormatForPlaySound;

    private TextToSpeechService toSpeechService;



    public AlarmService() {
        super("AlarmService");

    }

    @Override
    public void onHandleIntent(final Intent intent) {
        db = new DatabaseSource( getApplicationContext() );
        taskModel = new TaskModel(  );
        simpleDateFormatForPlaySound = new SimpleDateFormat("hh:mm a");
        myTTS = new TextToSpeech( getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage( Locale.UK );
                    myTTS.setPitch( 1.0f );
                    myTTS.setSpeechRate( .8f );
                    long taskTime = intent.getExtras().getLong( "taskTime" );
                    Log.d( "service ", "onHandleIntent: " +String.valueOf( taskTime ));
                    taskModel = db.getSingleTask( String.valueOf( taskTime ) );
                    msg  = "Sir you have a "+ taskModel.getTaskName()+ " Location at "+taskModel.getTaskLocation()
                            +" on "+ simpleDateFormatForPlaySound.format( taskModel.getTaskTime());
                    myTTS.speak( msg, TextToSpeech.QUEUE_FLUSH, null );
                    Log.d( "textToSpeech - if", "onInit: " +String.valueOf( status ));
                    sendNotification(msg);
                }else {
                    Log.d( "textToSpeech - else", "onInit: " +String.valueOf( status ));
                }
            }
        } );





    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService( Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TaskListActivity.class), 0);


//        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
        Notification alarmNotificationBuilder  = new Notification.Builder(
                this)
                .setContentTitle("Alarm")
                .setSmallIcon( R.drawable.ic_add_white_24dp)
                .setContentText(msg)
                .setAutoCancel( true )
                .setVibrate( new long[] { 1000, 1000, 1000, 1000, 1000 } )
                .setLights( Color.RED, 3000, 3000)
                .setContentIntent( contentIntent )
                .build();
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

//        notificationManager.notify((int) System.currentTimeMillis(), n);


//        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(Integer.valueOf( (int) System.currentTimeMillis() ), alarmNotificationBuilder);
        Log.d("AlarmService", "Notification sent.");

    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        myTTS.stop();
//        myTTS.shutdown();

        Log.d( "service ", "onDestroy: " );
        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }
}
