package com.example.mdjahirulislam.bssmu_demo.model;

/**
 * Created by mdjahirulislam on 11/12/17.
 */

public class TaskModel {

    private String taskId;
    private String taskUserID;
    private String taskName;
    private String taskLocation;
    private long taskTime;
    private int priority;
    private int category;
    private String description;
    private long createdAt;
    private String creator_id;

    public TaskModel(String taskId, String taskUserID, String taskName, String taskLocation,
                     long taskTime, int priority, int category ,String description, long createdAt, String creator_id) {
        this.taskId = taskId;
        this.taskUserID = taskUserID;
        this.taskName = taskName;
        this.taskLocation = taskLocation;
        this.taskTime = taskTime;
        this.priority = priority;
        this.category = category;
        this.description = description;
        this.createdAt = createdAt;
        this.creator_id = creator_id;
    }



    public TaskModel() {
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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
                "taskId='" + taskId + '\'' +
                ", taskUserID='" + taskUserID + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskLocation='" + taskLocation + '\'' +
                ", taskTime=" + taskTime +
                ", priority=" + priority +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", creator_id='" + creator_id + '\'' +
                '}';
    }
}
