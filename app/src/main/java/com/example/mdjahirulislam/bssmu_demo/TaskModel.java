package com.example.mdjahirulislam.bssmu_demo;

/**
 * Created by mdjahirulislam on 11/12/17.
 */

public class TaskModel {
    private String taskName;
    private String taskLocation;
    private long taskTime;

    public TaskModel(String taskName, String taskLocation, long taskTime) {
        this.taskName = taskName;
        this.taskLocation = taskLocation;
        this.taskTime = taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public void setTaskLocation(String taskLocation) {
        this.taskLocation = taskLocation;
    }

    public long getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(long taskTime) {
        this.taskTime = taskTime;
    }



}
