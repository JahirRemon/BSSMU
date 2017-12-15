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
import android.speech.tts.TextToSpeech;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.adapter.CustomAdapter;
import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.database.AppData;
import com.example.mdjahirulislam.bssmu_demo.database.DatabaseSource;
import com.example.mdjahirulislam.bssmu_demo.helper.Utilities;
import com.example.mdjahirulislam.bssmu_demo.model.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskListActivity extends AppCompatActivity {
    private final String TAG = TaskListActivity.class.getSimpleName();

    private ListView taskLV;
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

        try {
            myTTS = new TextToSpeech( getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        myTTS.setLanguage( Locale.UK );
                        myTTS.setPitch( 1.0f );
                        myTTS.setSpeechRate( .8f );
                    }
                }
            } );

        } catch (Exception e) {
            Log.d( "AlarmReceiver", "onReceive: " + e.getLocalizedMessage() );
        }

        taskArrayList = db.getMyAllTask( "1" );
        if (taskArrayList.size()>0) {
            arrayAdapter = new CustomAdapter( this,taskArrayList );
            taskLV.setAdapter( arrayAdapter );
            Log.d("MyActivity", "Alarm On");
        }
        taskLV.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String msg = "Sir you have a "+ taskArrayList.get( i ).getTaskName()+ " Location at "+taskArrayList.get( i ).getTaskLocation()
                        +" on "+ simpleDateFormatForPlaySound.format( taskArrayList.get( i ).getTaskTime());
                myTTS.speak(msg , TextToSpeech.QUEUE_FLUSH, null );

            }
        } );
    }

    @Override
    protected void onStart() {
        Log.d( "Remon", "onStart: " );
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
                        Log.d("Month",String.valueOf(calendar.getTime()));
                        TimePickerDialog timePickerDialog = new TimePickerDialog(TaskListActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        calendar.set( Calendar.HOUR_OF_DAY,hourOfDay );
                                        calendar.set( Calendar.MINUTE,minute );
                                        taskTimeInMills=calendar.getTimeInMillis();
                                        taskTimeBTN.setText(simpleDateFormat.format(calendar.getTimeInMillis()));
                                        Log.d( "REmon ", "onTimeSet:  "+hourOfDay + ":" + minute);
//                                        Intent myIntent = new Intent(TaskListActivity.this, AlarmReceiver.class);
//                                        pendingIntent = PendingIntent.getBroadcast(TaskListActivity.this, 0, myIntent, 0);
//
//                                        alarmManager.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                },year,month,date);
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

                                final int priority;
                                String p = priorityBTN.getText().toString().trim().toLowerCase();
                                if (p.equals( "high" )){
                                    priority = 1;
                                }else if (p.equals( "normal" )){
                                    priority = 0;
                                }else {
                                    priority = -1;
                                }


                                TaskModel taskModelOk = new TaskModel( "1",taskNameInput.getText().toString().trim(),
                                        taskLocationInput.getText().toString().trim(),
                                        taskTimeInMills,priority,taskDescription.getText().toString().trim(),System.currentTimeMillis());
                                db.addTask( taskModelOk );
                                if (!Utilities.setAlarm(TaskListActivity.this,
                                        taskTimeInMills,alarmId,taskId)){
                                    Log.d( TAG, " Today Alarm not set" );
//                                    db.addAlarmToSetAlarmTable(String.valueOf((int)System.currentTimeMillis()),alarmId);
                                }
                                taskArrayList.add( taskModelOk );
                                Log.d( TAG, "onClick: "+taskModelOk.toString() );
                                arrayAdapter = new CustomAdapter( getApplicationContext(),taskArrayList );
                                taskLV.setAdapter( arrayAdapter );
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

    public ArrayList<TaskModel> getAllTask() {
        ArrayList<TaskModel> taskModelArrayList = new ArrayList<>(  );
        taskModelArrayList.add( new TaskModel( "1","Operation", "505", 1231324343 , 1 , "a",765123456) );
        taskModelArrayList.add( new TaskModel( "1","Appointment", "505", 1231324343 , 1 , "a",765123456) );
        taskModelArrayList.add( new TaskModel( "2","Class", "505", 1231324343 , 0 , "a",765123456) );
        taskModelArrayList.add( new TaskModel( "1","Movie", "Star Cineplex", 1231324343 , 0 , "a",765123456) );
        taskModelArrayList.add( new TaskModel( "1","Hospital", "Mirpur", 1231324343 , 0 , "a",765123456) );

        for (TaskModel taskModel: taskModelArrayList) {

            db.addTask( taskModel );
        }
        return taskModelArrayList;
    }
}
