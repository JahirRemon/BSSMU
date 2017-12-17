package com.example.mdjahirulislam.bssmu_demo.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;


import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
//        String id = intent.getStringExtra( "taskID" );
        long taskTime = intent.getExtras().getLong( "taskTime" );
        Log.d( "receiver ", "onReceive: "+String.valueOf( taskTime ) );
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
//        context.startService( new Intent(context,AlarmService.class).putExtra( "taskTime" ,taskTime) );

        startWakefulService(context,new Intent(context,AlarmService.class).putExtra( "taskTime" ,taskTime));
        setResultCode( Activity.RESULT_OK);
    }
}


