package com.example.mdjahirulislam.bssmu_demo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.service.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskListActivity extends AppCompatActivity {

    private ListView taskLV;
    private ArrayList<TaskModel> taskArrayList;
    private CustomAdapter arrayAdapter;
    private TaskModel taskModel;
    private Calendar calendar;
    private long taskTimeInMills;
    private SimpleDateFormat simpleDateFormat;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static TaskListActivity inst;

    public static TaskListActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task_list );
        taskLV = findViewById( R.id.taskListLV );
        taskArrayList = new ArrayList<>( );
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(TaskListActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(TaskListActivity.this, 0, myIntent, 0);


        taskArrayList = getAllTask();
        if (taskArrayList.size()>0) {
            arrayAdapter = new CustomAdapter( this,taskArrayList );
            taskLV.setAdapter( arrayAdapter );

            arrayAdapter.notifyDataSetChanged();
            Log.d("MyActivity", "Alarm On");
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
//            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());


        }

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

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_task_dialog_design, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText taskNameInput = (EditText) promptsView.findViewById(R.id.taskNameET);
        final EditText taskLocationInput = (EditText) promptsView.findViewById(R.id.taskNameET);

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


                                        alarmManager.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

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
                                taskArrayList.add( new TaskModel( taskNameInput.getText().toString().trim(),
                                        taskLocationInput.getText().toString().trim(),
                                        taskTimeInMills) );
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
        taskModelArrayList.add( new TaskModel( "Operation", "505", 1231324343 ) );
        taskModelArrayList.add( new TaskModel( "Appointment", "505", 1231324343 ) );
        taskModelArrayList.add( new TaskModel( "Class", "505", 1231324343 ) );
        taskModelArrayList.add( new TaskModel( "Movie", "505", 1231324343 ) );
        taskModelArrayList.add( new TaskModel( "Hospital", "505", 1231324343 ) );
        taskModelArrayList.add( new TaskModel( "House", "505", 1231324343 ) );

        return taskModelArrayList;
    }
}
