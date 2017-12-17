package com.example.mdjahirulislam.bssmu_demo.view;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.adapter.CustomAdapter;
import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.database.AppData;
import com.example.mdjahirulislam.bssmu_demo.database.DatabaseSource;
import com.example.mdjahirulislam.bssmu_demo.helper.ConnectionApi;
import com.example.mdjahirulislam.bssmu_demo.helper.Utilities;
import com.example.mdjahirulislam.bssmu_demo.model.AddTaskResponseModel;
import com.example.mdjahirulislam.bssmu_demo.model.TaskModel;
import com.example.mdjahirulislam.bssmu_demo.model.TaskResponseModel;
import com.example.mdjahirulislam.bssmu_demo.model.UserModel;
import com.example.mdjahirulislam.bssmu_demo.service.UpdateTaskDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private final String TAG = TaskListActivity.class.getSimpleName();

    private ListView taskLV;
    private TextView userNameTV;
    private ArrayList<TaskModel> taskArrayList;
    private CustomAdapter arrayAdapter;
    private TaskModel taskModel;
    private Calendar calendar;
    private long taskTimeInMills;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleDateFormatForPlaySound;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static TaskListActivity inst;

    private DatabaseSource db;
    public static TaskListActivity instance() {
        return inst;
    }
    private AppData appData;
    private ImageView playBTN;
    private TextToSpeech myTTS;

    private SwipeRefreshLayout swipeLayout;
    private ConnectionApi connectionApi;
    private TaskResponseModel taskResponseModel;
    private AddTaskResponseModel addTaskResponseModel;
    private UserModel userModel;
    public static final String UPCOMING_TASK = "1";
    public static final String OLD_TASK = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task_list );
        taskLV = findViewById( R.id.taskListLV );
        taskArrayList = new ArrayList<>( );
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
        simpleDateFormatForPlaySound = new SimpleDateFormat("hh:mm a");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        playBTN = findViewById( R.id.playSound );
        db = new DatabaseSource( this );
        appData = new AppData( this );
        userNameTV = findViewById( R.id.userNameTV );
        connectionApi = Utilities.getRetrofit().create( ConnectionApi.class );
        userModel = new UserModel(  );
        userModel = db.getUser( appData.getUserId() );

        userNameTV.setText( "WelCome "+ userModel.getUser_full_name().toString() );

        try {
            myTTS = new TextToSpeech( getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        myTTS.setLanguage( Locale.ENGLISH );
                        myTTS.setPitch( 1.0f );
                        myTTS.setSpeechRate( .8f );
                    }
                }
            } );

        } catch (Exception e) {
            Log.d( "AlarmReceiver", "onReceive: " + e.getLocalizedMessage() );
        }

        Log.d( TAG, "onCreate: "+appData.getUserId() );

        taskArrayList = db.getMyAllTask( appData.getUserId() );
        if (taskArrayList.size()>0) {
            arrayAdapter = new CustomAdapter( this,taskArrayList );
            taskLV.setAdapter( arrayAdapter );
            Log.d( TAG, "taskArrayList size "+taskArrayList.size());
        }else {
            Log.d(TAG, "taskArrayList size else "+taskArrayList.size());
        }
        taskLV.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String msg = "Sir you have a "+ taskArrayList.get( i ).getTaskName()+ " Location at "+taskArrayList.get( i ).getTaskLocation()
                        +" on "+ simpleDateFormatForPlaySound.format( taskArrayList.get( i ).getTaskTime());
                myTTS.speak(msg , TextToSpeech.QUEUE_FLUSH, null );

            }
        } );


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    protected void onStart() {
        Log.d( TAG, "onStart: " );
        super.onStart();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(TaskListActivity.this, alarmUri);
        ringtone.stop();
//        alarmManager.cancel(pendingIntent);
    }

    public void addReminder(View view) {

        final String alarmId=String.valueOf((int) System.currentTimeMillis());
        final String taskId= UUID.randomUUID().toString();

        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.add_task_dialog_design, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText taskNameInput = (EditText) promptsView.findViewById(R.id.taskNameET);
        final EditText taskLocationInput = (EditText) promptsView.findViewById(R.id.taskLocationET);
        final EditText taskDescription = (EditText) promptsView.findViewById(R.id.taskDescriptionET);
        final RadioGroup radioPriority = promptsView.findViewById( R.id.radioPriority );

        final Button taskTimeBTN = promptsView.findViewById( R.id.taskTimeBTN );
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DAY_OF_MONTH);
        final int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int mMinute = calendar.get(Calendar.MINUTE);




        taskTimeBTN.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(TaskListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set( Calendar.DAY_OF_MONTH,dayOfMonth);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.YEAR,year);
