package com.example.mdjahirulislam.bssmu_demo.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mdjahirulislam.bssmu_demo.service.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by mdjahirulislam on 14/12/17.
 */

public class Utilities {

    public static boolean setAlarm(Context context, long alarmTimeInMills, String alarmId, String taskID) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( alarmTimeInMills );
        if (isItToday( calendar.getTimeInMillis() )) {
            if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
                Intent alarmIntent = new Intent( context, AlarmReceiver.class );
                alarmIntent.putExtra( "taskID", taskID );
                PendingIntent pendingIntent = PendingIntent.getBroadcast( context, Integer.parseInt( alarmId ), alarmIntent, PendingIntent.FLAG_ONE_SHOT );
                AlarmManager manager = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
                manager.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent );
                Log.d( "set-Alarm", "Alarm set at" + calendar.getTime() );
                return true;
            } else {
                Log.d( "set-Check_current", "Alarm time is lower then present time - " + calendar.getTime() );
                return false;
            }
        } else {
            Log.d( "set-ToDayCheck", "Alarm time is not today - " + calendar.getTime() );
            return false;
        }

    }

    public static boolean isItToday(long dateInMills) {

        Calendar calendar=Calendar.getInstance();
        Calendar calendar2=Calendar.getInstance();
        calendar2.setTimeInMillis(dateInMills);

        if (calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)) {
            return true;
        }else {
            return false;
        }
    }
}
