package com.example.mdjahirulislam.bssmu_demo.model;

/**
 * Created by mdjahirulislam on 11/12/17.
 */

public class TaskModel {

    private String taskUserID;
    private String taskName;
    private String taskLocation;
    private long taskTime;
    private int priority;
    private String description;
    private long createdAt;

    public TaskModel(String taskUserID, String taskName, String taskLocation, long taskTime, int priority, String description, long createdAt) {
        this.taskUserID = taskUserID;
        this.taskName = taskName;
        this.taskLocation = taskLocation;
        this.taskTime = taskTime;
        this.priority = priority;
        this.description = description;
        this.createdAt = createdAt;
    }

    public TaskModel(String taskName, String taskLocation, long taskTime) {
        this.taskName = taskName;
        this.taskLocation = taskLocation;
        this.taskTime = taskTime;
    }

    public String getTaskUserID() {
        return taskUserID;
    }

    public void setTaskUserID(String taskUserID) {
        this.taskUserID = taskUserID;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "taskUserID='" + taskUserID + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskLocation='" + taskLocation + '\'' +
                ", taskTime=" + taskTime +
                ", priority=" + priority +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
