package com.example.mdjahirulislam.bssmu_demo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mdjahirulislam on 17/12/17.
 */

public class AddTaskResponseModel {
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AddTaskResponseModel{" +
                "status='" + status + '\'' +
                '}';
    }
}
