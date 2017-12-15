package com.example.mdjahirulislam.bssmu_demo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mdjahirulislam.bssmu_demo.database.AppData;
import com.example.mdjahirulislam.bssmu_demo.database.DatabaseSource;
import com.example.mdjahirulislam.bssmu_demo.helper.Utilities;
import com.example.mdjahirulislam.bssmu_demo.model.AlarmModel;

import java.util.ArrayList;
import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {

    private static final String TAG =DeviceBootReceiver.class.getSimpleName();

//    SessionManager session;
    private AppData appData;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            appData=new AppData(context);
            Calendar alarmCalendar;
            DatabaseSource myDatabase=new DatabaseSource(context);
//            session=new SessionManager(context);

            ArrayList<AlarmModel> alarmModelArrayList=myDatabase.getAlarmFromAlarmTable(appData.getUserId());
            myDatabase.deleteSetAlarm();

            for (AlarmModel aModel: alarmModelArrayList) {
                alarmCalendar=Calendar.getInstance();
                long alarmTime=Long.parseLong(aModel.getAlarmTimeInMillis());

                if ( Utilities.setAlarm(context,alarmTime,
                        aModel.getAlarmUniqueId(),aModel.getTaskID())){

                    alarmCalendar.setTimeInMillis(alarmTime);
                    myDatabase.addAlarmToSetAlarmTable(String.valueOf((int)System.currentTimeMillis()),aModel.getAlarmUniqueId());
                    Log.d("set_alarm", "Alarm set successfully at :"+alarmCalendar.getTime());
                }
            }


            Log.d(TAG,"device reboot complete");

        }else {
            Log.d(TAG,"else device reboot complete");
        }
    }


}