//                        String dateString = sdf.format(calendar.getTimeInMillis());


                        Toast.makeText(getApplicationContext(),String.valueOf(calendar.getTime()),Toast.LENGTH_LONG).show();
                        Log.d(TAG,String.valueOf(calendar.getTime()));
                        TimePickerDialog timePickerDialog = new TimePickerDialog(TaskListActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        calendar.set( Calendar.HOUR_OF_DAY,hourOfDay );
                                        calendar.set( Calendar.MINUTE,minute );
                                        taskTimeInMills=calendar.getTimeInMillis();
                                        taskTimeBTN.setText(simpleDateFormat.format(calendar.getTimeInMillis()));
                                        Log.d( TAG, "onTimeSet:  "+hourOfDay + ":" + minute);
//                                        Intent myIntent = new Intent(TaskListActivity.this, AlarmReceiver.class);
//                                        pendingIntent = PendingIntent.getBroadcast(TaskListActivity.this, 0, myIntent, 0);
//
//                                        alarmManager.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


                                    }
                                }, mHour, mMinute, false);
//                        timePickerDialog.getCurrentFocus().
                        timePickerDialog.show();
                    }
                },year,month,date);
//                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();



                // Launch Time Picker Dialog

            }
        } );
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                final RadioButton priorityBTN = promptsView.findViewById( radioPriority.getCheckedRadioButtonId() );

                                final int priorityInt;
                                String p = priorityBTN.getText().toString().trim().toLowerCase();
                                if (p.equals( "high" )){
                                    priorityInt = 1;
                                }else if (p.equals( "normal" )){
                                    priorityInt = 2;
                                }else {
                                    priorityInt = 3;
                                }

                                RequestBody name = RequestBody.create( MultipartBody.FORM, taskNameInput.getText().toString().trim() );
                                RequestBody location = RequestBody.create( MultipartBody.FORM, taskLocationInput.getText().toString().trim() );
                                RequestBody description = RequestBody.create( MultipartBody.FORM, taskDescription.getText().toString().trim() );
                                RequestBody priority = RequestBody.create( MultipartBody.FORM, String.valueOf( priorityInt ) );
                                RequestBody taskDate = RequestBody.create( MultipartBody.FORM, Utilities.dateFormation( taskTimeInMills ) );
                                RequestBody taskTime = RequestBody.create( MultipartBody.FORM, Utilities.timeFormation( taskTimeInMills ) );
                                RequestBody doctors_id = RequestBody.create( MultipartBody.FORM, userModel.getDoctor_id().toString().trim() );
                                RequestBody creator_id = RequestBody.create( MultipartBody.FORM, userModel.getUser_unique_id().toString().trim() );

                                Log.d( TAG, "onClick: \n"+taskNameInput.getText().toString().trim()+"\n"+taskLocationInput.getText().toString().trim()+"\n"+taskDescription.getText().toString().trim()+"\n"+
                                        String.valueOf( priorityInt )+"\n"+Utilities.dateFormation( taskTimeInMills )+"\n"+Utilities.timeFormation( taskTimeInMills )+"\n"+
                                        userModel.getDoctor_id().toString().trim()+"\n"+userModel.getUser_unique_id().toString().trim() );


                                Call<AddTaskResponseModel> addTask = connectionApi.addTask( name,location,description,priority,taskDate,taskTime,doctors_id,creator_id );
                                addTask.enqueue( new Callback<AddTaskResponseModel>() {
                                    @Override
                                    public void onResponse(Call<AddTaskResponseModel> call, Response<AddTaskResponseModel> response) {
                                        if (response.code()==200) {
                                            addTaskResponseModel = response.body();
                                            Log.d( TAG, "onResponse: ----->  "+addTaskResponseModel.toString() );
                                            if (addTaskResponseModel.getStatus().equals( "success" )){
                                                getTaskList(  );
                                            }else {
                                                Log.d( TAG, "onResponse:  else ---> "+response.code() );
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AddTaskResponseModel> call, Throwable t) {

                                    }
                                } );
//                                TaskModel taskModelOk = new TaskModel( "1",taskNameInput.getText().toString().trim(),
//                                        taskLocationInput.getText().toString().trim(),
//                                        taskTimeInMills,priority,taskDescription.getText().toString().trim(),System.currentTimeMillis());
//                                db.addTask( taskModelOk );
//                                if (!Utilities.setAlarm(TaskListActivity.this,
//                                        taskTimeInMills,alarmId,taskId)){
//                                    Log.d( TAG, " Today Alarm not set" );
////                                    db.addAlarmToSetAlarmTable(String.valueOf((int)System.currentTimeMillis()),alarmId);
//                                }
//                                taskArrayList.add( taskModelOk );
//                                Log.d( TAG, "onClick: "+taskModelOk.toString() );
//                                arrayAdapter = new CustomAdapter( getApplicationContext(),taskArrayList );
//                                taskLV.setAdapter( arrayAdapter );
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public String timeConvert (String myDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = sdf.parse(myDate);
        long millis = date.getTime();
        return  String.valueOf( millis );
    }


    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"Call swipe refresh\n"+appData.getUsername()+"\n"+appData.getPassword());


        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {

                UpdateTaskDatabase updateTaskDatabase = new UpdateTaskDatabase( TaskListActivity.this );
                updateTaskDatabase.start();
//                updateTaskDatabase.sl

                getTaskList( );
                swipeLayout.setRefreshing(false);
            }
        }, 3000);

    }

    public void getTaskList(){
        final RequestBody userName = RequestBody.create( MultipartBody.FORM,appData.getUsername());
        final RequestBody password = RequestBody.create( MultipartBody.FORM,appData.getPassword());
        final RequestBody type = RequestBody.create( MultipartBody.FORM,UPCOMING_TASK);

        Call<TaskResponseModel> getTask = connectionApi.getTask( userName,password,type );
        getTask.enqueue( new Callback<TaskResponseModel>() {
            @Override
            public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {

                if (response.code()==200) {
                    taskResponseModel = response.body();
//                    Log.d( TAG, "onResponse: "+taskResponseModel.toString() );
                    boolean error = taskResponseModel.getError();
                    if (!error) {

                        ArrayList<TaskResponseModel.Datum> datumArrayList = (ArrayList<TaskResponseModel.Datum>) taskResponseModel.getData();
//                        Log.d( TAG, "onResponse: " + datumArrayList.toString());

                        if (datumArrayList.size()>0){
                            taskArrayList.clear();
                            for (TaskResponseModel.Datum datum:
                                    datumArrayList) {
                                String taskId = datum.getId().toString().trim();
                                String created_at = datum.getCreatedAt().toString().trim();
                                String updated_at = datum.getUpdatedAt().toString().trim();
                                String taskName = datum.getName().toString().trim();
                                String location = datum.getLocation().toString().trim();
                                String description = datum.getDescription().toString().trim();
                                String priority = datum.getPriority().toString().trim();
                                String taskTime = datum.getTasktime().toString().trim();
                                String taskDate = datum.getTaskdate().toString().trim();
                                String datetime = datum.getDatetime().toString().trim();
                                String doctors_id = datum.getDoctorsId().toString().trim();
                                String creator_id = datum.getCreatorId().toString().trim();

                                try {
                                    TaskModel taskModel = new TaskModel( taskId,doctors_id,taskName,location,
                                            Utilities.stringDateTimeToMills( datetime ),Integer.parseInt( priority ),description,
                                            Utilities.stringDateTimeToMills( created_at ),creator_id);
//                                    Log.d( TAG, "onResponse: Convert time ---> "+ Utilities.dateFormation( Utilities.stringDateTimeToMills( datetime ) ) );
//                                    Log.d( TAG, "onResponse: "+taskModel.toString() );

                                    taskArrayList.add( taskModel );

                                    boolean status = db.addTask( taskModel );
                                    if (status){
                                        final String alarmId = String.valueOf( (int) System.currentTimeMillis() );

                                        Log.d( TAG, "onResponse: true status ---> task insert into database" );
                                        if (!Utilities.setAlarm( TaskListActivity.this,
                                                Utilities.stringDateTimeToMills( datetime ), alarmId, taskId )) {
                                            Log.d( TAG, " Today Alarm not set" );
                                        }


                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Log.d( TAG, "onResponse: "+e.getMessage() );
                                }
                            }
//                            taskArrayList.notify();
                            arrayAdapter.notifyDataSetChanged();

                        }

//                            UserModel userModel = new UserModel( user_unique_id,user_full_name,user_name,avatar,email,user_password,role,doctors_id );



                    }else {
                        Log.d(TAG,"Not Successful : "+taskResponseModel.getError());
                    }
                }else {
                    Log.d(TAG,"Successful Error code : "+response.code());
                }


            }

            @Override
            public void onFailure(Call<TaskResponseModel> call, Throwable t) {

            }
        } );
    }

//    public void getOldTaskList(){
//        final RequestBody userName = RequestBody.create( MultipartBody.FORM,appData.getUsername());
//        final RequestBody password = RequestBody.create( MultipartBody.FORM,appData.getPassword());
//        final RequestBody type = RequestBody.create( MultipartBody.FORM,OLD_TASK);
//
//        Call<TaskResponseModel> getTask = connectionApi.getTask( userName,password,type );
//        getTask.enqueue( new Callback<TaskResponseModel>() {
//            @Override
//            public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {
//
//                if (response.code()==200) {
//                    taskResponseModel = response.body();
////                    Log.d( TAG, "onResponse: "+taskResponseModel.toString() );
//                    boolean error = taskResponseModel.getError();
//                    if (!error) {
//
//                        ArrayList<TaskResponseModel.Datum> datumArrayList = (ArrayList<TaskResponseModel.Datum>) taskResponseModel.getData();
////                        Log.d( TAG, "onResponse: " + datumArrayList.toString());
//
//                        if (datumArrayList.size()>0){
//                            taskArrayList.clear();
//                            for (TaskResponseModel.Datum datum:
//                                    datumArrayList) {
//                                String taskId = datum.getId().toString().trim();
//                                String created_at = datum.getCreatedAt().toString().trim();
//                                String updated_at = datum.getUpdatedAt().toString().trim();
//                                String taskName = datum.getName().toString().trim();
//                                String location = datum.getLocation().toString().trim();
//                                String description = datum.getDescription().toString().trim();
//                                String priority = datum.getPriority().toString().trim();
//                                String taskTime = datum.getTasktime().toString().trim();
//                                String taskDate = datum.getTaskdate().toString().trim();
//                                String datetime = datum.getDatetime().toString().trim();
//                                String doctors_id = datum.getDoctorsId().toString().trim();
//                                String creator_id = datum.getCreatorId().toString().trim();
//
//                                try {
//                                    TaskModel taskModel = new TaskModel( taskId,doctors_id,taskName,location,
//                                            Utilities.stringDateTimeToMills( datetime ),Integer.parseInt( priority ),description,
//                                            Utilities.stringDateTimeToMills( created_at ),creator_id);
////                                    Log.d( TAG, "onResponse: Convert time ---> "+ Utilities.dateFormation( Utilities.stringDateTimeToMills( datetime ) ) );
////                                    Log.d( TAG, "onResponse: "+taskModel.toString() );
//
//                                    taskArrayList.add( taskModel );
//
//                                    boolean status = db.addTask( taskModel );
//                                    if (status){
//                                        final String alarmId = String.valueOf( (int) System.currentTimeMillis() );
//
//                                        Log.d( TAG, "onResponse: true status ---> task insert into database" );
//                                        if (!Utilities.setAlarm( TaskListActivity.this,
//                                                Utilities.stringDateTimeToMills( datetime ), alarmId, taskId )) {
//                                            Log.d( TAG, " Today Alarm not set" );
//                                        }
//
//                                        arrayAdapter.notifyDataSetChanged();
//                                    }
//
//
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                    Log.d( TAG, "onResponse: "+e.getMessage() );
//                                }
//                            }
//
//                        }
//
////                            UserModel userModel = new UserModel( user_unique_id,user_full_name,user_name,avatar,email,user_password,role,doctors_id );
//
//
//
//                    }else {
//                        Log.d(TAG,"Not Successful : "+taskResponseModel.getError());
//                    }
//                }else {
//                    Log.d(TAG,"Successful Error code : "+response.code());
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<TaskResponseModel> call, Throwable t) {
//
//            }
//        } );
//    }
}
