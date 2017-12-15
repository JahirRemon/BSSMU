package com.example.mdjahirulislam.bssmu_demo.model;

/**
 * Created by YasinEnu on 2/21/2017.
 */

public class AlarmModel {

    private String alarmUniqueId;
    private String taskID;
    private String alarmTimeInMillis;

    public AlarmModel(String alarmUniqueId, String taskID, String alarmTimeInMillis) {
        this.alarmUniqueId = alarmUniqueId;
        this.taskID = taskID;
        this.alarmTimeInMillis = alarmTimeInMillis;
    }



    public String getTaskID() {
        return taskID;
    }

    public void setAlarmTaskUniqueId(String alarmImmunizationUniqueId) {
        this.taskID = alarmImmunizationUniqueId;
    }





    public String getAlarmUniqueId() {
        return alarmUniqueId;
    }

    public void setAlarmUniqueId(String alarmUniqueId) {
        this.alarmUniqueId = alarmUniqueId;
    }

    public String getAlarmTimeInMillis() {
        return alarmTimeInMillis;
    }

    public void setAlarmTimeInMillis(String alarmTimeInMillis) {
        this.alarmTimeInMillis = alarmTimeInMillis;
    }




}
