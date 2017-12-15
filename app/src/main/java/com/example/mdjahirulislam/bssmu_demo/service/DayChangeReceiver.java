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

/**
 * Created by Yasin on 10/11/2017.
 */

public class DayChangeReceiver extends BroadcastReceiver {

    private static final String TAG = DayChangeReceiver.class.getSimpleName();
    Calendar calendar;
//    SessionManager session;
    private AppData appData;
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.DATE_CHANGED")){


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



        }


    }
}
