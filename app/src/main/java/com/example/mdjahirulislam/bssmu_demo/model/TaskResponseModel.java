package com.example.mdjahirulislam.bssmu_demo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mdjahirulislam on 16/12/17.
 */

public class TaskResponseModel {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("error")
    @Expose
    private Boolean error;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public static class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("priority")
        @Expose
        private Integer priority;
        @SerializedName("tasktime")
        @Expose
        private String tasktime;
        @SerializedName("taskdate")
        @Expose
        private String taskdate;
        @SerializedName("datetime")
        @Expose
        private String datetime;
        @SerializedName("doctors_id")
        @Expose
        private Integer doctorsId;
        @SerializedName("creator_id")
        @Expose
        private Integer creatorId;
        @SerializedName("category")
        @Expose
        private String category;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTasktime() {
            return tasktime;
        }

        public void setTasktime(String tasktime) {
            this.tasktime = tasktime;
        }

        public String getTaskdate() {
            return taskdate;
        }

        public void setTaskdate(String taskdate) {
            this.taskdate = taskdate;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public Integer getDoctorsId() {
            return doctorsId;
        }

        public void setDoctorsId(Integer doctorsId) {
            this.doctorsId = doctorsId;
        }

        public Integer getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(Integer creatorId) {
            this.creatorId = creatorId;
        }

        @Override
        public String toString() {
            return "Datum{" +
                    "id=" + id +
                    ", createdAt='" + createdAt + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    ", name='" + name + '\'' +
                    ", location='" + location + '\'' +
                    ", description='" + description + '\'' +
                    ", priority=" + priority +
                    ", category='" + category + '\'' +
                    ", tasktime='" + tasktime + '\'' +
                    ", taskdate='" + taskdate + '\'' +
                    ", datetime='" + datetime + '\'' +
                    ", doctorsId=" + doctorsId +
                    ", creatorId=" + creatorId +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TaskResponseModel{" +
                "data=" + data +
                ", error=" + error +
                '}';
    }
}
