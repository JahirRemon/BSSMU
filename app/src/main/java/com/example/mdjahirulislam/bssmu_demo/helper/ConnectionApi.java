package com.example.mdjahirulislam.bssmu_demo.helper;

import com.example.mdjahirulislam.bssmu_demo.model.AddTaskResponseModel;
import com.example.mdjahirulislam.bssmu_demo.model.LoginResponseModel;
import com.example.mdjahirulislam.bssmu_demo.model.TaskModel;
import com.example.mdjahirulislam.bssmu_demo.model.TaskResponseModel;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by HP on 9/6/2017.
 http://reverb.com.bd/demo/taskpanel/#
 */

public interface ConnectionApi {
    @Multipart
    @POST("api/login")
    Call<LoginResponseModel> getLogin(
            @Part ("username") RequestBody username,
            @Part ("password") RequestBody password
    );

    @Multipart
    @POST("api/task/get")
    Call<TaskResponseModel> getTask(
            @Part ("username") RequestBody username,
            @Part ("password") RequestBody password,
            @Part ("type") RequestBody type,
            @Part ("category") RequestBody category

    );

    @Multipart
    @POST("api/task/add")
    Call<AddTaskResponseModel> addTask(

            @Part ("name") RequestBody name,
            @Part ("location") RequestBody location,
            @Part ("description") RequestBody description,
            @Part ("priority") RequestBody priority,
            @Part ("taskdate") RequestBody taskdate,
            @Part ("tasktime") RequestBody tasktime,
            @Part ("doctors_id") RequestBody doctors_id,
            @Part ("creator_id") RequestBody creator_id,
            @Part ("category") RequestBody category

    );


}
