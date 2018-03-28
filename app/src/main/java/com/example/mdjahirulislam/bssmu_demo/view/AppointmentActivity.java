package com.example.mdjahirulislam.bssmu_demo.view;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.adapter.CustomAdapter;
import com.example.mdjahirulislam.bssmu_demo.database.AppData;
import com.example.mdjahirulislam.bssmu_demo.database.DatabaseSource;
import com.example.mdjahirulislam.bssmu_demo.helper.ConnectionApi;
import com.example.mdjahirulislam.bssmu_demo.helper.Utilities;
import com.example.mdjahirulislam.bssmu_demo.model.AddTaskResponseModel;
import com.example.mdjahirulislam.bssmu_demo.model.RemoveTaskResponseModel;
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

public class AppointmentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = AppCompatActivity.class.getSimpleName();

    private ListView appointmentLV;
    private ArrayList<TaskModel> appointmentArrayList;
    private CustomAdapter arrayAdapter;
    private TaskModel taskModel;
    private Calendar calendar;
    private long taskTimeInMills = 0 ;
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
    private RemoveTaskResponseModel removeTaskResponseModel;
    private UserModel userModel;
    public static final String UPCOMING_TASK = "1";
    public static final String TASK_CATEGORY_APPOINTMENT = "1";
    public static final String OLD_TASK = "2";
    private Calendar minCalender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_appointment );
        appointmentLV = findViewById( R.id.appointmentLV );
        appointmentArrayList = new ArrayList<>( );
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
        simpleDateFormatForPlaySound = new SimpleDateFormat("hh:mm a");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        playBTN = findViewById( R.id.playSound );
        db = new DatabaseSource( this );
        appData = new AppData( this );
//        userNameTV = findViewById( R.id.userNameTV );
        connectionApi = Utilities.getRetrofit().create( ConnectionApi.class );
        userModel = new UserModel(  );
        userModel = db.getUser( appData.getUserId() );

        minCalender = Calendar.getInstance( Locale.getDefault());
