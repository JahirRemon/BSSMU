package com.example.mdjahirulislam.bssmu_demo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mdjahirulislam on 28/03/18.
 */

public class RemoveTaskResponseModel {


        @SerializedName("error")
        @Expose
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

    @Override
    public String toString() {
        return "RemoveTaskResponseModel{" +
                "error='" + error + '\'' +
                '}';
    }
}
