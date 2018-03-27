package com.example.mdjahirulislam.bssmu_demo.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.database.AppData;
import com.example.mdjahirulislam.bssmu_demo.database.DatabaseSource;
import com.example.mdjahirulislam.bssmu_demo.helper.ConnectionApi;
import com.example.mdjahirulislam.bssmu_demo.helper.Utilities;
import com.example.mdjahirulislam.bssmu_demo.model.LoginResponseModel;
import com.example.mdjahirulislam.bssmu_demo.model.TaskModel;
import com.example.mdjahirulislam.bssmu_demo.model.TaskResponseModel;
import com.example.mdjahirulislam.bssmu_demo.model.UserModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = LoginActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_RESULT = 1;
    private ProgressDialog progressDialog;

    private ConnectionApi connectionApi;
    private AppData appData;
    private DatabaseSource db;


    private EditText userNameET;
    private EditText passwordET;

    private Button loginBTN;
    private LoginResponseModel loginResponseModel;
    private TaskResponseModel taskResponseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        userNameET = findViewById( R.id.userNameET );
        passwordET = findViewById( R.id.passwordET );
        connectionApi = Utilities.getRetrofit().create( ConnectionApi.class );
        appData = new AppData( this );
        db = new DatabaseSource( this );
        progressDialog = new ProgressDialog( this, R.style.MyAlertDialogStyle );
        progressDialog.setCancelable( false );


        if (appData.getUsername() != (null)) {
            startActivity( new Intent( LoginActivity.this, TaskListActivity.class ) );
            finish();
        }

        // Get runtime permissions if build version >= Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET )
                    != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                            != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission( this, Manifest.permission.CAMERA )
                            != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE )
                            != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions( new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_RESULT );
            }
        }

    }

    public void getLogin(View view) {

        progressDialog.setMessage( "Login ...." );
        showDialog();

        final String userNameSt = userNameET.getText().toString().toLowerCase().trim();
        final String passwordSt = passwordET.getText().toString().toLowerCase().trim();
        final String upComingTask = "1";
        if (userNameSt.isEmpty()) {
            userNameET.setError( "Required Fields" );
            hideDialog();
        } else if (passwordSt.isEmpty()) {
            passwordET.setError( "Required Fields" );
            hideDialog();
        } else {
            final RequestBody userName = RequestBody.create( MultipartBody.FORM, userNameSt );
            final RequestBody password = RequestBody.create( MultipartBody.FORM, passwordSt );
            final RequestBody type = RequestBody.create( MultipartBody.FORM, upComingTask );
//            final RequestBody category = RequestBody.create( MultipartBody.FORM, "2" );

            Call<LoginResponseModel> getUser = connectionApi.getLogin( userName, password );
            getUser.enqueue( new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                    if (response.code() == 200) {
                        loginResponseModel = response.body();
                        Log.d( TAG, "onResponse: " + loginResponseModel.toString() );
//                        if (loginResponseModel.getError()==null) {
                        try {


                            boolean error = loginResponseModel.getError();
                            if (!error) {

                                String user_unique_id = loginResponseModel.getId().toString().trim();
                                String user_full_name = loginResponseModel.getName().toString().trim();
                                String user_name = loginResponseModel.getUsername().toString().trim();
                                String user_password = passwordSt;
                                String avatar = loginResponseModel.getAvatar().toString().trim();
                                String email = loginResponseModel.getEmail().toString().trim();
                                String role = loginResponseModel.getRole().toString().trim();
                                String doctors_id = loginResponseModel.getDoctorsId().toString().trim();

                                UserModel userModel = new UserModel( user_unique_id, user_full_name, user_name, avatar, email, user_password, role, doctors_id );

                                Log.d( TAG, "onResponse: " + userModel.toString() );
                                boolean status = db.addUser( userModel );
                                if (status) {
                                    appData.setUsername( userNameSt );
                                    appData.setPassword( passwordSt );
                                    appData.setUserId( user_unique_id );
                                    if (getTaskList( userName, password, type)) {

                                    }
                                }

                            } else {
                                Log.d( TAG, "Not Successful : " + loginResponseModel.getResponse() );
                            }
                        } catch (Throwable exception) {
                            hideDialog();
//                            Log.d( TAG, "onResponse: " + exception.getMessage().toString() );
                            Toast.makeText( LoginActivity.this, "User Name or Password is wrong!!!", Toast.LENGTH_LONG ).show();
                        }

                    } else {
                        Log.d( TAG, "Successful Error code : " + response.code() );
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    Toast.makeText( LoginActivity.this, "Connection Failed", Toast.LENGTH_SHORT ).show();
                    Log.d( TAG, "onFailure: Connection Failed" );
                    hideDialog();
                }
            } );


        }
    }

    public boolean getTaskList(RequestBody userName, RequestBody password, RequestBody type) {
        final boolean[] result = {true};
        Call<TaskResponseModel> getTask = connectionApi.getTask( userName, password, type , null);
        getTask.enqueue( new Callback<TaskResponseModel>() {
            @Override
            public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {

                if (response.code() == 200) {
                    taskResponseModel = response.body();
                    Log.d( TAG, "onResponse: " + taskResponseModel.toString() );
                    boolean error = taskResponseModel.getError();
                    if (!error) {

                        ArrayList<TaskResponseModel.Datum> datumArrayList = (ArrayList<TaskResponseModel.Datum>) taskResponseModel.getData();
                        Log.d( TAG, "onResponse: " + datumArrayList.toString() );
//                            {"":"Operation","":"OT","":"Heart","priority":3,"tasktime":"02:19:12","":"2017-12-18","datetime":"2017-12-18 02:19:12","doctors_id":542,"creator_id":542}

                        if (datumArrayList.size() > 0) {
                            for (TaskResponseModel.Datum datum :
                                    datumArrayList) {
                                String taskId = datum.getId().toString().trim();
                                String created_at = datum.getCreatedAt().toString().trim();
                                String updated_at = datum.getUpdatedAt().toString().trim();
                                String taskName = datum.getName().toString().trim();
                                String location = datum.getLocation().toString().trim();
                                String description = datum.getDescription().toString().trim();
                                String priority = datum.getPriority().toString().trim();
                                String category = datum.getPriority().toString().trim();
                                String taskTime = datum.getTasktime().toString().trim();
                                String taskDate = datum.getTaskdate().toString().trim();
                                String datetime = datum.getDatetime().toString().trim();
                                String doctors_id = datum.getDoctorsId().toString().trim();
                                String creator_id = datum.getCreatorId().toString().trim();

                                try {
                                    TaskModel taskModel = new TaskModel( taskId, doctors_id, taskName, location,
                                            Utilities.stringDateTimeToMills( datetime ), Integer.parseInt( priority ), Integer.parseInt( category ),
                                            description,
                                            Utilities.stringDateTimeToMills( created_at ), creator_id );
                                    Log.d( TAG, "onResponse: Convert time ---> "+ Utilities.dateTimeFormation( Utilities.stringDateTimeToMills( datetime ) ) );
//                                    Log.d( TAG, "onResponse: "+taskModel.toString() );
                                    boolean status = db.addTask( taskModel );
                                    if (status) {
                                        Log.d( TAG, "onResponse: true status ---> task insert into database" );
                                        final String alarmId = String.valueOf( (int) System.currentTimeMillis() );

                                        if (!Utilities.setAlarm( LoginActivity.this,
                                                Utilities.stringDateTimeToMills( datetime ), alarmId, taskId )) {
                                            Log.d( TAG, " Today Alarm not set" );
//                                    db.addAlarmToSetAlarmTable(String.valueOf((int)System.currentTimeMillis()),alarmId);
                                        }
                                        startActivity( new Intent( LoginActivity.this, HomeActivity.class )
                                                .setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                                        finish();
                                        result[0] = true;

                                    } else {
                                        Log.d( TAG, "onResponse: Task not insert into database" );
                                        Toast.makeText( LoginActivity.this, "Something Occurred wrong!!", Toast.LENGTH_SHORT ).show();
                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Log.d( TAG, "onResponse: " + e.getMessage() );
                                }
                            }

                        } else {
                            Toast.makeText( LoginActivity.this, "No Task Found", Toast.LENGTH_SHORT ).show();
                            startActivity( new Intent( LoginActivity.this, TaskListActivity.class )
                                    .setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                            finish();

                            hideDialog();
                        }


                        result[0] = true;
                    } else {
                        Log.d( TAG, "Not Successful : " + taskResponseModel.getError() );
                    }
                    result[0] = true;
                } else {
                    Log.d( TAG, "Successful Error code : " + response.code() );
                }
                result[0] = true;

            }

            @Override
            public void onFailure(Call<TaskResponseModel> call, Throwable t) {

                result[0] = false;
                hideDialog();
            }
        } );
        Log.d( TAG, "getTaskList: " + result[0] );
        return result[0];
    }

    public void showDialog() {

        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideDialog() {

        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