//        userNameTV.setText( "Welcome, "+ userModel.getUser_full_name().toString() );

        try {
            myTTS = new TextToSpeech( getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        myTTS.setLanguage( Locale.ENGLISH );
                        myTTS.setPitch( 1.0f );
                        myTTS.setSpeechRate( .7f );
                    }
                }
            } );

        } catch (Exception e) {
            Log.d( "AlarmReceiver", "onReceive: " + e.getLocalizedMessage() );
        }

        Log.d( TAG, "onCreate: "+appData.getUserId() );

        appointmentArrayList = db.getCategoryTask( appData.getUserId(),TASK_CATEGORY_APPOINTMENT );
        if (appointmentArrayList.size()>0) {
            arrayAdapter = new CustomAdapter( this, appointmentArrayList );
            appointmentLV.setAdapter( arrayAdapter );
            Log.d( TAG, "appointmentArrayList size "+ appointmentArrayList.size());
        }else {
            arrayAdapter = new CustomAdapter( this, appointmentArrayList );
            appointmentLV.setAdapter( arrayAdapter );
            Log.d(TAG, "appointmentArrayList size else "+ appointmentArrayList.size());
        }
        appointmentLV.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String msg = "Sir you have a "+ appointmentArrayList.get( i ).getTaskName()+ " Location at "+ appointmentArrayList.get( i ).getTaskLocation()
                        +" on "+ simpleDateFormatForPlaySound.format( appointmentArrayList.get( i ).getTaskTime());
                myTTS.speak(msg , TextToSpeech.QUEUE_FLUSH, null );

            }
        } );



        appointmentLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                final String taskId = appointmentArrayList.get( pos ).getTaskId();
                Log.d( TAG, "onItemLongClick: " +taskId);

                AlertDialog.Builder builder = new AlertDialog.Builder( AppointmentActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Title");
                builder.setMessage( "Are you sure DELETE this item!!!" );
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d( TAG, "onClick: Yes" );

                                RequestBody username = RequestBody.create( MultipartBody.FORM, appData.getUsername() );
                                RequestBody password = RequestBody.create( MultipartBody.FORM, appData.getPassword() );
                                RequestBody id = RequestBody.create( MultipartBody.FORM, taskId );


                                Call<RemoveTaskResponseModel> removeTask = connectionApi.removeTask( username, password, id );
                                removeTask.enqueue( new Callback<RemoveTaskResponseModel>() {
                                    @Override
                                    public void onResponse(Call<RemoveTaskResponseModel> call, Response<RemoveTaskResponseModel> response) {
                                        if (response.code() == 200) {
                                            removeTaskResponseModel = response.body();
                                            Log.d( TAG, "onResponse: ----->  " + removeTaskResponseModel.toString() );
                                            if (removeTaskResponseModel.getError().equals( "false" )) {
                                                if (db.deletePreviousTask( taskId )){

                                                    appointmentArrayList.remove( pos );

                                                    arrayAdapter.notifyDataSetChanged();

                                                }else {
                                                    Toast.makeText( AppointmentActivity.this, "Some thing is Wrong !!!", Toast.LENGTH_SHORT ).show();
                                                }


                                            } else {
                                                Log.d( TAG, "onResponse:  else ---> " + response.code() );
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<RemoveTaskResponseModel> call, Throwable t) {

                                        Log.d( TAG, "onFailure: " );
                                    }
                                } );

                                dialog.dismiss();

                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.d( TAG, "onClick: No" );
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshOperation);
        swipeLayout.setOnRefreshListener(AppointmentActivity.this);
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
        Ringtone ringtone = RingtoneManager.getRingtone(AppointmentActivity.this, alarmUri);
        ringtone.stop();
//        alarmManager.cancel(pendingIntent);
    }

    public void addReminder(View view) {

        final String alarmId=String.valueOf((int) System.currentTimeMillis());
        final String taskId= UUID.randomUUID().toString();

//        final int[] categoryNo = {2};

        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.add_task_dialog_design, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText taskNameInput = (EditText) promptsView.findViewById(R.id.taskNameET);
        final EditText taskLocationInput = (EditText) promptsView.findViewById(R.id.taskLocationET);
        final EditText taskDescription = (EditText) promptsView.findViewById(R.id.taskDescriptionET);
        final RadioGroup radioPriority = promptsView.findViewById( R.id.radioPriority );
        final Spinner categorySP = promptsView.findViewById( R.id.selectTaskCategorySP );

        categorySP.setVisibility( View.GONE );



        final Button taskTimeBTN = promptsView.findViewById( R.id.taskTimeBTN );
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DAY_OF_MONTH);
        final int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int mMinute = calendar.get(Calendar.MINUTE);


        final boolean[] allOk = new boolean[1];


        taskTimeBTN.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(AppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set( Calendar.DAY_OF_MONTH,dayOfMonth);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.YEAR,year);
//                        String dateString = sdf.format(calendar.getTimeInMillis());


                        Toast.makeText(getApplicationContext(),String.valueOf(calendar.getTime()),Toast.LENGTH_LONG).show();
                        Log.d(TAG,String.valueOf(calendar.getTime()));
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        calendar.set( Calendar.HOUR_OF_DAY,hourOfDay );
                                        calendar.set( Calendar.MINUTE,minute );
                                        taskTimeInMills=calendar.getTimeInMillis();
                                        taskTimeBTN.setText(simpleDateFormat.format(calendar.getTimeInMillis()));
                                        Log.d( TAG, "onTimeSet:  "+hourOfDay + ":" + minute);

                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                },year,month,date);
                Log.d( TAG, "onClick: " +simpleDateFormat.format( minCalender.getTimeInMillis() ));
                if (Build.VERSION.SDK_INT >= 20){
                    datePickerDialog.getDatePicker().setMinDate(minCalender.getTimeInMillis());
                }

                datePickerDialog.show();




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

                                int priorityInt = 2;
                                String p = new String(  );
                                try {
                                    p = priorityBTN.getText().toString().trim().toLowerCase();
                                    if (p.equals( "high" )){
                                        priorityInt = 1;
                                    }else if (p.equals( "normal" )){
                                        priorityInt = 2;
                                    }else {
                                        priorityInt = 3;
                                    }
                                }catch (Exception e){

                                }

                                if (taskNameInput.getText().toString().trim().isEmpty()){
                                    taskNameInput.setError( "Required Fields" );
                                    Log.d( TAG, "onClick: " );
                                    Toast.makeText( AppointmentActivity.this, "Please fill all filed", Toast.LENGTH_SHORT ).show();
                                }else if (taskLocationInput.getText().toString().trim().isEmpty() ){
                                    taskLocationInput.setError( "Required Fields" );
                                    Toast.makeText( AppointmentActivity.this, "Please fill all filed", Toast.LENGTH_SHORT ).show();

                                }else if (taskDescription.getText().toString().trim().isEmpty()){
                                    taskDescription.setError( "Required Fields" );
                                    Toast.makeText( AppointmentActivity.this, "Please fill all filed", Toast.LENGTH_SHORT ).show();

                                }else if (p.isEmpty()){
                                    priorityBTN.setChecked( true );
                                    Toast.makeText( AppointmentActivity.this, "Please fill all filed", Toast.LENGTH_SHORT ).show();

                                }else if (TASK_CATEGORY_APPOINTMENT.equals( "0" )){
                                    Toast.makeText( AppointmentActivity.this, "Please Select Category", Toast.LENGTH_SHORT ).show();
                                    Log.d( TAG, "onClick: Please Select Category" );

                                }
                                else if (taskTimeInMills == 0){

                                    Toast.makeText( AppointmentActivity.this, "Please Select Date and Time", Toast.LENGTH_SHORT ).show();
                                }else {
                                    allOk[0] = true;

                                    Log.d( TAG, "onClick: "+simpleDateFormat.format( taskTimeInMills )+"--------->"+taskTimeInMills );
                                    RequestBody name = RequestBody.create( MultipartBody.FORM, taskNameInput.getText().toString().trim() );
                                    RequestBody location = RequestBody.create( MultipartBody.FORM, taskLocationInput.getText().toString().trim() );
                                    RequestBody description = RequestBody.create( MultipartBody.FORM, taskDescription.getText().toString().trim() );
                                    RequestBody priority = RequestBody.create( MultipartBody.FORM, String.valueOf( priorityInt ) );
                                    RequestBody taskDate = RequestBody.create( MultipartBody.FORM, Utilities.dateFormation( taskTimeInMills ) );
                                    RequestBody taskTime = RequestBody.create( MultipartBody.FORM, Utilities.timeFormation( taskTimeInMills ) );
                                    RequestBody doctors_id = RequestBody.create( MultipartBody.FORM, userModel.getDoctor_id().toString().trim() );
                                    RequestBody creator_id = RequestBody.create( MultipartBody.FORM, userModel.getUser_unique_id().toString().trim() );
                                    RequestBody category = RequestBody.create( MultipartBody.FORM, TASK_CATEGORY_APPOINTMENT );

                                    Log.d( TAG, "onClick: \n"+taskNameInput.getText().toString().trim()+"\n"+taskLocationInput.getText().toString().trim()+"\n"+taskDescription.getText().toString().trim()+"\n"+
                                            String.valueOf( priorityInt )+"\n"+Utilities.dateFormation( taskTimeInMills )+"\n"+Utilities.timeFormation( taskTimeInMills )+"\n"+
                                            userModel.getDoctor_id().toString().trim()+"\n"+userModel.getUser_unique_id().toString().trim()+"\ncategory:  "+TASK_CATEGORY_APPOINTMENT );


                                    Call<AddTaskResponseModel> addTask = connectionApi.addTask( name,location,description,priority,taskDate,taskTime,doctors_id,creator_id,category );
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
                                }



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

                UpdateTaskDatabase updateTaskDatabase = new UpdateTaskDatabase( AppointmentActivity.this );
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
        final RequestBody category = RequestBody.create( MultipartBody.FORM,TASK_CATEGORY_APPOINTMENT);

        Call<TaskResponseModel> getTask = connectionApi.getTask( userName,password,type, category );
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
                            appointmentArrayList.clear();
                            for (TaskResponseModel.Datum datum:
                                    datumArrayList) {
                                String taskId = datum.getId().toString().trim();
                                String created_at = datum.getCreatedAt().toString().trim();
                                String updated_at = datum.getUpdatedAt().toString().trim();
                                String taskName = datum.getName().toString().trim();
                                String location = datum.getLocation().toString().trim();
                                String description = datum.getDescription().toString().trim();
                                String priority = datum.getPriority().toString().trim();
                                String category = datum.getCategory().toString().trim();
                                String taskTime = datum.getTasktime().toString().trim();
                                String taskDate = datum.getTaskdate().toString().trim();
                                String datetime = datum.getDatetime().toString().trim();
                                String doctors_id = datum.getDoctorsId().toString().trim();
                                String creator_id = datum.getCreatorId().toString().trim();

                                try {
                                    TaskModel taskModel = new TaskModel( taskId,doctors_id,taskName,location,
                                            Utilities.stringDateTimeToMills( datetime ),Integer.parseInt( priority ), category ,description,
                                            Utilities.stringDateTimeToMills( created_at ),creator_id);
//                                    Log.d( TAG, "onResponse: Convert time ---> "+ Utilities.dateFormation( Utilities.stringDateTimeToMills( datetime ) ) );
//                                    Log.d( TAG, "onResponse: "+taskModel.toString() );

                                    appointmentArrayList.add( taskModel );

                                    boolean status = db.addTask( taskModel );
                                    if (status){
                                        arrayAdapter.notifyDataSetChanged();

                                        final String alarmId = String.valueOf( (int) System.currentTimeMillis() );

                                        Log.d( TAG, "onResponse: true status ---> task insert into database" );
                                        if (!Utilities.setAlarm( AppointmentActivity.this,
                                                Utilities.stringDateTimeToMills( datetime ), alarmId, taskId )) {
                                            Log.d( TAG, " Today Alarm not set" );
                                        }


                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Log.d( TAG, "onResponse: "+e.getMessage() );
                                }
                            }

                        }


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
}
