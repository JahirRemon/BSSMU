package com.example.mdjahirulislam.bssmu_demo.helper;

import com.example.mdjahirulislam.bssmu_demo.model.TaskModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
/**
 * Created by HP on 9/6/2017.
 http://reverb.com.bd/demo/taskpanel/#
 */

public interface ConnectionApi {
    @POST("demo/taskpanel/#")
    Call<ArrayList<TaskModel>> getNews(@Body TaskModel userRequest);


}
